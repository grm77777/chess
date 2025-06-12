package client;

import model.AuthData;
import service.results.RegisterResult;
import ui.EscapeSequences;
import ui.Repl;

import java.util.Arrays;

public class PreloginClient implements Client {

    private final String serverUrl;
    private final ServerFacade serverFacade;

    public PreloginClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
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
            return ex.getMessage();
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
            String username = params[0];
            String password = params[1];
            String email = params[2];
            var result = serverFacade.register(username, password, email);
            enterPostloginRepl(result);
            return "Welcome back to the main menu! Options:\n\t" + help();
        }
        throw new ResponseException(400, "Expected username, password, and email.");
    }

    private String login(String... params) {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            var result = serverFacade.login(username, password);
            enterPostloginRepl(result);
            return "Welcome back to the main menu! Options:\n\t" + help();
        }
        throw new ResponseException(400, "Expected username and password.");
    }

    private void enterPostloginRepl(AuthData authData) {
        var repl = new Repl(serverUrl, authData);
        repl.run();
    }
}
