package codes.recursive;

import io.micronaut.runtime.Micronaut;

import java.util.TimeZone;

public class Application {

    public static void main(String[] args) {
        System.setProperty("oracle.jdbc.fanEnabled", "false");
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        Micronaut.run(Application.class, args);
    }
}
