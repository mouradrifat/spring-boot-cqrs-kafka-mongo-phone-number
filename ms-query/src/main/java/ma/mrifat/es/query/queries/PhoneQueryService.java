package ma.mrifat.es.query.queries;

import ma.mrifat.es.query.application.dto.PhoneResponse;

public interface PhoneQueryService {

    PhoneResponse handle(String aggregateId);
}
