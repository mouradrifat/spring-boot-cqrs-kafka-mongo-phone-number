package ma.mrifat.es.query.eventstore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mrif.es.common.aggregates.AggregateRoot;
import ma.mrif.es.common.exceptions.AggregateNotFoundException;
import ma.mrif.es.common.models.Event;
import ma.mrif.es.common.models.Snapshot;
import ma.mrif.es.common.utils.EventSourcingUtils;
import ma.mrifat.es.query.repository.EventRepository;
import ma.mrifat.es.query.repository.SnapshotRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventStore implements EventStoreDB {

    private final EventRepository eventRepository;
    private final SnapshotRepository snapshotRepository;

    @Override
    @Transactional(readOnly = true)
    public <T extends AggregateRoot> T load(String aggregateId, Class<T> aggregateType) {

        final Optional<Snapshot> snapshot = this.loadSnapshot(aggregateId);

        final var aggregate = this.getSnapshotFromClass(snapshot, aggregateId, aggregateType);

        final List<Event> events = this.loadEvents(aggregateId, aggregate.getVersion());
        events.forEach(event -> {
            aggregate.raiseEvent(event);
            log.info("raise event version: {}", event.getVersion());
        });

        if (aggregate.getVersion() == 0) throw new AggregateNotFoundException(aggregateId);

        log.info("(load) loaded aggregate: {}", aggregate);
        return aggregate;
    }

    private List<Event> loadEvents(String aggregateId, long version) {
        return eventRepository.findByAggregateIdAndVersionGreaterThan(aggregateId, version);
    }

    private Optional<Snapshot> loadSnapshot(String aggregateId) {
        return Optional.ofNullable(snapshotRepository.findByAggregateId(aggregateId));
    }

    private <T extends AggregateRoot> T getAggregate(final String aggregateId, final Class<T> aggregateType) {
        try {
            return aggregateType.getConstructor(String.class).newInstance(aggregateId);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private <T extends AggregateRoot> T getSnapshotFromClass(Optional<Snapshot> snapshot, String aggregateId, Class<T> aggregateType) {
        if (snapshot.isEmpty()) {
            final var defaultSnapshot = EventSourcingUtils.snapshotFromAggregate(getAggregate(aggregateId, aggregateType));
            return EventSourcingUtils.aggregateFromSnapshot(defaultSnapshot, aggregateType);
        }
        return EventSourcingUtils.aggregateFromSnapshot(snapshot.get(), aggregateType);
    }

}

