package Controller.Gui;

//Filen er skrevet af flere. Se individuelle metoder
import Controller.Controller;
import Model.GameOptions;
import Model.Model;
import MsgPass.ModelMsg.GuiReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import Shared.TileColor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.media.*;

public class Gui extends Application {

    // InitGui og de tilhørende fields er skrevet af Thor
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
    static ButtonMute muteButton;
    static Board board;

    static VBox gameCenter;

    // Skrevet af Katinka
    /**
     * Sets the content of the center box to board and top/bottom menu
     */
    private static void makeCenter(GameOptions gameOptions) {
        gameCenter = new VBox();
        board = new Board();
        MenuTop topMenu = new MenuTop(gameOptions);
        MenuBottom bottomMenu = new MenuBottom(gameOptions);
        gameCenter.getChildren().add(topMenu);
        gameCenter.getChildren().add(board);
        gameCenter.getChildren().add(bottomMenu);

        gameCenter.setAlignment(Pos.CENTER);
        gameCenter.setSpacing(10);
        gameGuiRoot.setCenter(gameCenter);
    }

    // Skrevet af Katinka
    public static Board getBoard() {
        return board;
    }

    // Skrevet af Katinka
    public static MenuBottom getMenuBottom() {
        return (MenuBottom) (gameCenter.getChildren().get(2));
    }

    // Skrevet af Thor
    static GameOptions prevGameOptions;

    // Skrevet af Frederik
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
        MenuLeft left = new MenuLeft(gameOptions);
        MenuRight right = new MenuRight(gameOptions);

        stackRoot.getChildren().clear();
        gameGuiRoot.getChildren().clear();
        stackRoot.getChildren().add(gameGuiRoot);
        gameGuiRoot.setLeft(left);
        gameGuiRoot.setRight(right);
        removeMuteButton();
        addMuteButton();
        makeCenter(gameOptions);

        Model.sendGameMsg(new GuiReadyMsg());
    }

    // Skrevet af Frederik
    public static void displayWinner(TileColor color, int blackPoints, int whitePoints) {
        VBox gameover = new VBox();
        gameover.setAlignment(Pos.CENTER);
        gameover.setBackground(
                new Background(new BackgroundImage(new Image("/Assets/BackgroundWin.png"), BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        gameover.setPrefSize(getScreenWidth(), getScreenHeight());
        gameover.getChildren().add(new WinnerIndication(color));
        gameover.getChildren().add(new WinnerPointCounter(blackPoints, whitePoints));
        gameover.getChildren().add(new ButtonRestart());
        gameover.getChildren().add(new ButtonMainMenu());
        gameover.getChildren().add(new ButtonExitGame());

        gameover.setSpacing(15);
        stackRoot.getChildren().add(gameover);
        removeMuteButton();
        addMuteButton();
        updateMusic("/Assets/sounds/music/winnerMusic.mp3");
    }

    // Skrevet af Katinka
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
        muteButton = new ButtonMute();
        makeStartMenu();

        stage.show();
        System.out.println("Gui ready to receive gamemode");

        Controller.setGuiInitDone();
    }

    // Skrevet af Christian
    /**
     * Positions mute button in the lower left corner of the screen
     */
    public static void addMuteButton() {
        stackRoot.getChildren().add(Gui.muteButton);
        StackPane.setAlignment(Gui.muteButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(Gui.muteButton, new Insets(15, 15, 15, 15));
    }

    // Skrevet af Christian
    public static void removeMuteButton() {
        stackRoot.getChildren().remove(Gui.muteButton);
    }

    // Skrevet af Katinka
    public static void makeStartMenu() {
        updateMusic("/Assets/sounds/music/mainMenuMusic.mp3");

        var mainMenu = new MenuMainCenter();
        startMenuRoot.getChildren().add(mainMenu);

        addMuteButton();
        startMenuRoot.setAlignment(Pos.CENTER);

    }

    // Skrevet af Christian
    public static void setMusic(String path) {
        var player = new MusicPlayer(path);
        player.play();

    }

    // Skrevet af Christian
    static void playMusic() {
        if (musicPlayer != null) {
            musicPlayer.play();
        }
    }

    // Skrevet af Christian
    static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    // Skrevet af Christian
    public static void updateMusic(String path) {
        stopMusic();
        setMusic(path);
        if (!muteButton.muted) {
            playMusic();
        }
    }

    // Skrevet af Katinka
    public static PlayerCharacter yourCharacter = PlayerCharacter.Stalin;
    public static MenuMultiplayer main;
    public static MenuMultiplayerJoin join;
    public static MenuMultiplayerHost host;
    public static MenuCharacterSelection characterSelect;
    public static MenuDisplayCharacter displaySelected;
    public static MenuDisplayUnknown displayUnknown;

    // Skrevet af Katinka
    public static void makeMultiplayerMenu() {
        main = new MenuMultiplayer();
        multiplayerMenuRoot = new VBox();

        updateMultiplayerMenu();
        multiplayerMenuRoot.getChildren().add(main);
        multiplayerMenuRoot.setAlignment(Pos.CENTER);
        multiplayerMenuRoot.setSpacing(15);
    }

    // Skrevet af Katinka
    public static void makeJoinMenu() {
        join = new MenuMultiplayerJoin();
        multiplayerMenuRoot.getChildren().remove(main);
        multiplayerMenuRoot.getChildren().add(join);
    }

    // Skrevet af Katinka
    public static void makeHostMenu() {
        host = new MenuMultiplayerHost();
        multiplayerMenuRoot.getChildren().remove(main);
        multiplayerMenuRoot.getChildren().add(host);
    }

    // Skrevet af Katinka
    public static void setYourCharacter(PlayerCharacter character) {
        yourCharacter = character;
    }

    // Skrevet af Katinka
    public static void updateMultiplayerMenu() {
        displaySelected = new MenuDisplayCharacter(yourCharacter);
        displayUnknown = new MenuDisplayUnknown();
        stackRoot.getChildren().clear();
        stackRoot.getChildren().add(displaySelected);
        stackRoot.getChildren().add(displayUnknown);
        stackRoot.getChildren().add(multiplayerMenuRoot);

        multiplayerMenuRoot.getChildren().remove(characterSelect);
        characterSelect = new MenuCharacterSelection();

        multiplayerMenuRoot.getChildren().add(0, characterSelect);
        removeMuteButton();
        addMuteButton();
    }

    // Skrevet af Thor
    /**
     * Sets up everthing that doesn't have to do with the scene.
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

    // Skrevet af Katinka
    public static double fitTileSize() {
        return getScreenHeight() / 11;
    }

    // Skrevet af Katinka
    public static double getScreenHeight() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getHeight();
    }

    // Skrevet af Katinka
    public static double getScreenWidth() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getWidth();
    }

    // Skrevet af Katinka
    public static void close() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.close();
            }
        });
    }
}