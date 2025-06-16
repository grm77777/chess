package ui;

import chess.*;
import java.util.Collection;

public class DrawChessBoard {

    private static final int SQUARE_LENGTH = 1;
    private boolean lightSquare;
    private final ChessBoard board;
    private final StringBuilder string;

    public DrawChessBoard(ChessBoard board) {
        this.board = board;
        lightSquare = false;
        string = new StringBuilder();
    }

    public String drawBoardWhite() {
        for (int row = 9; row >= 0; row--) {
            string.append(EscapeSequences.SET_BG_COLOR_BLACK + "\t");
            for (int col = 0; col < 10; col++) {
                drawSquare(row, col);
            }
            lightSquare = !lightSquare;
            string.append(EscapeSequences.SET_BG_COLOR_BLACK + "\n");
        }
        return string.toString();
    }

    public String drawBoardBlack() {
        for (int row = 0; row < 10; row++) {
            string.append(EscapeSequences.SET_BG_COLOR_BLACK + "\t");
            for (int col = 9; col >= 0; col--) {
                drawSquare(row, col);
            }
            lightSquare = !lightSquare;
            setBlack();
            string.append(EscapeSequences.SET_BG_COLOR_BLACK + "\n");
        }
        return string.toString();
    }

    public String drawValidMovesWhite(Collection<ChessPosition> validMoves) {
        for (int row = 9; row >= 0; row--) {
            string.append(EscapeSequences.SET_BG_COLOR_BLACK + "\t");
            for (int col = 0; col < 10; col++) {
                drawSquare(row, col, validMoves);
            }
            lightSquare = !lightSquare;
            string.append(EscapeSequences.SET_BG_COLOR_BLACK + "\n");
        }
        return string.toString();
    }

    public String drawValidMovesBlack(Collection<ChessPosition> validMoves) {
        for (int row = 0; row < 10; row++) {
            string.append(EscapeSequences.SET_BG_COLOR_BLACK + "\t");
            for (int col = 9; col >= 0; col--) {
                drawSquare(row, col, validMoves);
            }
            lightSquare = !lightSquare;
            setBlack();
            string.append(EscapeSequences.SET_BG_COLOR_BLACK + "\n");
        }
        return string.toString();
    }

    private void drawSquare(int row, int col) {
        // Headers
        if (row == 0 || row == 9) {
            drawRowHeader(col);
        } else if (col == 0 || col == 9) {
            drawColHeader(row);
        }
        // Board
        else {
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            setTeamColor(piece);
            String pieceSymbol = getPieceSymbol(piece);
            if (lightSquare) {
                drawLightGraySquare(pieceSymbol);
                lightSquare = false;
            } else {
                drawDarkGraySquare(pieceSymbol);
                lightSquare = true;
            }
        }
    }

    private void drawSquare(int row, int col, Collection<ChessPosition> validMoves) {
        // Headers
        if (row == 0 || row == 9) {
            drawRowHeader(col);
        } else if (col == 0 || col == 9) {
            drawColHeader(row);
        }
        // Board
        else {
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            setTeamColor(piece);
            String pieceSymbol = getPieceSymbol(piece);
            ChessPosition currPosition = new ChessPosition(row, col);
            if (validMoves.contains(currPosition)) {
                drawGreenSquare(pieceSymbol);
                lightSquare = !lightSquare;
            } else if (lightSquare) {
                drawLightGraySquare(pieceSymbol);
                lightSquare = false;
            } else {
                drawDarkGraySquare(pieceSymbol);
                lightSquare = true;
            }
        }
    }

    private void setTeamColor(ChessPiece piece) {
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                string.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            } else {
                string.append(EscapeSequences.SET_TEXT_COLOR_BLACK);
            }
        }
    }

    private String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        return switch (piece.getPieceType()) {
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case KING -> EscapeSequences.BLACK_KING;
            case PAWN -> EscapeSequences.BLACK_PAWN;
        };
    }

    private void drawRowHeader(int col) {
        String[] headers = {EscapeSequences.EMPTY, "\u2003a ", "\u2003b ", "\u2003c ", "\u2003d ",
                "\u2003e ", "\u2003f ", "\u2003g ", "\u2003h ", EscapeSequences.EMPTY};
        drawBlackSquare(headers[col]);
    }

    private void drawColHeader(int row) {
        String[] headers = {EscapeSequences.EMPTY, "\u20031 ", "\u20032 ", "\u20033 ", "\u20034 ",
                "\u20035 ", "\u20036 ", "\u20037 ", "\u20038 ", EscapeSequences.EMPTY};
        drawBlackSquare(headers[row]);
    }

    private void drawBlackSquare(String ch) {
        setBlack();
        for (int i = 0; i < SQUARE_LENGTH; i++) {
            string.append(ch);
        }
    }

    private void drawDarkGraySquare(String ch) {
        setDarkGrey();
        for (int i = 0; i < SQUARE_LENGTH; i++) {
            string.append(ch);
        }
    }

    private void drawLightGraySquare(String ch) {
        setLightGray();
        for (int i = 0; i < SQUARE_LENGTH; i++) {
            string.append(ch);
        }
    }

    private void drawGreenSquare(String ch) {
        setGreen();
        for (int i = 0; i < SQUARE_LENGTH; i++) {
            string.append(ch);
        }
    }

    public void setBlack() {
        string.append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private void setDarkGrey() {
        string.append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.SET_BG_COLOR_DARK_GREY);
    }

    private void setLightGray() {
        string.append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
    }

    private void setGreen() {
        string.append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.SET_BG_COLOR_MAGENTA);
    }
}
