package com.bazaarvoice.commons.data.model.json.schema.types;

import com.bazaarvoice.commons.data.model.json.GenericJSONObjectMap;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaNamedProperty;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaPatternProperty;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaProperty;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ResultType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResult;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class JSONSchemaObjectType implements JSONSchemaType<JSONSchemaObjectType> {
    private Map<String, JSONSchemaNamedProperty> _namedProperties = Maps.newLinkedHashMap();
    private Map<Pattern, JSONSchemaPatternProperty> _patternProperties = Maps.newLinkedHashMap();
    private JSONSchema _additionalPropertiesSchema = new JSONSchema();

    public Iterable<JSONSchemaProperty> getAllProperties() {
        return Iterables.<JSONSchemaProperty>concat(getNamedProperties(), getPatternProperties());
    }

    public void setAllProperties(Iterable<? extends JSONSchemaProperty> properties) {
        _namedProperties.clear();
        _patternProperties.clear();
        for (final JSONSchemaProperty property : properties) {
            addProperty(property);
        }
    }

    public JSONSchemaObjectType allProperties(Iterable<? extends JSONSchemaProperty> properties) {
        setAllProperties(properties);
        return this;
    }

    public JSONSchemaObjectType allProperties(JSONSchemaProperty... properties) {
        setAllProperties(Arrays.asList(properties));
        return this;
    }

    public JSONSchemaObjectType addProperty(JSONSchemaProperty property) {
        if (property instanceof JSONSchemaNamedProperty) {
            addNamedProperty((JSONSchemaNamedProperty) property);
        } else if (property instanceof JSONSchemaPatternProperty) {
            addPatternProperty((JSONSchemaPatternProperty) property);
        } else {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        return this;
    }

    @Nullable
    public JSONSchemaNamedProperty getNamedProperty(String name) {
        return _namedProperties.get(name);
    }

    public Collection<JSONSchemaNamedProperty> getNamedProperties() {
        return _namedProperties.values();
    }

    public void setNamedProperties(Iterable<JSONSchemaNamedProperty> namedProperties) {
        _namedProperties.clear();
        for (JSONSchemaNamedProperty namedProperty : namedProperties) {
            addNamedProperty(namedProperty);
        }
    }

    public JSONSchemaObjectType namedProperties(Iterable<JSONSchemaNamedProperty> namedProperties) {
        setNamedProperties(namedProperties);
        return this;
    }

    public JSONSchemaObjectType namedProperties(JSONSchemaNamedProperty... namedProperties) {
        setNamedProperties(Arrays.asList(namedProperties));
        return this;
    }

    public JSONSchemaObjectType addNamedProperty(JSONSchemaNamedProperty namedProperty) {
        _namedProperties.put(namedProperty.getName(), namedProperty);
        return this;
    }

    public Collection<JSONSchemaPatternProperty> getPatternProperties() {
        return _patternProperties.values();
    }

    public void setPatternProperties(Iterable<JSONSchemaPatternProperty> patternProperties) {
        _patternProperties.clear();
        for (JSONSchemaPatternProperty patternProperty : patternProperties) {
            addPatternProperty(patternProperty);
        }
    }

    public JSONSchemaObjectType patternProperties(Iterable<JSONSchemaPatternProperty> patternProperties) {
        setPatternProperties(patternProperties);
        return this;
    }

    public JSONSchemaObjectType patternProperties(JSONSchemaPatternProperty... patternProperties) {
        setPatternProperties(Arrays.asList(patternProperties));
        return this;
    }

    public JSONSchemaObjectType addPatternProperty(JSONSchemaPatternProperty patternProperty) {
        _patternProperties.put(patternProperty.getRegex(), patternProperty);
        return this;
    }

    public JSONSchema getAdditionalPropertiesSchema() {
        return _additionalPropertiesSchema;
    }

    public void setAdditionalPropertiesSchema(JSONSchema additionalPropertiesSchema) {
        _additionalPropertiesSchema = additionalPropertiesSchema;
    }

    /**
     * Sets the schema for any additional properties. If null, additional properties are not allowed. Defaults to ANY.
     */
    public JSONSchemaObjectType additionalPropertiesSchema(JSONSchema additionalPropertiesSchema) {
        setAdditionalPropertiesSchema(additionalPropertiesSchema);
        return this;
    }

    public JSONSchemaObjectType allowAdditionalProperties() {
        setAdditionalPropertiesSchema(new JSONSchema());
        return this;
    }

    public JSONSchemaObjectType disallowAdditionalProperties() {
        setAdditionalPropertiesSchema(null);
        return this;
    }

    @Override
    public JSONSchemaObjectType clone() {
        return new JSONSchemaObjectType().namedProperties(Iterables.transform(_namedProperties.values(), new Function<JSONSchemaNamedProperty, JSONSchemaNamedProperty>() {
            @Override
            public JSONSchemaNamedProperty apply(JSONSchemaNamedProperty property) {
                return property.clone();
            }
        })).patternProperties(Iterables.transform(_patternProperties.values(), new Function<JSONSchemaPatternProperty, JSONSchemaPatternProperty>() {
            @Override
            public JSONSchemaPatternProperty apply(JSONSchemaPatternProperty patternProperty) {
                return patternProperty.clone();
            }
        })).additionalPropertiesSchema(_additionalPropertiesSchema != null ? _additionalPropertiesSchema.clone() : null);
    }

    @Override
    public JSONSchemaObjectType merge(@Nullable JSONSchemaObjectType parentType) {
        if (parentType == null) {
            return this;
        }

        for (final JSONSchemaNamedProperty parentNamedProperty : parentType._namedProperties.values()) {
            JSONSchemaNamedProperty namedProperty = _namedProperties.get(parentNamedProperty.getName());
            if (namedProperty != null) {
                namedProperty.merge(parentNamedProperty);
            } else {
                addNamedProperty(parentNamedProperty.clone());
            }
        }

        for (final JSONSchemaPatternProperty parentPatternProperty : parentType._patternProperties.values()) {
            JSONSchemaPatternProperty patternProperty = _patternProperties.get(parentPatternProperty.getRegex());
            if (patternProperty != null) {
                patternProperty.merge(parentPatternProperty);
            } else {
                addPatternProperty(parentPatternProperty.clone());
            }
        }

        if (_additionalPropertiesSchema != null) {
            if (_additionalPropertiesSchema.isEmpty() && parentType._additionalPropertiesSchema == null) {
                _additionalPropertiesSchema = null;
            } else {
                _additionalPropertiesSchema.merge(parentType._additionalPropertiesSchema);
            }
        }

        return this;
    }

    @Override
    public void validate(JSONSchema schema, Object obj, String path, ValidationResults results) {
        Map<String, Object> map;
        if (obj instanceof JSONObject) {
            map = new GenericJSONObjectMap((JSONObject) obj);
        } else {
            //noinspection unchecked
            map = (Map<String,Object>) obj;
        }

        Set<JSONSchemaProperty> seenProperties = Sets.newHashSet();

        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (JSONSchema.SCHEMA_KEY.equals(key)) {
                if (!(value instanceof String)) {
                    results.addResult(new ValidationResult().type(ResultType.INVALID_SCHEMA).path(path).message(JSONSchema.SCHEMA_KEY + " must be a string"));
                    continue;
                }

                String schemaID = (String) value;
                if (!Strings.isNullOrEmpty(schema.getID()) && !schemaID.equals(schema.getID())) {
                    results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message(JSONSchema.SCHEMA_KEY + " should be " + schema.getID() + ": " + schemaID));
                }

                continue;
            }

            JSONSchemaProperty property = getPropertyForKey(key);
            JSONSchema valueSchema;
            if (property != null) {
                valueSchema = property.getValueSchema();
                seenProperties.add(property);
            } else {
                valueSchema = _additionalPropertiesSchema;
            }

            if (valueSchema != null) {
                valueSchema.validate(value, path + "." + key, results);
            } else {
                results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path + "." + key).message("Additional properties not supported: " + entry));
            }
        }

        for (final JSONSchemaProperty property : getAllProperties()) {
            if (!seenProperties.contains(property) && property.getValueSchema().isRequired()) {
                String propertyDescription;
                if (property instanceof JSONSchemaNamedProperty) {
                    propertyDescription = ((JSONSchemaNamedProperty) property).getName();
                } else if (property instanceof JSONSchemaPatternProperty) {
                    propertyDescription = ((JSONSchemaPatternProperty) property).getPattern();
                } else {
                    throw new IllegalStateException("Unknown property: " + property);
                }

                results.addResult(new ValidationResult().type(ResultType.REQUIRED_VALUE).path(path).message("Required parameter " + propertyDescription + " missing: " + obj));
            }
        }
    }

    @Nullable
    private JSONSchemaProperty getPropertyForKey(String key) {
        JSONSchemaNamedProperty jsonSchemaProperty = getNamedProperty(key);
        if (jsonSchemaProperty != null) {
            return jsonSchemaProperty;
        }

        for (final JSONSchemaPatternProperty jsonSchemaPatternProperty : getPatternProperties()) {
            if (jsonSchemaPatternProperty.getRegex().matcher(key).matches()) {
                return jsonSchemaPatternProperty;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[properties=" + _namedProperties + "]" +
                "[patternProperties=" + _patternProperties + "]" +
                "[additionalPropertiesSchema=" + _additionalPropertiesSchema + "]";
    }
}
