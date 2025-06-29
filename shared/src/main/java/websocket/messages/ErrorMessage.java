package websocket.messages;

import java.util.Objects;

public class ErrorMessage extends ServerMessage {

    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type, errorMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ErrorMessage that = (ErrorMessage) o;
        return Objects.equals(getErrorMessage(), that.getErrorMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getErrorMessage());
    }
}
