package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.model.AuditAction;

public interface AuditActionType {

    /**
     * Retrieves the class associated with this enum value.
     */
    Class<? extends AuditAction> getActionClass();

}
