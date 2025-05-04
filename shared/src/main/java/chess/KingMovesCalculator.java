package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Calculates the possible moves of a Queen piece
 */
public class KingMovesCalculator extends PieceMovesCalculator{

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
     * Checks all the possible diagonal moves for the piece.
     *
     * @return An ArrayList<ChessMove></ChessMove> with all possible diagonal moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        checkSurrounding(myPosition, Directions.UP, Directions.RIGHT, moves, null);
        checkSurrounding(myPosition, Directions.UP, Directions.LEFT, moves, null);
        checkSurrounding(myPosition, Directions.DOWN, Directions.LEFT, moves, null);
        checkSurrounding(myPosition, Directions.DOWN, Directions.RIGHT, moves, null);
        checkSurrounding(myPosition, Directions.UP, Directions.STAY, moves, null);
        checkSurrounding(myPosition, Directions.DOWN, Directions.STAY, moves, null);
        checkSurrounding(myPosition, Directions.STAY, Directions.LEFT, moves, null);
        checkSurrounding(myPosition, Directions.STAY, Directions.RIGHT, moves, null);
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
