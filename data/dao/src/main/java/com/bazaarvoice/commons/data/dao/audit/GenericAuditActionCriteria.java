package com.bazaarvoice.commons.data.dao.audit;

import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

public interface GenericAuditActionCriteria<U extends User> extends AuditActionCriteria<AuditAction<U>, U, GenericAuditActionCriteria<U>> {
}
