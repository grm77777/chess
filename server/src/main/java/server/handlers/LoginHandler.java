package server.handlers;

import service.*;
import service.requests.LoginRequest;
import service.results.LoginResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class LoginHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        LoginResult result;
        try {
            verifyRequest(request);
            UserService service = new UserService();
            result = service.login(request);
        } catch (BadRequest e) {
            res.status(400);
            result = new LoginResult(null, null, e.getMessage());
        } catch (UnauthorizedRequest e) {
            res.status(401);
            result = new LoginResult(null, null, e.getMessage());
        } catch (Exception e) {
            res.status(500);
            result = new LoginResult(null, null, e.getMessage());
        }
        res.body(gson.toJson(result));
        return res.body();
    }

    private void verifyRequest(LoginRequest request) throws BadRequest {
        if (request.username() == null || request.password() == null) {
            throw new BadRequest();
        }
    }
}
