package ma.mrif.es.command.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mrif.es.common.models.Event;
import ma.mrif.es.common.events.EventBus;
import ma.mrif.es.common.utils.SerializerUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEventBus implements EventBus {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final static long sendTimeout = 3000;

    @Value(value = "NewTopic")
    private String phoneTopicName;

    @Override
    public void publish(List<Event> events) {
        final byte[] eventsBytes = SerializerUtils.serializeToJsonBytes(events.toArray(new Event[]{}));
        final ProducerRecord<String, byte[]> record = new ProducerRecord<>(phoneTopicName, eventsBytes);

        try {
            kafkaTemplate.send(record).get(sendTimeout, TimeUnit.MILLISECONDS);
            log.info("publishing kafka record value >>>>> {}", new String(record.value()));

        } catch (Exception ex) {
            log.error("(KafkaEventBus) publish get timeout", ex);
            throw new RuntimeException(ex);
        }
    }
}
