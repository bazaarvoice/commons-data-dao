package com.bazaarvoice.commons.data.model.json.schema;

import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;

import javax.annotation.Nullable;
import java.io.Serializable;

public interface JSONSchemaType<S extends JSONSchemaType<S>> extends Serializable, Cloneable {

    S clone();

    /**
     * Merges this type with a parent type
     */
    S merge(@Nullable S parentType);

    void validate(JSONSchema schema, Object obj, String path, ValidationResults results);

}
