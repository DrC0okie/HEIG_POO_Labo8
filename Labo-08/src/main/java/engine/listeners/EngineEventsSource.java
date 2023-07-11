package engine.listeners;

import chess.PlayerColor;
import engine.board.Cell;
import engine.pieces.*;

/**
 * Event source class that notifies the EngineObserver.
 * @author Emilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public abstract class EngineEventsSource {

    /**
     * Observer that provides methods to update the user interface.
     */
    private EngineObserver observer;

    /**
     * Method that allows an observer (listener) to attach itself to this events source.
     * @param engineObserver The Observer that wants to attach itself to the subject.
     */
    public void attach(EngineObserver engineObserver){
        observer = engineObserver;
    }

    /**
     * Notifies the observer to remove a piece from the view.
     *
     * @param cell The cell to be removed.
     */
    public void updateRemovePiece(Cell cell) {
        observer.updateRemovePiece(cell);
    }

    /**
     * Notifies the observer to add a new piece to the view.
     *
     * @param piece The piece to be added.
     */
    public void updateAddPiece(Piece piece) {
        observer.updateAddPiece(piece);
    }

    /**
     * Notifies the observer to the current turn.
     */
    public void updateNextTurn(){
        observer.updateNextTurn();
    }

    /**
     * Notifies the observer to make the pop-up view appear for the user to choose the promoted
     * piece
     *
     * @param p The pawn that is being promoted
     * @return The piece that the user chose
     */
    public Piece updatePopUp(Piece p){
        return observer.updatePopUp(p);
    }

    public void notifyInCheck(PlayerColor color){
        observer.updateInCheck(color);
    }
}
