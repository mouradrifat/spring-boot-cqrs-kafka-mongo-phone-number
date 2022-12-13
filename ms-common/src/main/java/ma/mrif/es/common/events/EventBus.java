package ma.mrif.es.common.events;

import ma.mrif.es.common.models.Event;

import java.util.List;

public interface EventBus {
    void publish(List<Event> events);
}