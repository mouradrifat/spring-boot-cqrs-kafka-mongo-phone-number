package ma.mrif.es.command.application.controller;

import ma.mrif.es.command.application.dto.CreatePhoneRequest;
import ma.mrif.es.command.commands.PhoneCommandService;
import ma.mrif.es.command.commands.CreatePhoneCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class PhoneCommandController {

    @Autowired
    private PhoneCommandService commandService;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> newPhone(@RequestBody CreatePhoneRequest req) {
        final var aggregateID = UUID.randomUUID().toString();
        final var id = commandService.handle(new CreatePhoneCommand(aggregateID, req.getName(), req.getNumber(), req.getUser()));
        log.info("Created bank account id: {}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}
