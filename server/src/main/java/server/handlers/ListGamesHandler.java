package server.handlers;

import service.*;
import service.results.ListGamesResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

/**
 * Handles list games requests from the server.
 */
public class ListGamesHandler extends Handler implements Route {

    /**
     * Handles ListGames requests from the server.
     * Catches exceptions and processes a ListGamesResult
     * for the server to send to the client.
     *
     * @param req The request received by the server
     * @param res A result the server can send to the client
     * @return The body of the result
     */
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        String authToken = req.headers("Authorization");
        ListGamesResult result;
        try {
            congfigureDatabase();
            GameService service = new GameService(authToken);
            result = service.listGames();
        } catch (UnauthorizedRequest e) {
            res.status(401);
            result = new ListGamesResult(null, e.getMessage());
        } catch (Exception e) {
            res.status(500);
            result = new ListGamesResult(null, e.getMessage());
        }
        res.body(gson.toJson(result));
        return res.body();
    }
}

