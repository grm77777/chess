package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String backUsername, String gameName, ChessGame game) {
}
