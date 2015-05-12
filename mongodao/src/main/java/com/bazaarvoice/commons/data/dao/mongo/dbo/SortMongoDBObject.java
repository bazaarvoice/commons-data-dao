package com.bazaarvoice.commons.data.dao.mongo.dbo;

import java.util.Arrays;

/**
 * Used to add sorting to results.
 */
public class SortMongoDBObject extends MongoDBObject<SortMongoDBObject> {
    public SortMongoDBObject ascendByID() {
        return ascendBy(ID_FIELD);
    }

    public SortMongoDBObject descendByID() {
        return descendBy(ID_FIELD);
    }

    public SortMongoDBObject ascendBy(String... keys) {
        return ascendBy(Arrays.asList(keys));
    }

    public SortMongoDBObject ascendBy(Iterable<String> keys) {
        for (String key : keys) {
            put(key, 1);
        }
        return this;
    }

    public SortMongoDBObject descendBy(String... keys) {
        return descendBy(Arrays.asList(keys));
    }

    public SortMongoDBObject descendBy(Iterable<String> keys) {
        for (String key : keys) {
            append(key, -1);
        }
        return this;
    }

    public SortMongoDBObject $natural() {
        clear();
        return this;
    }

    public SortMongoDBObject $reverseNatural() {
        clear();
        return append("$natural", -1);
    }
}
