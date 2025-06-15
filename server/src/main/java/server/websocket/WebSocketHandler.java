package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.BadRequest;
import service.Service;
import service.UnauthorizedRequest;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler() {
        connections = new ConnectionManager();
        var service = new Service();
        authDAO = service.getAuthDAO();
        gameDAO = service.getGameDAO();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        try {
            switch (command.getCommandType()) {
                case UserGameCommand.CommandType.CONNECT -> connect(command.getAuthToken(), command.getGameID(), command.getPlayerType(), session);
//            case UserGameCommand.CommandType.MAKE_MOVE -> enter(command, session);
//            case UserGameCommand.CommandType.LEAVE -> enter(command, session);
//            case UserGameCommand.CommandType.RESIGN -> enter(command, session);
            }
        } catch (UnauthorizedRequest | BadRequest ex) {
            var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            var gson = new Gson();
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }

    private void connect(String authToken, Integer gameID, UserGameCommand.PlayerType playerType, Session session)
            throws IOException, UnauthorizedRequest, BadRequest {
        checkAuth(authToken);
        String username = getUsername(authToken);
        connections.add(username, gameID, session);
        var loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, getGame(gameID));
        connections.alert(username, loadGameMessage);
        var broadcastMessage = formatConnectMsg(username, playerType);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage);
        connections.broadcast(username, gameID, notification);
    }

    private String formatConnectMsg(String username, UserGameCommand.PlayerType playerType) throws BadRequest {
        if (playerType == null) {
//            throw new BadRequest();
            playerType = UserGameCommand.PlayerType.OBSERVER;
        }
        return switch (playerType) {
            case WHITE -> String.format("%s joined the game as white player.", username);
            case BLACK -> String.format("%s joined the game as black player.", username);
            case OBSERVER -> String.format("%s joined the game an observer.", username);
        };
    }

//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }

    private void checkAuth(String authToken) throws UnauthorizedRequest {
        if (authDAO.verifyAuth(authToken) == null) {
            throw new UnauthorizedRequest();
        }
    }

    private String getUsername(String authToken) {
        return authDAO.verifyAuth(authToken).userName();
    }

    private ChessGame getGame(Integer gameID) throws BadRequest {
        var gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new BadRequest();
        }
        return gameData.game();
    }
}
