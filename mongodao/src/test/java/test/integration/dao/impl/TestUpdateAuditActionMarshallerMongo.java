package test.integration.dao.impl;

import com.bazaarvoice.commons.data.dao.mongo.audit.AbstractAuditActionMongoMarshaller;
import com.bazaarvoice.commons.data.dao.mongo.dbo.InsertMongoDBObject;
import com.bazaarvoice.commons.data.dao.mongo.dbo.MongoDBObject;
import test.integration.dao.TestUpdateAuditAction;
import test.integration.dao.TestUser;

import java.util.EnumSet;
import java.util.Set;

public class TestUpdateAuditActionMarshallerMongo extends AbstractAuditActionMongoMarshaller<TestAuditActionType, TestUpdateAuditAction, TestUser> {

    @Override
    public InsertMongoDBObject toDBObject(TestUpdateAuditAction action) {
        return super.toDBObject(action).append(getFieldUpdatedField(), action.getFieldUpdated());
    }

    @Override
    public TestUpdateAuditAction fromDBObject(MongoDBObject<?> dbObject) {
        TestUpdateAuditAction auditAction = super.fromDBObject(dbObject);
        auditAction.setFieldUpdated(dbObject.getString(getFieldUpdatedField()));
        return auditAction;
    }

    @Override
    public Set<TestAuditActionType> getSupportedActionTypes() {
        return EnumSet.of(TestAuditActionType.UPDATE);
    }

    private String getFieldUpdatedField() {
        return "fieldUpdated";
    }
}
