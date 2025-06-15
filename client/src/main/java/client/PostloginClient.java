package client;

import chess.ChessBoard;
import facades.ServerFacade;
import model.AuthData;
import model.ListGameData;
import ui.DrawChessBoard;
import java.util.ArrayList;
import java.util.Arrays;

public class PostloginClient implements Client {

    private final String username;
    private final String authToken;
    private final ServerFacade serverFacade;

    public PostloginClient(String serverUrl, AuthData authData) {
        this.serverFacade = new ServerFacade(serverUrl);
        this.username = authData.userName();
        this.authToken = authData.authToken();
    }

    @Override
    public String openingMessage() {
        String openingMessage = String.format("\tWelcome, %s. Options:", username) + "\n";
        return DEFAULT_SETUP + openingMessage + help();
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "list" -> list(params);
                case "create" -> create(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> QUIT_MESSAGE;
                default -> help();
            };
        } catch (ResponseException ex) {
            return "\t" + ex.getMessage();
        }
    }

    private String help() {
        return  HEADER + "\tlist " +
                BODY + "- list all existing games\n" +
                HEADER + "\tcreate <NAME> " +
                BODY + "- create a new game\n" +
                HEADER + "\tjoin <ID> <WHITE|BLACK> " +
                BODY + "- join a game\n" +
                HEADER + "\tobserve <ID> " +
                BODY + "- to observe a game\n" +
                HEADER + "\tlogout " +
                BODY + "- to logout\n" +
                HEADER + "\tquit " +
                BODY + "- to go back to the main menu\n" +
                HEADER + "\thelp " +
                BODY + "- display possible commands";
    }

    private String list(String... params) {
        if (params.length == 0) {
            var result = serverFacade.listGames(authToken);
            return formatList(result);
        }
        return help();
    }

    private String formatList(ArrayList<ListGameData> games) {
        if (!games.isEmpty()) {
            String list = "";
            for (int i = 0; i < games.size(); i++) {
                ListGameData game = games.get(i);
                list = list + ("\t" + (i + 1) + ". " + formatGame(game) + "\n");
            }
            return list;
        } else {
            return "\tThere are no existing games. Create one to get started!";
        }
    }

    private String formatGame(ListGameData game) {
        return HEADER + game.gameName() + BODY + " - " +
                "White Player: " + DEFAULT + game.whiteUsername() + ", " +
                BODY + "Black Player: " + DEFAULT + game.blackUsername() + DEFAULT;
    }

    private String create(String... params) throws ResponseException {
        if (params.length == 1) {
            String gameName = params[0];
            serverFacade.createGame(authToken, gameName);
            return String.format("\tGame \"%s\" has been created.", gameName);
        }
        throw new ResponseException(400, "Must include one-word game name.");
    }

    private String join(String... params) throws ResponseException {
        if (params.length == 2) {
            int clientGameID = gameIdToInt(params[0]);
            int gameID = getServerGameID(clientGameID);
            String playerColor = checkPlayerColor(params[1]);
            serverFacade.joinGame(authToken, playerColor, gameID);
            return drawBoard(playerColor);
        }
        throw new ResponseException(400, "Must include game ID and player color.");
    }

    private String observe(String... params) {
        if (params.length == 1) {
            int clientGameID = gameIdToInt(params[0]);
            int gameID = getServerGameID(clientGameID);
            return drawBoard("white");
        }
        throw new ResponseException(400, "Must include game ID.");
    }

    private int gameIdToInt(String gameIDString) throws ResponseException {
        try {
            return Integer.parseInt(gameIDString);
        } catch (Exception ex) {
            throw new ResponseException(400, "Game ID must be a number.");
        }
    }

    private String checkPlayerColor(String playerColor) throws ResponseException {
        if (playerColor.equals("white")) {
            return "WHITE";
        } else if (playerColor.equals("black")) {
            return "BLACK";
        }
        throw new ResponseException(400, "Player color must be either WHITE or BLACK.");
    }

    private int getServerGameID(int clientGameID) throws ResponseException {
        var games = serverFacade.listGames(authToken);
        if ((clientGameID > 0) && (clientGameID <= games.size())) {
            var game = games.get(clientGameID - 1);
            return game.gameID();
        }
        throw new ResponseException(400, "Game ID must match a current game.");
    }

    private String drawBoard(String playerColor) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        DrawChessBoard drawBoard = new DrawChessBoard(board);
        if (playerColor.equals("BLACK")) {
            return drawBoard.drawBoardBlack();
        } else {
            return drawBoard.drawBoardWhite();
        }
    }

    private String logout(String... params) {
        if (params.length == 0) {
            serverFacade.logout(authToken);
            return QUIT_MESSAGE;
        }
        return help();
    }
}
