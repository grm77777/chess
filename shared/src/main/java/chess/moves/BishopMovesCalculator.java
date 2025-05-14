package chess.moves;

import chess.*;

import java.util.ArrayList;

/**
 * Calculates the possible moves of a Bishop piece
 */
public class BishopMovesCalculator extends PieceMovesCalculator {

    private final ChessPosition myPosition;

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(board, myPosition, pieceColor);
        this.myPosition = myPosition;
    }

    /**
     * Checks all the possible diagonal moves for the piece.
     *
     * @return An ArrayList of ChessMoves with all possible moves.
     */
    @Override
    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        super.setMoves(moves);
        checkToEdge(myPosition, Direction.UP, Direction.RIGHT, null);
        checkToEdge(myPosition, Direction.UP, Direction.LEFT, null);
        checkToEdge(myPosition, Direction.DOWN, Direction.LEFT, null);
        checkToEdge(myPosition, Direction.DOWN, Direction.RIGHT, null);
        return moves;
    }
}
