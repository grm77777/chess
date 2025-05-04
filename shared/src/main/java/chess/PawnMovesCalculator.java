package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Calculates the possible moves of a Pawn piece
 */
public class PawnMovesCalculator extends PieceMovesCalculator{

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor pieceColor;

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(board, myPosition, pieceColor);
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
    }

    /**
     * Checks all the possible moves for the piece.
     *
     * @return An ArrayList of ChessMoves with all possible moves.
     */
    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            whitePieceMoves(moves);
        } else {
            blackPieceMoves(moves);
        }
        return moves;
    }

    /**
     * Checks all the possible moves for a white piece.
     *
     * @param moves The Collection of ChessMoves to append valid moves to.
     */
    public void whitePieceMoves(Collection<ChessMove> moves) {
        boolean initialMove = myPosition.getRow() == 2;
        boolean promotionMove = myPosition.getRow() == 7;
        straightMoves(Direction.UP, moves, initialMove, promotionMove);
        diagonalMoves(Direction.UP, Direction.RIGHT, moves, promotionMove);
        diagonalMoves(Direction.UP, Direction.LEFT, moves, promotionMove);
    }

    /**
     * Checks all the possible moves for a black piece.
     *
     * @param moves The Collection of ChessMoves to append valid moves to.
     */
    public void blackPieceMoves(Collection<ChessMove> moves) {
        boolean initialMove = myPosition.getRow() == 7;
        boolean promotionMove = myPosition.getRow() == 2;
        straightMoves(Direction.DOWN, moves, initialMove, promotionMove);
        diagonalMoves(Direction.DOWN, Direction.RIGHT, moves, promotionMove);
        diagonalMoves(Direction.DOWN, Direction.LEFT, moves, promotionMove);
    }

    /**
     * Adds all the possible straight moves for a piece in the given direction, excluding when
     * blocked by another piece.
     *
     * @param row The direction to travel vertically.
     * @param moves The Collection of ChessMoves to append valid moves to.
     * @param initialMove A boolean tracking whether the move is the initial move for the piece.
     * @param promotionMove A boolean tracking whether the move is a promotion move for the piece.
     */
    public void straightMoves(Direction row, Collection<ChessMove> moves, boolean initialMove, boolean promotionMove) {
        if (promotionMove) {
            promotionOptions(myPosition, row, moves);
        } else if (initialMove) {
                initialMove(myPosition, row, moves, null);
        } else {
            checkForward(myPosition, row, moves, null);
        }
    }

    /**
     * Adds all the possible diagonal moves for a piece in the given direction, when
     * able to collect an enemy piece.
     *
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     * @param moves The Collection of ChessMoves to append valid moves to.
     * @param promotionMove A boolean tracking whether the move is a promotion move for the piece.
     */
    public void diagonalMoves(Direction row, Direction col, Collection<ChessMove> moves, boolean promotionMove) {
        if(enemyPiece(row, col)) {
            if (promotionMove) {
                promotionOptions(myPosition, row, col, moves);
            } else {
                checkSurrounding(myPosition, row, col, moves, null);
            }

        }
    }

    /**
     * Adds all promotion combinations for a given movement. Pulls from this class's method to allow for moves
     * that cannot collect an enemy piece.
     *
     * @param currPosition The current position to check.
     * @param row The direction to travel vertically.
     * @param moves The Collection of ChessMoves to append valid moves to.
     */
    public void promotionOptions(ChessPosition currPosition, Direction row, Collection<ChessMove> moves) {
        checkForward(currPosition, row, moves, ChessPiece.PieceType.BISHOP);
        checkForward(currPosition, row, moves, ChessPiece.PieceType.ROOK);
        checkForward(currPosition, row, moves, ChessPiece.PieceType.KNIGHT);
        checkForward(currPosition, row, moves, ChessPiece.PieceType.QUEEN);
    }

    /**
     * Adds all promotion combinations for a given movement. Pulls from super method to allow for pieces
     * that will be collecting an enemy piece.
     *
     * @param currPosition The current position to check.
     * @param row The direction to travel vertically.
     * @param col The direction to travel horizontally.
     * @param moves The Collection of ChessMoves to append valid moves to.
     */
    public void promotionOptions(ChessPosition currPosition, Direction row, Direction col, Collection<ChessMove> moves) {
        checkSurrounding(currPosition, row, col, moves, ChessPiece.PieceType.BISHOP);
        checkSurrounding(currPosition, row, col, moves, ChessPiece.PieceType.ROOK);
        checkSurrounding(currPosition, row, col, moves, ChessPiece.PieceType.KNIGHT);
        checkSurrounding(currPosition, row, col, moves, ChessPiece.PieceType.QUEEN);
    }

    /**
     * Adds a possible forward move for a piece along the given direction, excluding when
     * blocked by another piece.
     *
     * @param currPosition The current position to check.
     * @param row The direction to travel vertically.
     * @param moves The Collection of ChessMoves to append valid moves to.
     * @param promotionType The type of the promotion piece.
     */
    public void checkForward(ChessPosition currPosition, Direction row, Collection<ChessMove> moves, ChessPiece.PieceType promotionType) {
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
     * @param moves The Collection of ChessMoves to append valid moves to.
     * @param promotionType The type of the promotion piece.
     */
    public void initialMove(ChessPosition currPosition, Direction row, Collection<ChessMove> moves, ChessPiece.PieceType promotionType) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        PawnMovesCalculator that = (PawnMovesCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(myPosition, that.myPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), board, myPosition);
    }
}
