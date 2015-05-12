package com.bazaarvoice.commons.data.dao.json;

import org.json.JSONArray;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Marshals a data object to/from JSON arrays
 */
public interface ListingJSONMarshaller<T> extends JSONMarshaller<T> {

    JSONArray toJSONArray(Collection<? extends T> objects);

    Collection<T> fromJSONArray(@Nullable JSONArray jsonArray);

}
