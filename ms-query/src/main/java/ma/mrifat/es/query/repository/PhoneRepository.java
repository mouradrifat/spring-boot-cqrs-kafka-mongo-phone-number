package ma.mrifat.es.query.repository;

import ma.mrif.es.common.models.Phone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends MongoRepository<Phone, Integer> {

    Phone findByAggregateId(String aggregateId);
    void deleteByAggregateId(String aggregateId);
}
