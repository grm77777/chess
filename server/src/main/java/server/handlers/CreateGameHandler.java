package server.handlers;

import service.*;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class CreateGameHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameResult result;
        try {
            verifyRequest(request);
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
