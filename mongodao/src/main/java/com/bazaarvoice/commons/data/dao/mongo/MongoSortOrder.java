package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.SortOrder;
import com.bazaarvoice.commons.data.dao.mongo.dbo.SortMongoDBObject;

public interface MongoSortOrder<T, S extends SortOrder<T, S>> extends SortOrder<T, S> {
    SortMongoDBObject toDBObject();
}
