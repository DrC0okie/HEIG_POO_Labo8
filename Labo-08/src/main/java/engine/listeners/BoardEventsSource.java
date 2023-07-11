package engine.listeners;

import engine.board.Cell;
import engine.pieces.King;
import engine.pieces.Pawn;
import engine.pieces.Piece;
import engine.pieces.Rook;

/**
 * Events source class that notifies the BoardObserver.
 *
 * @author Emilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public abstract class BoardEventsSource {

    /**
     * Observer object that provides methods to update the board.
     */
    private BoardObserver observer = null;

    /**
     * Method that allows an observer (listener) to attach itself to this events source.
     * @param moveObserver The Observer that wants to attach itself to the subject.
     */
    public void attach(BoardObserver moveObserver){
        observer = moveObserver;
    }

    /**
     * Notifies the observer that a promotion must be done.
     */
    public void notifyPromotion(Pawn p) {
        observer.updatePromote(p);
    }

    /**
     * Get the piece located on the destination cell and returns it to the caller.
     *
     * @param to the destination cell
     */
    public Piece notifyPieceTo(Pawn p, Cell to) {
        return observer.updatePieceTo(p, to);
    }

    /**
     * Notifies the observer that the En Passant is done and the pawn must be removed from the board
     * @param cell The cell to be removed.
     */
    public void notifyEnPassant(Cell cell){
        observer.updateEnPassant(cell);
    }

    /**
     * Notifies the observer to update the board with the castling move.
     *
     * @param king King who do the castling.
     * @param rook Rook who do the castling.
     * @param direction Direction of the castling.
     */
    public void notifyCastling(King king, Rook rook, int direction) {
        observer.updateCastling(king, rook, direction);
    }
}
