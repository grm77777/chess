package client;

import chess.ChessBoard;
import chess.ChessGame;
import facades.NotificationHandler;
import facades.WebSocketFacade;
import model.AuthData;
import ui.DrawChessBoard;
import ui.EscapeSequences;
import websocket.commands.UserGameCommand;

import java.util.Arrays;

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
        return HEADER + "\tmoves <ROW> <COLUMN> " +
                BODY + "- to highlight the legal moves for a piece\n" +
                HEADER + "\tmove <CURRENT ROW> <CURRENT COLUMN> <NEW ROW> <NEW COLUMN> " +
                BODY + "- to move a piece\n" +
                HEADER + "\tredraw " +
                BODY + "- to see the board once again\n" +
                HEADER + "\tleave " +
                BODY + "- to leave and stop receiving alerts for the game\n" +
                HEADER + "\tresign " +
                BODY + "- to resign from the game. Other player automatically wins\n" +
                HEADER + "\thelp " +
                BODY + "- display possible commands";
    }

    private String highlightLegalMoves(String... params) {
        return "HIGHLIGHT LEGAL MOVES PLACEHOLDER";
    }

    private String move(String... params) {
        return "MOVE PLACEHOLDER";
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
            webSocketFacade.leaveGame(authToken, gameID);
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
}
