package com.bazaarvoice.commons.data.model.json.schema;

import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaAnyType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaIntegerType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaNullType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaNumberType;
import com.google.common.base.Function;

public final class JSONSchemaTypes {
    public static final JSONSchemaAnyType ANY = JSONSchemaAnyType.INSTANCE;
    public static final JSONSchemaNullType NULL = JSONSchemaNullType.INSTANCE;

    public static JSONSchemaIntegerType newNonNegativeInteger() {
        return new JSONSchemaIntegerType().minimum(0);
    }

    public static JSONSchemaIntegerType newPositiveInteger() {
        return new JSONSchemaIntegerType().minimum(0).minimumExclusive();
    }

    public static JSONSchemaNumberType newNonNegativeNumber() {
        return new JSONSchemaNumberType().minimum(0);
    }

    public static JSONSchemaNumberType newPositiveNumber() {
        return new JSONSchemaNumberType().minimum(0).minimumExclusive();
    }

    private JSONSchemaTypes() {
    }

    public static class CloningFunction implements Function<JSONSchemaType<?>, JSONSchemaType<?>> {
        public static final CloningFunction INSTANCE = new CloningFunction();

        @Override
        public JSONSchemaType<?> apply(JSONSchemaType<?> schemaType) {
            return schemaType.clone();
        }
    }
}
