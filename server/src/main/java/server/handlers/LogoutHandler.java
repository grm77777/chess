package server.handlers;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import service.*;
import service.requests.LogoutRequest;
import service.results.LogoutResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class LogoutHandler implements Route {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public LogoutHandler(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        LogoutRequest request = new LogoutRequest(req.headers("Authorization"));
        LogoutResult result;
        try {
            UserService service = new UserService(authDAO, userDAO);
            result = service.logout(request);
        } catch (UnauthorizedRequest e) {
            res.status(401);
            result = new LogoutResult(e.getMessage());
        } catch (Exception e) {
            res.status(500);
            result = new LogoutResult(e.getMessage());
        }
        res.body(gson.toJson(result));
        return res.body();
    }
}

