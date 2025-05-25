package server.handlers;

import service.*;
import service.requests.LoginRequest;
import service.results.LoginResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

/**
 * Handles login requests from the server.
 */
public class LoginHandler implements Route {

    /**
     * Handles Login requests from the server.
     * Catches exceptions and processes a LoginResult
     * for the server to send to the client.
     *
     * @param req The request received by the server
     * @param res A result the server can send to the client
     * @return The body of the result
     */
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

    /**
     * Verifies that the given LoginRequest contains
     * non-null instance variables.
     *
     * @param request LoginRequest to check
     * @throws BadRequest if any of the instance variables are null
     */
    private void verifyRequest(LoginRequest request) throws BadRequest {
        if (request.username() == null || request.password() == null) {
            throw new BadRequest();
        }
    }
}
