package Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Test {
    public static void main(String[] args) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] bytes = ByteBuffer.allocate(4).putInt(300).array();
        try {
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] out = stream.toByteArray();
        for (byte b : out) {
            System.out.println(b);
        }
        ByteBuffer buffer = ByteBuffer.wrap(out);
        int num = buffer.getInt();
        System.out.println(num);
    }
}
