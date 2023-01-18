package MsgPass.ModelMsg;
//Filen er skrevet af Katinka
import Controller.Gui.PlayerCharacter;

public class CharacterSelectedMsg extends ModelMsg{
    public PlayerCharacter character;

    public CharacterSelectedMsg(PlayerCharacter character) {
        this.character = character;
    }
    
}
