package engine.listeners;

import chess.PlayerColor;
import engine.board.Cell;
import engine.pieces.Piece;

/**
 * Event source class that notifies the EngineObserver.
 * @author Emilie Bressoud, Kevin Farine, Timothée Van Hove
 */

public interface EngineObserver {
    /**
     * Removes a piece from the view.
     *
     * @param cell The cell to be removed.
     */
    void updateRemovePiece(Cell cell);

    /**
     * Adds a new piece to the view.
     *
     * @param piece The piece to be added.
     */
    void updateAddPiece(Piece piece);

    /**
     * Changes the current turn.
     */
    void updateNextTurn();

    /**
     * Makes the view pop up appear for the user to choose the promoted piece.
     *
     * @param p The pawn that is being promoted.
     * @return The piece that the user chose to promote the pawn to.
     */
    Piece updatePopUp(Piece p);

    /**
     * Method that is called when a king is in check.
     *
     * @param color The color of the king that is in check.
     */
    void updateInCheck(PlayerColor color);
}
