package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import facades.NotificationHandler;
import facades.WebSocketFacade;
import model.AuthData;
import ui.DrawChessBoard;
import websocket.commands.UserGameCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GameplayClient implements Client {

    private final String authToken;
    private final UserGameCommand.PlayerType playerType;
    private final Integer gameID;
    private ChessGame game;
    private final WebSocketFacade webSocketFacade;

    public GameplayClient(String serverUrl, AuthData authData, int gameID, UserGameCommand.PlayerType playerType, NotificationHandler notificationHandler) {
        webSocketFacade = new WebSocketFacade(serverUrl, notificationHandler);
        authToken = authData.authToken();
        this.gameID = gameID;
        this.playerType = playerType;
        game = new ChessGame();
        game.getBoard().resetBoard();
    }

    @Override
    public String openingMessage() {
        webSocketFacade.connectToGame(authToken, gameID);
        String openingMessage = "\tWelcome to the game!\n";
        return DEFAULT_SETUP + openingMessage + help();
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "moves" -> highlightLegalMoves(params);
                case "move" -> move(params);
                case "redraw" -> redrawBoard(params);
                case "leave" -> leave(params);
                case "resign" -> checkResign(params);
                case "yes" -> resign(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return "\t" + ex.getMessage();
        }
    }

    private String help() {
        return HEADER + "\tmoves <COLUMN> <ROW> " +
                BODY + "- to highlight the legal moves for a piece\n" +
                HEADER + "\tmove <CURRENT COLUMN> <CURRENT ROW> <NEW COLUMN> <NEW ROW> <PROMOTION PIECE> " +
                BODY + "- to move a piece. Leave promotion piece blank unless pawn moves to end of board.\n" +
                HEADER + "\tredraw " +
                BODY + "- to see the board once again\n" +
                HEADER + "\tleave " +
                BODY + "- to leave and stop receiving alerts for the game\n" +
                HEADER + "\tresign " +
                BODY + "- to resign from the game. Other player automatically wins.\n" +
                HEADER + "\thelp " +
                BODY + "- display possible commands";
    }

    private String highlightLegalMoves(String... params) throws ResponseException {
        if (params.length == 2) {
            ChessPosition currPosition = getPosition(params[0], params[1]);
            Collection<ChessPosition> legalMoves = getLegalMoves(currPosition);
            return drawBoard(legalMoves);
        }
        throw new ResponseException(400, "Move must include current row and current column, divided by spaces.");
    }

    private Collection<ChessPosition> getLegalMoves(ChessPosition currPosition) throws ResponseException {
        Collection<ChessMove> validMoves = game.validMoves(currPosition);
        if (game.getBoard().getPiece(currPosition) == null) {
            throw new ResponseException(400, "No piece found at entered position.");
        }
        Collection<ChessPosition> legalPositions = new ArrayList<>();
        for (ChessMove move : validMoves) {
            legalPositions.add(move.getEndPosition());
        }
        return  legalPositions;
    }

    private String move(String... params) throws ResponseException {
        if (params.length == 4) {
            makeMove(params[0], params[1], params[2], params[3], null);
            return "";
        } else if (params.length == 5) {
            ChessPiece.PieceType promotionPiece = getPieceType(params[4]);
            makeMove(params[0], params[1], params[2], params[3], promotionPiece);
            return "";
        }
        throw new ResponseException(400, "Move must include current row, current column, new row, and new column, divided by spaces.");
    }

    private void makeMove(String currCol, String currRow, String newCol, String newRow, ChessPiece.PieceType promotionPiece)
            throws ResponseException {
        ChessPosition currPosition = getPosition(currCol, currRow);
        if (game.getBoard().getPiece(currPosition) == null) {
            throw new ResponseException(400, "No piece found at start position.");
        }
        ChessPosition newPosition = getPosition(newCol, newRow);
        ChessMove move = new ChessMove(currPosition, newPosition, promotionPiece);
        webSocketFacade.makeMove(authToken, gameID, move);
    }

    private ChessPosition getPosition(String colInput, String rowInput) throws ResponseException {
        int col = colToInt(colInput);
        int row = rowToInt(rowInput);
        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType getPieceType(String pieceInput) throws ResponseException {
        return switch (pieceInput) {
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "king" -> ChessPiece.PieceType.KING;
            case "pawn" -> ChessPiece.PieceType.PAWN;
            default -> throw new ResponseException(400, "Invalid piece type.");
        };
    }

    private int colToInt(String colInput) throws ResponseException {
        return switch (colInput) {
            case "a" -> 8;
            case "b" -> 7;
            case "c" -> 6;
            case "d" -> 5;
            case "e" -> 4;
            case "f" -> 3;
            case "g" -> 2;
            case "h" -> 1;
            default -> throw new ResponseException(400, "Invalid column.");
        };
    }

    private int rowToInt(String rowInput) throws ResponseException {
        int row;
        try {
            row = Integer.parseInt(rowInput);
        } catch (Exception ex) {
            throw new ResponseException(400, "Invalid row.");
        }
        if ((row < 0) || (row > 8)) {
            throw new ResponseException(400, "Invalid row.");
        } else {
            return row;
        }
    }

    private String redrawBoard(String... params) {
        if (params.length == 0) {
            return drawBoard();
        }
        return help();
    }

    private String leave(String... params) {
        if (params.length == 0) {
            webSocketFacade.leaveGame(authToken, gameID);
            return QUIT_MESSAGE;
        }
        return help();
    }

    private String checkResign(String... params) {
        if (params.length == 0) {
            return "\tAre you sure you want to resign?\n\tThe other player will automatically win.\n\tType 'Yes' to confirm.";
        }
        return help();
    }

    private String resign(String... params) {
        if (params.length == 0) {
            webSocketFacade.resign(authToken, gameID);
            return QUIT_MESSAGE;
        }
        return help();
    }

    public void updateGame(ChessGame game) {
        this.game = game;
    }

    public String drawBoard() {
        DrawChessBoard drawBoard = new DrawChessBoard(game.getBoard());
        if (playerType.equals(UserGameCommand.PlayerType.BLACK)) {
            return drawBoard.drawBoardBlack();
        } else {
            return drawBoard.drawBoardWhite();
        }
    }

    public String drawBoard(Collection<ChessPosition> validMoves) {
        DrawChessBoard drawBoard = new DrawChessBoard(game.getBoard());
        if (playerType.equals(UserGameCommand.PlayerType.BLACK)) {
            return drawBoard.drawValidMovesBlack(validMoves);
        } else {
            return drawBoard.drawValidMovesWhite(validMoves);
        }
    }
}
