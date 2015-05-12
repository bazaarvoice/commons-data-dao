package com.bazaarvoice.commons.data.model.json.schema.types;

import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;

public class JSONSchemaBooleanType extends AbstractJSONSchemaSimpleType<Boolean, JSONSchemaBooleanType> {

    @Override
    public void validate(JSONSchema schema, Object obj, String path, ValidationResults results) {
        super.validate(schema, obj, path, results);

        Boolean value = (Boolean)obj;

        // nothing to validate since it's guaranteed a boolean value is given
    }
}
