package model;

import chess.ChessGame;

/**
 * Represents the data associated with each game.
 */
public record GameData(int gameID, String whiteUsername, String blackUsername,
                       String gameName, ChessGame game) {
}
