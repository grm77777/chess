package client;

import ui.EscapeSequences;

import java.util.Arrays;

public class PreloginClient implements Client {

    private final String serverUrl;
    private String username;
    private ServerFacade serverFacade;

    public PreloginClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public String openingMessage() {
        String openingMessage = EscapeSequences.WHITE_ROOK + " Welcome to 240 chess. Login to get started. " +
                EscapeSequences.WHITE_ROOK + "\n\t";
        return DEFAULT_SETUP + openingMessage + help();
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quitting...";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.StatusCode() + " - " + ex.getMessage();
        }
    }

    private String help() {
        return HEADER + "register <USERNAME> <PASSWORD> <EMAIL> " +
               BODY + "- to create an account\n" +
               HEADER + "\tlogin <USERNAME> <PASSWORD> " +
               BODY + "- to login with an existing account\n" +
               HEADER + "\tquit " +
               BODY + "- exit the program\n" +
               HEADER + "\thelp " +
               BODY + "- display possible commands";
    }

    private String register(String... params) {
        if (params.length == 3) {
            username = params[0];
            String password = params[1];
            String email = params[2];
            serverFacade = new ServerFacade(serverUrl);
            serverFacade.register(username, password, email);
            return String.format("You signed in as %s.", username);
        }
        throw new ResponseException(400, "Expected username, password, and email.");
    }

    private String login(String... params) {
        return "LOGIN PLACEHOLDER";
    }

}
