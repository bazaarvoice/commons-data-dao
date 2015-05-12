package com.bazaarvoice.commons.data.dao.json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Marshals a data object to/from JSON
 */
public interface JSONMarshaller<T> {

    JSONObject toJSONObject(T object) throws JSONException;

    T fromJSONObject(JSONObject jsonObject) throws JSONException;

}
