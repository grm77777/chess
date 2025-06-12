package client;

import ui.EscapeSequences;

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
    public String eval() {
        return "";
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

}
