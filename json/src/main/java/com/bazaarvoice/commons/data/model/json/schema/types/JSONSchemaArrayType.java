package com.bazaarvoice.commons.data.model.json.schema.types;

import com.bazaarvoice.commons.data.model.json.GenericJSONArrayList;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaType;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemas;
import com.bazaarvoice.commons.data.model.json.schema.validation.ResultType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResult;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.json.JSONArray;

import javax.annotation.Nullable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class JSONSchemaArrayType implements JSONSchemaType<JSONSchemaArrayType> {
    private List<JSONSchema> _items = Collections.emptyList();
    private Integer _minimumItems;
    private Integer _maximumItems;
    private Boolean _allowDuplicateItems;
    private JSONSchema _additionalItemsSchema;

    /**
     * Determines whether or not this array is a tuple.
     */
    public boolean isTuple() {
        return _items.size() > 1;
    }

    public List<JSONSchema> getItems() {
        return _items;
    }

    public void setItems(Iterable<JSONSchema> items) {
        _items = Lists.newArrayList(items);
    }

    public JSONSchemaArrayType items(Iterable<JSONSchema> items) {
        setItems(items);
        return this;
    }

    public JSONSchemaArrayType items(JSONSchema... items) {
        setItems(Arrays.asList(items));
        return this;
    }

    /**
     * Assumes the array isn't a tuple and returns the schema for the items allowed in this array.
     */
    public JSONSchema getItem() {
        if (_items.isEmpty()) {
            return new JSONSchema();
        }

        return _items.get(0);
    }

    public void setItem(JSONSchema item) {
        _items = Collections.singletonList(item);
    }

    public JSONSchemaArrayType item(JSONSchema item) {
        setItem(item);
        return this;
    }

    public Integer getMinimumItems() {
        return _minimumItems;
    }

    public void setMinimumItems(Integer minimumItems) {
        _minimumItems = minimumItems;
    }

    public JSONSchemaArrayType minimumItems(Integer minimumItems) {
        setMinimumItems(minimumItems);
        return this;
    }

    public Integer getMaximumItems() {
        return _maximumItems;
    }

    public void setMaximumItems(Integer maximumItems) {
        _maximumItems = maximumItems;
    }

    public JSONSchemaArrayType maximumItems(Integer maximumItems) {
        setMaximumItems(maximumItems);
        return this;
    }

    /**
     * Returns <tt>true</tt> when {@link #getAllowDuplicateItems()} is <tt>false</tt>
     */
    public boolean isSet() {
        return _allowDuplicateItems == Boolean.FALSE;
    }

    public Boolean getAllowDuplicateItems() {
        return _allowDuplicateItems;
    }

    public void setAllowDuplicateItems(Boolean allowDuplicateItems) {
        _allowDuplicateItems = allowDuplicateItems;
    }

    public JSONSchemaArrayType allowDuplicateItems() {
        return allowDuplicateItems(true);
    }

    public JSONSchemaArrayType disallowDuplicateItems() {
        return allowDuplicateItems(false);
    }

    public JSONSchemaArrayType allowDuplicateItems(Boolean uniqueItemsRequired) {
        setAllowDuplicateItems(uniqueItemsRequired);
        return this;
    }

    public Boolean getRequireUniqueItems() {
        return _allowDuplicateItems != null ? !_allowDuplicateItems : null;
    }

    public void setRequireUniqueItems(Boolean requireUniqueItems) {
        _allowDuplicateItems = requireUniqueItems != null ? !requireUniqueItems : null;
    }

    public JSONSchemaArrayType requireUniqueItems(Boolean requireUniqueItems) {
        setRequireUniqueItems(requireUniqueItems);
        return this;
    }

    public JSONSchema getAdditionalItemsSchema() {
        return _additionalItemsSchema;
    }

    public void setAdditionalItemsSchema(JSONSchema additionalItemsSchema) {
        _additionalItemsSchema = additionalItemsSchema;
    }

    /**
     * When a tuple, sets the schema for any additional items. If null, additional items are not allowed. Defaults to ANY.
     */
    public JSONSchemaArrayType additionalItemsSchema(JSONSchema additionalItemsSchema) {
        setAdditionalItemsSchema(additionalItemsSchema);
        return this;
    }

    public JSONSchemaArrayType allowAdditionalItems() {
        setAdditionalItemsSchema(new JSONSchema());
        return this;
    }

    public JSONSchemaArrayType disallowAdditionalItems() {
        setAdditionalItemsSchema(null);
        return this;
    }

    @Override
    public JSONSchemaArrayType clone() {
        return new JSONSchemaArrayType().items(Iterables.transform(_items, JSONSchemas.CloningFunction.INSTANCE)).
                minimumItems(_minimumItems).
                maximumItems(_maximumItems).
                allowDuplicateItems(_allowDuplicateItems).
                additionalItemsSchema(_additionalItemsSchema != null ? _additionalItemsSchema.clone() : null);
    }

    @Override
    public JSONSchemaArrayType merge(@Nullable JSONSchemaArrayType parentType) {
        if (parentType == null) {
            return this;
        }

        int size = _items.size();
        if (size == parentType._items.size()) {
            // only merge the parent type's items if both are not a tuple or the tuples are the same arity
            for (int index = 0; index < size; index++) {
                _items.get(index).merge(parentType._items.get(index));
            }
        } else if (size == 0) {
            setItems(Collections2.transform(parentType._items, JSONSchemas.CloningFunction.INSTANCE));
        }

        if (_minimumItems == null) {
            _minimumItems = parentType._minimumItems;
        }

        if (_maximumItems == null) {
            _maximumItems = parentType._maximumItems;
        }

        if (_allowDuplicateItems == null) {
            _allowDuplicateItems = parentType._allowDuplicateItems;
        }

        if (_additionalItemsSchema != null) {
            if (_additionalItemsSchema.isEmpty() && parentType._additionalItemsSchema == null) {
                // null value means disallow additional items, so parent null conveys when this is the "empty" schema
                _additionalItemsSchema = null;
            } else {
                _additionalItemsSchema.merge(parentType._additionalItemsSchema);
            }
        }

        return this;
    }

    @Override
    public void validate(JSONSchema schema, Object obj, String path, ValidationResults results) {
        Collection coll;
        if (obj instanceof JSONArray) {
            coll = new GenericJSONArrayList((JSONArray) obj);
        } else if (obj instanceof Collection) {
            coll = (Collection) obj;
        } else {
            final Iterable iter = (Iterable) obj;
            coll = new AbstractCollection() {
                @Override
                public Iterator iterator() {
                    return iter.iterator();
                }

                @Override
                public int size() {
                    return Iterables.size(iter);
                }
            };
        }

        if (_minimumItems != null && coll.size() < _minimumItems) {
            results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Array does not have enough items, < " + _minimumItems + ": " + coll));
        }

        if (_maximumItems != null && coll.size() > _maximumItems) {
            results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Array has too many items, > " + _minimumItems + ": " + coll));
        }

        boolean hasDuplicates = false;
        Set<Object> set = Sets.newHashSet();
        if (isTuple()) {
            int tupleSize = _items.size();
            int collSize = _items.size();
            if (tupleSize > collSize) {
                results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Tuple is not the correct size, " + tupleSize + ": " + coll));
            }

            Iterator<JSONSchema> tupleIter = _items.iterator();
            Iterator collIter = coll.iterator();
            int index = 0;
            while (tupleIter.hasNext() && collIter.hasNext()) {
                JSONSchema tupleSchema = tupleIter.next();
                Object tupleObj = collIter.next();
                hasDuplicates = hasDuplicates || !set.add(tupleObj);
                tupleSchema.validate(tupleObj, path + "[" + index + "]", results);

                index++;
            }

            if (_additionalItemsSchema != null) {
                while (collIter.hasNext()) {
                    Object additionalObj = collIter.next();
                    hasDuplicates = hasDuplicates || !set.add(additionalObj);
                    _additionalItemsSchema.validate(additionalObj, path + "[" + index + "]", results);

                    index++;
                }
            } else if (collIter.hasNext()) {
                results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Tuple does not allow additional items: " + coll));
            }
        } else {
            int index = 0;
            JSONSchema itemSchema = getItem();
            for (final Object collObj : coll) {
                hasDuplicates = hasDuplicates || !set.add(collObj);
                itemSchema.validate(collObj, path + "[" + index + "]", results);

                index++;
            }
        }

        if (isSet() && hasDuplicates) {
            results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Set has duplicate items: " + coll));
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "[isTuple=" + isTuple() + "]" +
                "[items=" + _items + "]" +
                "[minimumItems=" + _minimumItems + "]" +
                "[maximumItems=" + _maximumItems + "]" +
                "[allowDuplicateItems=" + _allowDuplicateItems + "]" +
                "[additionalItemsSchema=" + _additionalItemsSchema + "]";
    }
}
