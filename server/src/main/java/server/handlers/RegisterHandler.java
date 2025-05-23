package server.handlers;

import service.*;
import service.requests.RegisterRequest;
import service.results.RegisterResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class RegisterHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResult result;
        try {
            verifyRequest(request);
            UserService service = new UserService();
            result = service.register(request);
        } catch (BadRequest e) {
            res.status(400);
            result = new RegisterResult(null, null, e.getMessage());
        } catch (AlreadyTaken e) {
            res.status(403);
            result = new RegisterResult(null, null, e.getMessage());
        } catch (Exception e) {
            res.status(500);
            result = new RegisterResult(null, null, e.getMessage());
        }
        res.body(gson.toJson(result));
        return res.body();
    }

    private void verifyRequest(RegisterRequest request) throws BadRequest {
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequest();
        }
    }
}
