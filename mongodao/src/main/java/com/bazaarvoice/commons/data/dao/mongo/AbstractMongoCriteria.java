package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.AbstractCriteria;
import com.bazaarvoice.commons.data.dao.Criteria;
import com.bazaarvoice.commons.data.dao.mongo.dbo.QueryMongoDBObject;

import java.util.Collection;

public abstract class AbstractMongoCriteria<T, C extends Criteria<T, C>> extends AbstractCriteria<T, C> implements MongoCriteria<T, C> {
    protected final QueryMongoDBObject _queryDBObject;

    protected AbstractMongoCriteria() {
        _queryDBObject = new QueryMongoDBObject();
    }

    protected AbstractMongoCriteria(QueryMongoDBObject queryDBObject) {
        _queryDBObject = queryDBObject;
    }

    @Override
    public QueryMongoDBObject toDBObject() {
        return _queryDBObject;
    }

    @Override
    public C addIDIn(Collection<String> objectIDs) {
        _queryDBObject.forIDs(objectIDs);

        //noinspection unchecked
        return (C) this;
    }

    @Override
    public C addIDNotIn(Collection<String> objectIDs) {
        _queryDBObject.notForIDs(objectIDs);

        //noinspection unchecked
        return (C) this;
    }
}
