package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.GameDataJson;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;
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

    private void verifyUser() throws UnauthorizedRequest {
        AuthData user = authDAO.verifyAuth(authToken);
        if (user == null) {
            throw new UnauthorizedRequest("Error: unauthorized");
        }
    }

}
