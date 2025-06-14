package chess;

import chess.moves.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {

    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * @param type which type of chess piece to set the piece to
     */
    public void setPieceType(PieceType type) {
        this.type = type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type==PieceType.PAWN) {
            PawnMovesCalculator calc = new PawnMovesCalculator(board, myPosition, pieceColor);
            return calc.pieceMoves();
        }
        if (type==PieceType.BISHOP) {
            BishopMovesCalculator calc = new BishopMovesCalculator(board, myPosition, pieceColor);
            return calc.pieceMoves();
        } else if (type==PieceType.ROOK) {
            RookMovesCalculator calc = new RookMovesCalculator(board, myPosition, pieceColor);
            return calc.pieceMoves();
        } else if (type==PieceType.KNIGHT) {
            KnightMovesCalculator calc = new KnightMovesCalculator(board, myPosition, pieceColor);
            return calc.pieceMoves();
        } else if (type==PieceType.QUEEN) {
            QueenMovesCalculator calc = new QueenMovesCalculator(board, myPosition, pieceColor);
            return calc.pieceMoves();
        } else {
            KingMovesCalculator calc = new KingMovesCalculator(board, myPosition, pieceColor);
            return calc.pieceMoves();
        }
    }

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
