package com.bazaarvoice.commons.data.model;

import java.util.Collection;

public interface RightBasedRoleBasedUser<L extends RightBasedRole<R>, R extends UserRight> extends RoleBasedUser<L> {

    /**
     * Whether or not the user has the given right.
     */
    boolean hasRight(R right);

    /**
     * Whether or not the user has one or more of the given rights.
     */
    boolean hasRight(R... rights);

    /**
     * Whether or not the user has one or more of the given rights.
     */
    boolean hasRight(Collection<? extends R> rights);

    /**
     * Whether or not the user has all of the given rights.
     */
    boolean hasAllRights(R... rights);

    /**
     * Whether or not the user has all of the given rights.
     */
    boolean hasAllRights(Collection<R> rights);

}
