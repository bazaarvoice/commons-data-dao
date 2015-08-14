package com.bazaarvoice.commons.data.dao.mongo.dbo;

/**
 * Used to create new objects or to replace an entire object in MongoDB (e.g. Insert or Save operations)
 */
public class InsertMongoDBObject extends MongoDBObject<InsertMongoDBObject> {
    public InsertMongoDBObject setID(final Object id) {
        return append(ID_FIELD, id);
    }
}
