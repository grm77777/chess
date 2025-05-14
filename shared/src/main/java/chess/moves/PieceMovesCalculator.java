package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Calculates the possible moves of a chess piece
 * <p>
 * Note: This is a parent class that will return an empty ArrayList if called.
 */
public abstract class PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor pieceColor;
    private ArrayList<ChessMove> moves;

    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
        this.moves = new ArrayList<>();
    }

    /**
     * The various different chess piece movement options
     */
    public enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT,
        DOUBLE_UP,
        DOUBLE_DOWN,
        DOUBLE_RIGHT,
        DOUBLE_LEFT,
        STAY
    }

    public void setMoves(ArrayList<ChessMove> moves) {
        this.moves = moves;
    }

    public abstract ArrayList<ChessMove> pieceMoves();

    /**
     * Adds all the possible moves along a diagonal or row, stopping either at the end of the board,
     * when blocked by a teammate, or when presented the chance to capture an enemy piece.
     *
     * @param currPosition The last position that was checked.
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     * @param type The type of the promotion piece (null of not applicable).
     */
    public void checkToEdge(ChessPosition currPosition, Direction row, Direction col, ChessPiece.PieceType type) {
        if (endSpace(currPosition, row, col)) {
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
        checkToEdge(nextPosition, row, col, type);
    }

    /**
     * Adds all the possible moves surrounding a piece along the given directions, excluding when
     * blocked by a teammate.
     *
     * @param currPosition The current position to check.
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     * @param type The type of the piece.
     */
    public void checkSurrounding(ChessPosition currPosition, Direction row, Direction col, ChessPiece.PieceType type) {
        if (!endSpace(currPosition, row, col)) {
            ChessPosition nextPosition = getNextPosition(currPosition, row, col);
            ChessPiece piece = board.getPiece(nextPosition);
            if (piece == null || piece.getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, nextPosition, type));
            }
        }
    }

    /**
     * Check whether the next position would be off the end of the chessboard
     *
     * @param position The position to check
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     * @return True if the position is an end space, false if not.
     */
    public boolean endSpace(ChessPosition position, Direction row, Direction col) {
        int nextRow = getNextRow(position, row);
        if (nextRow >= 9 || nextRow <= 0) {
            return true;
        }
        int nextCol = getNextColumn(position, col);
        if (nextCol >= 9 || nextCol <= 0) {
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
    public ChessPosition getNextPosition(ChessPosition currPosition, Direction row, Direction col) {
        int nextRow = getNextRow(currPosition, row);
        int nextCol = getNextColumn(currPosition, col);
        return new ChessPosition(nextRow, nextCol);
    }

    /**
     * Gets the row of the next position on the board based off the given direction.
     *
     * @param currPosition The current position
     * @param row The direction to travel vertically.
     * @return The row of the next position on the board.
     */
    public int getNextRow(ChessPosition currPosition, Direction row) {
        if (row == Direction.UP) {
            return currPosition.getRow() + 1;
        } else if (row == Direction.DOWN) {
            return currPosition.getRow() - 1;
        } else if (row == Direction.DOUBLE_UP) {
            return currPosition.getRow() + 2;
        } else if (row == Direction.DOUBLE_DOWN) {
            return currPosition.getRow() - 2;
        }else {
            return currPosition.getRow();
        }
    }

    /**
     * Gets the column of the next position on the board based off the given direction.
     *
     * @param currPosition The current position
     * @param col The direction to travel vertically.
     * @return The column of the next position on the board.
     */
    public int getNextColumn(ChessPosition currPosition, Direction col) {
        if (col == Direction.RIGHT) {
            return currPosition.getColumn() + 1;
        } else if (col == Direction.LEFT) {
            return currPosition.getColumn() - 1;
        } else if (col == Direction.DOUBLE_RIGHT) {
            return currPosition.getColumn() + 2;
        } else if (col == Direction.DOUBLE_LEFT) {
            return currPosition.getColumn() - 2;
        } else {
            return currPosition.getColumn();
        }
    }

}
