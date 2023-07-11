package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Cell;
import java.util.LinkedList;
import java.util.List;

/**
 * Subclass of Piece who represent a Rook.
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public class Rook extends Piece {

    /**
     * Boolean who track is the rook has moved during the game or not
     * Useful for the special rook move with the king.
     */
    private boolean hasMoved = false;

    /**
     * Constructs an object of type Rook with his color and his cell.
     *
     * @param color The color of the player
     * @param cell  The cell where the piece begin
     */
    public Rook(PlayerColor color, Cell cell) {
        super(PieceType.ROOK, color, cell);
    }

    /**
     * Checks if the cell of destination is an allowed move for this piece.
     *
     * @param to The cell of destination.
     * @return true if the move is allowed, false instead.
     */
    @Override
    public boolean canMove(Cell to) {
        if (super.canMove(to)) {
            if (to.getX() == cell.getX() || to.getY() == cell.getY()) {
                hasMoved = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Converts the information of the piece in text value.
     *
     * @return the text value of the piece.
     */
    public String textValue() {
        return super.textValue();
    }

    /**
     * Gathers all Cells between the Cell from and the Cell to.
     *
     * @param to The cell where the piece want to go.
     * @return List of all Cell in between (do not contain the Cell from and to).
     */
    @Override
    public List<Cell> path(Cell to) {
        List<Cell> path = new LinkedList<>();

        int distanceX = cell.distanceX(to);
        int distanceY = cell.distanceY(to);

        if (distanceX == 0) {
            for (int i = 1; i < distanceY; i++) {
                path.add(new Cell(cell.getX(), cell.getY() + i * cell.directionY(to)));
            }
        } else if (distanceY == 0) {
            for (int i = 1; i < distanceX; i++) {
                path.add(new Cell(cell.getX() + i * cell.directionX(to), cell.getY()));
            }
        }
        return path;
    }

    /**
     * Getter for the hasMoved attribute.
     *
     * @return the value of the hasMoved attribute.
     */
    public boolean getHasMoved() {
        return hasMoved;
    }
}
