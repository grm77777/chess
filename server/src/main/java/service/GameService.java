package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.GameDataJson;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;
import service.results.JoinGameResult;
import service.results.ListGamesResult;
import java.util.HashSet;

public class GameService {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final String authToken;

    public GameService(AuthDAO authDAO, GameDAO gameDAO, String authToken) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.authToken = authToken;
    }

    public CreateGameResult createGame(CreateGameRequest request) {
        verifyUser();
        GameData gameData = gameDAO.createGame(request.gameName());
        return new CreateGameResult(gameData.gameID(), null);
    }

    public ListGamesResult listGames() {
        verifyUser();
        HashSet<GameDataJson> allGames = gameDAO.listGames();
        return new ListGamesResult(allGames, null);
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws BadRequest, AlreadyTaken {
        AuthData user = verifyUser();
        GameData gameData = gameDAO.getGame(request.gameID());
        if (gameData == null) {
            throw new BadRequest();
        }
        checkPlayerColor(gameData, request.playerColor());
        gameDAO.updateGame(gameData, user.userName(), request.playerColor());
        return new JoinGameResult(null);
    }

    private void checkPlayerColor(GameData gameData, String playerColor) throws AlreadyTaken {
        if (playerColor.equals("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new AlreadyTaken();
            }
        } else {
            if (gameData.blackUsername() != null) {
                throw new AlreadyTaken();
            }
        }
    }

    private AuthData verifyUser() throws UnauthorizedRequest {
        AuthData user = authDAO.verifyAuth(authToken);
        if (user == null) {
            throw new UnauthorizedRequest();
        }
        return user;
    }

}
