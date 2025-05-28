package server.handlers;

import service.*;
import service.requests.JoinGameRequest;
import service.results.JoinGameResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

/**
 * Handles join game requests from the server.
 */
public class JoinGameHandler extends Handler implements Route {

    /**
     * Handles JoinGame requests from the server.
     * Catches exceptions and processes a JoinGameResult
     * for the server to send to the client.
     *
     * @param req The request received by the server
     * @param res A result the server can send to the client
     * @return The body of the result
     */
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        JoinGameResult result;
        try {
            verifyRequest(request);
            createDatabase();
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

    /**
     * Verifies that the given JoinGameRequest contains
     * non-null instance variables. All verifies that the
     * playerColor is either "WHITE" or "BLACK."
     *
     * @param request JoinGameRequest to check
     * @throws BadRequest if any of the instance variables are null
     */
    private void verifyRequest(JoinGameRequest request) throws BadRequest {
        String playerColor = request.playerColor();
        if (playerColor == null || !(playerColor.equals("WHITE") || playerColor.equals("BLACK")) || request.gameID() == null) {
            throw new BadRequest();
        }
    }
}
