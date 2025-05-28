package server.handlers;

import service.*;
import service.requests.LogoutRequest;
import service.results.LogoutResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

/**
 * Handles logout requests from the server.
 */
public class LogoutHandler extends Handler implements Route {

    /**
     * Handles Logout requests from the server.
     * Catches exceptions and processes a LogoutResult
     * for the server to send to the client.
     *
     * @param req The request received by the server
     * @param res A result the server can send to the client
     * @return The body of the result
     */
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        LogoutRequest request = new LogoutRequest(req.headers("Authorization"));
        LogoutResult result;
        try {
            createDatabase();
            UserService service = new UserService();
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

