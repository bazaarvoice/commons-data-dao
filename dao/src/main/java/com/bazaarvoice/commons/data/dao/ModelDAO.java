package com.bazaarvoice.commons.data.dao;

import javax.annotation.Nullable;
import java.util.List;

public interface ModelDAO<T> {
    /** Loads the object with the specified primary key.  Returns null if the object does not exist. */
    @Nullable
    T get(String objectID);

    /**
     * Loads objects with the specified primary keys in the order of the "id" values. Omits results
     * not found in the store; may return an empty list.
     */
    List<T> get(List<String> objectIDs);
}
