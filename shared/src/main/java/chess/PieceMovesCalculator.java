package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

public class PieceMovesCalculator {

    private ChessBoard board;
    private ChessPosition myPosition;

    public PieceMovesCalculator() {}

    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public static void main(String [] args) {
        ChessPosition p1 = new ChessPosition(1, 1);
        ChessPosition p2 = new ChessPosition(1, 1);
        System.out.println(p1.equals(p2));
        ChessMove m1 = new ChessMove(p1, p2, ChessPiece.PieceType.BISHOP);
        ChessMove m2 = new ChessMove(p1, p2, ChessPiece.PieceType.BISHOP);
        System.out.println(m1.equals(m2));
        ChessPiece b1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece b2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        System.out.println(b1.equals(b2));
        ChessBoard brd1 = new ChessBoard();
        ChessBoard brd2 = new ChessBoard();
        System.out.println(brd1.equals(brd2));
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

    public boolean isEmpty(ChessPosition position) {
        if (position.getRow() >= 8 || position.getRow() <= 1) {
            return false;
        } else if (position.getColumn() >= 8 || position.getColumn() <= 1) {
            return false;
        }
        return true;
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
