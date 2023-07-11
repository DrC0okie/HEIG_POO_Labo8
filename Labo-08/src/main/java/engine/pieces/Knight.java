package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Cell;
import java.util.LinkedList;
import java.util.List;

/**
 * Subclass of Piece who represent a Knight.
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public class Knight extends Piece {
    /**
     * Constructs an object of type Knight with his color and his cell.
     *
     * @param color The color of the player.
     * @param cell  The cell where the piece begin.
     */
    public Knight(PlayerColor color, Cell cell) {
        super(PieceType.KNIGHT, color, cell);
    }

    /**
     * Checks if the cell of destination is an allowed move for this piece.
     *
     * @param to The cell of destination.
     * @return true if the move is allowed, false instead.
     */
    @Override
    public boolean canMove(Cell to) {
        super.canMove(to);

        // Check if the move is valid for a knight (L-shaped move)
        return cell.distanceX(to) * cell.distanceY(to) == 2;
    }

    /**
     * Gathers all cells between the cell from and the cell to.
     *
     * @param to The cell where the piece want to go.
     * @return a list of all Cell in between (do not contain the Cell from and to).
     */
    @Override
    public List<Cell> path(Cell to) {
        return new LinkedList<>();
    }
}
