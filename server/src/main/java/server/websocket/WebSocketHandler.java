package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import websocket.messages.*;
import java.io.IOException;
import static websocket.commands.UserGameCommand.PlayerType.*;

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
                case UserGameCommand.CommandType.CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
                case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), command.getMove());
                case UserGameCommand.CommandType.LEAVE -> leave(command.getAuthToken(), command.getGameID());
                case UserGameCommand.CommandType.RESIGN -> resign(command.getAuthToken(), command.getGameID());
            }
        } catch (UnauthorizedRequest | BadRequest | InvalidMoveException | IOException ex) {
            var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            var gson = new Gson();
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }

    private void connect(String authToken, Integer gameID, Session session)
            throws IOException, UnauthorizedRequest, BadRequest {
        checkAuth(authToken);
        String username = getUsername(authToken);
        connections.add(username, gameID, session);
        var loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, getGame(gameID));
        connections.alert(username, loadGameMessage);
        var playerType = getPlayerType(username, gameID);
        var broadcastMessage = formatConnectMsg(username, playerType);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage);
        connections.broadcast(username, gameID, notification);
    }

    private String formatConnectMsg(String username, UserGameCommand.PlayerType playerType) throws BadRequest {
        if (playerType == null) {
            throw new BadRequest();
        }
        return switch (playerType) {
            case WHITE -> String.format("%s joined the game as white player.", username);
            case BLACK -> String.format("%s joined the game as black player.", username);
            case OBSERVER -> String.format("%s joined the game as an observer.", username);
        };
    }

    private void makeMove(String authToken, Integer gameID, ChessMove move)
            throws UnauthorizedRequest, BadRequest, InvalidMoveException, IOException {
        checkAuth(authToken);
        String username = getUsername(authToken);
        var game = getGame(gameID);
        makeMove(username, move, gameID, game);
        var loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.broadcast(null, gameID, loadGameMessage);
        String broadcastMessage = formatMoveMsg(username, move, game);
        var notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage);
        connections.broadcast(username, gameID, notificationMessage);
        checkCheckmateStalemate(username, gameID, game);
    }

    private void makeMove(String username, ChessMove move, Integer gameID, ChessGame game) throws InvalidMoveException, BadRequest {
        checkPiece(username, gameID, move);
        checkTurn(username, gameID);
        game.makeMove(move);
        gameDAO.makeMove(gameID, game);
    }

    private void checkPiece(String username, Integer gameID, ChessMove move) throws InvalidMoveException {
        var playerType = getPlayerType(username, gameID);
        var game = getGame(gameID);
        var piece = game.getBoard().getPiece(move.getStartPosition());
        if (playerType.equals(WHITE) && piece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            throw new InvalidMoveException("Error: Move attempted with other team's piece.");
        } else if (playerType.equals(BLACK) && piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            throw new InvalidMoveException("Error: Move attempted with other team's piece.");
        } else if (playerType.equals(OBSERVER)) {
            throw new InvalidMoveException("Error: Observers cannot make moves.");
        }
    }

    private void checkTurn(String username, Integer gameID) throws BadRequest, InvalidMoveException {
        var playerType = getPlayerType(username, gameID);
        var game = getGame(gameID);
        if (game.getGameOver()) {
            throw new InvalidMoveException("Error: Cannot make moves once the game is over.");
        }
        if (playerType.equals(WHITE) && game.getTeamTurn().equals(ChessGame.TeamColor.BLACK)) {
            throw new InvalidMoveException("Error: Move attempted on other team's turn.");
        } else if (playerType.equals(BLACK) && game.getTeamTurn().equals(ChessGame.TeamColor.WHITE)) {
            throw new InvalidMoveException("Error: Move attempted on other team's turn.");
        } else if (playerType.equals(OBSERVER)) {
            throw new InvalidMoveException("Error: Observers cannot make moves.");
        }
    }

    private String formatMoveMsg(String username, ChessMove move, ChessGame game) {
        var piece = game.getBoard().getPiece(move.getEndPosition());
        return String.format("%s moved their %s from %s to %s.", username, piece.getPieceType(), move.getStartPosition(), move.getEndPosition());
    }

    private void checkCheckmateStalemate(String username, Integer gameID, ChessGame game)
            throws IOException, BadRequest {
        var playerType = getPlayerType(username, gameID);
        String nextPlayerName;
        ChessGame.TeamColor nextPlayerType;
        if (playerType.equals(WHITE)) {
            nextPlayerName = gameDAO.getGame(gameID).blackUsername();
            nextPlayerType = ChessGame.TeamColor.BLACK;
        } else if (playerType.equals(BLACK)) {
            nextPlayerName = gameDAO.getGame(gameID).whiteUsername();
            nextPlayerType = ChessGame.TeamColor.WHITE;
        } else {
            throw new BadRequest();
        }
        String message = null;
        if (game.isInCheckmate(nextPlayerType)) {
            message = String.format("%s is in checkmate. %s wins!", nextPlayerName, username);
        } else if (game.isInCheck(nextPlayerType)) {
            message = String.format("%s is in check.", nextPlayerName);
        } else if (game.isInStalemate(nextPlayerType)) {
            message = "Game is a stalemate.";
        }
        if (message != null) {
            var notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(null, gameID, notificationMessage);
        }
    }

    private void leave(String authToken, Integer gameID)
            throws IOException, UnauthorizedRequest, BadRequest {
        checkAuth(authToken);
        String username = getUsername(authToken);
        var playerType = getPlayerType(username, gameID);
        removeFromGame(username, gameID, playerType);
        var broadcastMessage = String.format("%s left the game.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage);
        connections.broadcast(username, gameID, notification);
    }

    private void removeFromGame(String username, Integer gameID, UserGameCommand.PlayerType playerType) throws BadRequest {
        getGame(gameID);
        if (playerType == UserGameCommand.PlayerType.WHITE) {
            gameDAO.updateGame(gameDAO.getGame(gameID), null, "WHITE");
        } else if (playerType == UserGameCommand.PlayerType.BLACK) {
            gameDAO.updateGame(gameDAO.getGame(gameID), null, "BLACK");
        }
        connections.remove(username);
    }

    private void resign(String authToken, Integer gameID) throws BadRequest, IOException {
        checkAuth(authToken);
        String username = getUsername(authToken);
        checkPlayer(username, gameID);
        var game = getGame(gameID);
        game.gameOver();
        gameDAO.makeMove(gameID, game);
        String broadcastMessage = formatResignMsg(username, gameID);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage);
        connections.broadcast(null, gameID, notification);
    }

    private String formatResignMsg(String username, Integer gameID) throws BadRequest {
        var playerType = getPlayerType(username, gameID);
        if (playerType == null) {
            throw new BadRequest();
        }
        var gameData = gameDAO.getGame(gameID);
        String otherPlayer;
        if (playerType.equals(UserGameCommand.PlayerType.WHITE)) {
            otherPlayer = gameData.blackUsername();
        } else {
            otherPlayer = gameData.whiteUsername();
        }
        return String.format("%s resigned. %s wins!", username, otherPlayer);
    }

    private void checkAuth(String authToken) throws UnauthorizedRequest {
        if (authDAO.verifyAuth(authToken) == null) {
            throw new UnauthorizedRequest();
        }
    }

    private String getUsername(String authToken) {
        return authDAO.verifyAuth(authToken).userName();
    }

    private void checkPlayer(String username, Integer gameID) throws BadRequest {
        var playerType = getPlayerType(username, gameID);
        if (playerType.equals(OBSERVER)) {
            throw new BadRequest();
        }
        var gameData = gameDAO.getGame(gameID);
        if (gameData.game().getGameOver()) {
            throw new BadRequest();
        }
    }

    private UserGameCommand.PlayerType getPlayerType(String username, Integer gameID) {
        var gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new BadRequest();
        }
        if (username.equals(gameData.whiteUsername())) {
            return UserGameCommand.PlayerType.WHITE;
        } else if (username.equals(gameData.blackUsername())) {
            return UserGameCommand.PlayerType.BLACK;
        } else {
            return UserGameCommand.PlayerType.OBSERVER;
        }
    }

    private ChessGame getGame(Integer gameID) throws BadRequest {
        var gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new BadRequest();
        }
        return gameData.game();
    }


}
