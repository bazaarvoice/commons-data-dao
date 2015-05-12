package com.bazaarvoice.commons.data.dao.json;

import com.google.common.base.Predicate;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

public interface MappingJSONMarshaller<T> extends JSONMarshaller<T> {

    JSONObject toMappedJSONObject(Iterable<? extends T> styles);

    Collection<T> fromMappedJSONObject(JSONObject jsonObject, Predicate<? super Map.Entry<String, JSONObject>> predicate);

}
