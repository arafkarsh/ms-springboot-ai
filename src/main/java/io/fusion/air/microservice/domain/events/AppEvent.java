package io.fusion.air.microservice.domain.events;

import java.time.LocalDateTime;

/**
 * App Event
 *
 * @author arafkarsh
 */
public class AppEvent {

    private String id;
    private String name;
    private LocalDateTime eventTime;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }
}
