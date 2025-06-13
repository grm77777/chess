package ui;

import client.Client;
import client.PostloginClient;
import client.PreloginClient;
import model.AuthData;

import java.util.Scanner;

public class Repl {

    private final Client client;
    final static String PROMPT = EscapeSequences.RESET_TEXT_BOLD_FAINT + EscapeSequences.SET_TEXT_COLOR_GREEN;
    final static String INPUT = EscapeSequences.SET_TEXT_COLOR_WHITE;

    public Repl(String serverUrl) {
        client = new PreloginClient(serverUrl);
    }

    public Repl(String serverUrl, AuthData authData) {
        client = new PostloginClient(serverUrl, authData);
    }

    public void run() {
        System.out.println(client.openingMessage());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("\tquitting...")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print("\t" + msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + PROMPT + ">>> " + INPUT);
    }

}
