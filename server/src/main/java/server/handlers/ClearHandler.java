package server.handlers;

import com.google.gson.Gson;
import service.*;
import service.results.ClearResult;
import spark.Route;
import spark.Request;
import spark.Response;

public class ClearHandler implements Route {

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
