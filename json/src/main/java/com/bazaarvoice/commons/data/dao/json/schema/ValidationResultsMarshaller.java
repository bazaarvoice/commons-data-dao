package com.bazaarvoice.commons.data.dao.json.schema;

import com.bazaarvoice.commons.data.dao.json.AbstractJSONMarshaller;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;
import org.json.JSONException;
import org.json.JSONObject;

public class ValidationResultsMarshaller extends AbstractJSONMarshaller<ValidationResults> {
    private ValidationResultMarshaller _validationResultMarshaller = new ValidationResultMarshaller();

    @Override
    public JSONObject toJSONObject(ValidationResults results)
            throws JSONException {
        return new JSONObject().
                put(getResultsField(), _validationResultMarshaller.toJSONArray(results.getResults()));
    }

    @Override
    public ValidationResults fromJSONObject(JSONObject jsonObject)
            throws JSONException {
        return new ValidationResults().
                addResults(_validationResultMarshaller.fromJSONArray(jsonObject.optJSONArray(getResultsField())));
    }

    private String getResultsField() {
        return "results";
    }
}
