package com.bazaarvoice.commons.data.model.json;

import org.json.JSONArray;

import javax.annotation.Nullable;

public class StringJSONArrayList extends AbstractJSONArrayList<String> {
    public StringJSONArrayList(@Nullable JSONArray jsonArray) {
        super(jsonArray);
    }

    @Nullable
    @Override
    protected String getFromJSONArray(JSONArray jsonArray, int index) {
        return (String) jsonArray.opt(index);
    }
}
