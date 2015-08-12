package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.audit.AuditActionSortOrder;
import com.bazaarvoice.commons.data.dao.mongo.AbstractMongoSortOrder;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.SortDirection;
import com.bazaarvoice.commons.data.model.User;

public abstract class AbstractAuditActionMongoSortOrder<T extends AuditAction<U>, U extends User, S extends AuditActionSortOrder<T,U,S>>
        extends AbstractMongoSortOrder<T,S>
        implements AuditActionSortOrder<T,U,S> {

    protected final AuditActionMongoFields _fields;

    public AbstractAuditActionMongoSortOrder(AuditActionMongoFields fields) {
        _fields = fields;
    }

    @Override
    public S byDate(SortDirection direction) {
        return addByField(_fields.getDateField(), direction);
    }
}
