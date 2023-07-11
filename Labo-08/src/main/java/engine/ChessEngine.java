package engine;

import chess.ChessController;
import chess.ChessView;
import chess.PlayerColor;
import engine.board.Board;
import engine.board.Cell;
import engine.listeners.EngineObserver;
import engine.pieces.*;

/**
 * The engine who creates, show and resolve a game of chess.
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public class ChessEngine implements ChessController, EngineObserver {

    /**
     * View of the board game.
     */
    private ChessView view;

    /**
     * The board representation.
     */
    private Board board;

    /**
     * Number of turn
     */
    private int turn;

    /**
     * Constructor
     */
    public ChessEngine() {
        board = new Board(this);
    }

    /**
     * Get which player must play.
     *
     * @return The color of the actual player that can play.
     */
    private PlayerColor playerTurn() {
        return turn % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;
    }

    /**
     * Increments the turn and updates the view message.
     */
    private void nextTurn(){
        turn++;
        displayMessage();
    }

    /**
     * Starts the game.
     *  according to sequence.png, the game starts the view without any pieces.
     * @param view the view to use
     */
    @Override
    public void start(ChessView view) {
        this.view = view;
        view.startView();
    }

    /**
     * Initialize or reinitialize the board to start a new game.
     */
    @Override
    public void newGame() {
        for (Piece piece : board.getPieces().values())
            view.removePiece(piece.getCell().getX(), piece.getCell().getY());
        turn = 0;
        board.clear();
        board.init();
        displayMessage();
    }

    /**
     * Moves a Piece from a cell to another
     *
     * @param fromX the x coordinate of the cell where the piece is
     * @param fromY the y coordinate of the cell where the piece is
     * @param toX the x coordinate of the cell where the piece must go
     * @param toY the y coordinate of the cell where the piece must go
     * @return true if the move is allowed, false instead
     */
    @Override
    public boolean move(int fromX, int fromY, int toX, int toY){
        displayMessage();
        if(board.move(fromX, fromY, toX, toY, playerTurn())){
            nextTurn();
            return true;
        }
        return false;
    }

    /**
     * displays the current player turn (color) on the view.
     */
    private void displayMessage() {
        if (view == null || board == null)
            return;

        String color = playerTurn() == PlayerColor.WHITE ? "white" : "black";
        view.displayMessage(color + " turn");
    }

    /**
     * Removes a piece from the view.
     *
     * @param cell The cell to be removed
     */
    @Override
    public void updateRemovePiece(Cell cell) {
        if(cell == null)
            throw new NullPointerException("Couldn't find the piece to remove from the board");

        view.removePiece(cell.getX(), cell.getY());
    }

    /**
     * Adds a new piece to the view.
     *
     * @param piece The piece to be added
     */
    @Override
    public void updateAddPiece(Piece piece) {
        view.putPiece(piece.getType(),piece.getColor(), piece.getCell().getX(),
                piece.getCell().getY());
    }

    /**
     * Changes the current turn.
     */
    public void updateNextTurn(){
        nextTurn();
    }

    /**
     * Makes the view pop up appear for the user to choose the promoted piece.
     *
     * @param p The pawn that will be promoted
     * @return The piece that the user has chosen
     */
    public Piece updatePopUp(Piece p){
        Piece[] promoteTo = {
                new Queen(p.getColor(), p.getCell()),
                new Knight(p.getColor(), p.getCell()),
                new Rook(p.getColor(), p.getCell()),
                new Bishop(p.getColor(), p.getCell())
        };

        //ask the user to choose a piece to promote to
        return  view.askUser("Promotion", "Choose a piece to promote", promoteTo);
    }

    @Override
    public void updateInCheck(PlayerColor color) {
        if (view == null || board == null)
            return;

        String colorMsg = color == PlayerColor.WHITE ? "white" : "black";
        view.displayMessage(colorMsg + " king is in check");
    }
}