package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition implements Cloneable {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public ChessPosition clone() {
        try {
            return (ChessPosition) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.getRow() && col == that.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {return colToString() + row;
    }

    private String colToString() {
        return switch (col) {
            case 8 -> "a";
            case 7 -> "b";
            case 6 -> "c";
            case 5 -> "d";
            case 4 -> "e";
            case 3 -> "f";
            case 2 -> "g";
            case 1 -> "h";
            default -> "";
        };
    }
}
