package ma.mrif.es.common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Event {

    @Id
    @BsonProperty(value = "_id")
    private String id;

    @BsonProperty(value = "aggregateId")
    private String aggregateId;

    @BsonProperty(value = "eventType")
    private String eventType;

    @BsonProperty(value = "aggregateType")
    private String aggregateType;

    @BsonProperty(value = "version")
    private long version;

    @BsonProperty(value = "data")
    private byte[] data;

    @BsonProperty(value = "metaData")
    private byte[] metaData;

    @BsonProperty(value = "timeStamp")
    private LocalDateTime timeStamp;


    public Event(String eventType, String aggregateType) {
        this.id = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.timeStamp = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", aggregateId='" + aggregateId + '\'' +
            ", eventType='" + eventType + '\'' +
            ", aggregateType='" + aggregateType + '\'' +
            ", version=" + version + '\'' +
            ", timeStamp=" + timeStamp + '\'' +
            ", data=" + new String(data) + '\'' +
            '}';
    }
}
