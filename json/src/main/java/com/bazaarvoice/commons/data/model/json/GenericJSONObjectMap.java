package com.bazaarvoice.commons.data.model.json;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

public class GenericJSONObjectMap extends AbstractJSONObjectMap<Object> {
    public GenericJSONObjectMap(@Nullable JSONObject jsonObject) {
        super(jsonObject);
    }

    @Nullable
    @Override
    protected Object getFromJSONObject(JSONObject jsonObject, String name)
            throws JSONException {
        return jsonObject.opt(name);
    }

    @Override
    protected void putToJSONObject(JSONObject jsonObject, String name, Object value)
            throws JSONException {
        jsonObject.put(name, value);
    }
}
