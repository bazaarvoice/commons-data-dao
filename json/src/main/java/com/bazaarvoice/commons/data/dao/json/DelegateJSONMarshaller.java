package com.bazaarvoice.commons.data.dao.json;

import java.util.Set;

public interface DelegateJSONMarshaller<T, E extends Enum & DelegateItemType<?>> extends JSONMarshaller<T> {

    Set<E> getSupportedDelegateItemTypes();

}
