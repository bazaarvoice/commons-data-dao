package com.bazaarvoice.commons.data.dao.audit;

import com.bazaarvoice.commons.data.dao.CriteriaDAO;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

public interface AuditActionDAO<T extends AuditAction<U>, U extends User, C extends AuditActionCriteria<T,U,C>, S extends AuditActionSortOrder<T,U,S>> extends CriteriaDAO<T,C,S> {
}
