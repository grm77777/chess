package ui;

import client.Client;
import client.PreloginClient;
import java.util.Scanner;

public class Repl  {

    private final Client client;
    final String PROMPT = EscapeSequences.RESET_TEXT_BOLD_FAINT + EscapeSequences.SET_TEXT_COLOR_GREEN;
    final String INPUT = EscapeSequences.SET_TEXT_COLOR_WHITE;

    public Repl(String serverUrl) {
        client = new PreloginClient(serverUrl);
    }

    public void run() {
        System.out.println(client.openingMessage());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quitting...")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print("\t" + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + PROMPT + ">>> " + INPUT);
    }

}
