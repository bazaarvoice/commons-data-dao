package com.bazaarvoice.commons.data.model;

import java.util.Set;

public interface RoleBasedUser<L extends UserRole> extends User {

    Set<L> getRoles();

    boolean hasRoles();

    /**
     * Whether or not the user is in at least one of the given roles.
     */
    boolean isInRole(L... roles);

}
