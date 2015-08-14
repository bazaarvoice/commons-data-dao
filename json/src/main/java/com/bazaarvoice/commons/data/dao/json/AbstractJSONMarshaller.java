package com.bazaarvoice.commons.data.dao.json;

import com.bazaarvoice.commons.data.model.json.AbstractJSONArrayList;
import com.bazaarvoice.commons.data.model.json.GenericJSONArrayList;
import com.bazaarvoice.commons.data.model.json.GenericJSONObjectMap;
import com.bazaarvoice.commons.data.model.json.JSONObjectJSONArrayList;
import com.bazaarvoice.commons.data.model.json.JSONObjectJSONObjectMap;
import com.bazaarvoice.commons.data.model.json.StringJSONArrayList;
import com.bazaarvoice.commons.data.model.json.StringJSONObjectMap;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractJSONMarshaller<T> implements ListingJSONMarshaller<T> {
    @Override
    public JSONArray toJSONArray(Collection<? extends T> objects) {
        Collection<JSONObject> jsonObjects = Collections2.transform(objects, new Function<T, JSONObject>() {
            @Override
            public JSONObject apply(T object) {
                return toJSONObject(object);
            }
        });
        return new JSONArray(jsonObjects.toArray());
    }

    @Override
    public Collection<T> fromJSONArray(@Nullable JSONArray jsonArray) {
        return new AbstractJSONArrayList<T>(jsonArray) {
            @Override
            protected T getFromJSONArray(JSONArray jsonArray, int index) {
                return fromJSONObject(jsonArray.optJSONObject(index));
            }
        };
    }

    protected String convertFromEnum(Enum enumValue) {
        return enumValue != null ? enumValue.name() : null;
    }

    /**
     * If the given value is an enum, converts it to a string value.  If a collection, optionally converts the collection by calling {@link #convertIfEnums(Collection)}.
     */
    protected Object convertIfEnum(Object val) {
        if (val instanceof Enum) {
            return convertFromEnum((Enum) val);
        }

        if (val instanceof Collection) {
            return convertIfEnums((Collection) val);
        }

        return val;
    }

    /**
     * Converts any enum values in the collection to strings.
     */
    protected Collection<Object> convertIfEnums(Collection<?> values) {
        return Collections2.transform(values, new Function<Object, Object>() {
            @Override
            public Object apply(Object input) {
                return convertIfEnum(input);
            }
        });
    }

    /**
     * Returns the property as an enumeration value.
     */
    protected <E extends Enum<E>> E convertToEnum(Class<E> enumType, String enumValue) {
        if (Strings.isNullOrEmpty(enumValue)) {
            return null;
        }

        return Enum.valueOf(enumType, enumValue);
    }

    /**
     * Returns the collection of properties as a collection of enumeration values.
     */
    protected <E extends Enum<E>> Collection<E> convertToEnums(Class<E> enumType, Collection<String> enumValues) {
        Collection<E> enums = Lists.newArrayList();
        for (String enumValue : enumValues) {
            enums.add(convertToEnum(enumType, enumValue));
        }
        return enums;
    }

    protected static List<Object> convertJSONArrayToList(@Nullable JSONArray jsonArray) {
        return new GenericJSONArrayList(jsonArray);
    }

    protected static List<String> convertJSONArrayToStringList(@Nullable JSONArray jsonArray) {
        return new StringJSONArrayList(jsonArray);
    }

    protected static List<JSONObject> convertJSONArrayToJSONObjectList(@Nullable JSONArray jsonArray) {
        return new JSONObjectJSONArrayList(jsonArray);
    }

    protected static Map<String, Object> convertJSONObjectToMap(@Nullable final JSONObject jsonObject) {
        return new GenericJSONObjectMap(jsonObject);
    }

    protected static Map<String, String> convertJSONObjectToStringMap(@Nullable final JSONObject jsonObject) {
        return new StringJSONObjectMap(jsonObject);
    }

    protected static Map<String, JSONObject> convertJSONObjectToJSONObjectMap(@Nullable final JSONObject jsonObject) {
        return new JSONObjectJSONObjectMap(jsonObject);
    }

    protected String getSchemaField() {
        return "$schema";
    }
}
