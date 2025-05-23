package server.handlers;

import service.*;
import service.results.ListGamesResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class ListGamesHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        String authToken = req.headers("Authorization");
        ListGamesResult result;
        try {
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

