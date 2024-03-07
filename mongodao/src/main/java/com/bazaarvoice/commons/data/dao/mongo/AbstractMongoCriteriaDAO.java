package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.Criteria;
import com.bazaarvoice.commons.data.dao.CriteriaDAO;
import com.bazaarvoice.commons.data.dao.SortOrder;
import com.bazaarvoice.commons.data.dao.mongo.dbo.FieldsMongoDBObject;
import com.bazaarvoice.commons.data.dao.mongo.dbo.MongoDBObject;
import com.bazaarvoice.commons.data.model.Model;
import com.bazaarvoice.commons.data.model.QueryResults;
import com.bazaarvoice.commons.data.model.SimpleQueryResults;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractMongoCriteriaDAO<T extends Model, C extends Criteria<T, C>, S extends SortOrder<T, S>> extends AbstractMongoDAO<T> implements CriteriaDAO<T, C, S> {
    private static final Log _sLog = LogFactory.getLog(AbstractMongoCriteriaDAO.class);

    @Timed
    @ExceptionMetered
    @Override
    public int findCount(@Nullable C criteria) {
        try (DBCursor dbCursor = getPrimaryCollection().find(convertCriteriaToDBObject(criteria))) {
            return dbCursor.count();
        }
    }

    @Timed
    @ExceptionMetered
    @Override
    public Iterable<String> findIDs(@Nullable C criteria, @Nullable S sortOrder) {

        try (DBCursor dbCursor = getPrimaryCollection().find(convertCriteriaToDBObject(criteria), new FieldsMongoDBObject())
                                                       .sort(convertSortOrderToDBObject(sortOrder))) {
            logQueryDetails(dbCursor);

            return Iterables.transform(dbCursor, dbObject -> {
                Object id = dbObject.get(MongoDBObject.ID_FIELD);
                return id != null ? id.toString() : null;
            });
        }
    }

    @Timed
    @ExceptionMetered
    @Override
    public T findOne(@Nullable C criteria, @Nullable S sortOrder) {
        return findOne(criteria, sortOrder, null);
    }

    @Timed
    @ExceptionMetered
    @Override
    public T findOne(@Nullable C criteria, @Nullable S sortOrder, @Nullable Map<String, Integer> keys) {
        DBObject dbObjectKeys = buildProjectionCondition(keys);

        try (DBCursor dbCursor = getPrimaryCollection().find(convertCriteriaToDBObject(criteria), dbObjectKeys)
                                                       .sort(convertSortOrderToDBObject(sortOrder))
                                                       .limit(1)) {
            logQueryDetails(dbCursor);

            Iterator<DBObject> iter = dbCursor.iterator();
            return iter.hasNext() ? _modelMarshaller.fromDBObject(new MongoDBObject<>(iter.next())) : null;
        }
    }

    @Timed
    @ExceptionMetered
    @Override
    public Iterable<T> find(@Nullable C criteria, @Nullable S sortOrder) {
        return find(criteria, sortOrder, null);
    }

    @Timed
    @ExceptionMetered
    @Override
    public Iterable<T> find(@Nullable C criteria, @Nullable S sortOrder, @Nullable Map<String, Integer> keys) {
        DBObject dbObjectKeys = buildProjectionCondition(keys);

        try (DBCursor dbCursor = getPrimaryCollection().find(convertCriteriaToDBObject(criteria), dbObjectKeys)
                                                       .sort(convertSortOrderToDBObject(sortOrder))) {
            logQueryDetails(dbCursor);

            return Iterables.transform(dbCursor, dbObject -> _modelMarshaller.fromDBObject(new MongoDBObject<>(dbObject)));
        }
    }

    @Timed
    @ExceptionMetered
    @Override
    public Iterable<T> find(@Nullable C criteria, @Nullable S sortOrder, @Nullable Map<String, Integer> keys, int startIndex, int maxResults) {
        DBObject dbObjectKeys = buildProjectionCondition(keys);

        try (DBCursor dbCursor = getPrimaryCollection().find(convertCriteriaToDBObject(criteria), dbObjectKeys)
                                                       .sort(convertSortOrderToDBObject(sortOrder))
                                                       .skip(startIndex)
                                                       .limit(maxResults)) {
            logQueryDetails(dbCursor);

            return Iterables.transform(dbCursor, dbObject -> _modelMarshaller.fromDBObject(new MongoDBObject<>(dbObject)));
        }
    }


    @Timed
    @ExceptionMetered
    @Override
    public QueryResults<T> find(@Nullable C criteria, @Nullable S sortOrder, int startIndex, int maxResults) {
        return find(criteria, sortOrder, startIndex, maxResults, null);
    }


    @Timed
    @ExceptionMetered
    @Override
    public QueryResults<T> find(@Nullable C criteria, @Nullable S sortOrder, int startIndex, int maxResults, @Nullable Map<String, Integer> keys) {
        DBObject dbObjectKeys = buildProjectionCondition(keys);

        try (DBCursor dbCursor = getPrimaryCollection().find(convertCriteriaToDBObject(criteria), dbObjectKeys)
                                                       .sort(convertSortOrderToDBObject(sortOrder))
                                                       .skip(startIndex)
                                                       .limit(maxResults)) {
            logQueryDetails(dbCursor);

            List<T> list = Lists.newArrayList();
            for (DBObject dbObject : dbCursor) {
                list.add(_modelMarshaller.fromDBObject(new MongoDBObject<>(dbObject)));
            }
            return new SimpleQueryResults<>(list, startIndex, dbCursor.count());
        }
    }

    @Timed
    @ExceptionMetered
    @Override
    public Map<String, T> findIndexed(C criteria, @Nullable S sortOrder) {
        Map<String, T> map = Maps.newLinkedHashMap();  // preserve order
        for (T object : find(criteria, sortOrder)) {
            map.put(object.getID(), object);
        }
        return map;
    }


    @Timed
    @ExceptionMetered
    @Override
    public List<String> findDistinct(String propertyName, @Nullable C criteria) {
        //noinspection unchecked
        return (List<String>) getPrimaryCollection().distinct(propertyName, convertCriteriaToDBObject(criteria));
    }

    @Override
    public String getCollectionName() {
        return _primaryCollectionName;
    }


    protected void logQueryDetails(DBCursor dbCursor) {
        if (_sLog.isDebugEnabled()) {
            _sLog.debug("Query: " + dbCursor);
            _sLog.debug("Explain: " + dbCursor.explain());
        }
    }


    /**
     * Converts the given criteria to a DB object.  If not null, default implementation assumes the criteria is a {@link MongoCriteria} object.
     */
    protected DBObject convertCriteriaToDBObject(@Nullable C criteria) {
        if (criteria == null) {
            return new BasicDBObject();
        }

        Assert.isInstanceOf(MongoCriteria.class, criteria);

        MongoCriteria mongoCriteria = (MongoCriteria) criteria;
        return mongoCriteria.toDBObject();
    }

    /**
     * Converts the given sort order to a DB object.  If not null, default implementation assumes the sort order is a {@link MongoSortOrder} object.
     */
    protected DBObject convertSortOrderToDBObject(@Nullable S sortOrder) {
        if (sortOrder == null) {
            // DBCursor.sort() works properly with null values
            return null;
        }

        Assert.isInstanceOf(MongoSortOrder.class, sortOrder);

        MongoSortOrder mongoSortOrder = (MongoSortOrder) sortOrder;
        return mongoSortOrder.toDBObject();
    }

    /**
     * Convert the given map to a DB object representing fields that should or should not be projected from an underlying Mongo statement
     * <p>
     * A value of "1" against key entries can be used to include specific fields in the response
     * <p>
     * A value of "0" against key entries can be used to exclude specific fields in the response
     * @param keys Map of field names against target visibility
     */
    private DBObject buildProjectionCondition(@Nullable Map<String, Integer> keys) {
        final DBObject projection = new MongoDBObject<>();
        if (keys != null) {
            projection.putAll(keys);
        }

        return projection;
    }
}
