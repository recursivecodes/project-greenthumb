package codes.recursive.messaging;

import codes.recursive.client.PushoverClient;
import codes.recursive.domain.Reading;
import codes.recursive.pushover.PushNotificationResponse;
import codes.recursive.repository.ReadingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.env.Environment;
import io.micronaut.http.MediaType;
import io.micronaut.mqtt.annotation.MqttSubscriber;
import io.micronaut.mqtt.annotation.Topic;
import io.micronaut.websocket.WebSocketBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@MqttSubscriber
public class GreenThumbConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(GreenThumbConsumer.class);
    private final WebSocketBroadcaster broadcaster;
    private final ReadingRepository readingRepository;
    private final Environment environment;
    private final String userKey;
    private final String apiKey;
    private final PushoverClient pushoverClient;
    private final long interval = 1000 * 60 * 20;
    private final int moistureThreshold = 40;

    private long lastAlert = System.currentTimeMillis();

    public GreenThumbConsumer(
            WebSocketBroadcaster broadcaster,
            ReadingRepository readingRepository,
            Environment environment,
            @Property(name = "codes.recursive.pushover.userKey") String userKey,
            @Property(name = "codes.recursive.pushover.apiKey") String apiKey,
            PushoverClient pushoverClient
    ) {
        this.broadcaster = broadcaster;
        this.readingRepository = readingRepository;
        this.environment = environment;
        this.userKey = userKey;
        this.apiKey = apiKey;
        this.pushoverClient = pushoverClient;
    }

    @Topic("greenthumb/readings")
    public void receive(Map<String, Object> data) throws JsonProcessingException {
        Reading reading = new Reading(data);
        if( environment.getActiveNames().contains("oraclecloud") ) {
            readingRepository.saveAsync(reading);
            int soilMoisture = (int) reading.getReadingAsMap().get("moisture");
            if( (System.currentTimeMillis() - lastAlert > interval) && soilMoisture < moistureThreshold) {
                PushNotificationResponse response = pushoverClient.pushMessage(
                        apiKey,
                        userKey,
                        "Soil Moisture Alert! Current moisture: " + soilMoisture,
                        "http://greenthumb.toddrsharp.com:8080/page"
                ).blockingFirst();
                lastAlert = System.currentTimeMillis();
            }
        }
        else {
            LOG.info("[localhost]: skipping persistence");
        }
        broadcaster.broadcastAsync(data, MediaType.APPLICATION_JSON_TYPE);
    }
}