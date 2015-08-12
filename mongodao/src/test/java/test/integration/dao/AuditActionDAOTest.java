package test.integration.dao;

import com.bazaarvoice.commons.data.dao.audit.GenericAuditActionDAORW;
import com.bazaarvoice.commons.data.model.AuditAction;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.integration.dao.impl.TestDummyUserDAORW;

import java.util.Date;

public class AuditActionDAOTest extends AbstractDAOTest {
    @Autowired
    protected GenericAuditActionDAORW<TestUser> _auditActionDAORW;

    @Autowired
    protected TestDummyUserDAORW _userDAORW;

    protected TestUser createRandomTestUser() {
        TestUser user = new TestUser();
        int value = RANDOM.nextInt(1000);
        user.setID("TEST-" + value);
        user.setUsername("testuser" + value);
        _userDAORW.save(user);
        return user;
    }

    @Test
    public void testCreateAction() {
        TestAuditAction action = new TestAuditAction();
        action.setID(_auditActionDAORW.newID());
        action.setDate(new Date());
        action.setUser(createRandomTestUser());
        action.setComment("This is an action comment");
        save(action);

        AuditAction action2 = _auditActionDAORW.get(action.getID());
        Assert.assertNotNull(action2, "Action is null");
        Assert.assertEquals(action2.getDate(), action.getDate());
        Assert.assertEquals(action2.getUser(), action.getUser());
        Assert.assertEquals(action2.getComment(), action.getComment());
    }

    private void save(AuditAction<TestUser> action) {
        _auditActionDAORW.save(action);
    }

    @Test
    public void testFindActionByType() {
        TestUser user = createRandomTestUser();

        TestAuditAction action1 = new TestAuditAction();
        action1.setID(_auditActionDAORW.newID());
        action1.setDate(new Date());
        action1.setUser(user);
        save(action1);

        TestAuditAction action2 = new TestAuditAction();
        action2.setID(_auditActionDAORW.newID());
        action2.setDate(new Date());
        action2.setUser(user);
        save(action2);

        TestUpdateAuditAction action3 = new TestUpdateAuditAction();
        action3.setID(_auditActionDAORW.newID());
        action3.setDate(new Date());
        action3.setUser(user);
        action3.setFieldUpdated("onomatopoeia");
        save(action3);

        Iterable<AuditAction<TestUser>> actionsByType1 = _auditActionDAORW.find(_auditActionDAORW.newCriteria().actionTypeEquals(TestAuditAction.class).userEquals(user.getID()), null);
        Assert.assertEquals(Iterables.size(actionsByType1), 2);
        for (AuditAction action : actionsByType1) {
            Assert.assertEquals(action.getClass(), action1.getClass());
        }

        Iterable<AuditAction<TestUser>> actionsByType2 = _auditActionDAORW.find(_auditActionDAORW.newCriteria().actionTypeEquals(TestUpdateAuditAction.class).userEquals(user.getID()), null);
        Assert.assertEquals(Iterables.size(actionsByType2), 1);
        AuditAction action3a = Iterables.getOnlyElement(actionsByType2);
        Assert.assertEquals(action3a.getClass(), action3.getClass());
        Assert.assertEquals(((TestUpdateAuditAction) action3a).getFieldUpdated(), action3.getFieldUpdated());
    }

}
