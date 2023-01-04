package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.GuiReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

    private void makeCenter() {
        var centerBox = new BorderPane();
        centerBox.setTop(new TopMenu());
        centerBox.setBottom(new BotMenu());
        guiRoot.setCenter(centerBox);
        makeBoard();
    }

    public static Board getBoard() {
        return (Board) ((BorderPane) guiRoot.getCenter()).getCenter();
    }

    public static void makeBoard() {
        ((BorderPane) guiRoot.getCenter()).setCenter(new Board());
    }

    private void makeLeftMenu() {
        guiRoot.setLeft(new LeftMenu());
    }

    private void makeRightMenu() {
        guiRoot.setRight(new RightMenu());
    }

    @Override
    public void start(Stage stage) throws Exception {
        setupStageMeta(stage);

        stackRoot = new StackPane();
        guiRoot = new BorderPane();
        stackRoot.getChildren().add(guiRoot);

        Scene scene = new Scene(stackRoot);
        stage.setScene(scene);
        stage.show();

        stage.setMinWidth(400);
        stage.setMinHeight(400);

        makeCenter();
        makeLeftMenu();
        makeRightMenu();

        Model.sendModelMsg(new GuiReadyMsg());

        /*
         * root_old = new StackPane();
         * Scene scene = new Scene(root_old);
         * stage.setScene(scene);
         * stage.show();
         * stage.setMinHeight(400);
         * stage.setMinWidth(400);
         * System.out.println(scene.getHeight() + " " + scene.getWindow().getHeight());
         * 
         * // init af borderpane
         * panel_manager_old = new AnchorPane();
         * root_old.getChildren().add(panel_manager_old);
         * 
         * // tile.setImage(empty_tile);tile.setImage(empty_tile);init af spilbræt
         * initBoard();
         * 
         * double boardPosX = getScreenWidth() / 2 - fitTileSize() * 4;
         * double boardPosY = getScreenHeight() / 2 - fitTileSize() * 4 - 22;
         * AnchorPane.setLeftAnchor(board_old, boardPosX);
         * AnchorPane.setTopAnchor(board_old, boardPosY);
         * 
         * // init af pas-knap
         * InputStream passButtonSrc =
         * getClass().getResourceAsStream("/Assets/passButton.png");
         * double boardEndY = boardPosY + fitTileSize() * 8;
         * ButtonPosition passButtonSize = fitPassSize();
         * double passButtonX = boardPosX + fitTileSize() * 1.5;
         * double passButtonY = boardEndY + (scene.getHeight() - boardEndY) / 2 -
         * passButtonSize.y / 2;
         * PassButton passButton = new PassButton(passButtonX, passButtonY,
         * passButtonSize, passButtonSrc);
         * panel_manager_old.getChildren().add(passButton);
         * 
         * AnchorPane.setLeftAnchor(passButton, (double) passButton.getPosition().x);
         * AnchorPane.setTopAnchor(passButton, (double) passButton.getPosition().y);
         * 
         * // init restart knap
         * RestartBtn restartKnap = new RestartBtn();
         * panel_manager_old.getChildren().add(restartKnap);
         * restartKnap.setX(1000);
         * 
         * Model.sendModelMsg(new GuiReadyMsg());
         */
    }

    void setupStageMeta(Stage stage) {
        stage.setTitle("Reversi");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Model.sendModelMsg(new ModelWindowClosedMsg());
            }
        });

        stage.setMaximized(true);
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
