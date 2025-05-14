package chess.moves;

import chess.*;

import java.util.ArrayList;

/**
 * Calculates the possible moves of a King piece
 */
public class KingMovesCalculator extends PieceMovesCalculator {

    private final ChessPosition myPosition;

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
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
        checkSurrounding(myPosition, Direction.UP, Direction.RIGHT, null);
        checkSurrounding(myPosition, Direction.UP, Direction.LEFT, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.LEFT, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.RIGHT, null);
        checkSurrounding(myPosition, Direction.UP, Direction.STAY, null);
        checkSurrounding(myPosition, Direction.DOWN, Direction.STAY, null);
        checkSurrounding(myPosition, Direction.STAY, Direction.LEFT, null);
        checkSurrounding(myPosition, Direction.STAY, Direction.RIGHT, null);
        return moves;
    }
}
