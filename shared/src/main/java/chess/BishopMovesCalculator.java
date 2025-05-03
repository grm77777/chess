package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class BishopMovesCalculator extends PieceMovesCalculator{

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessPiece.PieceType type = null;

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
//        type = ChessPiece.PieceType.BISHOP;
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        checkToEdge(myPosition, Directions.UP, Directions.RIGHT, moves, type);
        checkToEdge(myPosition, Directions.UP, Directions.LEFT, moves, type);
        checkToEdge(myPosition, Directions.DOWN, Directions.LEFT, moves, type);
        checkToEdge(myPosition, Directions.DOWN, Directions.RIGHT, moves, type);
        return moves;
    }

    public void checkToEdge(ChessPosition currPosition, Directions row, Directions col, Collection<ChessMove> moves, ChessPiece.PieceType type) {
        if (!this.isEmpty(currPosition)) {
            return;
        }
        ChessPosition nextPosition = this.getNextPosition(currPosition, row, col);
        moves.add(new ChessMove(myPosition, nextPosition, type));
        checkToEdge(nextPosition, row, col, moves, type);
    }

    @Override
    public boolean isEmpty(ChessPosition position) {
        if (board.getPiece(position) != null && !position.equals(myPosition)) {
            return false;
        }
        return super.isEmpty(position);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BishopMovesCalculator that = (BishopMovesCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(myPosition, that.myPosition) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), board, myPosition, type);
    }
}
