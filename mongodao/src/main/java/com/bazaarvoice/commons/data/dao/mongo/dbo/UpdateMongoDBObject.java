package com.bazaarvoice.commons.data.dao.mongo.dbo;

import com.google.common.collect.Sets;
import com.mongodb.DBObject;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Used for changing objects that already exist in MongoDB.  Allows setting specific values, adding and removing from arrays, etc.
 */
public class UpdateMongoDBObject extends MongoDBObject<UpdateMongoDBObject> {
    public UpdateMongoDBObject $set(String key, Object value) {
        // can't set and unset the same field
        DBObject $unset = getDBObjectProperty(MongoDBObject.class, "$unset");
        if ($unset != null && $unset.containsField(key)) {
            $unset.removeField(key);
        }
        return appendToEnhancedProperty("$set", key, value);
    }

    public UpdateMongoDBObject $unset(String key) {
        // can't set and unset the same field
        DBObject $set = getDBObjectProperty(MongoDBObject.class, "$set");
        if ($set != null && $set.containsField(key)) {
            $set.removeField(key);
        }
        return appendToEnhancedProperty("$unset", key, 1);
    }

    public UpdateMongoDBObject $inc(String key) {
        return $inc(key, 1);
    }

    public UpdateMongoDBObject $dec(String key) {
        return $inc(key, -1);
    }

    public UpdateMongoDBObject $inc(String key, Number value) {
        return appendToEnhancedProperty("$inc", key, value);
    }

    public UpdateMongoDBObject $push(String key, Object value) {
        MongoDBObject $push = getOrCreateDBObjectProperty("$push");

        Object currentValue = $push.get(key);
        if (currentValue == null) {
            $push.put(key, value);
        } else {
            $pushAll(key, Collections.singletonList(currentValue));
        }

        return this;
    }

    public UpdateMongoDBObject $pushAll(String key, Collection<?> values) {
        MongoDBObject $pushAll = getOrCreateDBObjectProperty("$pushAll");

        List<Object> list = convertOrCreateList($pushAll.get(key));
        list.addAll(values);
        $pushAll.put(key, list);

        return this;
    }

    public UpdateMongoDBObject $addToSet(String key, Object value) {
        MongoDBObject $addToSet = getOrCreateDBObjectProperty("$addToSet");

        getOrCreateEachSet($addToSet, key).add(convertIfEnum(value));

        return this;
    }

    public UpdateMongoDBObject $addToSet(String key, Collection<?> values) {
        MongoDBObject $addToSet = getOrCreateDBObjectProperty("$addToSet");

        getOrCreateEachSet($addToSet, key).addAll(convertIfEnums(values));

        return this;
    }

    private Set<Object> getOrCreateEachSet(MongoDBObject<?> $addToSet, String key) {
        MongoDBObject<?> $each = $addToSet.getDBObjectProperty(MongoDBObject.class, key);
        if ($each == null) {
            $each = new MongoDBObject();
            $addToSet.put(key, $each);
        }

        //noinspection unchecked
        Set<Object> set = (Set<Object>) $each.get("$each");
        if (set == null) {
            set = Sets.newHashSet();
            $each.put("$each", set);
        }
        return set;
    }

    public UpdateMongoDBObject $popFirst(String key) {
        return appendToEnhancedProperty("$pop", key, -1);
    }

    public UpdateMongoDBObject $popLast(String key) {
        return appendToEnhancedProperty("$pop", key, 1);
    }

    public UpdateMongoDBObject $pull(String key, Object value) {
        return appendToEnhancedProperty("$pull", key, value);
    }

    public UpdateMongoDBObject $pullAll(String key, Collection<?> values) {
        return appendToEnhancedProperty("$pullAll", key, values);
    }

    public UpdateMongoDBObject $rename(String oldProperty, String newProperty) {
        return appendToEnhancedProperty("$rename", oldProperty, newProperty);
    }
}
