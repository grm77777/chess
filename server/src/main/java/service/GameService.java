package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;

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
        System.out.println("I verified the user!");
        GameData gameData = gameDAO.createGame(request.gameName());
        System.out.println("I created the game!");
        return new CreateGameResult(gameData.gameID(), null);
    }

    private void verifyUser() throws UnauthorizedRequest {
        AuthData user = authDAO.verifyAuth(authToken);
        if (user == null) {
            throw new UnauthorizedRequest("Error: unauthorized");
        }
    }

}
