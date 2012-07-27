package com.bazaarvoice.commons.data.dao;

import com.bazaarvoice.commons.data.model.VersionedModel;

import javax.annotation.Nullable;

public interface VersionedModelDAO<T extends VersionedModel> extends ModelDAO<T> {

    /** Loads the object with the specified primary key and version.  Returns null if the object or version does not exist. */
    @Nullable
    T get(String objectID, @Nullable String version);

}
