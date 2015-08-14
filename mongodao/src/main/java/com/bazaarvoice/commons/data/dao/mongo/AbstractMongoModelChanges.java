package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.mongo.dbo.QueryMongoDBObject;
import com.bazaarvoice.commons.data.dao.mongo.dbo.UpdateMongoDBObject;

import javax.annotation.Nullable;
import java.util.Collection;

public abstract class AbstractMongoModelChanges<T> implements MongoModelChanges<T> {
    protected final T _modelObject;
    protected final UpdateMongoDBObject _updateDBObject = new UpdateMongoDBObject();

    public AbstractMongoModelChanges(@Nullable T modelObject) {
        _modelObject = modelObject;
    }

    @Override
    @Nullable
    public T getModel() {
        return _modelObject;
    }

    @Override
    public UpdateMongoDBObject toDBObject() {
        return _updateDBObject;
    }

    protected void setValue(String propertyName, Object value) {
        _updateDBObject.$set(propertyName, value);
    }

    protected void unsetValue(String propertyName) {
        _updateDBObject.$unset(propertyName);
    }

    protected void addValue(String propertyName, Object value) {
        _updateDBObject.$push(propertyName, value);
    }

    protected void addValueToSet(String propertyName, Object value) {
        _updateDBObject.$addToSet(propertyName, value);
    }

    protected void addValues(String propertyName, Collection<?> values) {
        _updateDBObject.$pushAll(propertyName, values);
    }

    protected void addValuesToSet(String propertyName, Collection<?> values) {
        _updateDBObject.$addToSet(propertyName, values);
    }

    protected void removeValue(String propertyName, Object value) {
        _updateDBObject.$pull(propertyName, value);
    }

    protected void removeValueWithQuery(String propertyName, QueryMongoDBObject query) {
        removeValue(propertyName, query);
    }

    protected void removeValues(String propertyName, Collection<?> values) {
        _updateDBObject.$pullAll(propertyName, values);
    }
}
