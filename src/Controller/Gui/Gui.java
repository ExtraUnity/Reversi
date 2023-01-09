package Controller.Gui;

import Controller.Controller;
import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;
import MsgPass.ModelMsg.GuiReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import Server.ServerConn;
import Shared.TileColor;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Modality;
import javafx.stage.Screen;

public class Gui extends Application {

    static Thread guiMainThread;

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

    static StackPane stackRoot;
    static HBox startMenuRoot;
    static BorderPane gameGuiRoot;
    static Stage stage;

    /**
     * Sets the content of the center box to board and top/bottom menu
     */
    private static void makeCenter(GameOptions gameOptions) {
        var centerBox = new BorderPane();
        centerBox.setPrefWidth(8 * fitTileSize());
        centerBox.setTop(new MenuTop(gameOptions));
        centerBox.setBottom(new MenuBottom());
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
        stackRoot.getChildren().add(gameover);
    }

    @Override
    public void start(Stage stage) throws Exception {
        setupStageMeta(stage);
        Gui.stage = stage;

        stackRoot = new StackPane();
        startMenuRoot = new HBox();
        gameGuiRoot = new BorderPane();

        stackRoot.getChildren().add(startMenuRoot);

        stackRoot.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));

        Scene scene = new Scene(stackRoot);
        stage.setScene(scene);

        makeStartMenu();

        stage.show();
        System.out.println("Gui ready to receive gamemode");
        Controller.setGuiInitDone();
    }

    public static void makeStartMenu() {

        var classicButton = new Button("Classic game mode");
        classicButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                Model.startGame(GameMode.CLASSIC, new GameOptions(-1, false, TileColor.WHITE));
            }
        });
        var aiButton = new Button("ai game mode");
        aiButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                Model.startGame(GameMode.AI_GAME, new GameOptions(-1, false, TileColor.WHITE));
            }
        });
        var multiplayerButton = new Button("Multiplayer game mode");
        multiplayerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                VBox dialogVbox = new VBox(20);

                ServerConn conn = new ServerConn();

                dialogVbox.getChildren().add(new Text("Your id: " + conn.netId));
                var inputfield = new TextField();
                dialogVbox.getChildren().add(inputfield);
                var submitButton = new Button("Join");
                submitButton.setOnMouseClicked(new EventHandler<javafx.event.Event>() {
                    @Override
                    public void handle(javafx.event.Event arg0) {
                        conn.tryJoin(inputfield.getText());
                    }
                });
                dialogVbox.getChildren().add(submitButton);
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        });
        startMenuRoot.getChildren().add(classicButton);
        startMenuRoot.getChildren().add(aiButton);
        startMenuRoot.getChildren().add(multiplayerButton);
        startMenuRoot.setAlignment(Pos.CENTER);
        startMenuRoot.setSpacing(40);
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
}
