package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.audit.GenericAuditActionCriteria;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

public class GenericAuditActionCriteriaMongo<T extends Enum & AuditActionType, U extends User> extends AbstractAuditActionMongoCriteria<T, AuditAction<U>, U, GenericAuditActionCriteria<U>> implements GenericAuditActionCriteria<U> {
    public GenericAuditActionCriteriaMongo(AuditActionMongoFields fields, AuditActionClassToTypeConverter<T> auditActionClassToTypeConverterFunction) {
        super(fields, auditActionClassToTypeConverterFunction);
    }
}
