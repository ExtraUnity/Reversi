package Controller;

import java.io.InputStream;
import Model.Model;
import MsgPass.ModelMsg.GuiReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Shared.ButtonPosition;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Gui extends Application {

    static Thread guiMainThread;

    static void initGui() {
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

    static GridPane board;
    static StackPane root;
    static AnchorPane panel_manager;

    @Override
    public void start(Stage stage) throws Exception {

        // init af grundpanel
        stage.setTitle("Reversi");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Model.sendModelMsg(new ModelWindowClosedMsg());
            }
        });

        stage.setMaximized(true);

        root = new StackPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(400);
        stage.setMinWidth(400);
        System.out.println(scene.getHeight() + " " + scene.getWindow().getHeight());

        // init af borderpane
        panel_manager = new AnchorPane();
        root.getChildren().add(panel_manager);

        // tile.setImage(empty_tile);tile.setImage(empty_tile);init af spilbræt
        initBoard();

        double boardPosX = getScreenWidth() / 2 - fitTileSize() * 4;
        double boardPosY = getScreenHeight() / 2 - fitTileSize() * 4 - 22;
        AnchorPane.setLeftAnchor(board, boardPosX);
        AnchorPane.setTopAnchor(board, boardPosY);

        // init af pas-knap
        InputStream passButtonSrc = getClass().getResourceAsStream("/Assets/passButton.png");
        double boardEndY = boardPosY + fitTileSize() * 8;
        ButtonPosition passButtonSize = fitPassSize();
        double passButtonX = boardPosX + fitTileSize() * 1.5;
        double passButtonY = boardEndY + (scene.getHeight() - boardEndY) / 2 - passButtonSize.y / 2;
        PassButton passButton = new PassButton(passButtonX, passButtonY, passButtonSize, passButtonSrc);
        panel_manager.getChildren().add(passButton);

        AnchorPane.setLeftAnchor(passButton, (double) passButton.getPosition().x);
        AnchorPane.setTopAnchor(passButton, (double) passButton.getPosition().y);

        // init restart knap
        RestartBtn restartKnap = new RestartBtn();
        panel_manager.getChildren().add(restartKnap);

        Model.sendModelMsg(new GuiReadyMsg());
    }

    static void initBoard() {
        if (board != null) {
            panel_manager.getChildren().remove(board);
        }
        board = new GridPane();
        panel_manager.getChildren().add(board);

        // init af spilbræt
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Tile tile = new Tile(i, j);

                board.add(tile, i, j);
            }
        }

    }

    public static double fitTileSize() {
        return getScreenHeight() / 11;
    }

    public static ButtonPosition fitPassSize() {
        return new ButtonPosition(getScreenWidth() / 10, getScreenWidth() / 10 * 120 / 272);
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
