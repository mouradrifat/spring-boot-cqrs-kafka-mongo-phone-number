package ma.mrif.es.command.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreatePhoneCommand {

    public String aggregateID;
    public String name;
    public String number;
    public String user;
}
