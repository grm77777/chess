package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable {

    private ChessBoard board;
    private TeamColor currTurn;
    private boolean gameOver;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        currTurn = TeamColor.WHITE;
        gameOver = false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currTurn = team;
    }

    public boolean getGameOver() { return gameOver; }

    public void gameOver() { gameOver = true; }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : potentialMoves) {
            if (isSafeMove(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Checks if a move will put the team's king in check
     *
     * @param move chess move to test
     * @return True if move avoids check
     */
    private boolean isSafeMove(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessGame testGame = clone();
        ChessBoard testBoard = testGame.getBoard();
        ChessPiece piece = testBoard.getPiece(startPosition);
        testBoard.removePiece(startPosition);
        testBoard.addPiece(endPosition, piece);
        return !testGame.isInCheck(piece.getTeamColor());
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null || piece.getTeamColor() != currTurn) {
            throw new InvalidMoveException("Move attempted on other team's turn.");
        }
        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Move not valid.");
        }
        board.removePiece(startPosition);
        board.addPiece(endPosition, piece);
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        if (promotionPiece != null) {
            piece.setPieceType(promotionPiece);
        }
        nextTurn();
    }

    /**
     * Updates the turn to the next team
     *
     */
    private void nextTurn() {
        if (currTurn == TeamColor.WHITE) {
            currTurn = TeamColor.BLACK;
        } else {
            currTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currPosition = new ChessPosition(row, col);
                if (threatensKing(teamColor, currPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the ChessPosition threatens the king is in check
     *
     * @param kingColor the team's king to check
     * @param currPosition the position of the piece to check
     * @return True if the specified team is in check
     */
    private boolean threatensKing(TeamColor kingColor, ChessPosition currPosition) {
        ChessPosition kingPosition = board.findPiece(ChessPiece.PieceType.KING, kingColor);
        ChessPiece currPiece = board.getPiece(currPosition);
        if (currPiece != null && currPiece.getTeamColor() != kingColor) {
            Collection<ChessMove> pieceMoves = currPiece.pieceMoves(board, currPosition);
            for (ChessMove move : pieceMoves) {
                if (move.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return noValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        return noValidMoves(teamColor);
    }

    /**
     * Determines if the given team has any valid moves
     *
     * @param teamColor which team to check for valid moves
     * @return True if the specified team still has at least one valid move
     */
    private boolean noValidMoves(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currPosition = new ChessPosition(row, col);
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece != null && currPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = validMoves(currPosition);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        gameOver = true;
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public ChessGame clone() {
        try {
            ChessGame clone = (ChessGame) super.clone();
            ChessBoard cloneBoard = board.clone();
            clone.setBoard(cloneBoard);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(getBoard(), chessGame.getBoard()) && currTurn == chessGame.currTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoard(), currTurn);
    }
}
