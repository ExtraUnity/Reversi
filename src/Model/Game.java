package Model;

public class Game {
    public enum GameMode {
        Classic,
        AIGame,
        Multiplayer
    }

    public class GameOptions {
        int gametime;
    }

    GameState gamestate = GameState.Playing;
    final Thread modelMainThread;

    Game() {
        var game = this;
        modelMainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                game.run_game();
            }
        });
        modelMainThread.start();
    }

    void run_game() {
        while (gamestate == GameState.Playing) {
            //Game loop
        }
    }
}

enum GameState {
    Playing,
    WhiteWinner,
    BLackWinner
}