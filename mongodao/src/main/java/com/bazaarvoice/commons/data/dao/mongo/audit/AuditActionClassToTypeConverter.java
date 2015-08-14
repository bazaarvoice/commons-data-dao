package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.model.AuditAction;
import com.google.common.base.Function;

public interface AuditActionClassToTypeConverter<T extends Enum & AuditActionType> extends Function<Class<? extends AuditAction>, T> {

    /**
     * Returns the enumeration class of the types returned by this converter.
     */
    Class<T> getTypeEnumClass();

}
