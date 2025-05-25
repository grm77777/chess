package server.handlers;

import com.google.gson.Gson;
import service.*;
import service.results.ClearResult;
import spark.Route;
import spark.Request;
import spark.Response;

/**
 * Handles clear requests from the server.
 */
public class ClearHandler implements Route {

    /**
     * Handles Clear requests from the server.
     * Catches exceptions and processes a ClearResult
     * for the server to send to the client.
     *
     * @param req The request received by the server
     * @param res A result the server can send to the client
     * @return The body of the result
     */
    @Override
    public Object handle(Request req, Response res) {
        ClearResult result;
        try {
            ClearService clearService = new ClearService();
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
