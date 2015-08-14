package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.audit.GenericAuditActionCriteria;
import com.bazaarvoice.commons.data.dao.audit.GenericAuditActionDAORW;
import com.bazaarvoice.commons.data.dao.audit.GenericAuditActionSortOrder;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;
import org.springframework.beans.factory.annotation.Required;

public class GenericAuditActionDAORWMongo<T extends Enum & AuditActionType, U extends User>
        extends AbstractAuditActionMongoDAORW<AuditAction<U>, U, GenericAuditActionCriteria<U>, GenericAuditActionSortOrder<U>>
        implements GenericAuditActionDAORW<U> {

    private AuditActionMongoFields _fields;
    private AuditActionClassToTypeConverter<T> _auditActionClassToTypeConverter;

    @Required
    public void setFields(AuditActionMongoFields fields) {
        _fields = fields;
    }

    @Required
    public void setAuditActionClassToTypeConverter(AuditActionClassToTypeConverter<T> auditActionClassToTypeConverter) {
        _auditActionClassToTypeConverter = auditActionClassToTypeConverter;
    }

    @Override
    public GenericAuditActionCriteria<U> newCriteria() {
        return new GenericAuditActionCriteriaMongo<T,U>(_fields, _auditActionClassToTypeConverter);
    }

    @Override
    public GenericAuditActionSortOrder<U> newSortOrder() {
        return new GenericAuditActionSortOrderMongo<U>(_fields);
    }
}
