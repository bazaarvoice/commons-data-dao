package com.bazaarvoice.commons.data.dao.audit;

import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

public interface GenericAuditActionSortOrder<U extends User> extends AuditActionSortOrder<AuditAction<U>, U, GenericAuditActionSortOrder<U>> {
}
