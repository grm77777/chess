package client;

import model.AuthData;
import model.ListGameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
        String openingMessage = String.format("Welcome, %s. Options:", username) + "\n\t";
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
                case "quit" -> "quitting...";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String help() {
        return  HEADER + "list " +
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
            String list = "1. " + formatGame(games.get(0)) + "\n";
            for (int i = 1; i < games.size(); i++) {
                ListGameData game = games.get(i);
                list = list + ("\t" + (i + 1) + ". " + formatGame(game) + "\n");
            }
            return list;
        } else {
            return "There are no existing games. Create one to get started!";
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
            return String.format("Game \"%s\" has been created.", gameName);
        }
        throw new ResponseException(400, "Must include one-word game name.");
    }

    private String join(String... params) {
        return "JOIN PLACEHOLDER";
    }

    private String observe(String... params) {
        return "OBSERVER PLACEHOLDER";
    }

    private String logout(String... params) {
        if (params.length == 0) {
            serverFacade.logout(authToken);
            return String.format("%s has been logged out.", username);
        }
        return help();
    }
}
