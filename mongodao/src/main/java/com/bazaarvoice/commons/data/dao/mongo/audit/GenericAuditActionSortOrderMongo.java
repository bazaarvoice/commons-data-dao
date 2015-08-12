package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.audit.GenericAuditActionSortOrder;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

public class GenericAuditActionSortOrderMongo<U extends User> extends AbstractAuditActionMongoSortOrder<AuditAction<U>, U, GenericAuditActionSortOrder<U>> implements GenericAuditActionSortOrder<U> {
    public GenericAuditActionSortOrderMongo(AuditActionMongoFields fields) {
        super(fields);
    }
}
