package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Calculates the possible moves of a chess piece
 * <p>
 * Note: This is a parent class that will return an empty ArrayList if called.
 */
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

    /**
     * @return an empty ArrayList<ChessMove>()
     */
    public Collection<ChessMove> pieceMoves() {
        return new ArrayList<ChessMove>();
    }

    /**
     * Adds all the possible moves along a diagonal or row, stopping either at the end of the board,
     * when blocked by a teammate, or when presented the chance to capture an enemy piece.
     *
     * @param currPosition The last position that was checked.
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     * @param moves The Collection of ChessMoves to append valid moves to.
     * @param type The type of the piece.
     */
    public void checkToEdge(ChessPosition currPosition, Directions row, Directions col, Collection<ChessMove> moves, ChessPiece.PieceType type) {
        if (endSpace(currPosition)) {
            return;
        }
        ChessPosition nextPosition = getNextPosition(currPosition, row, col);
        ChessPiece piece = board.getPiece(nextPosition);
        if (piece != null) {
            boolean sameTeam = piece.getTeamColor() == pieceColor;
            if (!sameTeam) {
                moves.add(new ChessMove(myPosition, nextPosition, type));
            }
            return;
        }
        moves.add(new ChessMove(myPosition, nextPosition, type));
        checkToEdge(nextPosition, row, col, moves, type);
    }

    /**
     * Check whether a position is at the end of the chessboard
     *
     * @param position The position to check
     * @return True if the position is an end space, false if not.
     */
    public boolean endSpace(ChessPosition position) {
        if (position.getRow() >= 8 || position.getRow() <= 1) {
            return true;
        } else if (position.getColumn() >= 8 || position.getColumn() <= 1) {
            return true;
        }
        return false;
    }

    /**
     * Gets the next position on the board based off the given directions.
     *
     * @param currPosition The current position
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     * @return The next position on the board.
     */
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
