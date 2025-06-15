package client;

import chess.ChessGame;
import facades.ServerFacade;
import model.AuthData;
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
                case "quit" -> QUIT_MESSAGE;
                default -> help();
            };
        } catch (ResponseException ex) {
            return "\t" + ex.getMessage();
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
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            var result = serverFacade.register(username, password, email);
            enterPostloginRepl(result);
            return "\tWelcome back to the main menu! Options:\n" + help();
        }
        throw new ResponseException(400, "Expected username, password, and email.");
    }

    private String login(String... params) {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            var result = serverFacade.login(username, password);
            enterPostloginRepl(result);
            return "\tWelcome back to the main menu! Options:\n" + help();
        }
        throw new ResponseException(400, "Expected username and password.");
    }

    private void enterPostloginRepl(AuthData authData) {
        var repl = new Repl(serverUrl, authData);
        repl.run();
    }

    @Override
    public void updateGame(ChessGame game) {}

    @Override
    public String drawBoard() {
        return "";
    }
}
