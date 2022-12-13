package ma.mrifat.es.query.projection;

import ma.mrif.es.common.aggregates.PhoneAggregate;
import ma.mrif.es.common.events.PhoneCreatedEvent;
import ma.mrif.es.common.models.Event;
import ma.mrif.es.common.models.Phone;
import ma.mrifat.es.query.repository.PhoneRepository;
import ma.mrif.es.common.utils.SerializerUtils;
import lombok.extern.log4j.Log4j2;
import ma.mrifat.es.query.application.converter.PhoneConverter;
import ma.mrifat.es.query.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
public class KafkaEventListener implements Projection {

    @Autowired
    private PhoneRepository phoneRepository;

    private EventStore eventStoreDB;

    @KafkaListener(topics = {"${microservice.kafka.topics.phone-event-store}"},
            groupId = "${microservice.kafka.groupId}",
            concurrency = "${microservice.kafka.default-concurrency}")
    public void listen(@Payload byte[] data, ConsumerRecordMetadata meta) {
        log.info("(PhoneMongoProjection) topic: {}, offset: {}, partition: {}, timestamp: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp());

        try {
            final Event[] events = SerializerUtils.deserializeEventsFromJsonBytes(data);
            this.processEvents(Arrays.stream(events).collect(Collectors.toList()));
            log.info("ack events: {}", Arrays.toString(events));
        } catch (Exception ex) {
            log.error("(PhoneMongoProjection) topic: {}, offset: {}, partition: {}, timestamp: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), ex);
        }
    }

    private void processEvents(List<Event> events) {
        if (events.isEmpty()) return;

        try {
            events.forEach(this::when);
        } catch (Exception ex) {
            phoneRepository.deleteByAggregateId(events.get(0).getAggregateId());
            PhoneAggregate phoneAggregate = eventStoreDB.load(events.get(0).getAggregateId(), PhoneAggregate.class);
            final var document = PhoneConverter.phoneDocumentDTOFromAggregate(phoneAggregate);
            final var result = phoneRepository.save(document);
            log.info("(processEvents) saved document: {}", result);
        }
    }

    @Override
    public void when(Event event) {
        final var aggregateId = event.getAggregateId();
        log.info("(when) >>>>> aggregateId: {}", aggregateId);

        switch (event.getEventType()) {
            case PhoneCreatedEvent.PHONE_CREATED_V1:
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), PhoneCreatedEvent.class));
            default: log.error("unknown event type: {}", event.getEventType());
        }
    }

    private void handle(PhoneCreatedEvent event) {
        log.info("(when) PhoneCreatedEvent: {}, aggregateID: {}", event, event.getAggregateId());

        final var document = Phone.builder()
                .aggregateId(event.getAggregateId())
                .name(event.getName())
                .number(event.getNumber())
                .build();

        final var insert = phoneRepository.insert(document);
        log.info("(PhoneCreatedEvent) insert: {}", insert);
    }
}
