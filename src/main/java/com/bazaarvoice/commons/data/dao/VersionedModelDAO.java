package com.bazaarvoice.commons.data.dao;

import com.bazaarvoice.commons.data.model.VersionedModel;

import javax.annotation.Nullable;

public interface VersionedModelDAO<T extends VersionedModel> extends ModelDAO<T> {

    /** Loads the object with the specified primary key and version.  Returns null if the object or version does not exist. */
    @Nullable
    T get(String objectID, @Nullable String version);

    /**
     * Delete versioned document.
     * @param version   if no version passed then document and all it's versions are deleted,
     *                  otherwise single version is deleted.
     */
    void deleteByID(String objectID, @Nullable String version);

}
