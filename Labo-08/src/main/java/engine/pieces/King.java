package engine.pieces;

import chess.PieceType;
import chess.PlayerColor;
import engine.board.Cell;
import engine.listeners.BoardObserver;
import engine.listeners.BoardEventsSource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Subclass of Piece who represent a King.
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public class King extends Piece{
    /**
     * Boolean who tracks if the rook has moved during the game or not.
     * Useful for the special king move with the rook.
     */
    private boolean hasMoved = false;

    /**
     * Stores whether the king is in check or not.
     */
    private boolean isAttacked;

    /**
     * The piece located on the destination cell (updated by the PieceProvider).
     */
    private HashMap<Cell, Piece> board;

    /**
     * Anonymous inner class used for notification handling to the observer.
     */
    private final BoardEventsSource events = new BoardEventsSource() {};

    /**
     * Construct an object of type King with his color and his cell.
     *
     * @param color The color of the player.
     * @param cell  The cell where the piece begin.
     */
    public King(PlayerColor color, Cell cell, HashMap<Cell, Piece> board, BoardObserver observer) {
        super(PieceType.KING, color, cell);
        events.attach(observer);
        this.board = board;
    }

    public boolean getIsAttacked(){
        return isAttacked;
    }

    public void setIsAttacked(boolean state){
        isAttacked = state;
    }

    /**
     * Indicates if the current player's king is in check.
     *
     * @return true if the current player's king is in check, else returns false.
     */
    public boolean isCheck() {
        boolean pieceInBetween;
        //Check if at least one opponent piece can reach the king of the current player color
        for (Piece p : board.values()) {
            if (p!= null && p.getType() != PieceType.KING && p.getColor() != color && p.canMove(cell)) {
                pieceInBetween = false;

                //Checks if there is a piece in between the opponent and this king
                for (Cell cell : p.path(cell)) {
                    if (board.get(cell) != null) {
                        pieceInBetween = true;
                        break;
                    }
                }
                if (!pieceInBetween)
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if the castling move is allowed or not.
     *
     * @param to The cell where the piece wants to go.
     * @return true if the castling move is allowed, false otherwise.
     */
    public boolean castling(Cell to) {
        //if moved, castling is not possible
        if (hasMoved)
            return false;

        //Check cell where there might be a rook
        boolean onLeft = to.getX() < cell.getX();

        int directionX = cell.directionX(to);

        //TODO replace 7 by a constant
        Piece dest = board.get(new Cell(onLeft ? 0 : 7, cell.getY()));

        //Check if it is a rook and if it has not moved yet
        if (dest == null || isCheck())
            return false;

        if (!(dest instanceof Rook) || dest.getHasMoved())
            return false;

        //Check if there is no piece between the king and the rook
        for (int i = 1; i < cell.distanceX(dest.cell); i++) {
            Cell nextCell = new Cell(cell.getX() + i * directionX, cell.getY());
            Piece p = board.get(nextCell);

            //check that one of the cell on the path can't be attacked by an opponent piece
            King virtualKing = new King(color, nextCell, board, null);

            if (p != null || virtualKing.isCheck())
                return false;
        }

        //Warns the observer that the king can castle
        events.notifyCastling(this, (Rook) dest, directionX);
        return true;
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

        //Only check for diagonal move. if move horizontally/vertically, the calculi is wrong (0*1 = 0)
        if (cell.distanceX(to) * cell.distanceX(to) == 1) {
            hasMoved = true;
            return true;
        }
        //check for horizontal/vertical move
        if (cell.distanceX(to) + cell.distanceY(to) == 1) {
            hasMoved = true;
            return true;
        }
        //check for castling
        if (cell.distanceX(to) == 2 && castling(to)) {
            hasMoved = true;

            //skip the move call in ChessEngine because it is already done in castling
            return false;
        }
        return false;
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

        if (cell.distanceX(to) == 2) {
            path.add(new Cell(cell.getX() + cell.directionX(to), cell.getY()));
        }

        return path;
    }
}
