package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Calculates the possible moves of a Knight piece
 */
public class KnightMovesCalculator extends PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor pieceColor;

    public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(board, myPosition, pieceColor);
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
    }

    /**
     * Checks all the possible surrounding moves for the piece.
     *
     * @return An ArrayList of ChessMoves with all possible moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        checkSurrounding(myPosition, Direction.DOUBLE_UP, Direction.RIGHT, moves, null);
        checkSurrounding(myPosition, Direction.DOUBLE_UP, Direction.LEFT, moves, null);
        checkSurrounding(myPosition, Direction.UP, Direction.DOUBLE_RIGHT, moves, null);
        checkSurrounding(myPosition, Direction.UP, Direction.DOUBLE_LEFT, moves, null);
        checkSurrounding(myPosition, Direction.DOUBLE_DOWN, Direction.RIGHT, moves, null);
        checkSurrounding(myPosition, Direction.DOUBLE_DOWN, Direction.LEFT, moves, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.DOUBLE_RIGHT, moves, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.DOUBLE_LEFT, moves, null);
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        KnightMovesCalculator that = (KnightMovesCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(myPosition, that.myPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), board, myPosition);
    }
}
