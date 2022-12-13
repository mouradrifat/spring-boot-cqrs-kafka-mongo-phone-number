package ma.mrif.es.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Snapshot {

    @Id
    private UUID id;
    private String aggregateId;
    private String aggregateType;
    private byte[] data;
    private byte[] metaData;
    private long version;
    private LocalDateTime timeStamp;

    @Override
    public String toString() {
        return "Snapshot{" +
            "id=" + id +
            ", aggregateId='" + aggregateId + '\'' +
            ", aggregateType='" + aggregateType + '\'' +
            ", data=" + data.length + " bytes" +
            ", metaData=" + metaData.length + " bytes" +
            ", version=" + version +
            ", timeStamp=" + timeStamp +
            '}';
    }
}