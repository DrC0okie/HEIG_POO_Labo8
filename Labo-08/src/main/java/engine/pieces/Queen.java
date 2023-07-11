package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Cell;
import java.util.LinkedList;
import java.util.List;

/**
 * Subclass of Piece who represent a Queen
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public class Queen extends Piece {

    /**
     * Constructs an object of type Queen with its color and its cell.
     *
     * @param color The color of the player.
     * @param cell  The cell where the piece begin.
     */
    public Queen(PlayerColor color, Cell cell) {
        super(PieceType.QUEEN, color, cell);
    }

    /**
     * Checks if the cell of destination is an allowed move for this piece.
     *
     * @param to The cell of destination.
     * @return true if the move is allowed, false otherwise.
     */
    @Override
    public boolean canMove(Cell to) {
        super.canMove(to);
        if (cell.distanceX(to) == cell.distanceY(to)) {
            return true;
        }
        return to.getX() == cell.getX() || to.getY() == cell.getY();
    }

    /**
     * Gathers all cells between the cell from and the cell to.
     *
     * @param to The cell where the piece want to go.
     * @return a list of all Cell in between (do not contain the Cell from and to).
     */
    @Override
    public List<Cell> path(Cell to) {
        List<Cell> path = new LinkedList<>();
        int distanceX = cell.distanceX(to);
        int distanceY = cell.distanceY(to);

        //adds the cells in case of a diagonal move
        if (distanceX == distanceY) {
            for (int i = 1; i < distanceX; ++i) {
                int x = cell.getX() + i * cell.directionX(to);
                int y = cell.getY() + i * cell.directionY(to);
                path.add(new Cell(x, y));
            }
        }
        //adds the cells in case of a horizontal move
        else if (cell.getX() == to.getX()) {
            for (int i = 1; i < distanceY; ++i) {
                int y = cell.getY() + i * cell.directionY(to);
                path.add(new Cell(cell.getX(), y));
            }
        }
        //Adds the cells in case of a vertical move
        else if (cell.getY() == to.getY()) {
            for (int i = 1; i < distanceX; ++i) {
                int x = cell.getX() + i * cell.directionX(to);
                path.add(new Cell(x, cell.getY()));
            }
        }
        return path;
    }
}
