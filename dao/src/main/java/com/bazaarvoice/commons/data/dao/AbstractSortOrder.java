package com.bazaarvoice.commons.data.dao;

public abstract class AbstractSortOrder<T, S extends SortOrder<T, S>> implements SortOrder<T, S> {
    @SuppressWarnings ( {"CloneDoesntDeclareCloneNotSupportedException"})
    @Override
    public S clone() {
        S sortOrder;

        try {
            //noinspection unchecked
            sortOrder = (S) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return sortOrder;
    }
}
