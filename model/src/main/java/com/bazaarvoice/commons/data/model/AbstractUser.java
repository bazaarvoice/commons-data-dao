package com.bazaarvoice.commons.data.model;

public abstract class AbstractUser<U extends AbstractUser<U>> extends AbstractModel<U> implements User {
    protected String _username;

    @Override
    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public U username(String username) {
        setUsername(username);
        
        //noinspection unchecked
        return (U) this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[username='" + _username + "']";
    }
}
