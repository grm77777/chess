package server.handlers;

import service.*;
import service.requests.RegisterRequest;
import service.results.RegisterResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

/**
 * Handles register requests from the server.
 */
public class RegisterHandler extends Handler implements Route {

    /**
     * Handles Register requests from the server.
     * Catches exceptions and processes a RegisterResult
     * for the server to send to the client.
     *
     * @param req The request received by the server
     * @param res A result the server can send to the client
     * @return The body of the result
     */
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResult result;
        try {
            verifyRequest(request);
            congfigureDatabase();
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

    /**
     * Verifies that the given RegisterResult contains
     * non-null instance variables.
     *
     * @param request RegisterResult to check
     * @throws BadRequest if any of the instance variables are null
     */
    private void verifyRequest(RegisterRequest request) throws BadRequest {
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequest();
        }
    }
}
