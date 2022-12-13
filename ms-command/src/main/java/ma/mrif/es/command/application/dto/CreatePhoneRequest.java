package ma.mrif.es.command.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePhoneRequest {

    private String name;
    private String number;
    private String user;
}
