package codes.recursive.websocket;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

@ServerWebSocket("/data/{topic}")
public class GreenThumbWebSocket {

    private static final Logger LOG = LoggerFactory.getLogger(GreenThumbWebSocket.class);

    private final WebSocketBroadcaster broadcaster;

    public GreenThumbWebSocket(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public void onOpen(String topic, WebSocketSession session) {
        broadcaster.broadcastSync("Joined channel", isValid(topic, session));
    }

    @OnMessage
    public void onMessage(String topic, String message, WebSocketSession session) {
        broadcaster.broadcastSync(message, isValid(topic, session));
    }

    @OnClose
    public void onClose(String topic, WebSocketSession session) {
        broadcaster.broadcastSync("Disconnected", isValid(topic, session));
    }

    private Predicate<WebSocketSession> isValid(String topic, WebSocketSession session) {
        return s -> s != session &&
                topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null));
    }
}
