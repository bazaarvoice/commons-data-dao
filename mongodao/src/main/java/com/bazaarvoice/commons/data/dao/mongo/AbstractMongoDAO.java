package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.ModelChanges;
import com.bazaarvoice.commons.data.dao.ModelDAO;
import com.bazaarvoice.commons.data.dao.mongo.dbo.MongoDBObject;
import com.bazaarvoice.commons.data.dao.mongo.dbo.QueryMongoDBObject;
import com.bazaarvoice.commons.data.model.Model;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.annotation.ExceptionMetered;
import com.yammer.metrics.annotation.Timed;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AbstractMongoDAO<T extends Model> implements ModelDAO<T> {
    protected MongoAccessService _mongoAccessService;
    protected String _primaryCollectionName;
    protected MongoDataMarshaller<T> _modelMarshaller;

    @Required
    public void setMongoAccessService(MongoAccessService mongoAccessService) {
        _mongoAccessService = mongoAccessService;
    }

    @Required
    public void setPrimaryCollectionName(String primaryCollectionName) {
        _primaryCollectionName = primaryCollectionName;
    }

    @Required
    public void setModelMarshaller(MongoDataMarshaller<T> modelMarshaller) {
        _modelMarshaller = modelMarshaller;
    }

    private final Timer createTimer = Metrics.newTimer(getClass(), "create");
    private final Meter createExceptionsMeter = Metrics.newMeter(getClass(), "createExceptions", "exceptions", TimeUnit.SECONDS);

    @Timed
    @ExceptionMetered
    @Override
    @Nullable
    public T get(String objectID) {
        T object = getCachedObject(objectID);
        if (object != null) {
            return object;
        }

        DBCursor dbCursor = getPrimaryCollection().find(new QueryMongoDBObject().forID(objectID)).limit(1);
        try {
            Iterator<DBObject> iterator = dbCursor.iterator();
            if (!iterator.hasNext()) {
                return null;
            }

            object = (T) _modelMarshaller.fromDBObject(new MongoDBObject(iterator.next()));
            cacheObject(object);

            return object;
        } finally {
            dbCursor.close();
        }
    }

    @Timed
    @ExceptionMetered
    @Override
    public List<T> get(List<String> objectIDs) {
        // enhance: cache objects
        // enhance: support lazy-loading of objects

        DBCursor dbCursor = getPrimaryCollection().find(new QueryMongoDBObject().forIDs(objectIDs)).batchSize(Integer.MAX_VALUE);

        Map<String, T> index = Maps.newHashMap();
        try {
            for (DBObject dbObject : dbCursor) {
                if (dbObject != null) {
                    T object = (T) _modelMarshaller.fromDBObject((MongoDBObject) dbObject);
                    index.put(object.getID(), object);
                }
            }
        } finally {
            dbCursor.close();
        }

        List<T> sorted = Lists.newArrayListWithCapacity(index.size());
        for (String objectID : objectIDs) {
            T object = index.get(objectID);
            if (object != null) {
                sorted.add(object);
            }
        }

        return sorted;
    }

    protected T getCachedObject(String id) {
        return null;
    }

    protected void cacheObject(T object) {
    }

    @Timed
    @ExceptionMetered
    public void save(T object) {
        saveAll(Collections.singleton(object));
    }

    @Timed
    @ExceptionMetered
    public void saveAll(Collection<T> objects) {
        for (T object : objects) {
            getPrimaryCollection().save(_modelMarshaller.toDBObject(object));
        }
    }

    public void create(T... objects) {
        // must handle this manually since AOP pointcuts don't work with ellipses methods!
        TimerContext timer = createTimer.time();
        try {
            create(Arrays.asList(objects));
        } catch (Exception e) {
            createExceptionsMeter.mark();
            throw Throwables.propagate(e);
        } finally {
            timer.stop();
        }
    }

    @Timed
    @ExceptionMetered
    public void create(Collection<T> objects) {
        List<DBObject> dbObjects = Lists.newArrayList();
        for (T object : objects) {
            dbObjects.add(_modelMarshaller.toDBObject(object));
        }
        getPrimaryCollection().insert(dbObjects);
    }

    protected void saveModelChanges(String objectID, ModelChanges<T> modelChanges) {
        Assert.isInstanceOf(MongoModelChanges.class, modelChanges);
        MongoModelChanges<T> mongoModelChanges = (MongoModelChanges<T>) modelChanges;

        DBCollection primaryCollection = getPrimaryCollection();
        primaryCollection.update(new QueryMongoDBObject().forID(objectID), mongoModelChanges.toDBObject(), false, false);
    }

    @Timed
    @ExceptionMetered
    public void delete(T object) {
        deleteAllByID(Collections.singletonList(object.getID()));
    }

    @Timed
    @ExceptionMetered
    public void deleteByID(String objectID) {
        deleteAllByID(Collections.singletonList(objectID));
    }

    @Timed
    @ExceptionMetered
    public void deleteAll(Collection<T> objects) {
        deleteAllByID(Collections2.transform(objects, new Function<T, String>() {
            @Override
            public String apply(T input) {
                return input.getID();
            }
        }));
    }

    @Timed
    @ExceptionMetered
    public void deleteAllByID(Collection<String> objectIDs) {
        getPrimaryCollection().remove(new QueryMongoDBObject().forIDs(objectIDs));
    }

    protected int getNextSequenceValue(String sequenceName) {
        return _mongoAccessService.nextSequenceValue(sequenceName);
    }

    protected DBCollection getPrimaryCollection() {
        return _mongoAccessService.getDB().getCollection(_primaryCollectionName);
    }
}
