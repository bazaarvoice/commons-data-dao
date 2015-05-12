package com.bazaarvoice.commons.data.model;

import java.util.Date;

/**
 * Represents an audit action
 */
public abstract class AbstractAuditAction<A extends AbstractAuditAction<A,U>, U extends User> extends AbstractModel<A> implements AuditAction<U> {
    private U _user;
    private Date _date;
    private String _comment;
    private String _relatedItemID;

    @Override
    public U getUser() {
        return _user;
    }

    public void setUser(U user) {
        _user = user;
    }

    public A user(U user) {
        setUser(user);

        //noinspection unchecked
        return (A) this;
    }

    @Override
    public Date getDate() {
        return _date;
    }

    public void setDate(Date date) {
        _date = date;
    }

    public A date(Date date) {
        setDate(date);

        //noinspection unchecked
        return (A) this;
    }

    @Override
    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        _comment = comment;
    }

    public A comment(String comment) {
        setComment(comment);

        //noinspection unchecked
        return (A) this;
    }

    @Override
    public String getRelatedItemID() {
        return _relatedItemID;
    }

    public void setRelatedItemID(String relatedItemID) {
        _relatedItemID = relatedItemID;
    }

    public A relatedItemID(String relatedItemID) {
        setRelatedItemID(relatedItemID);

        //noinspection unchecked
        return (A) this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[user=" + _user + "]" +
                "[date=" + _date + "]" +
                "[comment=" + _comment + "]" +
                "[relatedItemID=" + _relatedItemID + "]";
    }
}
