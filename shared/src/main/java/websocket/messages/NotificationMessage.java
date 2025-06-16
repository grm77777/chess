package websocket.messages;

import java.util.Objects;

public class NotificationMessage extends ServerMessage {

    public NotificationMessage(ServerMessageType type, String notificationMessage) {
        super(type, notificationMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        NotificationMessage that = (NotificationMessage) o;
        return Objects.equals(getNotificationMessage(), that.getNotificationMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNotificationMessage());
    }
}
