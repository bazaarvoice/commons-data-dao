package com.bazaarvoice.commons.data.dao;

import java.io.Serializable;
import java.util.Collection;

/**
 * Criteria interface for querying for objects.
 */
public interface Criteria<T, C extends Criteria<T, C>> extends Cloneable, Serializable {
    @SuppressWarnings ({"CloneDoesntDeclareCloneNotSupportedException"})
    C clone();

    C addIDIn(Collection<String> objectIDs);

    C addIDNotIn(Collection<String> objectIDs);
}
