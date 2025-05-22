package server;


import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import service.*;
import service.requests.RegisterRequest;
import service.results.RegisterResult;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class RegisterHandler implements Route {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public RegisterHandler(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResult result;
        try {
            verifyRequest(request);
            UserService service = new UserService(authDAO, userDAO);
            result = service.register(request);
        } catch (BadRequest e) {
            res.status(400);
            result = new RegisterResult(null, null, e.getMessage());
        } catch (AlreadyTakenException e) {
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
            throw new BadRequest("Error: Bad Request");
        }
    }
}
