package com.bazaarvoice.commons.data.model.json.schema.types;

import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ResultType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResult;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

public abstract class AbstractJSONSchemaSimpleType<V, S extends AbstractJSONSchemaSimpleType<V,S>> implements JSONSchemaType<S>, Cloneable {
    private V _defaultValue;
    protected Set<V> _enumValues = Sets.newSetFromMap(new LinkedHashMap<V, Boolean>());

    public V getDefaultValue() {
        return _defaultValue;
    }

    public void setDefaultValue(V defaultValue) {
        _defaultValue = defaultValue;
    }

    public S defaultValue(V defaultValue) {
        _defaultValue = defaultValue;

        //noinspection unchecked
        return (S) this;
    }

    public Collection<V> getEnumValues() {
        return _enumValues;
    }

    public void setEnumValues(Collection<V> enumValues) {
        _enumValues.clear();
        _enumValues.addAll(enumValues);
    }

    public S enumValues(Collection<V> enumValues) {
        setEnumValues(enumValues);

        //noinspection unchecked
        return (S) this;
    }

    public S enumValues(V... enumValues) {
        setEnumValues(Arrays.asList(enumValues));

        //noinspection unchecked
        return (S) this;
    }

    public S addEnumValue(V enumValue) {
        _enumValues.add(enumValue);

        //noinspection unchecked
        return (S) this;
    }

    @Override
    public S clone() {
        try {
            @SuppressWarnings ("unchecked")
            S clone = (S) super.clone();
            return clone.enumValues(_enumValues);
        } catch (CloneNotSupportedException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public S merge(@Nullable S parentType) {
        if (parentType == null) {
            //noinspection unchecked
            return (S) this;
        }

        if (_defaultValue == null) {
            _defaultValue = parentType.getDefaultValue();
        }

        _enumValues.addAll(parentType.getEnumValues());

        //noinspection unchecked
        return (S) this;
    }

    @Override
    public void validate(JSONSchema schema, Object obj, String path, ValidationResults results) {
        @SuppressWarnings ("unchecked")
        V value = (V) obj;

        if (!_enumValues.isEmpty() && !_enumValues.contains(value)) {
            results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Value not in enumerated values: " + value));
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "[defaultValue=" + _defaultValue + "]" +
                "[enumValues=" + _enumValues + "]";
    }
}
