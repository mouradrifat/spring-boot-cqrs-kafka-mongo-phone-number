package ma.mrif.es.command.commands;

import ma.mrif.es.command.eventstore.EventStore;
import ma.mrif.es.common.aggregates.PhoneAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PhoneCommandHandler implements PhoneCommandService {

    @Autowired
    private EventStore eventStoreDB;

    @Override
    public String handle(CreatePhoneCommand command) {
        final PhoneAggregate aggregate = new PhoneAggregate(command.getAggregateID());
        aggregate.createPhone(command.getName(), command.getNumber(), command.getUser());
        eventStoreDB.save(aggregate);

        log.info("(CreatePhoneCommand) aggregate: {}", aggregate);
        return aggregate.getId();
    }
}
