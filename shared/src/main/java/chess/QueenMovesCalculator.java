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
    private final ChessPiece.PieceType type = null;

    public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(board, myPosition, pieceColor);
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
//        type = ChessPiece.PieceType.QUEEN;
    }

    /**
     * Checks all the possible diagonal moves for the piece.
     *
     * @return An ArrayList<ChessMove></ChessMove> with all possible diagonal moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        checkToEdge(myPosition, Directions.UP, Directions.RIGHT, moves, type);
        checkToEdge(myPosition, Directions.UP, Directions.LEFT, moves, type);
        checkToEdge(myPosition, Directions.DOWN, Directions.LEFT, moves, type);
        checkToEdge(myPosition, Directions.DOWN, Directions.RIGHT, moves, type);
        checkToEdge(myPosition, Directions.UP, Directions.STAY, moves, type);
        checkToEdge(myPosition, Directions.DOWN, Directions.STAY, moves, type);
        checkToEdge(myPosition, Directions.STAY, Directions.LEFT, moves, type);
        checkToEdge(myPosition, Directions.STAY, Directions.RIGHT, moves, type);
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

