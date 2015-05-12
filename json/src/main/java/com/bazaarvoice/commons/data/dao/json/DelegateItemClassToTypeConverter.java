package com.bazaarvoice.commons.data.dao.json;

import com.google.common.base.Function;

public interface DelegateItemClassToTypeConverter<E extends Enum & DelegateItemType<?>> extends Function<Class<?>, E> {

    /**
     * Returns the enumeration class of the types returned by this converter.
     */
    Class<E> getTypeEnumClass();

}
