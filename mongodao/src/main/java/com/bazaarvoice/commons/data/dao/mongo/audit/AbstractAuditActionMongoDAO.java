package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.dao.audit.AuditActionCriteria;
import com.bazaarvoice.commons.data.dao.audit.AuditActionDAO;
import com.bazaarvoice.commons.data.dao.audit.AuditActionSortOrder;
import com.bazaarvoice.commons.data.dao.mongo.AbstractMongoCriteriaDAO;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

public abstract class AbstractAuditActionMongoDAO<T extends AuditAction<U>, U extends User, C extends AuditActionCriteria<T,U,C>, S extends AuditActionSortOrder<T,U,S>>
        extends AbstractMongoCriteriaDAO<T,C,S>
        implements AuditActionDAO<T,U,C,S> {
}
