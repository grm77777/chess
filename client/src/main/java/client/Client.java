package client;

import ui.EscapeSequences;

public interface Client {

    final String DEFAULT_SETUP = EscapeSequences.SET_BG_COLOR_DARK_GREY
            + EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.RESET_TEXT_BOLD_FAINT;
    final String HEADER = EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_TEXT_BOLD;
    final String BODY = EscapeSequences.SET_TEXT_COLOR_MAGENTA + EscapeSequences.RESET_TEXT_BOLD_FAINT;

    String openingMessage();

    String eval();
}
