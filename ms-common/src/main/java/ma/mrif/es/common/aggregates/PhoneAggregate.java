package ma.mrif.es.common.aggregates;

import lombok.*;
import ma.mrif.es.common.models.Event;
import ma.mrif.es.common.events.PhoneCreatedEvent;
import ma.mrif.es.common.utils.SerializerUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PhoneAggregate extends AggregateRoot {

    public static final String AGGREGATE_TYPE = "PhoneAggregate";

    public PhoneAggregate(String id) {
        super(id, AGGREGATE_TYPE);
    }

    private String name;
    private String number;
    private String creationDate;

    @Override
    public void when(Event event) {
        switch (event.getEventType()) {
            case PhoneCreatedEvent.PHONE_CREATED_V1:
                    handle(SerializerUtils.deserializeFromJsonBytes(event.getData(), PhoneCreatedEvent.class));
            //default: throw new InvalidEventTypeException(event.getEventType());
        }
    }

    private void handle(final PhoneCreatedEvent event) {
        this.name = event.getName();
        this.number = event.getNumber();
    }

    public void createPhone(String name, String number, String user) {
        final var data = PhoneCreatedEvent.builder()
                .aggregateId(id)
                .name(name)
                .user(user)
                .number(number)
                .build();

        final byte[] dataBytes = SerializerUtils.serializeToJsonBytes(data);
        final var event = this.createEvent(PhoneCreatedEvent.PHONE_CREATED_V1, dataBytes, null);
        this.apply(event);
    }

    @Override
    public String toString() {
        return "PhoneAggregate{" +
                "name='" + name + '\'' +
                ",number='" + number + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", version=" + version +
                ", changes=" + changes.size() +
                '}';
    }
}