package server;

import server.handlers.*;
import server.websocket.WebSocketHandler;
import spark.*;

public class Server {

    private final WebSocketHandler webSocketHandler;

    public Server() {
        webSocketHandler = new WebSocketHandler();
    }

    /**
     * Runs a server on the given port.
     *
     * @param desiredPort Port to run the server on
     * @return The port that was used
     */
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        Spark.post("/user", new RegisterHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());
        Spark.delete("/db", new ClearHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.get("/game", new ListGamesHandler());
        Spark.put("/game", new JoinGameHandler());

        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    /**
     * Stops the server.
     */
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
