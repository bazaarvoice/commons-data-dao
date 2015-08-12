package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.audit.AuditActionCriteria;
import com.bazaarvoice.commons.data.dao.audit.AuditActionDAORW;
import com.bazaarvoice.commons.data.dao.audit.AuditActionSortOrder;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;
import org.bson.types.ObjectId;

public abstract class AbstractAuditActionMongoDAORW<T extends AuditAction<U>, U extends User, C extends AuditActionCriteria<T,U,C>, S extends AuditActionSortOrder<T,U,S>>
        extends AbstractAuditActionMongoDAO<T,U,C,S>
        implements AuditActionDAORW<T,U,C,S> {

    @Override
    public String newID() {
        return ObjectId.get().toString();
    }
}
