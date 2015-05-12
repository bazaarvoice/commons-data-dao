package com.bazaarvoice.commons.data.model.json.schema;

import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaArrayType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaBooleanType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaIntegerType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaNumberType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaObjectType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaStringType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaUnionType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class JSONSchema implements Serializable {
    public static final String SCHEMA_KEY = "$schema";

    private JSONSchema _referencesSchema;
    private String _referencesSchemaID;
    private String _id;
    private JSONSchema _extendsSchema;
    private String _extendsSchemaID;
    private Boolean _required;
    private String _title;
    private String _description;

    private Map<Class<? extends JSONSchemaType>, JSONSchemaType<?>> _types = Maps.newLinkedHashMap();

    public boolean isEmpty() {
        return (_referencesSchemaID == null || (_referencesSchema != null && _referencesSchema.isEmpty())) &&
                (_extendsSchemaID == null || (_extendsSchema != null && _extendsSchema.isEmpty())) &&
                (_types.isEmpty() || (_types.size() == 1 && hasType(JSONSchemaTypes.ANY.getClass())));
    }

    public JSONSchema getReferencesSchema() {
        return _referencesSchema;
    }

    public void setReferencesSchema(JSONSchema referencesSchema) {
        _referencesSchema = referencesSchema;
        _referencesSchemaID = referencesSchema != null ? referencesSchema.getID() : null;

        if (referencesSchema != null && referencesSchema.getID() == null) {
            throw new IllegalArgumentException("Referenced schema cannot have null ID");
        }

        if (_referencesSchemaID != null) {
            _id = _referencesSchemaID;
        }

        // cannot be extending if referencing
        _extendsSchema = null;
        _extendsSchemaID = null;
    }

    public JSONSchema referencesSchema(JSONSchema referencesSchema) {
        setReferencesSchema(referencesSchema);
        return this;
    }

    public String getReferencesSchemaID() {
        return _referencesSchemaID;
    }

    public void setReferencesSchemaID(String referenceSchemaID) {
        _referencesSchemaID = referenceSchemaID;
        _referencesSchema = null;

        if (_referencesSchemaID != null) {
            _id = _referencesSchemaID;
        }

        // cannot be extending if referencing
        _extendsSchema = null;
        _extendsSchemaID = null;
    }

    public JSONSchema referencesSchemaID(String referenceSchemaID) {
        setReferencesSchemaID(referenceSchemaID);
        return this;
    }

    public String getID() {
        return _id;
    }

    public void setID(String id) {
        _id = id;
    }

    public JSONSchema id(String id) {
        setID(id);
        return this;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public JSONSchema title(String title) {
        setTitle(title);
        return this;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public JSONSchema description(String description) {
        setDescription(description);
        return this;
    }

    public JSONSchema getExtendsSchema() {
        return _extendsSchema;
    }

    public void setExtendsSchema(JSONSchema extendsSchema) {
        _extendsSchema = extendsSchema;
        _extendsSchemaID = extendsSchema != null ? extendsSchema.getID() : null;

        // cannot be referencing if extending
        _referencesSchema = null;
        _referencesSchemaID = null;
    }

    public JSONSchema extendsSchema(JSONSchema extendsSchema) {
        setExtendsSchema(extendsSchema);
        return this;
    }

    public String getExtendsSchemaID() {
        return _extendsSchemaID;
    }

    public void setExtendsSchemaID(String extendsSchemaID) {
        _extendsSchemaID = extendsSchemaID;
        _extendsSchema = null;

        // cannot be referencing if extending
        _referencesSchema = null;
        _referencesSchemaID = null;
    }

    public JSONSchema extendsSchemaID(String extendsSchemaID) {
        setExtendsSchemaID(extendsSchemaID);
        return this;
    }

    public boolean isRequired() {
        return Boolean.TRUE == _required;
    }

    public Boolean getRequired() {
        return _required;
    }

    public void setRequired(Boolean required) {
        _required = required;
    }

    public JSONSchema required() {
        return required(true);
    }

    public JSONSchema optional() {
        return required(false);
    }

    public JSONSchema required(Boolean required) {
        setRequired(required);
        return this;
    }

    public boolean isAnyAllowed() {
        // any is allowed for an empty schema as well as any schema that has an ANY type
        return _types.size() == 0 || hasType(JSONSchemaTypes.ANY.getClass());
    }

    public JSONSchema anyAllowed() {
        return addType(JSONSchemaTypes.ANY);
    }

    public boolean isNullable() {
        return hasType(JSONSchemaTypes.NULL.getClass());
    }

    public void setNullable(boolean nullable) {
        if (nullable) {
            addType(JSONSchemaTypes.NULL);
        } else {
            removeType(JSONSchemaTypes.NULL);
        }
    }

    public JSONSchema nullable() {
        return nullable(true);
    }

    public JSONSchema nullable(boolean nullable) {
        setNullable(nullable);
        return this;
    }

    public boolean isSingleType() {
        return _types.size() <= 1 && !hasType(JSONSchemaUnionType.class);
    }

    public JSONSchemaType<?> getType() {
        if (!isSingleType()) {
            throw new IllegalStateException("Not a single type");
        }

        return Iterables.getFirst(_types.values(), null);
    }

    public boolean hasType(Class<? extends JSONSchemaType> cls) {
        return _types.containsKey(cls);
    }

    @Nullable
    public <S extends JSONSchemaType<S>> S getType(Class<S> cls) {
        //noinspection unchecked
        return (S) _types.get(cls);
    }

    public Collection<JSONSchemaType<?>> getTypes() {
        return _types.values();
    }

    public void setTypes(Iterable<? extends JSONSchemaType<?>> types) {
        _types.clear();
        for (final JSONSchemaType<?> type : types) {
            addType(type);
        }
    }

    public JSONSchema types(Iterable<? extends JSONSchemaType<?>> types) {
        setTypes(types);
        return this;
    }

    public JSONSchema types(JSONSchemaType<?>... types) {
        setTypes(Arrays.asList(types));
        return this;
    }

    public JSONSchema addType(JSONSchemaType<?> type) {
        _types.put(type.getClass(), type);
        return this;
    }

    public JSONSchema removeType(JSONSchemaType<?> type) {
        return removeType(type.getClass());
    }

    public JSONSchema removeType(Class<? extends JSONSchemaType> cls) {
        _types.remove(cls);
        return this;
    }

    /**
     * Deep clone of the schema, except for the extended schema, if any.
     */
    public JSONSchema clone() {
        JSONSchema clonedSchema = new JSONSchema().id(_id).title(_title).description(_description).required(_required).
                types(Collections2.transform(_types.values(), JSONSchemaTypes.CloningFunction.INSTANCE));

        if (_referencesSchema != null) {
            clonedSchema.setReferencesSchema(_referencesSchema);
        } else if (_referencesSchemaID != null) {
            clonedSchema.setReferencesSchemaID(_referencesSchemaID);
        }

        if (_extendsSchema != null) {
            clonedSchema.setExtendsSchema(_extendsSchema);
        } else if (_extendsSchemaID != null) {
            clonedSchema.setExtendsSchemaID(_extendsSchemaID);
        }

        return clonedSchema;
    }

    /**
     * Merges this schema with its {@link #getExtendsSchema() ancestors}
     */
    public JSONSchema flatten() {
        if (_referencesSchema == null && _extendsSchema == null) {
            return this;
        }

        // referenced schema takes precedence over extends, and there should ever be only one
        if (_referencesSchema != null) {
            merge(_referencesSchema);
        } else {
            merge(_extendsSchema);
        }

        _referencesSchema = null;
        _referencesSchemaID = null;
        _extendsSchema = null;
        _extendsSchemaID = null;

        return this;
    }

    /**
     * Merges this schema with the given parent schema (will flatten parent if necessary)
     */
    public JSONSchema merge(@Nullable JSONSchema parentJSONSchema) {
        if (parentJSONSchema == null) {
            return this;
        }

        if (parentJSONSchema._referencesSchema != null || parentJSONSchema._extendsSchema != null) {
            parentJSONSchema = parentJSONSchema.clone().flatten();
        }

        if (_required == null) {
            _required = parentJSONSchema._required;
        }

        for (final JSONSchemaType<?> parentType : parentJSONSchema._types.values()) {
            mergeParentType((JSONSchemaType) parentType);
        }

        return this;
    }

    /**
     * Validates the given object. <tt>null</tt> is considered not set, whereas {@link JSONObject#NULL} is the explicit null value.
     */
    public void validate(@Nullable Object obj, String path, ValidationResults results) {
        if (_referencesSchema != null || _extendsSchema != null) {
            clone().flatten().validate(obj, path, results);
            return;
        } else if (_referencesSchemaID != null) {
            throw new IllegalStateException("Cannot validate. References Schema ID is set, but no reference to References Schema was found");
        } else if (_extendsSchemaID != null) {
            throw new IllegalStateException("Cannot validate. Extends Schema ID is set, but no reference to Extends Schema was found");
        }

        if (obj == null) {
            if (isRequired()) {
                results.addResult(ValidationResults.newRequiredErrorResult(path));
            }

            // do no more checks on null
            return;
        }

        if (obj == JSONObject.NULL) {
            if (!isNullable()) {
                results.addResult(ValidationResults.newNullErrorResult(path));
            }

            // do no more checks on NULL
            return;
        }

        // first check the explicit types. if one exists, ignore any union schemas
        JSONSchemaType<?> type;
        String typeName;
        if (obj instanceof String) {
            type = getType(JSONSchemaStringType.class);
            typeName = "string";
        } else if (obj instanceof Integer) {
            type = getType(JSONSchemaIntegerType.class);
            typeName = "integer";
            if (type == null) {
                type = getType(JSONSchemaNumberType.class);
                typeName = "number";
            }
        } else if (obj instanceof Number) {
            type = getType(JSONSchemaNumberType.class);
            typeName = "number";
        } else if (obj instanceof Boolean) {
            type = getType(JSONSchemaBooleanType.class);
            typeName = "boolean";
        } else if (obj instanceof Iterable || obj instanceof JSONArray) {
            type = getType(JSONSchemaArrayType.class);
            typeName = "array";
        } else if (obj instanceof Map || obj instanceof JSONObject) {
            type = getType(JSONSchemaObjectType.class);
            typeName = "object";
        } else {
            throw new IllegalArgumentException("Unrecognized object: " + obj);
        }

        if (type == null) {
            // type wasn't found. let's check the union types.
            type = getType(JSONSchemaUnionType.class);
        }

        if (type == null) {
            // type still null. guess there wasn't a union type either.
            if (isAnyAllowed()) {
                // well, I guess we'll just allow anything, so whatever
                return;
            }

            results.addResult(ValidationResults.newTypeErrorResult(path, obj, typeName));
            return;
        }

        // got a type. let's validate it
        type.validate(this, obj, path, results);
    }

    private <S extends JSONSchemaType<S>> void mergeParentType(S parentType) {
        @SuppressWarnings ("unchecked")
        S type = (S) getType(parentType.getClass());
        if (type != null) {
            type.merge(parentType);
        } else {
            _types.put(parentType.getClass(), parentType.clone());
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "[referenceSchema=" + (_referencesSchema != null ? _referencesSchema.toString() : _referencesSchemaID) + "]" +
                "[id='" + _id + "']" +
                "[extendsSchema=" + (_extendsSchema != null ? _extendsSchema.toString() : _extendsSchemaID) + "]" +
                "[title='" + _title + "']" +
                "[description='" + _description + "']" +
                "[required=" + _required + "]" +
                "[types=" + getTypes() + "]";
    }

}
