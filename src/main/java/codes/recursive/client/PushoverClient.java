package codes.recursive.client;

import codes.recursive.pushover.PushNotificationResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

@Client("https://api.pushover.net")
public interface PushoverClient {
    @Post(value = "/1/messages.json", produces = MediaType.APPLICATION_FORM_URLENCODED, consumes = MediaType.APPLICATION_JSON)
    Flowable<PushNotificationResponse> pushMessage(String token, String user, String message, String url);
}
