package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.model.AbstractAuditAction;
import com.bazaarvoice.commons.data.model.User;

import java.util.EnumSet;
import java.util.Set;

public class GenericAuditActionMarshallerMongo<T extends Enum & AuditActionType, A extends AbstractAuditAction<A, U>, U extends User> extends AbstractAuditActionMongoMarshaller<T,A,U> {
    @Override
    public Set<T> getSupportedActionTypes() {
        return EnumSet.allOf(_classToTypeConverter.getTypeEnumClass());
    }

}
