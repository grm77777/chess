package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Calculates the possible moves of a Rook piece
 */
public class RookMovesCalculator extends PieceMovesCalculator{

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type = null;

    public RookMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(board, myPosition, pieceColor);
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
//        type = ChessPiece.PieceType.ROOK;
    }

    /**
     * Checks all the possible straight moves for the piece.
     *
     * @return An ArrayList<ChessMove></ChessMove> with all possible straight moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
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
        RookMovesCalculator that = (RookMovesCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(myPosition, that.myPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), board, myPosition);
    }
}
