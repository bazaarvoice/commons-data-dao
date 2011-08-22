package com.bazaarvoice.commons.data.dao;

import javax.annotation.Nullable;

public interface ModelDAORWC<T, C extends ModelChanges<T>> extends ModelDAORW<T> {
    /**
     * Create a new Changes descriptor for the given object.
     *
     * @param object The object, or <tt>null</tt> if not available
     *
     * @return The new Changes descriptor
     */
    C newChanges(@Nullable T object);

    /** Updates an object by ID, making specific changes rather than saving the whole object to the database */
    void update(String objectID, C modelChanges);
}
