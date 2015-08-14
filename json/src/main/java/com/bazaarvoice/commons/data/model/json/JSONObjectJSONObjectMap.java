package com.bazaarvoice.commons.data.model.json;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

public class JSONObjectJSONObjectMap extends AbstractJSONObjectMap<JSONObject> {
    public JSONObjectJSONObjectMap(@Nullable JSONObject jsonObject) {
        super(jsonObject);
    }

    @Nullable
    @Override
    protected JSONObject getFromJSONObject(JSONObject jsonObject, String name)
            throws JSONException {
        return (JSONObject) jsonObject.opt(name);
    }

    @Override
    protected void putToJSONObject(JSONObject jsonObject, String name, JSONObject value)
            throws JSONException {
        jsonObject.put(name, value);
    }
}
