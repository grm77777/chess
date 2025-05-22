package server;

import dataaccess.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", new RegisterHandler(authDAO, userDAO));
        Spark.post("/session", new LoginHandler(authDAO, userDAO));
        Spark.delete("/session", new LogoutHandler(authDAO, userDAO));
        Spark.delete("/db", new ClearHandler(authDAO, userDAO, gameDAO));
        Spark.post("/game", new CreateGameHandler(authDAO, gameDAO));

        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
