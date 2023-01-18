package Server;
//Filen er skrevet af Thor

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Random;

public class Server {
    private static final int PORT = 4000;

    public static void main(String[] args) {

        try (ServerSocket socket = new ServerSocket(PORT)) {
            // Her ville jeg bruge en byte[] som key. Men det virker ikke fordi java er et
            // lortesprog. Fait nok hvis bytes ikke var en primitiv men det er de. Så det er
            // dumt at man ikke får det samme får man hasher to primitiver med samme værdi!
            var hosts = new HashMap<String, Socket>();
            while (true) {
                try {
                    var clientSocket = socket.accept();
                    /*
                     * 1.
                     * Protocollen er at hvis der bliver sendt en "1" byte, vil clienten gerne
                     * hoste.
                     * Hvis der bliver sendt en "0" vil der gerne joine et eksiterende spil.
                     * 2.
                     * Hvis den hoster vil serveren sende 6 bytes som er dens id.
                     * Hvis den vil joine vil den læse 6 bytes som er id'et af den som forbindelsen
                     * vil joine.
                     * 3.
                     * Hvis det lykkes at joine en anden person bliver der sendt et "1" til begger
                     * parter.
                     * Hvis den prøver at joine en person som ikke eksisterer vil der blive sendt et
                     * "0" tilbage.
                     * 4.
                     * Efter hver ny forbindelse hvil den loope gennem alle tidligere forbindelser
                     * for at fjerne de ubrugte.
                     */

                    // Læs hvilken slags forbindelse der bliver skabt:
                    var inStream = clientSocket.getInputStream();
                    var outSteam = clientSocket.getOutputStream();

                    int connectionType = inStream.read();
                    if (connectionType == 1) {
                        // Siden den gerne vil hoste. Skal der blive dannet et tilfældigt id. Funktionen
                        // tager hosts som argument fordi den tjekker for kollisioner
                        byte[] id = generateId(hosts);

                        // Send id bytesne til clienten og smid den i mappet.
                        outSteam.write(id);
                        hosts.put(new String(id), clientSocket);

                    } else if (connectionType == 0) {
                        // Siden den gerne vil joine har den brug for at læse et id fra streamen.
                        byte[] id = readJoinId(inStream);

                        // Nu skal den tjekke om der findes en host med dette it id
                        var host = hosts.remove(new String(id));
                        if (host == null) {
                            // Der findes ikke en host med dette. Send "0" byte tilbage
                            outSteam.write(0);
                            // Der kan ikke gøres mere. Så dræb forbindelsen
                            clientSocket.close();
                        } else {
                            // Der findes en host med dette navn. Først skal der tjekke om forbindelsen
                            // stadig er i live.
                            if (host.isConnected()) {
                                // Hvis den stadig er frisk. Skriv "1" til dem begge så de ved at der er åbnet
                                // et spil. Og åben spillet på en ny thread
                                outSteam.write(1);
                                host.getOutputStream().write(1);
                                GameHost game = new GameHost(host, clientSocket);
                                game.spawn();
                            } else {
                                // Hvis den ikke længere er i live. Sig til clienten at det ikke var et validt
                                // id.
                                outSteam.write(0);
                            }
                        }
                    } else {
                        // Hvis den modtager en forkert byte. Er det nok bare en crawler som har fundet
                        // forbindelsen. Dræb den.
                        clientSocket.close();
                    }
                    // Til sidst skal den bare rengøre og slette all gamle forbindelser.
                    for (String id : hosts.keySet()) {
                        var host = hosts.get(id);
                        if (!host.isConnected()) {
                            // Hvis den ikke længere er forbundet. Skal den fjernes
                            hosts.remove(id);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException serverException) {
            // Det her sker hvis serversocket crasher. Det er meget usandsynligt og serveren
            // kommer til at lukke hvis det sker.
            serverException.printStackTrace();
        }

    }

    public static byte[] readJoinId(InputStream stream) throws IOException {
        byte[] id = new byte[6];
        stream.read(id);
        return id;
    }

    private static byte[] generateId(HashMap<String, Socket> map) {
        // Den skal blive ved med at genererer id'er indtil den finder et unikt. Det er
        // meget usandsynligt at den nogensinde kommer til at lave en kollision
        while (true) {
            int leftLimit = 65; // letter 'A'
            int rightLimit = 90; // letter 'Z'
            int targetStringLength = 6;
            Random random = new Random();

            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            byte[] bytes = generatedString.getBytes();
            if (map.get(new String(bytes)) == null) {
                return bytes;
            } else {
                System.out.println("Wow ok der var collision med id'et " + generatedString);
            }
        }

    }
}

class GameHost {
    Socket socket1;
    Socket socket2;

    GameHost(Socket player1, Socket player2) {
        socket1 = player1;
        socket2 = player2;
    }

    void spawn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    var in1 = socket1.getInputStream();
                    var out1 = socket1.getOutputStream();
                    var in2 = socket2.getInputStream();
                    var out2 = socket2.getOutputStream();
                    while (true) {
                        Thread.sleep(100);
                        if (in1.available() >= 4) {
                            byte[] msgLenBytes = new byte[4];
                            in1.read(msgLenBytes);
                            for (int i = 0; i < msgLenBytes.length; i++) {
                                System.out.println(msgLenBytes[i]);
                            }
                            ByteBuffer buffer = ByteBuffer.wrap(msgLenBytes);
                            int len = buffer.getInt();
                            byte[] message = new byte[len];
                            in1.read(message);
                            out2.write(msgLenBytes);
                            out2.write(message);
                        }
                        if (in2.available() >= 4) {
                            byte[] msgLenBytes = new byte[4];
                            in2.read(msgLenBytes);
                            for (int i = 0; i < msgLenBytes.length; i++) {
                                System.out.println(msgLenBytes[i]);
                            }
                            ByteBuffer buffer = ByteBuffer.wrap(msgLenBytes);
                            int len = buffer.getInt();
                            byte[] message = new byte[len];
                            in2.read(message);
                            out1.write(msgLenBytes);
                            out1.write(message);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
