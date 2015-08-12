package com.bazaarvoice.commons.data.dao.json.schema;

import com.bazaarvoice.commons.data.dao.json.AbstractMappingJSONMarshaller;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaNamedProperty;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONSchemaNamedPropertyMarshaller extends AbstractMappingJSONMarshaller<JSONSchemaNamedProperty> {
    private JSONSchemaMarshaller _jsonSchemaMarshaller;

    public JSONSchemaNamedPropertyMarshaller(JSONSchemaMarshaller jsonSchemaMarshaller) {
        _jsonSchemaMarshaller = jsonSchemaMarshaller;
    }

    @Override
    public JSONObject toJSONObject(JSONSchemaNamedProperty namedProperty)
            throws JSONException {
        return _jsonSchemaMarshaller.toJSONObject(namedProperty.getValueSchema(), false);
    }

    @Override
    protected String getKey(JSONSchemaNamedProperty namedProperty) {
        return namedProperty.getName();
    }

    @Override
    public JSONSchemaNamedProperty fromJSONObject(JSONObject jsonObject)
            throws JSONException {
        return new JSONSchemaNamedProperty().valueSchema(_jsonSchemaMarshaller.fromJSONObject(jsonObject));
    }

    @Override
    protected JSONSchemaNamedProperty setKey(JSONSchemaNamedProperty namedProperty, String key) {
        return namedProperty.name(key);
    }
}
