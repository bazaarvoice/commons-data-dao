package com.bazaarvoice.commons.data.model.json.schema.types;

import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;

import javax.annotation.Nullable;

public final class JSONSchemaAnyType implements JSONSchemaType<JSONSchemaAnyType> {
    public static final JSONSchemaAnyType INSTANCE = new JSONSchemaAnyType();

    private JSONSchemaAnyType() {
    }

    @Override
    public JSONSchemaAnyType clone() {
        return this;
    }

    @Override
    public JSONSchemaAnyType merge(@Nullable JSONSchemaAnyType parentType) {
        return this;
    }

    @Override
    public void validate(JSONSchema schema, Object obj, String path, ValidationResults results) {
        // no validation rules
    }

}
