package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.audit.AuditActionCriteria;
import com.bazaarvoice.commons.data.dao.mongo.AbstractMongoCriteria;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;
import com.google.common.collect.Collections2;

import java.util.Collection;
import java.util.Date;

public abstract class AbstractAuditActionMongoCriteria<T extends Enum & AuditActionType, A extends AuditAction<U>, U extends User, C extends AuditActionCriteria<A,U,C>> extends AbstractMongoCriteria<A,C> implements AuditActionCriteria<A,U,C> {
    protected final AuditActionMongoFields _fields;
    protected final AuditActionClassToTypeConverter<T> _classToTypeConverter;

    public AbstractAuditActionMongoCriteria(AuditActionMongoFields fields, AuditActionClassToTypeConverter<T> classToTypeConverter) {
        _fields = fields;
        _classToTypeConverter = classToTypeConverter;
    }

    @Override
    public C userEquals(String userID) {
        _queryDBObject.$eq(_fields.getUserField(), userID);

        //noinspection unchecked
        return (C) this;
    }

    @Override
    public C userIn(Collection<String> userIDs) {
        _queryDBObject.$in(_fields.getUserField(), userIDs);

        //noinspection unchecked
        return (C) this;
    }

    @Override
    public C dateBefore(Date date) {
        _queryDBObject.$lt(_fields.getDateField(), date);

        //noinspection unchecked
        return (C) this;
    }

    @Override
    public C dateAfter(Date date) {
        _queryDBObject.$gt(_fields.getDateField(), date);

        //noinspection unchecked
        return (C) this;
    }

    @Override
    public C dateBetween(Date start, Date end) {
        _queryDBObject.$gte(_fields.getDateField(), start);
        _queryDBObject.$lt(_fields.getDateField(), end);

        //noinspection unchecked
        return (C) this;
    }

    @Override
    public C relatedItemIDEquals(String relatedItemID) {
        _queryDBObject.$eq(_fields.getRelatedItemField(), relatedItemID);

        //noinspection unchecked
        return (C) this;
    }

    @Override
    public C relatedItemIDIn(Collection<String> relatedItemIDs) {
        _queryDBObject.$in(_fields.getRelatedItemField(), relatedItemIDs);

        //noinspection unchecked
        return (C) this;
    }

    @Override
    public C actionTypeEquals(Class<? extends A> actionClass) {
        _queryDBObject.$eq(_fields.getTypeField(), _classToTypeConverter.apply(actionClass));

        //noinspection unchecked
        return (C) this;
    }

    @Override
    public C actionTypeIn(Collection<Class<? extends A>> actionClasses) {
        _queryDBObject.$in(_fields.getTypeField(), Collections2.transform(actionClasses, _classToTypeConverter));

        //noinspection unchecked
        return (C) this;
    }

}
