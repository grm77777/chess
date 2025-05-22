package model;

import java.util.Objects;

public record GameDataJson(Integer gameID, String whiteUsername, String blackUsername, String gameName) {
}
