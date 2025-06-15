package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import service.BadRequest;
import websocket.messages.ServerMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections;

    public ConnectionManager() {
        connections = new ConcurrentHashMap<>();
    }

    public void add(String username, Integer gameID, Session session) {
        var connection = new Connection(username, gameID, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public Integer getGameID(String username) {
        Connection c = connections.get(username);
        return c.gameID;
    }

    public void broadcast(String excludeUsername, Integer gameID, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.gameID.equals(gameID) && !c.username.equals(excludeUsername)) {
                    c.send(notification);
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void alert(String username, ServerMessage serverMessage) throws IOException, BadRequest {
        var c = connections.get(username);
        if (c != null) {
            c.send(serverMessage);
        } else {
            throw new BadRequest();
        }
    }
}
