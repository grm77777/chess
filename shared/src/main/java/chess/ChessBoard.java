package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
        this.resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;
        board[row][column] = piece;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;
        return board[row][column];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        placePieces(ChessGame.TeamColor.WHITE, 1);
        placePieces(ChessGame.TeamColor.BLACK, 8);
    }

    /**
     * Places pieces of the given color on the board in the position of the default starting board
     * (How the game of chess normally starts)
     *
     * @param color The color of the pieces to place.
     * @param endRow The row marking the back row of pieces for that color
     *               (1 for white, 8 for black).
     */
    private void placePieces(ChessGame.TeamColor color, int endRow) {
        addPiece(new ChessPosition(endRow, 1), new ChessPiece(color, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(endRow, 2), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(endRow, 3), new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(endRow, 4), new ChessPiece(color, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(endRow, 5), new ChessPiece(color, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(endRow, 6), new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(endRow, 7), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(endRow, 8), new ChessPiece(color, ChessPiece.PieceType.ROOK));

        int pawnRow;
        if (color == ChessGame.TeamColor.WHITE) {
            pawnRow = 2;
        } else {
            pawnRow = 7;
        }
        for (int col = 1; col <= 8; col++) {
            addPiece(new ChessPosition(pawnRow, col), new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        String str = "";
        for (ChessPiece[] row : board) {
            for (ChessPiece col : row) {
                str = str + " | " + col;
            }
            str += " |\n";
        }
        return str;
    }
}
