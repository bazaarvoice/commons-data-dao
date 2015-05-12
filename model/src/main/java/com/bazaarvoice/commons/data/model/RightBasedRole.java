package com.bazaarvoice.commons.data.model;

import java.util.Set;

public interface RightBasedRole<R extends UserRight> extends UserRole {

    /**
     * Get all of the rights this role has.
     */
    Set<R> getRights();

    /**
     * Whether or not this role has at least one of the given rights.
     */
    @Deprecated
    boolean hasRight(R... rights);

}
