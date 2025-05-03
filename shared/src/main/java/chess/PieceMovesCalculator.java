package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

public class PieceMovesCalculator {

    private ChessBoard board;
    private ChessPosition myPosition;
    private final ChessGame.TeamColor pieceColor;

    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece movement options
     */
    public enum Directions {
        UP,
        DOWN,
        RIGHT,
        LEFT,
        STAY
    }

    public Collection<ChessMove> pieceMoves() {
        return new ArrayList<ChessMove>();
    }

    public boolean notValid(ChessPosition position) {
        if (position.getRow() >= 8 || position.getRow() <= 1) {
            return true;
        } else if (position.getColumn() >= 8 || position.getColumn() <= 1) {
            return true;
        }
        return false;
    }

    public ChessPosition getNextPosition(ChessPosition currPosition, Directions row, Directions col) {
        int nextRow;
        if (row == Directions.UP) {
            nextRow = currPosition.getRow() + 1;
        } else {
            nextRow = currPosition.getRow() - 1;
        }
        int nextCol;
        if (col == Directions.RIGHT) {
            nextCol = currPosition.getColumn() + 1;
        } else {
            nextCol = currPosition.getColumn() - 1;
        }
        return new ChessPosition(nextRow, nextCol);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PieceMovesCalculator that = (PieceMovesCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(myPosition, that.myPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, myPosition);
    }

}
