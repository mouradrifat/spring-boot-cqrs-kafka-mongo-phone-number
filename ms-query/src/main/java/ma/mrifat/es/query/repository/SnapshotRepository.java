package ma.mrifat.es.query.repository;

import ma.mrif.es.common.models.Snapshot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotRepository extends MongoRepository<Snapshot, Integer> {

    Snapshot findByAggregateId(String aggregateId);
}
