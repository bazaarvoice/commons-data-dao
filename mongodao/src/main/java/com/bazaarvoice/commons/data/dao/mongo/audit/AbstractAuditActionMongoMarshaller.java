package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.ModelDAO;
import com.bazaarvoice.commons.data.dao.mongo.AbstractMongoDataMarshaller;
import com.bazaarvoice.commons.data.dao.mongo.dbo.InsertMongoDBObject;
import com.bazaarvoice.commons.data.dao.mongo.dbo.MongoDBObject;
import com.bazaarvoice.commons.data.model.AbstractAuditAction;
import com.bazaarvoice.commons.data.model.User;
import com.google.common.base.Throwables;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nullable;

public abstract class AbstractAuditActionMongoMarshaller<T extends Enum & AuditActionType, A extends AbstractAuditAction<A,U>, U extends User> extends AbstractMongoDataMarshaller<A> implements AuditActionMongoMarshaller<T,A,U> {
    protected AuditActionMongoFields _fields;
    protected AuditActionClassToTypeConverter<T> _classToTypeConverter;

    private ModelDAO<? extends U> _userDAO;

    @Required
    public void setFields(AuditActionMongoFields fields) {
        _fields = fields;
    }

    @Required
    public void setClassToTypeConverter(AuditActionClassToTypeConverter<T> classToTypeConverter) {
        _classToTypeConverter = classToTypeConverter;
    }

    @Required
    public void setUserDAO(ModelDAO<? extends U> userDAO) {
        _userDAO = userDAO;
    }

    @Override
    public InsertMongoDBObject toDBObject(A action) {
        return new InsertMongoDBObject().setID(action.getID()).
                append(_fields.getTypeField(), getActionType(action)).
                append(_fields.getUserField(), action.getUser() != null ? action.getUser().getID() : null).
                append(_fields.getUsernameField(), action.getUser() != null ? action.getUser().getUsername() : null).
                append(_fields.getDateField(), action.getDate()).
                append(_fields.getCommentField(), action.getComment()).
                append(_fields.getRelatedItemField(), action.getRelatedItemID());
    }

    @Override
    public A fromDBObject(MongoDBObject<?> dbObject) {
        A action = newAction((T) dbObject.getEnum(_classToTypeConverter.getTypeEnumClass(), _fields.getTypeField()));

        action.setUser(getUser(dbObject.getString(_fields.getUserField())));
        action.setDate(dbObject.getDate(_fields.getDateField()));
        action.setComment(dbObject.getString(_fields.getCommentField()));
        action.setRelatedItemID(dbObject.getString(_fields.getRelatedItemField()));
        
        return action;
    }

    /**
     * Creates a new audit action of the expected type
     */
    protected A newAction(T type) {
        try {
            //noinspection unchecked
            return (A) type.getActionClass().newInstance();
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the action type based on the action object.
     */
    protected T getActionType(A action) {
        return _classToTypeConverter.apply(action.getClass());
    }

    @Nullable
    protected U getUser(String userID) {
        if (userID == null) {
            return null;
        }

        return _userDAO.get(userID);
    }
}
