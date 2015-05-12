package com.bazaarvoice.commons.data.model;

import java.io.Serializable;

public abstract class AbstractModel<M extends AbstractModel<M>> implements Model, Serializable {
    private String _id;

    @Override
    public String getID() {
        return _id;
    }

    public void setID(String id) {
        _id = id;
    }

    public M id(String id) {
        setID(id);

        //noinspection unchecked
        return (M) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractModel that = (AbstractModel) o;

        if (_id != null ? !_id.equals(that._id) : that._id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return super.toString() + "[_id='" + _id + "']";
    }
}
