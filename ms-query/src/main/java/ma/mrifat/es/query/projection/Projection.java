package ma.mrifat.es.query.projection;

import ma.mrif.es.common.models.Event;

public interface Projection {

    void when(Event event);
}
