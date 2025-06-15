package client;

import chess.ChessGame;
import ui.EscapeSequences;

public interface Client {

    String DEFAULT_SETUP = EscapeSequences.SET_BG_COLOR_BLACK
            + EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.RESET_TEXT_BOLD_FAINT;
    String HEADER = EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_TEXT_BOLD;
    String BODY = EscapeSequences.SET_TEXT_COLOR_MAGENTA + EscapeSequences.RESET_TEXT_BOLD_FAINT;
    String DEFAULT = EscapeSequences.SET_TEXT_COLOR_WHITE;
    String QUIT_MESSAGE = "\tquitting...";

    String openingMessage();

    String eval(String input);

    void updateGame(ChessGame game);

    String drawBoard();
}
