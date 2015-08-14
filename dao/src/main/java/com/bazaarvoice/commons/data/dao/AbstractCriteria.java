package com.bazaarvoice.commons.data.dao;

import com.google.common.base.Throwables;

public abstract class AbstractCriteria<T, C extends Criteria<T, C>> implements Criteria<T, C> {
    @SuppressWarnings ( {"CloneDoesntDeclareCloneNotSupportedException"})
    @Override
    public C clone() {
        C criteria;

        try {
            //noinspection unchecked
            criteria = (C) super.clone();
        } catch (CloneNotSupportedException e) {
            throw Throwables.propagate(e);
        }

        return criteria;
    }
}
