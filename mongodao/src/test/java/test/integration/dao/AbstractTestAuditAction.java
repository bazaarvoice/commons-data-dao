package test.integration.dao;

import com.bazaarvoice.commons.data.model.AbstractAuditAction;

public class AbstractTestAuditAction<A extends AbstractAuditAction<A, TestUser>> extends AbstractAuditAction<A, TestUser> {
}
