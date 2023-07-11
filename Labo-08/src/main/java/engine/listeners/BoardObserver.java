package engine.listeners;

import engine.board.Cell;
import engine.pieces.King;
import engine.pieces.Pawn;
import engine.pieces.Piece;
import engine.pieces.Rook;

/**
 * Interface that defines the methods that need to be implemented by an observer of the game.
 * @author Emilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public interface BoardObserver {

    /**
     * Method that is called when a castling move is made.
     * @param king The King that is moving in the castling move.
     * @param rook The Rook that is moving in the castling move.
     * @param direction The direction of the castling move (1 for right, -1 for left)
     */
    void updateCastling(King king, Rook rook, int direction);

    /**
     * Method that is called when a Pawn is moved to a new cell.
     * @param pawn The Pawn that is being moved.
     * @param to The Cell that the Pawn is being moved to.
     */
    Piece updatePieceTo(Pawn pawn, Cell to);

    /**
     * Method that is called when a Pawn is promoted.
     * @param p The Pawn that is being promoted.
     */
    void updatePromote(Pawn p);

    /**
     * Method that is called when a Pawn is moved to a new cell.
     * @param cell The Cell that the Pawn is being moved to.
     */
    void updateEnPassant(Cell cell);
}
