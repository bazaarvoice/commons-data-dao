package test.integration.dao;

public class TestUpdateAuditAction extends AbstractTestAuditAction<TestUpdateAuditAction> {
    private String _fieldUpdated;

    public String getFieldUpdated() {
        return _fieldUpdated;
    }

    public void setFieldUpdated(String fieldUpdated) {
        _fieldUpdated = fieldUpdated;
    }

    public TestUpdateAuditAction fieldUpdated(String fieldUpdated) {
        setFieldUpdated(fieldUpdated);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[fieldUpdated='" + _fieldUpdated + "']";
    }
}
