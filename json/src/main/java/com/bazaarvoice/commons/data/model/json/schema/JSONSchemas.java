package com.bazaarvoice.commons.data.model.json.schema;

import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaArrayType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaBooleanType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaIntegerType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaNumberType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaObjectType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaStringType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaTextFormat;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaUnionType;
import com.google.common.base.Function;

public final class JSONSchemas {

    public static JSONSchema extendSchema(JSONSchema schema) {
        return new JSONSchema().extendsSchema(schema);
    }

    public static JSONSchema referenceSchema(JSONSchema referencedSchema) {
        return new JSONSchema().referencesSchema(referencedSchema);
    }

    public static JSONSchema newSimpleSchema(JSONSchemaType<?> type) {
        return new JSONSchema().addType(type);
    }

    public static JSONSchema newObjectSchema(JSONSchemaProperty... properties) {
        return newSimpleSchema(new JSONSchemaObjectType().allProperties(properties));
    }

    public static JSONSchema newStrictObjectSchema(JSONSchemaProperty... properties) {
        return newSimpleSchema(new JSONSchemaObjectType().allProperties(properties).disallowAdditionalProperties());
    }

    public static JSONSchema newUniformObjectSchema(JSONSchema additionalProperties) {
        return newSimpleSchema(new JSONSchemaObjectType().additionalPropertiesSchema(additionalProperties));
    }

    public static JSONSchema newArraySchema(JSONSchema... itemSchemas) {
        return newSimpleSchema(new JSONSchemaArrayType().items(itemSchemas));
    }

    public static JSONSchema newSimpleArraySchema(JSONSchemaType<?> itemType) {
        return newArraySchema(newSimpleSchema(itemType));
    }

    public static JSONSchema newObjectArraySchema(JSONSchemaProperty... properties) {
        return newArraySchema(newObjectSchema(properties));
    }

    public static JSONSchema newStrictObjectArraySchema(JSONSchemaProperty... properties) {
        return newArraySchema(newStrictObjectSchema(properties));
    }

    public static JSONSchema newStringSchema() {
        return newSimpleSchema(new JSONSchemaStringType());
    }

    public static JSONSchema newStringFormatSchema(JSONSchemaTextFormat format) {
        return newSimpleSchema(new JSONSchemaStringType().format(format));
    }

    public static JSONSchema newNumberSchema() {
        return newSimpleSchema(new JSONSchemaNumberType());
    }

    public static JSONSchema newIntegerSchema() {
        return newSimpleSchema(new JSONSchemaIntegerType());
    }

    public static JSONSchema newBooleanSchema() {
        return newSimpleSchema(new JSONSchemaBooleanType());
    }

    public static JSONSchema newAnySchema() {
        return newSimpleSchema(JSONSchemaTypes.ANY);
    }

    public static JSONSchema newNullSchema() {
        return newSimpleSchema(JSONSchemaTypes.NULL);
    }

    public static JSONSchema newUnionSchema(JSONSchema... schemas) {
        return newSimpleSchema(new JSONSchemaUnionType().schemas(schemas));
    }

    private JSONSchemas() {
    }

    public static class CloningFunction implements Function<JSONSchema, JSONSchema> {
        public static final CloningFunction INSTANCE = new CloningFunction();

        @Override
        public JSONSchema apply(JSONSchema input) {
            return input.clone();
        }
    }
}
