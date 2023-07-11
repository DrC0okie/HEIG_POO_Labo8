import chess.ChessController;
import chess.ChessView;
import chess.views.gui.GUIView;
import engine.ChessEngine;

/**
 * Main program who will allow us to play a chess game.
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public class Main {
    public static void main(String[] args) {
        ChessController engine = new ChessEngine();

        // View choice (GUI or CLI)
        ChessView view = new GUIView(engine);
        // ChessView view = new ConsoleView(controller);
        engine.start(view);
    }
}