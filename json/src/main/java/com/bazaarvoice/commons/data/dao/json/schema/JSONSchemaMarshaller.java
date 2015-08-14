package com.bazaarvoice.commons.data.dao.json.schema;

import com.bazaarvoice.commons.data.dao.json.AbstractJSONMarshaller;
import com.bazaarvoice.commons.data.model.json.GenericJSONArrayList;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaType;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaTypes;
import com.bazaarvoice.commons.data.model.json.schema.types.AbstractJSONSchemaNumberType;
import com.bazaarvoice.commons.data.model.json.schema.types.AbstractJSONSchemaSimpleType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaAnyType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaArrayType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaBooleanType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaIntegerType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaNullType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaNumberType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaObjectType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaStringType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaTextFormat;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaUnionType;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONSchemaMarshaller extends AbstractJSONMarshaller<JSONSchema> {
    protected static final String DEFAULT_SCHEMA_VERSION = "http://json-schema.org/draft-03/schema";

    protected static final Pattern HTTP_CONTENT_TYPE_CHARSET = Pattern.compile("\\bcharset=([-a-zA-Z0-9]+)");

    protected final JSONSchemaNamedPropertyMarshaller _namedPropertyMarshaller = new JSONSchemaNamedPropertyMarshaller(this);
    protected final JSONSchemaPatternPropertyMarshaller _patternPropertyMarshaller = new JSONSchemaPatternPropertyMarshaller(this);

    protected final BiMap<JSONSchemaTextFormat, String> _schemaTextFormatJSONValues = buildSchemaTextFormatJSONValues();
    protected final BiMap<Class<? extends JSONSchemaType<?>>, String> _schemaTypeJSONTypeValues = buildSchemaTypeJSONTypeValues();

    private final LoadingCache<String, JSONSchema> _schemaCache = CacheBuilder.newBuilder().maximumSize(1000).concurrencyLevel(16).build(new JSONSchemaURLCacheLoader());

    protected final SchemaJSONRetriever _schemaJSONRetriever;

    public JSONSchemaMarshaller() {
        this(new DefaultSchemaJSONRetriever());
    }

    public JSONSchemaMarshaller(SchemaJSONRetriever schemaJSONRetriever) {
        _schemaJSONRetriever = schemaJSONRetriever;
    }

    @Override
    public JSONObject toJSONObject(JSONSchema schema)
            throws JSONException {
        return toJSONObject(schema, true);
    }

    public JSONObject toJSONObject(JSONSchema schema, boolean includeSchemaVersion)
            throws JSONException {
        JSONObject jsonObject = convertCommonSchemaFieldsToJSONObject(schema, new JSONObject(), includeSchemaVersion);

        convertTypesToJSONObject(schema, jsonObject);

        return jsonObject;
    }

    protected JSONObject convertCommonSchemaFieldsToJSONObject(JSONSchema schema, JSONObject jsonObject, boolean includeSchemaVersion) {
        return jsonObject.putOpt(JSONSchema.SCHEMA_KEY, includeSchemaVersion ? getSchemaVersion() : null).
                putOpt("$ref", schema.getReferencesSchemaID()).
                putOpt("id", schema.getReferencesSchemaID() == null ? schema.getID() : null).
                putOpt("extends", schema.getExtendsSchemaID()).
                putOpt("type", schemaTypesToJSON(schema)).
                putOpt("required", schema.getRequired()).
                putOpt("title", schema.getTitle()).
                putOpt("description", schema.getDescription());
    }

    protected String getSchemaVersion() {
        return DEFAULT_SCHEMA_VERSION;
    }

    protected JSONObject convertTypesToJSONObject(JSONSchema schema, JSONObject jsonObject) {
        Set<Object> enums = Sets.newSetFromMap(new LinkedHashMap<Object,Boolean>());
        for (final JSONSchemaType<?> type : schema.getTypes()) {
            convertTypeFieldsToJSONObject(type, jsonObject, enums);
        }

        if (!enums.isEmpty()) {
            jsonObject.put("enum", enums);
        }

        return jsonObject;
    }

    protected JSONObject convertTypeFieldsToJSONObject(JSONSchemaType<?> type, JSONObject jsonObject, Collection<Object> enums) {
        if (type instanceof AbstractJSONSchemaSimpleType<?,?>) {
            convertCommonSimpleTypeFieldsToJSONObject((AbstractJSONSchemaSimpleType<?, ?>) type, jsonObject, enums);
        }

        if (type instanceof AbstractJSONSchemaNumberType<?,?>) {
            convertNumberTypeFieldsToJSONObject((AbstractJSONSchemaNumberType<?, ?>) type, jsonObject);
        }

        if (type instanceof JSONSchemaStringType) {
            convertStringTypeFieldsToJSONObject((JSONSchemaStringType) type, jsonObject);
        } else if (type instanceof JSONSchemaObjectType) {
            convertObjectTypeFieldsToJSONObject((JSONSchemaObjectType) type, jsonObject);
        } else if (type instanceof JSONSchemaArrayType) {
            convertArrayTypeFieldsToJSONObject((JSONSchemaArrayType) type, jsonObject);
        }

        return jsonObject;
    }

    protected <V> JSONObject convertCommonSimpleTypeFieldsToJSONObject(AbstractJSONSchemaSimpleType<V, ?> simpleType, JSONObject jsonObject, Collection<Object> enums) {
        jsonObject.putOpt("default", simpleType.getDefaultValue());

        if (simpleType.getEnumValues() != null) {
            enums.addAll(simpleType.getEnumValues());
        }

        return jsonObject;
    }

    protected <V extends Number> JSONObject convertNumberTypeFieldsToJSONObject(AbstractJSONSchemaNumberType<V, ?> numberType, JSONObject jsonObject) {
        return jsonObject.putOpt("minimum", numberType.getMinimum()).
                putOpt("exclusiveMinimum", numberType.getMinimumExclusive()).
                putOpt("maximum", numberType.getMaximum()).
                putOpt("exclusiveMaximum", numberType.getMaximumExclusive()).
                putOpt("divisibleBy", numberType.getDivisibleBy());
    }

    protected JSONObject convertStringTypeFieldsToJSONObject(JSONSchemaStringType stringType, JSONObject jsonObject) {
        return jsonObject.putOpt("minLength", stringType.getMinimumLength()).
                putOpt("maxLength", stringType.getMaximumLength()).
                putOpt("pattern", stringType.getPattern()).
                putOpt("format", convertSchemaTextFormatToJSONValue(stringType.getFormat(), stringType.getCustomFormat()));
    }

    protected void convertObjectTypeFieldsToJSONObject(JSONSchemaObjectType objectType, JSONObject jsonObject) {
        jsonObject.put("properties", _namedPropertyMarshaller.toMappedJSONObject(objectType.getNamedProperties()));

        if (!objectType.getPatternProperties().isEmpty()) {
            jsonObject.put("patternProperties", _patternPropertyMarshaller.toMappedJSONObject(objectType.getPatternProperties()));
        }

        JSONSchema additionalPropertiesSchema = objectType.getAdditionalPropertiesSchema();
        if (additionalPropertiesSchema == null) {
            jsonObject.put("additionalProperties", false);
        } else if (!additionalPropertiesSchema.isEmpty()) {
            jsonObject.put("additionalProperties", toJSONObject(additionalPropertiesSchema, false));
        }
    }

    protected JSONObject convertArrayTypeFieldsToJSONObject(JSONSchemaArrayType arrayType, JSONObject jsonObject) {
        if (arrayType.isTuple()) {
            jsonObject.put("items", toJSONArray(arrayType.getItems(), false));

            JSONSchema additionalItemsSchema = arrayType.getAdditionalItemsSchema();
            if (additionalItemsSchema == null) {
                jsonObject.put("additionalItems", false);
            } else if (!additionalItemsSchema.isEmpty()) {
                jsonObject.put("additionalItems", toJSONObject(additionalItemsSchema, false));
            }
        } else {
            jsonObject.put("items", toJSONObject(arrayType.getItem(), false));
        }

        return jsonObject.putOpt("minItems", arrayType.getMinimumItems()).
                putOpt("maxItems", arrayType.getMaximumItems()).
                putOpt("uniqueItems", arrayType.getRequireUniqueItems());
    }

    @Override
    public JSONArray toJSONArray(Collection<? extends JSONSchema> objects) {
        return toJSONArray(objects, true);
    }

    protected JSONArray toJSONArray(Collection<? extends JSONSchema> objects, final boolean includeSchemaVersion) {
        Collection<JSONObject> jsonObjects = toJSONList(objects, includeSchemaVersion);
        return new JSONArray(jsonObjects.toArray());
    }

    private Collection<JSONObject> toJSONList(Collection<? extends JSONSchema> objects, final boolean includeSchemaVersion) {
        return Collections2.transform(objects, new Function<JSONSchema, JSONObject>() {
            @Override
            public JSONObject apply(JSONSchema schema) {
                try {
                    return toJSONObject(schema, includeSchemaVersion);
                } catch (JSONException e) {
                    throw Throwables.propagate(e);
                }
            }
        });
    }

    @Override
    public JSONSchema fromJSONObject(JSONObject jsonObject)
            throws JSONException {
        if (!isSupportedSchema(jsonObject.optString(JSONSchema.SCHEMA_KEY, getSchemaVersion()))) {
            throw new IllegalArgumentException("Unrecognized schema version: " + jsonObject.optString(JSONSchema.SCHEMA_KEY));
        }

        JSONSchema jsonSchema = new JSONSchema();
        String id = jsonObject.optString("id", null);
        if (!Strings.isNullOrEmpty(id)) {
            jsonSchema.setID(id);
            _schemaCache.put(id, jsonSchema);
        }

        String referencesSchemaID = jsonObject.optString("$ref", null);
        if (!Strings.isNullOrEmpty(referencesSchemaID)) {
            jsonSchema.setReferencesSchema(fromSchemaID(referencesSchemaID));
        }

        String extendsSchemaID = jsonObject.optString("extends", null);
        if (!Strings.isNullOrEmpty(extendsSchemaID)) {
            jsonSchema.setExtendsSchema(fromSchemaID(extendsSchemaID));
        }

        jsonSchema.setRequired((Boolean) jsonObject.opt("required"));
        jsonSchema.setTitle(jsonObject.optString("title", null));
        jsonSchema.setDescription(jsonObject.optString("description", null));

        convertTypesFromJSONObject(jsonObject, jsonSchema);

        return jsonSchema;
    }

    protected boolean isSupportedSchema(String schemaID) {
        return getSchemaVersion().equals(schemaID);
    }

    protected JSONSchema convertTypesFromJSONObject(JSONObject jsonObject, JSONSchema jsonSchema) {
        Object typeValue = jsonObject.opt("type");
        if (typeValue instanceof JSONArray) {
            jsonSchema.setTypes(convertSchemaTypesFromJSONValues(jsonObject, new GenericJSONArrayList((JSONArray) typeValue)));
        } else if (typeValue instanceof Collection) {
            //noinspection unchecked
            jsonSchema.setTypes(convertSchemaTypesFromJSONValues(jsonObject, (Collection) typeValue));
        } else if (typeValue != null) {
            jsonSchema.setTypes(convertSchemaTypesFromJSONValues(jsonObject, Collections.singleton(typeValue)));
        }

        for (final JSONSchemaType<?> type : jsonSchema.getTypes()) {
            convertTypeFieldsFromJSONObject(jsonObject, type);
        }

        return jsonSchema;
    }

    protected JSONSchemaType<?> convertTypeFieldsFromJSONObject(JSONObject jsonObject, JSONSchemaType<?> type) {
        if (type instanceof AbstractJSONSchemaSimpleType) {
            convertSimpleTypeFieldsFromJSONObject(jsonObject, (AbstractJSONSchemaSimpleType<?, ?>) type);
        } else if (type instanceof JSONSchemaObjectType) {
            convertObjectTypeFieldsFromJSONObject(jsonObject, (JSONSchemaObjectType) type);
        } else if (type instanceof JSONSchemaArrayType) {
            convertArrayTypeFieldsFromJSONObject(jsonObject, (JSONSchemaArrayType) type);
        }

        return type;
    }

    protected <V> AbstractJSONSchemaSimpleType<V,?> convertSimpleTypeFieldsFromJSONObject(JSONObject jsonObject, AbstractJSONSchemaSimpleType<V,?> simpleType) {
        @SuppressWarnings ("unchecked")
        Object defaultValue = jsonObject.opt("default");

        List<Object> enums = new GenericJSONArrayList(jsonObject.optJSONArray("enum"));

        if (simpleType instanceof JSONSchemaBooleanType) {
            convertBooleanTypeFieldsFromJSONObject(defaultValue, (JSONSchemaBooleanType) simpleType);
        } else if (simpleType instanceof JSONSchemaIntegerType) {
            convertIntegerTypeFieldsFromJSONObject(jsonObject, (JSONSchemaIntegerType) simpleType, defaultValue, enums);
        } else if (simpleType instanceof JSONSchemaNumberType) {
            convertNumberTypeFieldsFromJSONObject(jsonObject, (JSONSchemaNumberType) simpleType, defaultValue, enums);
        } else if (simpleType instanceof JSONSchemaStringType) {
            convertStringTypeFieldsFromJSONObject(jsonObject, (JSONSchemaStringType) simpleType, defaultValue, enums);
        }

        return simpleType;
    }

    protected JSONSchemaBooleanType convertBooleanTypeFieldsFromJSONObject(Object defaultValue, JSONSchemaBooleanType booleanType) {
        return setTypeDefaultValue(Boolean.class, defaultValue, booleanType);
    }

    protected JSONSchemaIntegerType convertIntegerTypeFieldsFromJSONObject(JSONObject jsonObject, JSONSchemaIntegerType intType, Object defaultValue, List<Object> enums) {
        Number minimum = (Number) jsonObject.opt("minimum");
        Boolean minimumExclusive = (Boolean) jsonObject.opt("exclusiveMinimum");
        Number maximum = (Number) jsonObject.opt("maximum");
        Boolean maximumExclusive = (Boolean) jsonObject.opt("exclusiveMaximum");
        Number divisibleBy = (Number) jsonObject.opt("divisibleBy");

        if (defaultValue instanceof Number) {
            intType.setDefaultValue(((Number) defaultValue).intValue());
        }

        if (minimum != null) {
            intType.setMinimum(minimum.intValue());
        }

        if (minimumExclusive != null) {
            intType.setMinimumExclusive(minimumExclusive);
        }

        if (maximum != null) {
            intType.setMaximum(maximum.intValue());
        }

        if (maximumExclusive != null) {
            intType.setMaximumExclusive(maximumExclusive);
        }

        if (divisibleBy != null) {
            intType.setDivisibleBy(divisibleBy.intValue());
        }

        intType.setEnumValues(Collections2.filter(Lists.transform(enums, new Function<Object, Integer>() {
            @Override
            public Integer apply(@Nullable Object input) {
                return input instanceof Number ? ((Number) input).intValue() : null;
            }
        }), Predicates.notNull()));

        return intType;
    }

    protected JSONSchemaNumberType convertNumberTypeFieldsFromJSONObject(JSONObject jsonObject, JSONSchemaNumberType numberType, Object defaultValue, List<Object> enums) {
        Number minimum = (Number) jsonObject.opt("minimum");
        Boolean minimumExclusive = (Boolean) jsonObject.opt("exclusiveMinimum");
        Number maximum = (Number) jsonObject.opt("maximum");
        Boolean maximumExclusive = (Boolean) jsonObject.opt("exclusiveMaximum");
        Number divisibleBy = (Number) jsonObject.opt("divisibleBy");

        if (defaultValue instanceof Number) {
            setTypeDefaultValue(Number.class, defaultValue, numberType);
        }

        if (minimum != null) {
            numberType.setMinimum(minimum);
        }

        if (minimumExclusive != null) {
            numberType.setMinimumExclusive(minimumExclusive);
        }

        if (maximum != null) {
            numberType.setMaximum(maximum);
        }

        if (maximumExclusive != null) {
            numberType.setMaximumExclusive(maximumExclusive);
        }

        if (divisibleBy != null) {
            numberType.setDivisibleBy(divisibleBy);
        }

        numberType.setEnumValues(Collections2.filter(Lists.transform(enums, new Function<Object, Number>() {
            @Override
            public Number apply(@Nullable Object input) {
                return input instanceof Number ? (Number) input : null;
            }
        }), Predicates.notNull()));

        return numberType;
    }

    protected JSONSchemaStringType convertStringTypeFieldsFromJSONObject(JSONObject jsonObject, JSONSchemaStringType stringType, Object defaultValue, List<Object> enums) {
        setTypeDefaultValue(String.class, defaultValue, stringType);

        Number minimumLength = (Number) jsonObject.opt("minLength");
        if (minimumLength != null) {
            stringType.setMinimumLength(minimumLength.intValue());
        }

        Number maximumLength = (Number) jsonObject.opt("maxLength");
        if (maximumLength != null) {
            stringType.setMaximumLength(maximumLength.intValue());
        }

        stringType.setPattern(jsonObject.optString("pattern", null));

        String format = jsonObject.optString("format");
        stringType.setFormat(convertSchemaTextFormatFromJSONValue(format));
        if (stringType.getFormat() == JSONSchemaTextFormat.CUSTOM) {
            stringType.setCustomFormat(format);
        }

        stringType.setEnumValues(Collections2.filter(Lists.transform(enums, new Function<Object, String>() {
            @Override
            public String apply(@Nullable Object input) {
                return input instanceof String ? (String) input : null;
            }
        }), Predicates.notNull()));

        return stringType;
    }

    protected <V, S extends AbstractJSONSchemaSimpleType<V, S>> S setTypeDefaultValue(Class<? extends V> defaultValueClass, Object defaultValue, S type) {
        if (defaultValueClass.isInstance(defaultValue)) {
            type.setDefaultValue(defaultValueClass.cast(defaultValue));
        }

        return type;
    }

    protected JSONSchemaObjectType convertObjectTypeFieldsFromJSONObject(JSONObject jsonObject, JSONSchemaObjectType objectType) {
        JSONObject propertiesJSONObject = jsonObject.optJSONObject("properties");
        if (propertiesJSONObject != null) {
            objectType.setNamedProperties(_namedPropertyMarshaller.fromMappedJSONObject(propertiesJSONObject, null));
        }

        JSONObject patternPropertiesJSONObject = jsonObject.optJSONObject("patternProperties");
        if (patternPropertiesJSONObject != null) {
            objectType.setPatternProperties(_patternPropertyMarshaller.fromMappedJSONObject(patternPropertiesJSONObject, null));
        }

        Object additionalProperties = jsonObject.opt("additionalProperties");
        if (additionalProperties instanceof JSONObject) {
            objectType.setAdditionalPropertiesSchema(fromJSONObject((JSONObject) additionalProperties));
        } else if (additionalProperties instanceof Boolean && !((Boolean) additionalProperties)) {
            objectType.setAdditionalPropertiesSchema(null);
        } else if (additionalProperties != null && additionalProperties != JSONObject.NULL) {
            throw new IllegalArgumentException("Unsupported additional properties: " + additionalProperties);
        }

        return objectType;
    }

    protected JSONSchemaArrayType convertArrayTypeFieldsFromJSONObject(JSONObject jsonObject, JSONSchemaArrayType arrayType) {
        Object items = jsonObject.opt("items");

        if (items instanceof JSONObject) {
            arrayType.setItem(fromJSONObject((JSONObject) items));
        } else if (items instanceof JSONArray || items instanceof Collection) {
            arrayType.setItems(fromJSONArray((JSONArray) items));

            Object additionalItems = jsonObject.opt("additionalItems");
            if (additionalItems instanceof JSONObject) {
                arrayType.additionalItemsSchema(fromJSONObject((JSONObject) additionalItems));
            } else if (additionalItems instanceof Boolean && !((Boolean) additionalItems)) {
                arrayType.additionalItemsSchema(null);
            } else if (additionalItems != null && additionalItems != JSONObject.NULL) {
                throw new IllegalArgumentException("Unsupported additional items: " + additionalItems);
            } else {
                arrayType.additionalItemsSchema(new JSONSchema());
            }
        }

        if (!jsonObject.isNull("minItems")) {
            arrayType.setMinimumItems(jsonObject.getInt("minItems"));
        }

        if (!jsonObject.isNull("maxItems")) {
            arrayType.setMaximumItems(jsonObject.getInt("maxItems"));
        }

        if (!jsonObject.isNull("uniqueItems")) {
            arrayType.setRequireUniqueItems(jsonObject.getBoolean("uniqueItems"));
        }

        return arrayType;
    }

    protected JSONSchema fromSchemaID(String schemaID) {
        try {
            return _schemaCache.get(schemaID).clone();
        } catch (ExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    protected String convertSchemaTextFormatToJSONValue(@Nullable JSONSchemaTextFormat format, @Nullable String customFormat) {
        if (format == null) {
            return null;
        }

        if (format == JSONSchemaTextFormat.CUSTOM) {
            return customFormat;
        }

        String jsonValue = _schemaTextFormatJSONValues.get(format);
        if (jsonValue == null) {
            throw new IllegalArgumentException("Unknown string format: " + format);
        }
        return jsonValue;
    }

    protected JSONSchemaTextFormat convertSchemaTextFormatFromJSONValue(@Nullable String formatJSONValue) {
        if (Strings.isNullOrEmpty(formatJSONValue)) {
            return null;
        }

        JSONSchemaTextFormat format = _schemaTextFormatJSONValues.inverse().get(formatJSONValue);
        if (format == null) {
            return JSONSchemaTextFormat.CUSTOM;
        }

        return format;
    }

    protected BiMap<JSONSchemaTextFormat, String> buildSchemaTextFormatJSONValues() {
        BiMap<JSONSchemaTextFormat, String> jsonSchemaTextFormatJSONValues = HashBiMap.create();

        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.DATE_TIME, "date-time");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.DATE, "date");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.TIME, "time");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.UTC_MILLISECONDS, "utc-millisec");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.REGEX, "regex");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.CSS_COLOR, "color");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.CSS_STYLE, "style");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.PHONE_NUMBER, "phone");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.URI, "uri");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.EMAIL_ADDRESS, "email");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.IPV4_ADDRESS, "ip-address");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.IPV6_ADDRESS, "ipv6");
        jsonSchemaTextFormatJSONValues.put(JSONSchemaTextFormat.HOSTNAME, "host-name");

        return jsonSchemaTextFormatJSONValues;
    }

    @Nullable
    protected Object schemaTypesToJSON(JSONSchema schema) {
        return schema.isSingleType() ? (schema.getType() != null ? convertSchemaTypeToJSONTypeValue(schema.getType()) : null) : convertSchemaTypesToJSONValues(schema.getTypes());
    }

    protected Collection<Object> convertSchemaTypesToJSONValues(Collection<JSONSchemaType<?>> types) {
        List<Object> typeValues = Lists.newArrayList();
        for (final JSONSchemaType<?> type : types) {
            if (type instanceof JSONSchemaUnionType) {
                JSONSchemaUnionType unionType = (JSONSchemaUnionType) type;
                typeValues.addAll(toJSONList(unionType.getSchemas(), false));
            } else {
                typeValues.add(convertSchemaTypeToJSONTypeValue(type));
            }
        }
        return typeValues;
    }

    protected Collection<JSONSchemaType<?>> convertSchemaTypesFromJSONValues(JSONObject jsonObject, Collection<Object> jsonValues) {
        List<JSONSchemaType<?>> types = Lists.newArrayList();
        JSONSchemaUnionType unionType = null;
        for (Object jsonValue : jsonValues) {
            if (jsonValue instanceof JSONObject) {
                if (unionType == null) {
                    unionType = new JSONSchemaUnionType();
                    types.add(unionType);
                }

                unionType.addSchema(fromJSONObject((JSONObject) jsonValue));
            } else {
                types.add(convertSchemaTypeFromJSONTypeValue(jsonObject, jsonValue.toString()));
            }
        }
        return types;
    }

    protected String convertSchemaTypeToJSONTypeValue(JSONSchemaType<?> type) {
        String jsonTypeValue = _schemaTypeJSONTypeValues.get(type.getClass());
        if (jsonTypeValue == null) {
            throw new IllegalArgumentException("Illegal JSONSchemaType: " + type);
        }
        return jsonTypeValue;
    }

    protected JSONSchemaType<?> convertSchemaTypeFromJSONTypeValue(JSONObject jsonObject, String jsonTypeValue) {
        Class<? extends JSONSchemaType<?>> typeClass = _schemaTypeJSONTypeValues.inverse().get(jsonTypeValue);
        if (typeClass == null) {
            throw new IllegalArgumentException("Unknown JSONSchemaType: " + jsonTypeValue);
        }

        if (typeClass == JSONSchemaAnyType.class) {
            return JSONSchemaTypes.ANY;
        }

        if (typeClass == JSONSchemaNullType.class) {
            return JSONSchemaTypes.NULL;
        }

        try {
            return typeClass.newInstance();
        } catch (InstantiationException e) {
            throw Throwables.propagate(e);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    protected BiMap<Class<? extends JSONSchemaType<?>>, String> buildSchemaTypeJSONTypeValues() {
        BiMap<Class<? extends JSONSchemaType<?>>, String> jsonSchemaTypeJSONTypeValues = HashBiMap.create();

        jsonSchemaTypeJSONTypeValues.put(JSONSchemaAnyType.class, "any");
        jsonSchemaTypeJSONTypeValues.put(JSONSchemaNullType.class, "null");
        jsonSchemaTypeJSONTypeValues.put(JSONSchemaArrayType.class, "array");
        jsonSchemaTypeJSONTypeValues.put(JSONSchemaBooleanType.class, "boolean");
        jsonSchemaTypeJSONTypeValues.put(JSONSchemaIntegerType.class, "integer");
        jsonSchemaTypeJSONTypeValues.put(JSONSchemaNumberType.class, "number");
        jsonSchemaTypeJSONTypeValues.put(JSONSchemaObjectType.class, "object");
        jsonSchemaTypeJSONTypeValues.put(JSONSchemaStringType.class, "string");

        return jsonSchemaTypeJSONTypeValues;
    }

    protected class JSONSchemaURLCacheLoader extends CacheLoader<String, JSONSchema> {
        @Override
        public JSONSchema load(String schemaID) throws Exception {
            return fromJSONObject(_schemaJSONRetriever.retrieveSchemaJSON(schemaID));
        }
    }

    protected static class DefaultSchemaJSONRetriever implements SchemaJSONRetriever {
        @Override
        public JSONObject retrieveSchemaJSON(String schemaID) throws Exception {
            // enhance: support other types of URLs besides http
            URL url = new URL(schemaID);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String schemaText = readUrlResponseString(urlConnection);
            return new JSONObject(schemaText);
        }
    }

    private static String readUrlResponseString(HttpURLConnection cxn) throws IOException {
        try {
            // try to extract the character set from the Content-Type header
            String characterEncoding = null;
            String contentType = cxn.getContentType();
            if (contentType != null) {
                Matcher matcher = HTTP_CONTENT_TYPE_CHARSET.matcher(contentType);
                if (matcher.find()) {
                    characterEncoding = StringUtils.trimToNull(matcher.group(1));
                }
            }
            if (characterEncoding == null) {
                characterEncoding = "UTF-8";
            }

            InputStream in = cxn.getInputStream();
            try {
                return IOUtils.toString(in, characterEncoding);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            InputStream err = cxn.getErrorStream();
            if (err != null) {
                throw new IOException(IOUtils.toString(err), e);
            } else {
                throw e;
            }
        } finally {
            IOUtils.closeQuietly(cxn.getErrorStream());
        }
    }

    public interface SchemaJSONRetriever {
        /**
         * Retrieves the JSONObject for the given schema ID
         */
        JSONObject retrieveSchemaJSON(String schemaID) throws Exception;
    }
}
