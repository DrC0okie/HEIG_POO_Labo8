package engine.board;

import chess.PlayerColor;
import engine.listeners.EngineObserver;
import engine.listeners.EngineEventsSource;
import engine.listeners.BoardObserver;
import engine.pieces.*;
import java.util.HashMap;
import java.util.List;

public class Board implements BoardObserver {

    /**
     * Size of the board.
     */
    private static final int BOARD_SIZE = 8;

    /**
     * The hash map representing the board disposition.
     */
    private HashMap<Cell, Piece> board;

    /**
     * The kings of the board.
     */
    private Kings kings;

    /**
     * Inner class used for notification handling to the observer.
     */
    private final EngineEventsSource events = new EngineEventsSource(){};

    /**
     * The Board constructor that initialize the board and the kings
     */
    public Board(EngineObserver observer) {
        board = new HashMap<>();
        kings = new Kings(board, this);
        events.attach(observer);
    }

    /**
     * Adds a piece to the board, then notifies the observer to update the GUI.
     *
     * @param piece The piece that has been added to the board.
     */
    public void addPiece(Piece piece) {
        board.put(piece.getCell(), piece);
        events.updateAddPiece(piece);
    }

    /**
     * Removes the piece in the hashmap, then call the observer to remove it from the GUI.
     *
     * @param cell The cell on which the piece has to be removed.
     * @return the piece that has been removed.
     */
    public Piece removePiece(Cell cell) {
        Piece removed = board.remove(cell);
        events.updateRemovePiece(removed.getCell());
        return removed;
    }

    /**
     * Moves a piece from a cell to another cell, then notifies the observer to update the GUI.
     *
     * @param to The destination cell.
     * @param piece The piece to be moved.
     * @return The piece that has been moved.
     */
    public Piece movePiece(Cell to, Piece piece) {
        if (to == null || piece == null)
            throw new NullPointerException("The from/to position for the movement is null");

        //Checks if there is a piece on the destination cell then removes it
        Piece eaten = board.get(to);
        if (eaten != null)
            removePiece(eaten.getCell());

        //Removes the piece on the actual cell
        removePiece(piece.getCell());

        //Adds the piece on the destination cell
        piece.setCell(to);
        addPiece(piece);

        return eaten;
    }

    /**
     * Clears all the pieces on the board.
     */
    public void clear() {
        board.clear();
    }

    /**
     * Returns the HashMap containing all the pieces of the board.
     *
     * @return The HashMap of the board
     */
    public HashMap<Cell, Piece> getPieces() {
        return board;
    }

    /**
     * Initializes the board with all the pieces at their initial positions.
     */
    public void init() {
        // Re-initialize the kings
        kings = new Kings(board, this);

        for (PlayerColor color : PlayerColor.values()) {
            int line = color == PlayerColor.WHITE ? 0 : BOARD_SIZE - 1;

            addPiece(new Rook(color, new Cell(0, line)));
            addPiece(new Knight(color, new Cell(1, line)));
            addPiece(new Bishop(color, new Cell(2, line)));
            addPiece(new Queen(color, new Cell(3, line)));
            addPiece(new Bishop(color, new Cell(5, line)));
            addPiece(new Knight(color, new Cell(6, line)));
            addPiece(new Rook(color, new Cell(7, line)));

            // Get the corresponding colored king to put it in the board
            King king = kings.getKing(color);
            king.setCell(new Cell(4, line));
            addPiece(king);

            int pawnLine = (color == PlayerColor.WHITE ? 1 : 6);
            for (int j = 0; j < BOARD_SIZE; j++) {
                Pawn p = new Pawn(color, new Cell(j, pawnLine), this);
                addPiece(p);
            }
        }
    }

    /**
     * Moves a piece from a cell to another if the move is legal.
     *
     * @param fromX The x coordinate of the initial position.
     * @param fromY The y coordinate of the initial position.
     * @param toX   The x coordinate of the final position.
     * @param toY   The y coordinate of the final position.
     * @return true if the move has been done, false if it is an illegal move.
     */
    public boolean move(int fromX, int fromY, int toX, int toY, PlayerColor color) {
        Cell from = new Cell(fromX, fromY), to = new Cell(toX, toY);
        Piece piece = board.get(from);
        Piece toPiece = board.get(to);

        //No piece on selected cell
        if (piece == null || (toPiece != null && toPiece.getColor() == color))
            return false;

        if (piece.getColor() == color && piece.canMove(to) && isEmptyBetween(piece.path(to))) {

            //Moves the piece. We retrieve the piece from the board again in case of promotion
            Piece eaten = movePiece(to, board.get(piece.getCell()));

            //Get the king that is in check
            King inCheck = kings.isCheck();
            if (inCheck != null) {

                //if the king was already attacked or set itself in check cancel the move
                if (inCheck.getIsAttacked() || inCheck.getColor() == color) {
                    movePiece(from, board.get(to));

                    //Put back the eaten piece
                    if (eaten != null)
                        addPiece(eaten);

                    return false;
                }
                inCheck.setIsAttacked(true);
            }
            resetTargetPassant(color);
            return true;
        }
        return false;
    }

