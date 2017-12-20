package com.bazaarvoice.commons.data.model.json;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractJSONObjectMap<T> extends AbstractMap<String, T> {
    private final JSONObject _jsonObject;

    public AbstractJSONObjectMap(@Nullable JSONObject jsonObject) {
        _jsonObject = jsonObject;
    }

    @Override
    public boolean containsKey(Object key) {
        return _jsonObject != null && _jsonObject.has((String) key);
    }

    @Override
    public T get(Object key) {
        if (_jsonObject == null) {
            return null;
        }

        try {
            return getFromJSONObject(_jsonObject, (String) key);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    protected abstract T getFromJSONObject(JSONObject jsonObject, String name) throws JSONException;

    @Override
    public T put(String key, T value) {
        if (_jsonObject == null) {
            throw new UnsupportedOperationException("Wrapped JSON Object is null");
        }

        T oldValue = get(key);
        try {
            putToJSONObject(_jsonObject, key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return oldValue;
    }

    protected abstract void putToJSONObject(JSONObject jsonObject, String name, T value) throws JSONException;

    @Override
    public Set<Entry<String, T>> entrySet() {
        return new AbstractSet<Entry<String, T>>() {
            @Override
            public Iterator<Entry<String, T>> iterator() {
                @SuppressWarnings ({"unchecked"})
                Iterator<String> keys = _jsonObject != null ? _jsonObject.keys() : Collections.<String>emptyIterator();

                return Iterators.transform(keys, new Function<String, Entry<String, T>>() {
                    @Override
                    public Entry<String, T> apply(final String name) {
                        return new Entry<String, T>() {
                            @Override
                            public String getKey() {
                                return name;
                            }

                            @Override
                            public T getValue() {
                                return get(name);
                            }

                            @Override
                            public T setValue(T value) {
                                return put(name, value);
                            }
                        };
                    }
                });
            }

            @Override
            public int size() {
                return _jsonObject != null ? _jsonObject.length() : 0;
            }
        };
    }
}
