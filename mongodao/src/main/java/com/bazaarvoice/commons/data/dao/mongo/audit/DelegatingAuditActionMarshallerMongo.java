package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.mongo.MongoDataMarshaller;
import com.bazaarvoice.commons.data.dao.mongo.dbo.MongoDBObject;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;
import com.google.common.collect.Maps;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.Map;

public class DelegatingAuditActionMarshallerMongo<T extends Enum & AuditActionType, U extends User> implements MongoDataMarshaller<AuditAction<U>> {
    private final Map<T, AuditActionMongoMarshaller<T, ?, ?>> _marshallersByType = Maps.newHashMap();

    private AuditActionMongoFields _fields;
    private AuditActionClassToTypeConverter<T> _classToTypeConverter;

    @Required
    public void setMarshallers(Collection<AuditActionMongoMarshaller<T, ?, ?>> marshallers) {
        for (AuditActionMongoMarshaller<T, ?, ?> marshaller : marshallers) {
            for (T type : marshaller.getSupportedActionTypes()) {
                if (!_marshallersByType.containsKey(type)) {
                    _marshallersByType.put(type, marshaller);
                }
            }
        }
    }

    @Required
    public void setFields(AuditActionMongoFields fields) {
        _fields = fields;
    }

    @Required
    public void setClassToTypeConverter(AuditActionClassToTypeConverter<T> classToTypeConverter) {
        _classToTypeConverter = classToTypeConverter;
    }

    @Override
    public DBObject toDBObject(AuditAction<U> action) {
        //noinspection unchecked
        return getMarshaller((Class<? extends AuditAction<U>>) action.getClass()).toDBObject(action);
    }

    @Override
    public AuditAction<U> fromDBObject(MongoDBObject<?> dbObject) {
        //noinspection unchecked
        return (AuditAction<U>) getMarshaller((T) dbObject.getEnum(_classToTypeConverter.getTypeEnumClass(), _fields.getTypeField())).fromDBObject(dbObject);
    }

    private AuditActionMongoMarshaller<T, AuditAction<U>, U> getMarshaller(Class<? extends AuditAction<U>> actionClass) {
        @SuppressWarnings ({"unchecked"})
        AuditActionMongoMarshaller<T, AuditAction<U>, U> marshaller = (AuditActionMongoMarshaller<T, AuditAction<U>, U>) _marshallersByType.get(_classToTypeConverter.apply(actionClass));
        if (marshaller == null) {
            throw new IllegalArgumentException("Marshaller not found for class: " + actionClass);
        }
        return marshaller;
    }

    private AuditActionMongoMarshaller<T, ?, ?> getMarshaller(T actionType) {
        AuditActionMongoMarshaller<T, ?, ?> marshaller = _marshallersByType.get(actionType);
        if (marshaller == null) {
            throw new IllegalArgumentException("Marshaller not found for type: " + actionType);
        }
        return marshaller;
    }
}
