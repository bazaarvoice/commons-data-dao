package com.bazaarvoice.commons.data.dao.json.schema;

import com.bazaarvoice.commons.data.dao.json.AbstractJSONMarshaller;
import com.bazaarvoice.commons.data.model.json.schema.validation.ResultType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResult;
import org.json.JSONException;
import org.json.JSONObject;

public class ValidationResultMarshaller extends AbstractJSONMarshaller<ValidationResult> {
    @Override
    public JSONObject toJSONObject(ValidationResult result)
            throws JSONException {
        return new JSONObject().
                put(getSeverityField(), convertFromEnum(result.getSeverity())).
                put(getTypeField(), convertFromEnum(result.getType())).
                put(getPathField(), result.getPath()).
                put(getMessageField(), result.getMessage());
    }

    @Override
    public ValidationResult fromJSONObject(JSONObject jsonObject)
            throws JSONException {
        return new ValidationResult().
                type(convertToEnum(ResultType.class, jsonObject.optString(getTypeField(), null))).
                path(jsonObject.optString(getPathField(), null)).
                message(jsonObject.optString(getMessageField(), null));
    }

    private String getSeverityField() {
        return "severity";
    }

    private String getTypeField() {
        return "type";
    }

    private String getPathField() {
        return "path";
    }

    private String getMessageField() {
        return "message";
    }
}
