package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Calculates the possible moves of a Queen piece
 */
public class QueenMovesCalculator extends PieceMovesCalculator{

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor pieceColor;

    public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(board, myPosition, pieceColor);
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
    }

    /**
     * Checks all the possible diagonal moves for the piece.
     *
     * @return An ArrayList<ChessMove></ChessMove> with all possible diagonal moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        checkToEdge(myPosition, Direction.UP, Direction.RIGHT, moves, null);
        checkToEdge(myPosition, Direction.UP, Direction.LEFT, moves, null);
        checkToEdge(myPosition, Direction.DOWN, Direction.LEFT, moves, null);
        checkToEdge(myPosition, Direction.DOWN, Direction.RIGHT, moves, null);
        checkToEdge(myPosition, Direction.UP, Direction.STAY, moves, null);
        checkToEdge(myPosition, Direction.DOWN, Direction.STAY, moves, null);
        checkToEdge(myPosition, Direction.STAY, Direction.LEFT, moves, null);
        checkToEdge(myPosition, Direction.STAY, Direction.RIGHT, moves, null);
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
        QueenMovesCalculator that = (QueenMovesCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(myPosition, that.myPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), board, myPosition);
    }
}

