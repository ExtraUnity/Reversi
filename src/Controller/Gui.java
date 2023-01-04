package Controller;

import java.io.InputStream;

import Model.Model;
import MsgPass.ModelMsg.GameReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
        stage.setMinHeight(400);
        stage.setMinWidth(400);

        // init af borderpane
        AnchorPane panel_manager = new AnchorPane();
        root.getChildren().add(panel_manager);

        // init af spilbræt
        board = initBoard();
        panel_manager.getChildren().add(board);
        
        panel_manager.setLeftAnchor(board,getScreenWidth()/2-fitTileSize()*4);
        panel_manager.setTopAnchor(board,getScreenHeight()/2-fitTileSize()*4-22);

        panel_manager.setCenter(board);
        Model.sendModelMsg(new GameReadyMsg());
    }

    private GridPane initBoard() {
        GridPane board = new GridPane();

        // init af tiles
        InputStream empty_tile_src = getClass().getResourceAsStream("/Assets/stoneTileEmpty.png");
        Image empty_tile = new Image(empty_tile_src,fitTileSize(),0,true,false);

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
        static private InputStream white_tile_src;
        static private Image white_tile;
        static private InputStream black_tile_src;
        static private Image black_tile;

        public Tile(int x, int y) {
            position = new TilePosition(x, y);
            setOnMouseClicked(e -> {
                Model.sendModelMsg(new TilePressedMsg(position));
            });
        }

        private Image getWhiteImage() {
            if (white_tile == null) {
                white_tile_src = getClass().getResourceAsStream("/Assets/stoneTileWhite.png");
                white_tile = new Image(white_tile_src,fitTileSize(),0,true,false);
               
            }
            return white_tile;
        }

        private Image getBlackImage() {
            if (black_tile == null) {
                black_tile_src = getClass().getResourceAsStream("/Assets/stoneTileBlack.png");
                black_tile = new Image(black_tile_src,fitTileSize(),0,true,false);
            }
            return black_tile;
        }


        public void setTilecolor(TileColor tilecolor) {
            this.tilecolor = tilecolor;
            System.out.println("Settings tile " + position + " to " + tilecolor);
            switch (tilecolor) {
                case WHITE:
                    setImage(getWhiteImage());
                    break;
                case BLACK:
                    setImage(getBlackImage());
                    break;

            }
        }
    }
    public double fitTileSize(){
        return getScreenHeight()/11;
    }

    public double getScreenHeight(){
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getHeight();
    }
    public double getScreenWidth(){
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getWidth();
    }
}
