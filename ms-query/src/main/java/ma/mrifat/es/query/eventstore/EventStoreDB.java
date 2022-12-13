package ma.mrifat.es.query.eventstore;

import ma.mrif.es.common.aggregates.AggregateRoot;

public interface EventStoreDB {

    <T extends AggregateRoot> T load(final String aggregateId, final Class<T> aggregateType);

}
