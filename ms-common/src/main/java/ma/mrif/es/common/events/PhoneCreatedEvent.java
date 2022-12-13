package ma.mrif.es.common.events;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PhoneCreatedEvent {

    public static final String PHONE_CREATED_V1 = "PHONE_CREATED_V1";

    private String aggregateId;
    private String name;
    private String number;
    private String user;
    private String creationDate;
}
