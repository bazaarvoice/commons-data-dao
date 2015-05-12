package com.bazaarvoice.commons.data.dao.audit;

import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

public interface GenericAuditActionDAORW<U extends User> extends GenericAuditActionDAO<U>, AuditActionDAORW<AuditAction<U>, U, GenericAuditActionCriteria<U>, GenericAuditActionSortOrder<U>> {
}
