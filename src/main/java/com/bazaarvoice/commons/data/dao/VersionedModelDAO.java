package com.bazaarvoice.commons.data.dao;

import com.bazaarvoice.commons.data.model.BhiveMetaData;
import com.bazaarvoice.commons.data.model.VersionedModel;

import javax.annotation.Nullable;

public interface VersionedModelDAO<T extends VersionedModel> extends ModelDAO<T> {

    /** Loads the object with the specified primary key and version.  Returns null if the object or version does not exist. */
    @Nullable
    T get(String objectID, @Nullable String version);

    BhiveMetaData getMetaData(String objectID, @Nullable Integer skip, @Nullable Integer maxResults);

    void deleteByID(String objectID, @Nullable String version);

}
