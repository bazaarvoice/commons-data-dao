package com.bazaarvoice.commons.data.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class AbstractRoleAndRightBasedUser<U extends AbstractRoleAndRightBasedUser<U,L,R>, L extends RightBasedRole<R>, R extends UserRight> extends AbstractRoleBasedUser<U,L>
        implements RightBasedRoleBasedUser<L,R> {

    @Override
    public boolean hasRight(R right) {
        return hasRight(Collections.singletonList(right));
    }

    @Override
    public boolean hasRight(R... rights) {
        return hasRight(Arrays.asList(rights));
    }

    @Override
    public boolean hasRight(Collection<? extends R> rights) {
        Set<L> roles = getRoles();
        if (roles == null) {
            return false;
        }

        for (L role : roles) {
            Set<R> roleRights = role.getRights();
            for (R right : rights) {
                if (roleRights.contains(right)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasAllRights(R... rights) {
        return hasAllRights(Arrays.asList(rights));
    }

    @Override
    public boolean hasAllRights(Collection<R> rights) {
        for(R r : rights){
            if(!hasRight(r)){
                return false;
            }
        }
        return true;
    }

}
