package com.bazaarvoice.commons.data.dao.audit;

import com.bazaarvoice.commons.data.dao.ModelDAORW;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

public interface AuditActionDAORW<T extends AuditAction<U>, U extends User, C extends AuditActionCriteria<T,U,C>, S extends AuditActionSortOrder<T,U,S>> extends AuditActionDAO<T,U,C,S>, ModelDAORW<T> {

    /**
     * Generate a new ID for the action.
     */
    String newID();

}
