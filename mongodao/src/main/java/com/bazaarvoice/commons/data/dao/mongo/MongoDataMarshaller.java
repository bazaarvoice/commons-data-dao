package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.mongo.dbo.MongoDBObject;
import com.mongodb.DBObject;

/**
 * Marshals a data object to/from Mongo
 */
public interface MongoDataMarshaller<T> {
    DBObject toDBObject(T object);

    T fromDBObject(MongoDBObject<?> dbObject);
}
