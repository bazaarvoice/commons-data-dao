package com.bazaarvoice.commons.data.dao.json;

import com.google.common.collect.Maps;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public abstract class AbstractDelegatingJSONMarshaller<T, E extends DelegateItemType<?>> extends AbstractMappingJSONMarshaller<T> {
    private final Map<E, JSONMarshaller<?>> _marshallersByType = Maps.newHashMap();

    protected final void addDelegateMarshaller(E type, JSONMarshaller<?> marshaller) {
        _marshallersByType.put(type, marshaller);
    }

    protected abstract E getDelegateItemType(Class<?> delegateItemClass);

    @Override
    public JSONObject toJSONObject(T object)
            throws JSONException {
        E delegateItemType = getDelegateItemType(object.getClass());
        return getMarshaller(delegateItemType).toJSONObject(object).put(getTypeField(), convertFromDelegateItemType(delegateItemType));
    }

    protected abstract String convertFromDelegateItemType(E delegateItemType);

    @Override
    protected String getKey(T object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T fromJSONObject(JSONObject jsonObject)
            throws JSONException {
        return getMarshaller(getDelegateItemType(jsonObject)).fromJSONObject(jsonObject);
    }

    protected E getDelegateItemType(JSONObject jsonObject) throws JSONException {
        return convertToDelegateItemType(jsonObject.getString(getTypeField()));
    }

    protected abstract E convertToDelegateItemType(String delegateItemTypeValue);

    @Override
    protected T setKey(T object, String key) {
        throw new UnsupportedOperationException();
    }

    protected JSONMarshaller<T> getMarshaller(E delegateItemType) {
        @SuppressWarnings ("unchecked")
        JSONMarshaller<T> marshaller = (JSONMarshaller<T>) _marshallersByType.get(delegateItemType);
        if (marshaller == null) {
            throw new IllegalArgumentException("Marshaller not found for type: " + delegateItemType);
        }
        return marshaller;
    }

    protected String getTypeField() {
        return "type";
    }

}
