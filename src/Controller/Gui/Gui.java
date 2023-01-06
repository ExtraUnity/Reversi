package Controller.Gui;

import Controller.Controller;
import Model.GameOptions;
import Model.Model;
import MsgPass.ModelMsg.GuiReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import Shared.TileColor;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
    static BorderPane guiRoot;

    /**
     * Sets the content of the center box to board and top/bottom menu
     */
    private static void makeCenter() {
        var centerBox = new BorderPane();
        centerBox.setPrefWidth(8 * fitTileSize());
        centerBox.setTop(new MenuTop());
        centerBox.setBottom(new MenuBottom());
        guiRoot.setCenter(centerBox);
        makeBoard();
    }

    public static Board getBoard() {
        return (Board) ((BorderPane) guiRoot.getCenter()).getCenter();
    }

    public static MenuBottom getMenuBottom() {
        return (MenuBottom) ((BorderPane) guiRoot.getCenter()).getBottom();
    }

    public static void makeBoard() {
        ((BorderPane) guiRoot.getCenter()).setCenter(new Board());
    }

    private static void makeMenuLeft(GameOptions gameOptions) {
        guiRoot.setLeft(new MenuLeft(gameOptions));
    }

    private static void makeMenuRight(GameOptions gameOptions) {
        guiRoot.setRight(new MenuRight(gameOptions));

    }

    static GameOptions prevGameOptions;

    /**
     * Hvis null bliver passeret som argument ville der blive brugt samme
     * gameOptions som sidste game
     */
    public static void buildGui(GameOptions gameOptions) {
        System.out.println("Gui received gameoptions " + gameOptions);
        if (gameOptions != null) {
            prevGameOptions = gameOptions;
        } else {
            gameOptions = prevGameOptions;
        }
        stackRoot.getChildren().clear();
        guiRoot.getChildren().clear();
        stackRoot.getChildren().add(guiRoot);

        makeMenuLeft(gameOptions);
        makeMenuRight(gameOptions);
        makeCenter();

        Model.sendGameMsg(new GuiReadyMsg());
    }

    public static void displayWinner(TileColor color) {
        VBox gameover = new VBox();
        gameover.setAlignment(Pos.CENTER);
        // gameover.setBackground(new Background(new BackgroundFill(Color.ORANGE, null,
        // null)));
        gameover.setPrefSize(getScreenWidth(), getScreenHeight());
        gameover.getChildren().add(new WinnerIndication(color));
        gameover.getChildren().add(new ButtonRestart());
        stackRoot.getChildren().add(gameover);
    }

    @Override
    public void start(Stage stage) throws Exception {
        setupStageMeta(stage);

        stackRoot = new StackPane();
        guiRoot = new BorderPane();
        stackRoot.getChildren().add(guiRoot);

        stackRoot.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));

        Scene scene = new Scene(stackRoot);
        stage.setScene(scene);

        stage.show();
        System.out.println("Gui ready to receive gamemode");
        Controller.setGuiInitDone();
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
