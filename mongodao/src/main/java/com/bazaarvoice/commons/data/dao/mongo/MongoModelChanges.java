package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.ModelChanges;
import com.bazaarvoice.commons.data.dao.mongo.dbo.UpdateMongoDBObject;

public interface MongoModelChanges<T> extends ModelChanges<T> {
    UpdateMongoDBObject toDBObject();
}
