package com.bazaarvoice.commons.data.model.json;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nullable;

public class JSONObjectJSONArrayList extends AbstractJSONArrayList<JSONObject> {
    public JSONObjectJSONArrayList(@Nullable JSONArray jsonArray) {
        super(jsonArray);
    }

    @Nullable
    @Override
    protected JSONObject getFromJSONArray(JSONArray jsonArray, int index) {
        return (JSONObject) jsonArray.opt(index);
    }
}
