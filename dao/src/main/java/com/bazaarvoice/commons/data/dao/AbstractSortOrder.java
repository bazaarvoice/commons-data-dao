package com.bazaarvoice.commons.data.dao;

import com.google.common.base.Throwables;

public abstract class AbstractSortOrder<T, S extends SortOrder<T, S>> implements SortOrder<T, S> {
    @SuppressWarnings ( {"CloneDoesntDeclareCloneNotSupportedException"})
    @Override
    public S clone() {
        S sortOrder;

        try {
            //noinspection unchecked
            sortOrder = (S) super.clone();
        } catch (CloneNotSupportedException e) {
            throw Throwables.propagate(e);
        }

        return sortOrder;
    }
}
