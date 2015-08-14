package com.bazaarvoice.commons.data.model.json.schema.types;

import com.bazaarvoice.commons.data.model.json.GenericJSONObjectMap;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ResultType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResult;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JSONSchemaUnionType implements JSONSchemaType<JSONSchemaUnionType> {
    private List<JSONSchema> _schemas = Lists.newArrayList();

    @Nullable
    public JSONSchema getSchema(final String schemaID) {
        return Iterables.find(_schemas, new Predicate<JSONSchema>() {
            @Override
            public boolean apply(@Nullable JSONSchema input) {
                return input != null && schemaID.equals(input.getID());
            }
        }, null);
    }

    public Collection<JSONSchema> getSchemas() {
        return _schemas;
    }

    public void setSchemas(Iterable<JSONSchema> schemas) {
        clearSchemas();
        for (final JSONSchema schema : schemas) {
            addSchema(schema);
        }
    }

    public JSONSchemaUnionType schemas(Iterable<JSONSchema> schemas) {
        setSchemas(schemas);
        return this;
    }

    public JSONSchemaUnionType schemas(JSONSchema... schemas) {
        setSchemas(Arrays.asList(schemas));
        return this;
    }

    public JSONSchemaUnionType addSchema(JSONSchema schema) {
        _schemas.add(schema);
        return this;
    }

    public JSONSchemaUnionType removeSchema(JSONSchema schema) {
        _schemas.remove(schema);
        return this;
    }

    public JSONSchemaUnionType clearSchemas() {
        _schemas.clear();
        return this;
    }

    @Override
    public JSONSchemaUnionType clone() {
        return new JSONSchemaUnionType().schemas(getSchemas());
    }

    @Override
    public JSONSchemaUnionType merge(@Nullable JSONSchemaUnionType parentType) {
        if (parentType == null) {
            return this;
        }

        for (final JSONSchema jsonSchema : parentType.getSchemas()) {
            if (!_schemas.contains(jsonSchema)) {
                addSchema(jsonSchema);
            }
        }

        return this;
    }

    @Override
    public void validate(JSONSchema schema, Object obj, String path, ValidationResults results) {
        Map<String, Object> map = null;
        if (obj instanceof JSONObject) {
            map = new GenericJSONObjectMap((JSONObject) obj);
        } else if (obj instanceof Map) {
            //noinspection unchecked
            map = (Map<String,Object>) obj;
        }

        if (map != null) {
            // must be an object, so let's find the "right" schema
            Object schemaValue = map.get(JSONSchema.SCHEMA_KEY);
            String schemaID = schemaValue != null ? schemaValue.toString() : null;
            if (schemaID == null) {
                results.addResult(new ValidationResult().type(ResultType.MISSING_SCHEMA).path(path).message("No " + JSONSchema.SCHEMA_KEY + " to disambiguate union schema"));
                return;
            }

            JSONSchema matchingSchema = getSchema(schemaID);
            if (matchingSchema == null) {
                results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Union schema not found: " + schemaID));
                return;
            }

            matchingSchema.validate(obj, buildPath(path, matchingSchema, 0), results);
            return;
        }

        List<ValidationResults> allUnionResults = Lists.newArrayList();

        // it's not an object, so just validate against all of them and see if there is a schema that's found without errors
        int index = 1;
        for (final JSONSchema unionSchema : getSchemas()) {
            ValidationResults testResults = new ValidationResults();
            unionSchema.validate(obj, buildPath(path, unionSchema, index++), testResults);
            if (!testResults.hasWarningsOrErrors()) {
                // found a good one, so short circuit
                return;
            }

            allUnionResults.add(testResults);
        }

        // didn't find a really good one, so add all of the validation results to the one passed in
        results.addResults(Iterables.concat(allUnionResults));
    }

    private String buildPath(String path, JSONSchema unionSchema, int index) {
        return path + "(" + (!Strings.isNullOrEmpty(unionSchema.getID()) ? unionSchema.getID() : index) + ")";
    }

    @Override
    public String toString() {
        return super.toString() +
                "[schemas=" + _schemas + "]";
    }
}
