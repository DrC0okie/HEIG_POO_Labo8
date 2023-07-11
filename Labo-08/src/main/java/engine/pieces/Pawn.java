package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Cell;
import engine.listeners.BoardObserver;
import engine.listeners.BoardEventsSource;
import java.util.LinkedList;
import java.util.List;

/**
 * Subclass of Piece who represent a Pawn.
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public class Pawn extends Piece {
    /**
     * Tracks if the rook has moved during the game or not.
     * useful for allowing the move of two case at first turn or not.
     */
    private boolean hasMoved = false;

    /**
     * Inner class used for notification handling to the observer.
     */
    private final BoardEventsSource events = new BoardEventsSource() {};

    private boolean targetEnPassant = false;

    /**
     * Constructs an object of type Pawn with his color and his cell.
     *
     * @param color The color of the player.
     * @param cell  The cell where the piece begin.
     */
    public Pawn(PlayerColor color, Cell cell, BoardObserver observer) {
        super(PieceType.PAWN, color, cell);
        events.attach(observer);
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

        //the distance on y-axis for the white pawn will become negative when moving forward,therefore using always a
        // positive value will simplify the conditions below and check if the pawn is moving forward
        //if the white or black pawn is moving backward, the distance will be negative for both and the move will be invalid
        int distanceY = to.getY() - cell.getY();
        int distanceX = cell.distanceX(to);
        Piece pieceTo = events.notifyPieceTo(this, to);

        if (color == PlayerColor.BLACK) {
            //if the black pawn is moving forward, the distance will be positive
            distanceY = -distanceY;
        }

        //vertical move
        //if the distance is equal to zero, it will mean that the pawn has not moved. Therefore, the move is incorrect
        if (cell.getX() == to.getX() && distanceY <= 2 && distanceY > 0) {

            //checks if it is not the pawn's first move
            if (distanceY == 2 && hasMoved)
                return false;

            //checks if there is a piece on the destination cell when moving upwards, which is not allowed
            if (pieceTo != null)
                return false;

            updateTargetEnPassant(distanceY);

            //check if promotion is possible
            checkPromoted(to);
            return hasMoved = true;
        }

        //diagonal move when there is an opponent piece
        if (distanceX == 1 && distanceY == 1) {

            //Notify the board that we need to know if there is an opponent on the destination cell
            events.notifyPieceTo(this, to);

            //If an opponent is on the diagonal cell, the pawn can move
            if (pieceTo != null && pieceTo.getColor() != color) {
                updateTargetEnPassant(distanceY);
                checkPromoted(to);
                return hasMoved = true;
            }
        }
        return enPassant(to);
    }

    /**
     * Gathers all cells between the cell from and the cell to
     *
     * @param to The cell where the piece want to go
     * @return a list of all Cell in between (do not contain the Cell from and to)
     */
    @Override
    public List<Cell> path(Cell to) {
        List<Cell> path = new LinkedList<>();

        //a collision is only possible when the pawn moves forward by two cells
        if (cell.distanceY(to) == 2)
            path.add(new Cell(cell.getX(), cell.getY() + cell.directionY(to)));

        return path;
    }

    /**
     * Notifies the listener if a promotion is occurred
     *
     * @param to Cell of destination
     */
    public void checkPromoted(Cell to) {

        //promotion of the pawn: no color condition as the pawns can only move forward
        if (to.getY() == 0 || to.getY() == 7) {
            Piece pieceTo = events.notifyPieceTo(this, to);

            if (pieceTo == null || pieceTo.getType() != PieceType.KING)
                events.notifyPromotion(this);
        }
    }

    /**
     * Converts the information of the piece in text value.
     *
     * @return the text value of the piece
     */
    @Override
    public String textValue() {
        return super.textValue();
    }

    /**
     * Checks if the pawn can do the en passant.
     *
     * @param to the destination cell.
     * @return true if the pawn can do the en passant, false instead.
     */
    public boolean enPassant(Cell to) {

        int distanceX = cell.distanceX(to);
        int distanceY = cell.distanceY(to);

        if (distanceX * distanceY == 1) {
            Piece pieceTo = null;
            if (color == PlayerColor.WHITE) {
                pieceTo = events.notifyPieceTo(this, new Cell(to.getX(), to.getY() - 1));
            } else if (color == PlayerColor.BLACK) {
                pieceTo = events.notifyPieceTo(this, new Cell(to.getX(), to.getY() + 1));
            }
            if (pieceTo != null && pieceTo.getType() == PieceType.PAWN && ((Pawn)(pieceTo)).getTargetEnPassant()) {
                events.notifyEnPassant(pieceTo.cell);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the pawn has moved.
     *
     * @return true if the pawn has moved, false otherwise.
     */
    public boolean getHasMoved() {
        return hasMoved;
    }

    /**
     * Get the value of targetEnPassant
     *
     * @return the value of targetEnPassant
     */
    public boolean getTargetEnPassant() {
        return targetEnPassant;
    }

    /**
     * Checks if the move of the pawn put itself as a target of move en passant
     *
     * @param distance needed to check if the target use a move with a distance of 2
     */
    public void updateTargetEnPassant(int distance) {
        //Only at first time we move the pawn and with a distance of 2
        targetEnPassant = !hasMoved && distance == 2;
    }
}
