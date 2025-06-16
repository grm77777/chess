package websocket.messages;

import chess.ChessGame;
import java.util.Objects;

public class LoadGameMessage extends ServerMessage {

    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type, game);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        LoadGameMessage that = (LoadGameMessage) o;
        return Objects.equals(getGame(), that.getGame());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGame());
    }
}
