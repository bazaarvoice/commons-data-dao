package com.bazaarvoice.commons.data.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractRoleBasedUser<U extends AbstractRoleBasedUser<U,L>, L extends UserRole> extends AbstractUser<U> implements RoleBasedUser<L> {
    private Set<L> _roles;

    @Override
    public Set<L> getRoles() {
        return _roles;
    }

    public void setRoles(Collection<L> roles) {
        _roles = roles != null ? new LinkedHashSet<L>(roles) : null;
    }

    public U roles(L... roles) {
        setRoles(Arrays.asList(roles));

        //noinspection unchecked
        return (U) this;
    }

    public U roles(Collection<L> roles) {
        setRoles(roles);

        //noinspection unchecked
        return (U) this;
    }

    @Override
    public boolean hasRoles() {
        return _roles != null && !_roles.isEmpty();
    }

    @Override
    public boolean isInRole(L... roles) {
        if (_roles == null) {
            return false;
        }

        for (L role : roles) {
            if (_roles.contains(role)) {
                return true;
            }
        }

        return false;
    }

    public U addRoles(L... roles) {
        addRoles(Arrays.asList(roles));

        //noinspection unchecked
        return (U) this;
    }

    public U addRoles(Collection<L> roles) {
        if (_roles == null) {
            _roles = new LinkedHashSet<L>(roles);
        } else {
            _roles.addAll(roles);
        }

        //noinspection unchecked
        return (U) this;
    }

    public U removeRoles(L... roles) {
        removeRoles(Arrays.asList(roles));

        //noinspection unchecked
        return (U) this;
    }

    public U removeRoles(Collection<L> roles) {
        if (_roles != null) {
            _roles.removeAll(roles);
        }

        //noinspection unchecked
        return (U) this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[roles=" + _roles + "]";
    }
}
