package ui;

import chess.ChessGame;
import client.Client;
import client.GameplayClient;
import client.PostloginClient;
import client.PreloginClient;
import facades.NotificationHandler;
import model.AuthData;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements NotificationHandler {

    private final Client client;
    final static String PROMPT = EscapeSequences.RESET_TEXT_BOLD_FAINT + EscapeSequences.SET_TEXT_COLOR_GREEN;
    final static String INPUT = EscapeSequences.SET_TEXT_COLOR_WHITE;
    final String QUIT_MESSAGE = Client.QUIT_MESSAGE;

    public Repl(String serverUrl) {
        client = new PreloginClient(serverUrl);
    }

    public Repl(String serverUrl, AuthData authData) {
        client = new PostloginClient(serverUrl, authData);
    }

    public Repl(String serverUrl, AuthData authData, int gameID, UserGameCommand.PlayerType playerType) {
        client = new GameplayClient(serverUrl, authData, gameID, playerType, this);
    }

    public void run() {
        System.out.println(client.openingMessage());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals(QUIT_MESSAGE)) {
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

    public void notify(ServerMessage notification) {
        if (notification.getClass().equals(LoadGameMessage.class)) {
            ChessGame game = ((LoadGameMessage) notification).getGame();
            client.updateGame(game);
            System.out.println(client.drawBoard());
        } else if (notification.getClass().equals(NotificationMessage.class)) {
            System.out.println(INPUT + "\t" + ((NotificationMessage) notification).getMessage());
        } else if (notification.getClass().equals(ErrorMessage.class)) {
            System.out.println(INPUT + "\t" + ((ErrorMessage) notification).getErrorMessage());
        }
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + PROMPT + ">>> " + INPUT);
    }

}
