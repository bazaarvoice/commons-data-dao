package com.bazaarvoice.commons.data.dao.audit;

import com.bazaarvoice.commons.data.dao.Criteria;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.bazaarvoice.commons.data.model.User;

import java.util.Collection;
import java.util.Date;

public interface AuditActionCriteria<T extends AuditAction<U>, U extends User, C extends AuditActionCriteria<T,U,C>> extends Criteria<T,C> {

    C userEquals(String userID);

    C userIn(Collection<String> userIDs);

    C dateBefore(Date date);

    C dateAfter(Date date);

    /**
     * Add date between the start (inclusive) and end (exclusive) date
     */
    C dateBetween(Date start, Date end);

    C relatedItemIDEquals(String relatedItemID);

    C relatedItemIDIn(Collection<String> relatedItemID);

    C actionTypeEquals(Class<? extends T> actionClass);

    C actionTypeIn(Collection<Class<? extends T>> actionClasses);

}
