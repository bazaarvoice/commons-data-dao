package com.bazaarvoice.commons.data.model;

public class AbstractVersionedModel<M extends AbstractVersionedModel<M>> extends AbstractModel<M> implements VersionedModel {
    private String _version;

    public String getVersion() {
        return _version;
    }

    public void setVersion(String version) {
        _version = version;
    }

    public M version(String version) {
        setVersion(version);

        //noinspection unchecked
        return (M) this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[version=" + _version + "]";
    }
}
