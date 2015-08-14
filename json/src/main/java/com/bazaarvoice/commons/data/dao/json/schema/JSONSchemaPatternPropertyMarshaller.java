package com.bazaarvoice.commons.data.dao.json.schema;

import com.bazaarvoice.commons.data.dao.json.AbstractMappingJSONMarshaller;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaPatternProperty;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONSchemaPatternPropertyMarshaller extends AbstractMappingJSONMarshaller<JSONSchemaPatternProperty> {
    private JSONSchemaMarshaller _jsonSchemaMarshaller;

    public JSONSchemaPatternPropertyMarshaller(JSONSchemaMarshaller jsonSchemaMarshaller) {
        _jsonSchemaMarshaller = jsonSchemaMarshaller;
    }

    @Override
    public JSONObject toJSONObject(JSONSchemaPatternProperty patternProperty)
            throws JSONException {
        return _jsonSchemaMarshaller.toJSONObject(patternProperty.getValueSchema(), false);
    }

    @Override
    protected String getKey(JSONSchemaPatternProperty patternProperty) {
        return patternProperty.getPattern();
    }

    @Override
    public JSONSchemaPatternProperty fromJSONObject(JSONObject jsonObject)
            throws JSONException {
        return new JSONSchemaPatternProperty().valueSchema(_jsonSchemaMarshaller.fromJSONObject(jsonObject));
    }

    @Override
    protected JSONSchemaPatternProperty setKey(JSONSchemaPatternProperty patternProperty, String key) {
        return patternProperty.pattern(key);
    }
}
