package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.GuiReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
        centerBox.setPrefWidth(8 * fitTileSize());
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

        
        makeLeftMenu();
        makeRightMenu();
        makeCenter();

        Model.sendModelMsg(new GuiReadyMsg());
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
