package com.bazaarvoice.commons.data.model.json;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

public class StringJSONObjectMap extends AbstractJSONObjectMap<String> {
    public StringJSONObjectMap(@Nullable JSONObject jsonObject) {
        super(jsonObject);
    }

    @Nullable
    @Override
    protected String getFromJSONObject(JSONObject jsonObject, String name)
            throws JSONException {
        return (String) jsonObject.opt(name);
    }

    @Override
    protected void putToJSONObject(JSONObject jsonObject, String name, String value)
            throws JSONException {
        jsonObject.put(name, value);
    }
}
