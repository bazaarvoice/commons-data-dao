package com.bazaarvoice.commons.data.model.json.schema;

public class JSONSchemaNamedProperty extends AbstractJSONSchemaProperty<JSONSchemaNamedProperty> {
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public JSONSchemaNamedProperty name(String name) {
        setName(name);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[name='" + _name + "']";
    }
}
