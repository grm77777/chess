package client;

import ui.EscapeSequences;

import java.util.Arrays;

public class PreloginClient implements Client {

    private final String serverUrl;

    public PreloginClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public String openingMessage() {
        String openingMessage = EscapeSequences.WHITE_ROOK + " Welcome to 240 chess. Login to get started. " +
                EscapeSequences.WHITE_ROOK + "\n";
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
            return ex.getMessage();
        }
    }

    private String help() {
        return HEADER + "\tregister <USERNAME> <PASSWORD> <EMAIL> " +
               BODY + "- to create an account\n" +
               HEADER + "\tlogin <USERNAME> <PASSWORD> " +
               BODY + "- to login with an existing account\n" +
               HEADER + "\tquit " +
               BODY + "- exit the program\n" +
               HEADER + "\thelp " +
               BODY + "- display possible commands";
    }

    private String register(String... params) {
        return "REGISTER PLACEHOLDER";
    }

    private String login(String... params) {
        return "LOGIN PLACEHOLDER";
    }

}
