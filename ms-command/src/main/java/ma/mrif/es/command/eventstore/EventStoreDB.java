package ma.mrif.es.command.eventstore;

import ma.mrif.es.common.aggregates.AggregateRoot;
import ma.mrif.es.common.models.Event;

import java.util.List;

public interface EventStoreDB {

    void saveEvents(final List<Event> events);

    <T extends AggregateRoot> void save(final T aggregate);

    Boolean exists(final String aggregateId);
}
