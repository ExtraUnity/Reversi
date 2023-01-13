package Controller.Gui;

import java.io.File;
import java.net.URL;

import Controller.Controller;
import Model.GameOptions;
import Model.Model;
import MsgPass.ModelMsg.GuiReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import Server.ServerConn;
import Shared.TileColor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.media.*;

public class Gui extends Application {

    static Thread guiMainThread;
    public static MediaPlayer musicPlayer;

    public static void initGui() {
        guiMainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runGui();
            }
        });
        guiMainThread.start();
    }

    static void runGui() {
        launch(new String[] {});
    }

    static Stage stage;
    static StackPane stackRoot;
    static VBox multiplayerMenuRoot;
    static VBox startMenuRoot;
    static BorderPane gameGuiRoot;

    /**
     * Sets the content of the center box to board and top/bottom menu
     */
    private static void makeCenter(GameOptions gameOptions) {
        var centerBox = new BorderPane();
        centerBox.setPrefWidth(8 * fitTileSize());
        centerBox.setTop(new MenuTop(gameOptions));
        centerBox.setBottom(new MenuBottom(gameOptions));
        gameGuiRoot.setCenter(centerBox);
        makeBoard();
    }

    public static Board getBoard() {
        return (Board) ((BorderPane) gameGuiRoot.getCenter()).getCenter();
    }

    public static MenuBottom getMenuBottom() {
        return (MenuBottom) ((BorderPane) gameGuiRoot.getCenter()).getBottom();
    }

    public static void makeBoard() {
        ((BorderPane) gameGuiRoot.getCenter()).setCenter(new Board());
    }

    private static void makeMenuLeft(GameOptions gameOptions) {
        gameGuiRoot.setLeft(new MenuLeft(gameOptions));
    }

    private static void makeMenuRight(GameOptions gameOptions) {
        gameGuiRoot.setRight(new MenuRight(gameOptions));

    }

    static GameOptions prevGameOptions;

    /**
     * Hvis null bliver passeret som argument ville der blive brugt samme
     * gameOptions som sidste game
     */
    public static void buildGameGui(GameOptions gameOptions) {
        System.out.println("Gui received gameoptions " + gameOptions);
        if (gameOptions != null) {
            prevGameOptions = gameOptions;
        } else {
            gameOptions = prevGameOptions;
        }
        stackRoot.getChildren().clear();
        gameGuiRoot.getChildren().clear();
        stackRoot.getChildren().add(gameGuiRoot);

        makeMenuLeft(gameOptions);
        makeMenuRight(gameOptions);
        makeCenter(gameOptions);

        Model.sendGameMsg(new GuiReadyMsg());
    }

    public static void displayWinner(TileColor color) {
        VBox gameover = new VBox();
        gameover.setAlignment(Pos.CENTER);
        gameover.setBackground(
                new Background(new BackgroundImage(new Image("/Assets/BackgroundWin.png"), BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        gameover.setPrefSize(getScreenWidth(), getScreenHeight());
        gameover.getChildren().add(new WinnerIndication(color));
        gameover.getChildren().add(new ButtonRestart());
        gameover.getChildren().add(new ButtonMainMenu());
        gameover.getChildren().add(new ButtonExitGame());
        gameover.setSpacing(15);
        stackRoot.getChildren().add(gameover);
        updateMusic("./src/Assets/sounds/music/winnerMusic.mp3");
    }

    @Override
    public void start(Stage stage) throws Exception {
        setupStageMeta(stage);
        Gui.stage = stage;
        stackRoot = new StackPane();
        startMenuRoot = new VBox();
        gameGuiRoot = new BorderPane();

        stackRoot.getChildren().add(startMenuRoot);

        stackRoot.setBackground(
                new Background(
                        new BackgroundImage(new Image("/Assets/BackgroundGame.png", 0, fitTileSize() * 11, true, false),
                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                                new BackgroundSize(getScreenWidth(), getScreenHeight(), false, false, false, false))));

        Scene scene = new Scene(stackRoot);
        stage.setScene(scene);

        makeStartMenu();

        stage.show();
        System.out.println("Gui ready to receive gamemode");
        setMusic("./src/Assets/sounds/music/mainMenuMusic.mp3");

        Controller.setGuiInitDone();
    }

    public static void makeStartMenu() {
        var gameModeButtons = new MenuMainCenter();
        var exitGameButtons = new MenuMainBottom();
        var title = new Title();

        startMenuRoot.getChildren().add(title);
        startMenuRoot.getChildren().add(gameModeButtons);
        startMenuRoot.getChildren().add(exitGameButtons);
        startMenuRoot.setAlignment(Pos.CENTER);
    }

    /**
     * Loads mp3 from directory and plays on repeat until updated
     */
    public static void setMusic(String path) {
        File musicDirectory = new File(path);
        Media backgroundMusic = new Media(musicDirectory.toURI().toString());
        musicPlayer = new MediaPlayer(backgroundMusic);
        musicPlayer.setVolume(0.4);
        musicPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                musicPlayer.seek(Duration.ZERO);
            }
        });
        musicPlayer.play();
    }

    private static void stopMusic() {
        musicPlayer.stop();
    }

    public static void updateMusic(String path) {
        stopMusic();
        setMusic(path);
    }

    public static PlayerCharacter yourCharacter = PlayerCharacter.Stalin;

    public static void makeMultiplayerMenu(ServerConn conn) {
        var displaySelected = new MenuDisplayCharacter(yourCharacter);
        multiplayerMenuRoot = new VBox();
        stackRoot.getChildren().clear();
        stackRoot.getChildren().add(displaySelected);
        stackRoot.getChildren().add(multiplayerMenuRoot);

        var joinButton = new MenuMultiplayer(conn);
        var characterSelect = new MenuCharacterSelection(conn);

        multiplayerMenuRoot.getChildren().add(characterSelect);
        multiplayerMenuRoot.getChildren().add(joinButton);

        multiplayerMenuRoot.setAlignment(Pos.CENTER);

    }

    public static void setYourCharacter(PlayerCharacter character) {
        yourCharacter = character;
    }

    /**
     * Sets up everthing that doesn't have to do with the scene.
     * 
     * @param stage
     */
    void setupStageMeta(Stage stage) {
        stage.setTitle("Reversi");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Model.sendGameMsg(new ModelWindowClosedMsg());
                Model.shutdownModel();
            }
        });

        stage.setMaximized(true);
        stage.setMinWidth(400);
        stage.setMinHeight(400);
    }

    public static double fitTileSize() {
        return getScreenHeight() / 11;
    }

    public static double getScreenHeight() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getHeight();
    }

    public static double getScreenWidth() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getWidth();
    }

    public static void close() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.close();
            }
        });

    }
}