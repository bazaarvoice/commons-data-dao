package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.mongo.MongoDataMarshaller;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

import java.util.Set;

public interface AuditActionMongoMarshaller<T extends Enum & AuditActionType, A extends AuditAction<U>, U extends User> extends MongoDataMarshaller<A> {

    Set<T> getSupportedActionTypes();

}
