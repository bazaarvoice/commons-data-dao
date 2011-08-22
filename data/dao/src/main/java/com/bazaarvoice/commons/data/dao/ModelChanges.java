package com.bazaarvoice.commons.data.dao;

import javax.annotation.Nullable;

/**
 * Represents a set of changes for a model.  When changes are made, if non-null, the model object will be changed appropriately.
 */
public interface ModelChanges<T> {
    @Nullable
    T getModel();
}
