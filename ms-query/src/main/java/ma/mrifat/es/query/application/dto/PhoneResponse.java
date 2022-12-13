package ma.mrifat.es.query.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneResponse {

    private String user;
    private String number;
    private String name;
}
