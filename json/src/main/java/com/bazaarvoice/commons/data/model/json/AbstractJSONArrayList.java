package com.bazaarvoice.commons.data.model.json;

import org.json.JSONArray;
import org.json.JSONException;

import javax.annotation.Nullable;
import java.util.AbstractList;

public abstract class AbstractJSONArrayList<T> extends AbstractList<T> {
    private final JSONArray _jsonArray;

    public AbstractJSONArrayList(@Nullable JSONArray jsonArray) {
        _jsonArray = jsonArray;
    }

    @Override
    public T get(int index) {
        if (_jsonArray == null || index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        try {
            return getFromJSONArray(_jsonArray, index);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    protected abstract T getFromJSONArray(JSONArray jsonArray, int index)
            throws JSONException;

    @Override
    public int size() {
        return _jsonArray != null ? _jsonArray.length() : 0;
    }
}
