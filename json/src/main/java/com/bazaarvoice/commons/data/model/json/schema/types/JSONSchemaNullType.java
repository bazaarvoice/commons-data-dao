package com.bazaarvoice.commons.data.model.json.schema.types;

import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;

import javax.annotation.Nullable;

public final class JSONSchemaNullType implements JSONSchemaType<JSONSchemaNullType> {
    public static final JSONSchemaNullType INSTANCE = new JSONSchemaNullType();

    private JSONSchemaNullType() {
    }

    @Override
    public JSONSchemaNullType clone() {
        return this;
    }

    @Override
    public JSONSchemaNullType merge(@Nullable JSONSchemaNullType parentType) {
        return this;
    }

    @Override
    public void validate(JSONSchema schema, Object obj, String path, ValidationResults results) {
        throw new UnsupportedOperationException("Null values shouldn't be validated");
    }

}
