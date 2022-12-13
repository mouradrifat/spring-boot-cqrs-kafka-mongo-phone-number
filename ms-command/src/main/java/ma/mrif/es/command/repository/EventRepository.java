package ma.mrif.es.command.repository;

import ma.mrif.es.common.models.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends MongoRepository<Event, UUID> {

    List<Event> findByAggregateId(String aggregateId);
}
