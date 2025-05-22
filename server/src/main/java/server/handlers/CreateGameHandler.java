package server.handlers;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import service.*;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class CreateGameHandler implements Route {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameResult result;
        try {
            verifyRequest(request);
            GameService service = new GameService(authDAO, gameDAO, req.headers("Authorization"));
            result = service.createGame(request);
        } catch (BadRequest e) {
            res.status(400);
            result = new CreateGameResult(null, e.getMessage());
        } catch (UnauthorizedRequest e) {
            res.status(401);
            result = new CreateGameResult(null, e.getMessage());
        } catch (Exception e) {
            res.status(500);
            result = new CreateGameResult(null, e.getMessage());
        }
        res.body(gson.toJson(result));
        return res.body();
    }

    private void verifyRequest(CreateGameRequest request) throws BadRequest {
        if (request.gameName() == null) {
            throw new BadRequest();
        }
    }
}
