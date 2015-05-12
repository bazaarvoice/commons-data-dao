package com.bazaarvoice.commons.data.model.json.schema;

import com.google.common.base.Throwables;

import javax.annotation.Nullable;
import java.io.Serializable;

public abstract class AbstractJSONSchemaProperty<P extends AbstractJSONSchemaProperty<P>> implements JSONSchemaProperty, Serializable, Cloneable {
    protected JSONSchema _valueSchema;

    @Override
    public JSONSchema getValueSchema() {
        return _valueSchema;
    }

    public void setValueSchema(JSONSchema valueSchema) {
        _valueSchema = valueSchema;
    }

    public P valueSchema(JSONSchema valueSchema) {
        setValueSchema(valueSchema);

        //noinspection unchecked
        return (P) this;
    }

    public P clone() {
        try {
            @SuppressWarnings ("unchecked")
            P clone = (P) super.clone();
            return clone.valueSchema(_valueSchema != null ? _valueSchema.clone() : null);
        } catch (CloneNotSupportedException e) {
            throw Throwables.propagate(e);
        }
    }

    public P merge(@Nullable P parentPatternProperty) {
        if (parentPatternProperty == null) {
            //noinspection unchecked
            return (P) this;
        }

        if (_valueSchema != null) {
            _valueSchema.merge(parentPatternProperty._valueSchema);
        } else if (parentPatternProperty._valueSchema != null) {
            _valueSchema = parentPatternProperty._valueSchema.clone();
        }

        //noinspection unchecked
        return (P) this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[valueSchema=" + _valueSchema + "]";
    }
}
