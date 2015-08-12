package com.bazaarvoice.commons.data.model;

/**
 * Basic model interface for objects that contain version information.
 */
public interface VersionedModel extends Model {

    String getVersion();

    void setVersion(String version);

}
