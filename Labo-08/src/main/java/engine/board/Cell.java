package engine.board;

import java.util.Objects;

/**
 * A class representing a cell on the chess board.
 * Stores the x and y coordinates of the cell.
 *
 * @author Émilie Bressoud, Kevin Farine, Timothée Van Hove
 */
public class Cell {

    /**
     * The x coordinate
     */
    private final int x;

    /**
     * The y coordinate
     */
    private final int y;

    /**
     * Constructs a new Cell with the given x and y coordinates.
     *
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x coordinate of the cell.
     *
     * @return The x coordinate of the cell.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the cell.
     *
     * @return The y coordinate of the cell.
     */
    public int getY() {
        return y;
    }

    /**
     * Creates a hashcode for the cell
     *
     * @return hashcode of the cell
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Checks if the cell is equal to another object
     *
     * @param o The object to compare
     * @return true if the object is a cell and has the same coordinates
     */
    @Override
    public boolean equals(Object o) {
        // Overrides the default implementation to compare the x and y coordinates of the cells
        if (o == this)
            return true;

        if (o == null || o.getClass() != getClass())
            return false;

        Cell other = (Cell) o;
        return x == other.x && y == other.y;
    }

    /**
     * Calculates the distance between two cells on x-axis
     *
     * @param to The cell to compare distance on x-axis with this cell
     * @return the distance on x-axis between this cell and the cell to
     */
    public int distanceX(Cell to) {
        return Math.abs(x - to.getX());
    }

    /**
     * Calculates the distance between two cells on y-axis
     *
     * @param to The cell to compare distance on y-axis with this cell
     * @return the distance on y-axis between this cell and the cell to
     */
    public int distanceY(Cell to) {
        return Math.abs(y - to.getY());
    }

    /**
     * Gives the direction between two cells on x-axis
     *
     * @param to The cell to get the direction from
     * @return 1 if the cell to is on the right of this cell, -1 if it's on the left, 0 if it's on the same column
     */
    public int directionX(Cell to) {
        return Integer.signum(to.getX() - x);
    }

    /**
     * Give the direction between two cells on y-axis
     *
     * @param to The cell to get the direction from
     * @return 1 if the cell to is on the top of this cell, -1 if it's on the bottom, 0 if it's on the same row
     */
    public int directionY(Cell to) {
        return Integer.signum(to.getY() - y);
    }
}
