package MsgPass.ModelMsg;

import Controller.Gui.PlayerCharacter;

public class CharacterSelectedMsg {
    public PlayerCharacter character;

    public CharacterSelectedMsg(PlayerCharacter character) {
        this.character = character;
    }
    
}
