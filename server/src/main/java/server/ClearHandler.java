package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.*;
import service.results.ClearResult;
import spark.Route;
import spark.Request;
import spark.Response;

public class ClearHandler implements Route {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public ClearHandler(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request req, Response res) {
        ClearResult result;
        try {
            ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
            result = clearService.clear();
        } catch (Exception e) {
            res.status(500);
            result = new ClearResult(e.getMessage());
        }
        Gson gson = new Gson();
        res.body(gson.toJson(result));
        return res.body();
    }
}
