package ma.mrif.es.command.eventstore;

import ma.mrif.es.common.aggregates.AggregateRoot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mrif.es.common.events.EventBus;
import ma.mrif.es.common.models.Event;
import ma.mrif.es.command.repository.EventRepository;
import ma.mrif.es.command.repository.SnapshotRepository;
import ma.mrif.es.common.utils.EventSourcingUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventStore implements EventStoreDB {

    public static final int SNAPSHOT_FREQUENCY = 3;
    private final EventBus eventBus;
    private final EventRepository eventRepository;
    private final SnapshotRepository snapshotRepository;

    @Override
    @Transactional
    public <T extends AggregateRoot> void save(T aggregate) {
        final List<Event> aggregateEvents = new ArrayList<>(aggregate.getChanges());

        if (aggregate.getVersion() > 1) {
            this.handleConcurrency(aggregate.getId());
        }

        this.saveEvents(aggregate.getChanges());
        if (aggregate.getVersion() % SNAPSHOT_FREQUENCY == 0) {
            this.saveSnapshot(aggregate);
        }

        eventBus.publish(aggregateEvents);

        log.info("(save) saved aggregate: {}", aggregate);
    }

    @Override
    public void saveEvents(List<Event> events) {
        if (events.isEmpty()) return;

        final List<Event> changes = new ArrayList<>(events);
        if (changes.size() > 1) {
            eventRepository.insert(changes);
            log.info("(saveEvents) BATCH saved result: {}", changes.size());
            return;
        }

        final Event event = changes.get(0);

        eventRepository.save(event);

        log.info("(saveEvents) saved event: {}", event);
    }

    private <T extends AggregateRoot> void saveSnapshot(T aggregate) {
        aggregate.toSnapshot();
        final var snapshot = EventSourcingUtils.snapshotFromAggregate(aggregate);

        snapshotRepository.save(snapshot);

        log.info("(saveSnapshot) snapshot: {}", snapshot);
    }

    private void handleConcurrency(String aggregateId) {
        try {
            String aggregateID = eventRepository.findByAggregateId(aggregateId).get(0).getAggregateId();
            log.info("(handleConcurrency) aggregateID for lock: {}", aggregateID);
        } catch (EmptyResultDataAccessException e) {
            log.info("(handleConcurrency) EmptyResultDataAccessException: {}", e.getMessage());
        }
        log.info("(handleConcurrency) aggregateID for lock: {}", aggregateId);
    }

    @Override
    public Boolean exists(String aggregateId) {
        try {
            final var snapshot = snapshotRepository.findByAggregateId(aggregateId);
            log.info("aggregate exists id: {}", snapshot.getAggregateId());
            return true;
        } catch (Exception ex) {
            if (!(ex instanceof EmptyResultDataAccessException)) {
                throw new RuntimeException(ex);
            }
            return false;
        }
    }
}

