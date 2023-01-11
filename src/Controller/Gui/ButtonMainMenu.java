package Controller.Gui;


public class ButtonMainMenu extends Button{

    public ButtonMainMenu() {
        super(Buttons.MainMenu, 1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            setImage(img);
            Gui.stackRoot.getChildren().clear();
            Gui.stackRoot.getChildren().add(Gui.startMenuRoot);

        });
    }
        
}
