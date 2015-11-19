package com.bazaarvoice.commons.data.dao.mongo.dbo;

import com.mongodb.DBObject;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Used to query MongoDB, supporting advanced querying concepts.
 */
public class QueryMongoDBObject extends MongoDBObject<QueryMongoDBObject> {
    public QueryMongoDBObject forID(Object id) {
        return $eq(ID_FIELD, id);
    }

    public QueryMongoDBObject forIDs(Object... values) {
        return $in(ID_FIELD, values);
    }

    public QueryMongoDBObject forIDs(Collection<?> ids) {
        return $in(ID_FIELD, ids);
    }

    public QueryMongoDBObject notForIDs(Object... values) {
        return $nin(ID_FIELD, values);
    }

    public QueryMongoDBObject notForIDs(Collection<?> ids) {
        return $nin(ID_FIELD, ids);
    }

    public QueryMongoDBObject $eq(String key, @Nullable Object value) {
        return append(key, value);
    }

    public QueryMongoDBObject $exists(String key) {
        return $exists(key, true);
    }

    public QueryMongoDBObject $notExists(String key) {
        return $exists(key, false);
    }

    private QueryMongoDBObject $exists(String key, boolean value) {
        return appendToEnhancedProperty(key, "$exists", value);
    }

    public QueryMongoDBObject $notNull(String key) {
        return $ne(key, null);
    }

    public QueryMongoDBObject $ne(String key, @Nullable Object value) {
        return appendToEnhancedProperty(key, "$ne", value);
    }

    public QueryMongoDBObject $gt(String key, Object value) {
        return appendToEnhancedProperty(key, "$gt", value);
    }

    public QueryMongoDBObject $gte(String key, Object value) {
        return appendToEnhancedProperty(key, "$gte", value);
    }

    public QueryMongoDBObject $lt(String key, Object value) {
        return appendToEnhancedProperty(key, "$lt", value);
    }

    public QueryMongoDBObject $lte(String key, Object value) {
        return appendToEnhancedProperty(key, "$lte", value);
    }

    public QueryMongoDBObject $all(String key, Object... values) {
        return $all(key, Arrays.asList(values));
    }

    public QueryMongoDBObject $all(String key, Collection<?> values) {
        return appendToEnhancedProperty(key, "$all", values);
    }

    public QueryMongoDBObject $in(String key, Object... values) {
        return $in(key, Arrays.asList(values));
    }

    public QueryMongoDBObject $in(String key, Collection<?> values) {
        return appendToEnhancedProperty(key, "$in", values);
    }

    public QueryMongoDBObject $nin(String key, Object... values) {
        return $nin(key, Arrays.asList(values));
    }

    public QueryMongoDBObject $nin(String key, Collection<?> values) {
        return appendToEnhancedProperty(key, "$nin", values);
    }

    public QueryMongoDBObject $and(DBObject... queries) {
        return $and(Arrays.asList(queries));
    }

    public QueryMongoDBObject $and(List<? extends DBObject> queries) {
        List<Object> list = convertOrCreateList(get("$and"));
        list.addAll(queries);
        put("$and", list);

        return this;
    }

    public QueryMongoDBObject $or(DBObject... queries) {
        return $or(Arrays.asList(queries));
    }

    public QueryMongoDBObject $or(List<? extends DBObject> queries) {
        List<Object> list = convertOrCreateList(get("$or"));
        list.addAll(queries);
        put("$or", list);

        return this;
    }

    public QueryMongoDBObject $nor(DBObject... queries) {
        return $nor(Arrays.asList(queries));
    }

    public QueryMongoDBObject $nor(List<? extends DBObject> queries) {
        List<Object> list = convertOrCreateList(get("$nor"));
        list.addAll(queries);
        put("$nor", list);

        return this;
    }

    public QueryMongoDBObject $not(String key, Object value) {
        return appendToEnhancedProperty(key, "$not", value);
    }

    public QueryMongoDBObject $size(String key, long value) {
        return appendToEnhancedProperty(key, "$size", value);
    }

    public QueryMongoDBObject $type(String key, int value) {
        return appendToEnhancedProperty(key, "$type", value);
    }

    public QueryMongoDBObject $mod(String key, long value1, long value2) {
        return appendToEnhancedProperty(key, "$mod", Arrays.asList(value1, value2));
    }

    public QueryMongoDBObject $regex(String key, Pattern pattern) {
        // $regex not needed when a Pattern is used
        return append(key, pattern);
    }

    public QueryMongoDBObject $regex(String key, String query, boolean fullText, boolean caseSensitive) {
        if (!fullText) {
            query = "^" + query;
        }

        Pattern pattern;
        if (caseSensitive) {
            pattern = Pattern.compile(query);
        } else {
            pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        }

        // $regex not needed when a Pattern is used
        return append(key, pattern);
    }

    public QueryMongoDBObject $where(String key, String value) {
        return appendToEnhancedProperty(key, "$where", value);
    }

    public QueryMongoDBObject $elemMatch(String key, QueryMongoDBObject subQuery) {
        return appendToEnhancedProperty(key, "$elemMatch", subQuery);
    }

    public QueryMongoDBObject $isolated() {
        return append("$isolated", 1);
    }
}
