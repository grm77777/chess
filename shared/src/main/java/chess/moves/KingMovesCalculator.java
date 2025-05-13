package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Calculates the possible moves of a King piece
 */
public class KingMovesCalculator extends PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor pieceColor;

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
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
        checkSurrounding(myPosition, Direction.UP, Direction.RIGHT, moves, null);
        checkSurrounding(myPosition, Direction.UP, Direction.LEFT, moves, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.LEFT, moves, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.RIGHT, moves, null);
        checkSurrounding(myPosition, Direction.UP, Direction.STAY, moves, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.STAY, moves, null);
        checkSurrounding(myPosition, Direction.STAY, Direction.LEFT, moves, null);
        checkSurrounding(myPosition, Direction.STAY, Direction.RIGHT, moves, null);
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
        KingMovesCalculator that = (KingMovesCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(myPosition, that.myPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), board, myPosition);
    }
}
