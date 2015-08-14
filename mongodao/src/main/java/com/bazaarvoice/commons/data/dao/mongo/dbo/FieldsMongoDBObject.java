package com.bazaarvoice.commons.data.dao.mongo.dbo;

import java.util.Arrays;

/**
 * Used to restrict the fields returned from a MongoDB query
 */
public class FieldsMongoDBObject extends MongoDBObject<FieldsMongoDBObject> {
    private boolean _isIncluding = false;
    private boolean _isExcluding = false;

    public FieldsMongoDBObject() {
    }

    /**
     * Include the given keys
     *
     * @param keys The keys to include
     */
    public FieldsMongoDBObject(String... keys) {
        this(true, keys);
    }

    /**
     * Include or exclude the given keys
     *
     * @param isInclude Whether to include or exclude the keys
     * @param keys      The keys to include/exclude
     */
    public FieldsMongoDBObject(boolean isInclude, String... keys) {
        if (isInclude) {
            include(keys);
        } else {
            exclude(keys);
        }
    }

    public void clear() {
        super.clear();

        _isIncluding = false;
        _isExcluding = false;
    }

    public FieldsMongoDBObject excludeID() {
        return exclude(ID_FIELD);
    }

    public FieldsMongoDBObject include(String... keys) {
        return include(Arrays.asList(keys));
    }

    public FieldsMongoDBObject include(Iterable<String> keys) {
        for (String key : keys) {
            setIncluding(key);
            put(key, 1);
        }
        return this;
    }

    public FieldsMongoDBObject exclude(String... keys) {
        return exclude(Arrays.asList(keys));
    }

    public FieldsMongoDBObject exclude(Iterable<String> keys) {
        for (String key : keys) {
            setExcluding(key);
            put(key, 0);
        }
        return this;
    }

    public FieldsMongoDBObject $sliceFirst(String key, int limit) {
        setIncluding(key);

        return append(key, new MongoDBObject().append("$slice", limit));
    }

    public FieldsMongoDBObject $sliceFirst(String key, int start, int limit) {
        setIncluding(key);

        int[] sliceParams = {start, limit};

        return append(key, new MongoDBObject().append("$slice", sliceParams));
    }

    public FieldsMongoDBObject $sliceLast(String key, int limit) {
        setIncluding(key);

        return append(key, new MongoDBObject().append("$slice", -limit));
    }

    public FieldsMongoDBObject $sliceLast(String key, int start, int limit) {
        setIncluding(key);

        int[] sliceParams = {-start, limit};

        return append(key, new MongoDBObject().append("$slice", sliceParams));
    }

    private void setIncluding(String key) {
        assertNotExcluding(key);
        _isIncluding = true;
    }

    private void assertNotExcluding(String key) {
        if (_isExcluding && !isIDField(key)) {
            throw new IllegalStateException("Cannot include and exclude fields at the same time: " + key);
        }
    }

    private void setExcluding(String key) {
        assertNotIncluding(key);
        _isExcluding = true;
    }

    private void assertNotIncluding(String key) {
        if (_isIncluding && !isIDField(key)) {
            throw new IllegalStateException("Cannot include and exclude fields at the same time: " + key);
        }
    }

    private boolean isIDField(String key) {
        return ID_FIELD.equals(key);
    }
}
