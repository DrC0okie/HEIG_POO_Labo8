package engine.pieces;

import chess.*;
import engine.board.Cell;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class that has the common information of all type of piece.
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
abstract public class Piece implements ChessView.UserChoice{

    /**
     * The type of the piece.
     */
    protected final PieceType type;

    /**
     * The color of the piece.
     */
    protected final PlayerColor color;

    /**
     * The cell where the piece is.
     */
    protected Cell cell;

    /**
     * Constructs an object of type Piece with his type, his color and his cell.
     * @param type The type of the piece.
     * @param color The color of the player.
     * @param cell The cell where the piece begin.
     */
    public Piece(PieceType type, PlayerColor color, Cell cell){
        this.type = type;
        this.color = color;
        this.cell = cell;
    }

    /**
     * Method that checks if the cell of destination is an allowed move for this piece.
     * @param to The destination cell.
     * @return True if the move is allowed, false otherwise.
     */
    public boolean canMove(Cell to) {
        //move is incorrect when it is on the same cell
        if(to.equals(this.cell))
            return false;

        return to.getX() < 8 && to.getY() < 8;
    }

    /**
     * Gets the piece type.
     * @return The piece type
     */
    public PieceType getType(){
        return type;
    }

    /**
     * Gets the piece color.
     * @return The piece color.
     */
    public PlayerColor getColor(){
        return color;
    }

    /**
     * Gets the cell where the piece is on.
     * @return The cell where the piece is on.
     */
    public Cell getCell(){
        return cell;
    }

    /**
     * Sets the cell where the piece is on.
     * @param cell The cell where the piece is on.
     */
    public void setCell(Cell cell){
        this.cell = new Cell(cell.getX(), cell.getY());
    }

    /**
     * Compares two pieces and return true if the two objects are absolutely identical.
     * @param o The object we compare with.
     * @return True if they're absolutely identical, false otherwise.
     */
    @Override
    public boolean equals(Object o){
        return o == this || o != null && o.getClass() == getClass()
                && ((Piece) o).cell == cell
                && ((Piece) o).type == type
                && ((Piece) o).color == color;
    }

    /**
     * returns The hashcode of the object.
     * @return The hashcode of the object.
     */
    @Override
    public int hashCode(){
        return Objects.hash(cell, type, color);
    }

    /**
     * Converts the information of the piece in text value.
     * @return The text value of the piece.
     */
    public String textValue(){
        return getClass().getSimpleName();
    }

    /**
     * Returns th piece type.
     * @return The piece type as String.
     */
    public String toString(){
        return textValue();
    }

    /**
     * Checks if the path between the current piece cell and the destination is free.
     * @param to The destination cell.
     * @return the list of all cells in between (do not contain the cell from and to).
     */
    abstract public List<Cell> path(Cell to);

    /**
     * Returns weather the piece has moved or not.
     * @return True if the piece has moved, false otherwise.
     */
    public boolean getHasMoved(){return true;}
}
