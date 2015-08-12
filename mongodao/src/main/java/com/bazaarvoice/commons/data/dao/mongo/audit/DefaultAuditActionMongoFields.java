package com.bazaarvoice.commons.data.dao.mongo.audit;

public class DefaultAuditActionMongoFields implements AuditActionMongoFields {
    @Override
    public String getUserField() {
        return "userID";
    }

    @Override
    public String getUsernameField() {
        return "username";
    }

    @Override
    public String getDateField() {
        return "date";
    }

    @Override
    public String getCommentField() {
        return "comment";
    }

    @Override
    public String getTypeField() {
        return "type";
    }

    @Override
    public String getRelatedItemField() {
        return "relatedItemID";
    }
}
