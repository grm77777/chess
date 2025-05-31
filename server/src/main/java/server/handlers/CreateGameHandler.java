package server.handlers;

import service.*;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

/**
 * Handles create game requests from the server.
 */
public class CreateGameHandler extends Handler implements Route {

    /**
     * Handles CreateGame requests from the server.
     * Catches exceptions and processes a CreateGameResult
     * for the server to send to the client.
     *
     * @param req The request received by the server
     * @param res A result the server can send to the client
     * @return The body of the result
     */
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameResult result;
        try {
            verifyRequest(request);
            congfigureDatabase();
            GameService service = new GameService(req.headers("Authorization"));
            result = service.createGame(request);
        } catch (BadRequest e) {
            res.status(400);
            result = new CreateGameResult(null, e.getMessage());
        } catch (UnauthorizedRequest e) {
            res.status(401);
            result = new CreateGameResult(null, e.getMessage());
        } catch (Exception e) {
            res.status(500);
            result = new CreateGameResult(null, "Server Error: " + e.getMessage());
        }
        res.body(gson.toJson(result));
        return res.body();
    }

    /**
     * Verifies that the given CreateGameRequest contains
     * non-null instance variables.
     *
     * @param request CreateGameRequest to check
     * @throws BadRequest if any of the instance variables are null
     */
    private void verifyRequest(CreateGameRequest request) throws BadRequest {
        if (request.gameName() == null) {
            throw new BadRequest();
        }
    }
}
