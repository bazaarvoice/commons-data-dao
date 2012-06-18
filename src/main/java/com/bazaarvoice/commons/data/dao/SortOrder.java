package com.bazaarvoice.commons.data.dao;

import com.bazaarvoice.commons.data.model.SortDirection;

public interface SortOrder<T, S extends SortOrder<T, S>> {
    @SuppressWarnings ({"CloneDoesntDeclareCloneNotSupportedException"})
    S clone();

    S addByID(SortDirection direction);
}
