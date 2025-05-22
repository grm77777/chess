package server;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import service.*;
import service.results.ListGamesResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class ListGamesHandler implements Route {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesHandler (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        String authToken = req.headers("Authorization");
        ListGamesResult result;
        try {
            GameService service = new GameService(authDAO, gameDAO, authToken);
            result = service.listGames();
            System.out.println(result);
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

