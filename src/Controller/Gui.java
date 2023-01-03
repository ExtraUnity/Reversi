package Controller;

import java.io.InputStream;

import Model.Model;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
        StackPane root = new StackPane();
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
        stage.setMinHeight(400);
        stage.setMinWidth(400);

        // init af borderpane
        BorderPane panel_manager = new BorderPane();
        root.getChildren().add(panel_manager);

        // init af gridpane
        board = initBoard();
        panel_manager.setCenter(board);

    }

    private GridPane initBoard() {
        GridPane board = new GridPane();

        // init af tiles
        InputStream empty_tile_src = getClass().getResourceAsStream("/Assets/stoneTileEmpty.jpg");
        Image empty_tile = new Image(empty_tile_src);

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

    class Tile extends ImageView {
        private TilePosition position;
        TileColor tilecolor = null;

        // init af tiles
        InputStream white_tile_src = getClass().getResourceAsStream("/Assets/stoneTileWhite.jpg");
        Image white_tile = new Image(white_tile_src);
        InputStream black_tile_src = getClass().getResourceAsStream("/Assets/stoneTileBlack.jpg");
        Image black_tile = new Image(black_tile_src);

        public Tile(int x, int y) {
            position = new TilePosition(x, y);
            setOnMouseClicked(e -> {
                Model.sendModelMsg(new TilePressedMsg(position));
            });
        }

        public void setTilecolor(TileColor tilecolor) {
            this.tilecolor = tilecolor;
            System.out.println("Settings tile " + position + " to " + tilecolor);
            switch (tilecolor) {
                case WHITE:
                    setImage(white_tile);
                    break;
                case BLACK:
                    setImage(black_tile);
                    break;

            }
        }
    }

}