    /**
     * Resets "targetEnPassant" in the Pawn of opposite player.
     */
    public void resetTargetPassant(PlayerColor color) {
        for (Piece piece : board.values())
            if (piece instanceof Pawn && piece.getColor() != color)
                ((Pawn) piece).updateTargetEnPassant(0);
    }

    /**
     * Determines if the cells between the initial and final position are not occupied by pieces.
     *
     * @param path A list of all cells between the actual position and the final position.
     * @return true if no pieces are detected between the initial and final positions.
     */
    private boolean isEmptyBetween(List<Cell> path) {
        for (Cell cell : path)
            if (board.get(cell) != null)
                return false;

        return true;
    }

    /**
     * Moves the king and the rook to do a castling.
     *
     * @param king The King that is moving in the castling move.
     * @param rook The Rook that is moving in the castling move.
     * @param direction The direction of the castling move (1 for right, -1 for left)
     */
    @Override
    public void updateCastling(King king, Rook rook, int direction) {
        int distance = king.getCell().getX() + direction;

        removePiece(king.getCell());
        king.setCell(new Cell(king.getCell().getX() + direction * 2, king.getCell().getY()));
        addPiece(king);

        removePiece(rook.getCell());
        rook.setCell(new Cell(distance, king.getCell().getY()));
        addPiece(rook);

        //Two moves from castling, so the next move will be the opponent
        events.updateNextTurn();
    }

    /**
     * Updates the board when a pawn is promoted and notify the engine to call the pop-up window.
     *
     * @param p The Pawn that is being promoted.
     */
    @Override
    public void updatePromote(Pawn p) {
        if (p == null)
            throw new NullPointerException("The promoted piece is null");

        Piece promoted = events.updatePopUp(p);

        //Remove the piece from the board (not in the GUI)
        if (board.remove(promoted.getCell()) == null)
            throw new NullPointerException("Couldn't find the piece to remove from the board");

        board.remove(p.getCell());
        //Add the promoted piece (not in the GUI)
        board.put(promoted.getCell(), promoted);
    }

    /**
     * Sends the piece located on the cell given by the pawn.
     *
     * @param pawn The pawn that has triggered this update.
     * @param to The cell from which retrieve the piece to send back to the pawn.
     * @return The piece that has been sent back to the pawn.
     */
    @Override
    public Piece updatePieceTo(Pawn pawn, Cell to) {
        return board.get(to);
    }

    /**
     * Removes the pawn who has been take by en passant
     *
     * @param cell the cell where the pawn was taken out
     */
    public void updateEnPassant(Cell cell) {
        removePiece(cell);
    }

    /**
     * Inner class used to easily retrieve the kings of the board instead of searching them in it.
     *
     * @author Emilie Bressoud, Kevin Farine, Timothee Van Hove
     */
    private class Kings {

        /**
         * The black king
         */
        private final King black;

        /**
         * The white king
         */
        private final King white;

        /**
         * Kings constructor that creates the two kings of the board
         * @param board The board to search for the kings
         */
        Kings(HashMap<Cell, Piece> board, BoardObserver observer) {
            black = new King(PlayerColor.BLACK, null, board, observer);
            white = new King(PlayerColor.WHITE, null, board, observer);
        }

        /**
         * Checks if the king of the given color is in check
         *
         * @return The king that is in check, or null if both are not in check
         */
        King isCheck() {
            if (black.isCheck()) {
                white.setIsAttacked(false);
                events.notifyInCheck(black.getColor());
                return black;
            }
            if (white.isCheck()) {
                black.setIsAttacked(false);
                events.notifyInCheck(white.getColor());
                return white;
            }
            white.setIsAttacked(false);
            black.setIsAttacked(false);
            return null;
        }

        /**
         * Gets the king of the given color
         *
         * @return The king of the given color
         */
        King getKing(PlayerColor color) {
            return color == black.getColor() ? black : white;
        }
    }
}
