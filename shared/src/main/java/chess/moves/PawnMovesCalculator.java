package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Calculates the possible moves of a Pawn piece
 */
public class PawnMovesCalculator extends PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor pieceColor;
    private final ArrayList<ChessMove> moves;

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(board, myPosition, pieceColor);
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
        moves = new ArrayList<>();
        super.setMoves(moves);
    }

    /**
     * Checks all the possible moves for the piece.
     *
     * @return An ArrayList of ChessMoves with all possible moves.
     */
    @Override
    public ArrayList<ChessMove> pieceMoves() {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            whitePieceMoves();
        } else {
            blackPieceMoves();
        }
        return moves;
    }

    /**
     * Checks all the possible moves for a white piece.
     *
     */
    public void whitePieceMoves() {
        boolean initialMove = myPosition.getRow() == 2;
        boolean promotionMove = myPosition.getRow() == 7;
        straightMoves(Direction.UP, initialMove, promotionMove);
        diagonalMoves(Direction.UP, Direction.RIGHT, promotionMove);
        diagonalMoves(Direction.UP, Direction.LEFT, promotionMove);
    }

    /**
     * Checks all the possible moves for a black piece.
     *
     */
    public void blackPieceMoves() {
        boolean initialMove = myPosition.getRow() == 7;
        boolean promotionMove = myPosition.getRow() == 2;
        straightMoves(Direction.DOWN, initialMove, promotionMove);
        diagonalMoves(Direction.DOWN, Direction.RIGHT, promotionMove);
        diagonalMoves(Direction.DOWN, Direction.LEFT, promotionMove);
    }

    /**
     * Adds all the possible straight moves for a piece in the given direction, excluding when
     * blocked by another piece.
     *
     * @param row The direction to travel vertically.
     * @param initialMove A boolean tracking whether the move is the initial move for the piece.
     * @param promotionMove A boolean tracking whether the move is a promotion move for the piece.
     */
    public void straightMoves(Direction row, boolean initialMove, boolean promotionMove) {
        if (promotionMove) {
            promotionOptions(myPosition, row);
        } else if (initialMove) {
                initialMove(myPosition, row, null);
        } else {
            checkForward(myPosition, row, null);
        }
    }

    /**
     * Adds all the possible diagonal moves for a piece in the given direction, when
     * able to collect an enemy piece.
     *
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     * @param promotionMove A boolean tracking whether the move is a promotion move for the piece.
     */
    public void diagonalMoves(Direction row, Direction col, boolean promotionMove) {
        if(enemyPiece(row, col)) {
            if (promotionMove) {
                promotionOptions(myPosition, row, col);
            } else {
                checkSurrounding(myPosition, row, col, null);
            }

        }
    }

    /**
     * Adds all promotion combinations for a given movement. Pulls from this class's method to allow for moves
     * that cannot collect an enemy piece.
     *
     * @param currPosition The current position to check.
     * @param row The direction to travel vertically.
     */
    public void promotionOptions(ChessPosition currPosition, Direction row) {
        checkForward(currPosition, row, ChessPiece.PieceType.BISHOP);
        checkForward(currPosition, row, ChessPiece.PieceType.ROOK);
        checkForward(currPosition, row, ChessPiece.PieceType.KNIGHT);
        checkForward(currPosition, row, ChessPiece.PieceType.QUEEN);
    }

    /**
     * Adds all promotion combinations for a given movement. Pulls from super method to allow for pieces
     * that will be collecting an enemy piece.
     *
     * @param currPosition The current position to check.
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     */
    public void promotionOptions(ChessPosition currPosition, Direction row, Direction col) {
        checkSurrounding(currPosition, row, col, ChessPiece.PieceType.BISHOP);
        checkSurrounding(currPosition, row, col, ChessPiece.PieceType.ROOK);
        checkSurrounding(currPosition, row, col, ChessPiece.PieceType.KNIGHT);
        checkSurrounding(currPosition, row, col, ChessPiece.PieceType.QUEEN);
    }

    /**
     * Adds a possible forward move for a piece along the given direction, excluding when
     * blocked by another piece.
     *
     * @param currPosition The current position to check.
     * @param row The direction to travel vertically.
     * @param promotionType The type of the promotion piece.
     */
    public void checkForward(ChessPosition currPosition, Direction row, ChessPiece.PieceType promotionType) {
        if (!endSpace(currPosition, row, Direction.STAY)) {
            ChessPosition nextPosition = getNextPosition(currPosition, row, Direction.STAY);
            ChessPiece piece = board.getPiece(nextPosition);
            if (piece == null) {
                moves.add(new ChessMove(myPosition, nextPosition, promotionType));
            }
        }
    }

    /**
     * Adds a possible initial moves for a piece along the given direction (up to two spaces in front),
     * excluding when blocked by another piece.
     *
     * @param currPosition The current position to check.
     * @param row The direction to travel vertically.
     * @param promotionType The type of the promotion piece.
     */
    public void initialMove(ChessPosition currPosition, Direction row, ChessPiece.PieceType promotionType) {
        ChessPosition nextPosition = getNextPosition(currPosition, row, Direction.STAY);
        ChessPiece piece = board.getPiece(nextPosition);
        if (piece == null) {
            moves.add(new ChessMove(myPosition, nextPosition, promotionType));
            ChessPosition secondPosition = getNextPosition(nextPosition, row, Direction.STAY);
            piece = board.getPiece(secondPosition);
            if (piece == null) {
                moves.add(new ChessMove(myPosition, secondPosition, promotionType));
            }
        }
    }

    /**
     * Checks whether a space the piece may move to contains an enemy piece.
     *
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     * @return True if there is an enemy piece, false if not.
     */
    public boolean enemyPiece(Direction row, Direction col) {
        if (endSpace(myPosition, row, col)) {
            return false;
        }
        ChessPosition nextPosition = getNextPosition(myPosition, row, col);
        ChessPiece piece = board.getPiece(nextPosition);
        if (piece == null) {
            return false;
        }
        return piece.getTeamColor() != pieceColor;
    }
}
