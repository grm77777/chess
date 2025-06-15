package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class Connection {
    public String username;
    public Integer gameID;
    public Session session;

    public Connection(String visitorName, Integer gameID, Session session) {
        this.username = visitorName;
        this.gameID = gameID;
        this.session = session;
    }

    public void send(ServerMessage msg) throws IOException {
        Gson gson = new Gson();
        session.getRemote().sendString(gson.toJson(msg));
    }
}
