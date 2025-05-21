package server;


import service.AlreadyTakenException;
import service.RegisterRequest;
import service.RegisterResult;
import service.UserService;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class RegisterHandler implements Route{

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResult result;
        try {
            UserService service = new UserService();
            result = service.register(request);
        } catch (AlreadyTakenException e) {
            res.status(403);
            result = new RegisterResult(null, null, e.getMessage());
        }
        res.body(gson.toJson(result));
        return res.body();
    }
}
