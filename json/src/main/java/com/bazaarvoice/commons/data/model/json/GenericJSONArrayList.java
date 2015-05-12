package com.bazaarvoice.commons.data.model.json;

import org.json.JSONArray;

import javax.annotation.Nullable;

public class GenericJSONArrayList extends AbstractJSONArrayList<Object> {
    public GenericJSONArrayList(@Nullable JSONArray jsonArray) {
        super(jsonArray);
    }

    @Nullable
    @Override
    protected Object getFromJSONArray(JSONArray jsonArray, int index) {
        return jsonArray.opt(index);
    }
}
