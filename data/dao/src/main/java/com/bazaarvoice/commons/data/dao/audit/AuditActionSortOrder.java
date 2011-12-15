package com.bazaarvoice.commons.data.dao.audit;

import com.bazaarvoice.commons.data.dao.SortOrder;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.SortDirection;
import com.bazaarvoice.commons.data.model.User;

public interface AuditActionSortOrder<T extends AuditAction<U>, U extends User, S extends AuditActionSortOrder<T,U,S>> extends SortOrder<T,S> {

    S byDate(SortDirection direction);

}
