package server.handlers;

import service.*;
import service.requests.JoinGameRequest;
import service.results.JoinGameResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class JoinGameHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        JoinGameResult result;
        try {
            verifyRequest(request);
            GameService service = new GameService(req.headers("Authorization"));
            result = service.joinGame(request);
        } catch (BadRequest e) {
            res.status(400);
            result = new JoinGameResult(e.getMessage());
        } catch (UnauthorizedRequest e) {
            res.status(401);
            result = new JoinGameResult(e.getMessage());
        } catch (AlreadyTaken e) {
            res.status(403);
            result = new JoinGameResult(e.getMessage());
        } catch (Exception e) {
            res.status(500);
            result = new JoinGameResult(e.getMessage());
        }
        res.body(gson.toJson(result));
        return res.body();
    }

    private void verifyRequest(JoinGameRequest request) throws BadRequest {
        String playerColor = request.playerColor();
        if (playerColor == null || !(playerColor.equals("WHITE") || playerColor.equals("BLACK")) || request.gameID() == null) {
            throw new BadRequest();
        }
    }
}
