package test.integration.dao.impl;

import com.bazaarvoice.commons.data.dao.mongo.audit.AuditActionType;
import com.bazaarvoice.commons.data.model.AuditAction;
import test.integration.dao.TestAuditAction;
import test.integration.dao.TestCreateAuditAction;
import test.integration.dao.TestUpdateAuditAction;

public enum TestAuditActionType implements AuditActionType {
    GENERIC(TestAuditAction.class),
    UPDATE(TestUpdateAuditAction.class),
    CREATE(TestCreateAuditAction.class),
    ;

    private final Class<? extends AuditAction> _actionClass;

    TestAuditActionType(Class<? extends AuditAction> actionClass) {
        _actionClass = actionClass;
    }

    @Override
    public Class<? extends AuditAction> getActionClass() {
        return _actionClass;
    }
}
