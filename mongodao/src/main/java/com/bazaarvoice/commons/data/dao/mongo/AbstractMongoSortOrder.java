package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.AbstractSortOrder;
import com.bazaarvoice.commons.data.dao.SortOrder;
import com.bazaarvoice.commons.data.dao.mongo.dbo.SortMongoDBObject;
import com.bazaarvoice.commons.data.model.SortDirection;

public abstract class AbstractMongoSortOrder<T, S extends SortOrder<T, S>> extends AbstractSortOrder<T, S> implements MongoSortOrder<T, S> {
    protected final SortMongoDBObject _sortDBObject = new SortMongoDBObject();

    @Override
    public SortMongoDBObject toDBObject() {
        return _sortDBObject;
    }

    @Override
    public S addByID(SortDirection direction) {
        if (direction == SortDirection.DESCENDING) {
            _sortDBObject.descendByID();
        } else {
            _sortDBObject.ascendByID();
        }

        //noinspection unchecked
        return (S) this;
    }

    protected S addByField(String fieldName, SortDirection direction) {
        if (direction == SortDirection.DESCENDING) {
            _sortDBObject.descendBy(fieldName);
        } else {
            _sortDBObject.ascendBy(fieldName);
        }

        //noinspection unchecked
        return (S) this;
    }
}
