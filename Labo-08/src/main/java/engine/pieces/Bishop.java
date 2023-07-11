package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Cell;
import java.util.LinkedList;
import java.util.List;

/**
 * Subclass of Piece that represents a Bishop.
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public class Bishop extends Piece {

    /**
     * Construct an object of type Bishop with its color and the cell it is located in.
     *
     * @param color The color of the player
     * @param cell  The cell where the piece is located
     */
    public Bishop(PlayerColor color, Cell cell) {
        super(PieceType.BISHOP, color, cell);
    }

    /**
     * Checks if the destination cell is a valid move for this piece
     *
     * @param to The destination cell
     * @return true if the move is valid, false otherwise
     */
    @Override
    public boolean canMove(Cell to) {
        super.canMove(to);
        // can move diagonally
        return cell.distanceX(to) == cell.distanceY(to);
    }

    /**
     * Gathers all the cells between the current cell and the destination cell
     *
     * @param to The destination cell
     * @return A list of all the cells in between the current cell and the destination cell (does not include the current cell and the destination cell)
     */
    @Override
    public List<Cell> path(Cell to) {
        List<Cell> path = new LinkedList<>();

        // gets the distance on the X axis between the current cell and the destination cell
        int distanceX = cell.distanceX(to);
        int directionX = cell.directionX(to);
        int directionY = cell.directionY(to);

        // Iterate over the distance on the X axis to gather all the cells between the current cell and the destination cell
        for (int i = 1; i < distanceX; i++)
            path.add(new Cell(cell.getX() + i * directionX, cell.getY() + i * directionY));

        return path;
    }
}
