package ma.mrifat.es.query.queries;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mrif.es.common.aggregates.PhoneAggregate;
import ma.mrif.es.common.models.Phone;
import ma.mrifat.es.query.repository.PhoneRepository;
import ma.mrifat.es.query.application.converter.PhoneConverter;
import ma.mrifat.es.query.application.dto.PhoneResponse;
import ma.mrifat.es.query.eventstore.EventStoreDB;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PhoneQueryHandler implements PhoneQueryService {

    private final EventStoreDB eventStoreDB;
    private final PhoneRepository phoneRepository;

    @Override
    public PhoneResponse handle(String aggregateId) {

        Phone phone = phoneRepository.findByAggregateId(aggregateId);
        if (Objects.nonNull(phone)) {
            return PhoneConverter.phoneResponseDTOFromDocument(phone);
        }

        PhoneAggregate phoneAggregate = eventStoreDB.load(aggregateId, PhoneAggregate.class);
        Phone phoneDocument = phoneRepository.save(PhoneConverter.phoneDocumentDTOFromAggregate(phoneAggregate));
        log.info("(GetPhoneByIDQuery) savedDocument: {}", phoneDocument);

        PhoneResponse phoneResponse = PhoneConverter.phoneResponseDTOFromAggregate(phoneAggregate);
        log.info("(GetPhone) response: {}", phoneResponse);
        return phoneResponse;
    }

}
