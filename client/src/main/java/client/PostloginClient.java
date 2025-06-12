package client;

import model.AuthData;

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
//        if (params.length == 0) {
////            var result = serverFacade.listGames();
//        }
        return help();
    }

    private String create(String... params) {
        return "CREATE PLACEHOLDER";
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
