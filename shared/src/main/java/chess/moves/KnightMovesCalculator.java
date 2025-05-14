package chess.moves;

import chess.*;

import java.util.ArrayList;

/**
 * Calculates the possible moves of a Knight piece
 */
public class KnightMovesCalculator extends PieceMovesCalculator {

    private final ChessPosition myPosition;

    public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(board, myPosition, pieceColor);
        this.myPosition = myPosition;
    }

    /**
     * Checks all the possible surrounding moves for the piece.
     *
     * @return An ArrayList of ChessMoves with all possible moves.
     */
    @Override
    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        super.setMoves(moves);
        checkSurrounding(myPosition, Direction.DOUBLE_UP, Direction.RIGHT, null);
        checkSurrounding(myPosition, Direction.DOUBLE_UP, Direction.LEFT, null);
        checkSurrounding(myPosition, Direction.UP, Direction.DOUBLE_RIGHT, null);
        checkSurrounding(myPosition, Direction.UP, Direction.DOUBLE_LEFT, null);
        checkSurrounding(myPosition, Direction.DOUBLE_DOWN, Direction.RIGHT, null);
        checkSurrounding(myPosition, Direction.DOUBLE_DOWN, Direction.LEFT, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.DOUBLE_RIGHT, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.DOUBLE_LEFT, null);
        return moves;
    }
}
