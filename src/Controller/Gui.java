package Controller;

import java.io.InputStream;

import Model.Model;
import MsgPass.ModelMsg.GameReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

        StackPane root = new StackPane();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setMinHeight(400);
        stage.setMinWidth(400);

        // init af Anchorpane
        AnchorPane panel_manager = new AnchorPane();
        root.getChildren().add(panel_manager);

        // init af spilbræt
        board = initBoard();
        panel_manager.getChildren().add(board);

        // init af pas-knap
        InputStream passButtonSrc = getClass().getResourceAsStream("/Assets/passButton.png");
        PassButton passButton = new PassButton(0, 0, passButtonSrc);
        panel_manager.getChildren().add(passButton);

        AnchorPane.setLeftAnchor(passButton, (double) passButton.getPosition().x);
        AnchorPane.setTopAnchor(passButton, (double) passButton.getPosition().y);

        AnchorPane.setLeftAnchor(board, getScreenWidth() / 2 - fitTileSize() * 4);
        AnchorPane.setTopAnchor(board, getScreenHeight() / 2 - fitTileSize() * 4 - 22);

        Model.sendModelMsg(new GameReadyMsg());
    }

    private GridPane initBoard() {
        GridPane board = new GridPane();

        // init af tiles
        InputStream empty_tile_src = getClass().getResourceAsStream("/Assets/stoneTileEmpty.png");
        Image empty_tile = new Image(empty_tile_src, fitTileSize(), 0, true, false);

        // init af spilbræt
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Tile tile = new Tile(i, j);

                tile.setImage(empty_tile);

                board.add(tile, i, j);
            }
        }
        return board;
    }

    public static double fitTileSize() {
        return getScreenHeight() / 11;
    }

    public static double fitPassSize() {
        return getScreenWidth() / 10;
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
