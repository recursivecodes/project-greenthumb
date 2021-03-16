package codes.recursive.pushover;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class PushNotificationResponse {
    private int status;
    private String request;

    public PushNotificationResponse(int status, String request) {
        this.status = status;
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
