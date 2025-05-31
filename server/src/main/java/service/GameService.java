package service;

import model.AuthData;
import model.GameData;
import model.ListGameData;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;
import service.results.JoinGameResult;
import service.results.ListGamesResult;
import java.util.ArrayList;

/**
 * Implements services associated with the CreateGame,
 * JoinGame, and ListGame handlers.
 */
public class GameService extends Service {

    private final String authToken;

    public GameService(String authToken) {
        super();
        this.authToken = authToken;
    }

    /**
     * Creates a new game
     *
     * @param request CreateGameRequest with details about the new game.
     * @throws UnauthorizedRequest if the authToken is unauthorized.
     * @return CreateGameResult with details about the new game.
     */
    public CreateGameResult createGame(CreateGameRequest request) throws UnauthorizedRequest {
        verifyUser();
        GameData gameData = GAME_DAO.createGame(request.gameName());
        return new CreateGameResult(gameData.gameID(), null);
    }

    /**
     * List all the games currently in the database.
     *
     * @throws UnauthorizedRequest if the authToken is unauthorized.
     * @return ListGamesResults with a list of all current games.
     */
    public ListGamesResult listGames() throws UnauthorizedRequest {
        verifyUser();
        ArrayList<ListGameData> allGames = GAME_DAO.listGames();
        return new ListGamesResult(allGames, null);
    }

    /**
     * Join a game currently in the database.
     *
     * @param request JoinGameRequest with details about the game.
     * @throws UnauthorizedRequest if the authToken is unauthorized.
     * @throws BadRequest if the requested gameID isn't registered in the database.
     * @throws AlreadyTaken if the requested playerColor is already taken in that game.
     * @return empty JoinGameRequest
     */
    public JoinGameResult joinGame(JoinGameRequest request) throws UnauthorizedRequest, BadRequest, AlreadyTaken {
        AuthData user = verifyUser();
        GameData gameData = GAME_DAO.getGame(request.gameID());
        if (gameData == null) {
            throw new BadRequest();
        }
        checkPlayerColor(gameData, request.playerColor());
        GAME_DAO.updateGame(gameData, user.userName(), request.playerColor());
        return new JoinGameResult(null);
    }

    /**
     * Checks whether the playerColor is already taken in the given game.
     *
     * @param gameData The game to check
     * @param playerColor The color to check
     * @throws AlreadyTaken if the requested playerColor is already taken in that game.
     */
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

    /**
     * Checks whether the player is authorized.
     *
     * @throws UnauthorizedRequest if the player's authToken isn't found in the database.
     * @return the player's AuthData if they are authorized.
     */
    private AuthData verifyUser() throws UnauthorizedRequest {
        AuthData user = AUTH_DAO.verifyAuth(authToken);
        if (user == null) {
            throw new UnauthorizedRequest();
        }
        return user;
    }

}
