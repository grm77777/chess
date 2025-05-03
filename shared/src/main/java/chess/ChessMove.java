package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;

    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        System.out.println(chessMove);
        boolean b = promotionPiece == chessMove.getPromotionPiece();
        System.out.println("\t" + startPosition.equals(chessMove.getStartPosition()));
        System.out.println("\t" + endPosition.equals(chessMove.getEndPosition()));
        System.out.println("\t" + b);
        System.out.println("\t" + promotionPiece);
        System.out.println("\t" + chessMove.getPromotionPiece());
        return startPosition.equals(chessMove.getStartPosition()) &&
                endPosition.equals(chessMove.getEndPosition()) &&
                promotionPiece == chessMove.getPromotionPiece();
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    @Override
    public String toString() {
        return "Move{" + startPosition +
                " -> " + endPosition + "}";
    }
}
