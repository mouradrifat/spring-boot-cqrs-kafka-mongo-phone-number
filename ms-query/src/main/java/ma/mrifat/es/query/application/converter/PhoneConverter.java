package ma.mrifat.es.query.application.converter;

import ma.mrif.es.common.aggregates.PhoneAggregate;
import ma.mrif.es.common.models.Phone;
import ma.mrifat.es.query.application.dto.PhoneResponse;

public class PhoneConverter {

    public static PhoneResponse phoneResponseDTOFromAggregate(PhoneAggregate phoneAggregate) {
        return PhoneResponse.builder()
                .number(phoneAggregate.getNumber())
                .name(phoneAggregate.getName())
                .build();
    }

    public static PhoneResponse phoneResponseDTOFromDocument(Phone phone) {
        return PhoneResponse.builder()
                .number(phone.getNumber())
                .name(phone.getName())
                .build();
    }

    public static Phone phoneDocumentDTOFromAggregate(PhoneAggregate phoneAggregate) {
        return Phone.builder()
                .number(phoneAggregate.getNumber())
                .name(phoneAggregate.getName())
                .build();
    }
}
