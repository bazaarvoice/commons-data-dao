package com.bazaarvoice.commons.data.dao.audit;

import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

public interface GenericAuditActionDAO<U extends User> extends AuditActionDAO<AuditAction<U>, U, GenericAuditActionCriteria<U>, GenericAuditActionSortOrder<U>> {
}
