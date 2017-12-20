package com.bazaarvoice.commons.data.dao.json;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMappingJSONMarshaller<T> extends AbstractJSONMarshaller<T> implements MappingJSONMarshaller<T> {

    /**
     * Converts the given objects into a single JSON object with the IDs as keys
     */
    @Override
    public JSONObject toMappedJSONObject(Iterable<? extends T> objects) {
        try {
            JSONObject mappedJSONObject = new JSONObject();
            for (final T object : objects) {
                mappedJSONObject.put(getKey(object), toJSONObject(object));
            }
            return mappedJSONObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the key for the given object
     */
    protected abstract String getKey(T object);

    /**
     * Converts the given objects from a single JSON object with the IDs as keys.
     */
    @Override
    public Collection<T> fromMappedJSONObject(JSONObject mappedJSONObject, @Nullable Predicate<? super Map.Entry<String, JSONObject>> predicate) {
        Collection<Map.Entry<String, JSONObject>> entries = convertJSONObjectToJSONObjectMap(mappedJSONObject).entrySet();
        if (predicate != null) {
            entries = Collections2.filter(entries, predicate);
        }

        return Collections2.transform(entries, new Function<Map.Entry<String, JSONObject>, T>() {
            @Override
            public T apply(Map.Entry<String, JSONObject> entry) {
                return setKey(fromJSONObject(entry.getValue()), entry.getKey());
            }
        });
    }

    /**
     * Set the key on the object.
     *
     * @return The object
     */
    protected abstract T setKey(T object, String key);

    /**
     * This can be overridden to ignore special keys, such as an object type key
     */
    protected Set<String> getKeysToIgnore() {
        return Collections.emptySet();
    }
}
