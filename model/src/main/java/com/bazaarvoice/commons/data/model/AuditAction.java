package com.bazaarvoice.commons.data.model;

import java.util.Date;

public interface AuditAction<U extends User> extends Model {

    U getUser();

    Date getDate();

    String getComment();

    String getRelatedItemID();
    
}
