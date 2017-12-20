package com.bazaarvoice.commons.data.dao;

public abstract class AbstractCriteria<T, C extends Criteria<T, C>> implements Criteria<T, C> {
    @SuppressWarnings ( {"CloneDoesntDeclareCloneNotSupportedException"})
    @Override
    public C clone() {
        C criteria;

        try {
            //noinspection unchecked
            criteria = (C) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return criteria;
    }
}
