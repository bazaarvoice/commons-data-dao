package com.bazaarvoice.commons.data.dao.json;

import com.google.common.io.Resources;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.annotation.Nullable;
import java.nio.charset.Charset;

public class JSONTestHelper {
    private JSONTestHelper() {}

    public static void compareJSONTextWithResource(String jsonText, Class<?> cls, String resourceLocation) throws Exception {
        compareJSONTextWithResource(jsonText, cls, resourceLocation, null);
    }

    public static void compareJSONTextWithResource(String jsonText, Class<?> cls, String resourceLocation, @Nullable String message) throws Exception {
        compareJSONWithResource(new JSONObject(jsonText), cls, resourceLocation, message);
    }

    public static void compareJSONWithResource(JSONObject jsonObject, Class<?> cls, String resourceLocation) throws Exception {
        compareJSONWithResource(jsonObject, cls, resourceLocation, null);
    }

    public static void compareJSONWithResource(JSONObject jsonObject, Class<?> cls, String resourceLocation, @Nullable String message) throws Exception {
        String comparisonJSONText = toString(jsonObject);
        String resourceJSONText = normalizeJSON(Resources.toString(cls.getResource(resourceLocation), Charset.defaultCharset()));
        JSONAssert.assertEquals(resourceJSONText, comparisonJSONText, true);
    }

    public static <T> void compareReadWriteJSON(JSONMarshaller<T> jsonMarshaller, Class<?> cls, String resourceLocation) throws Exception {
        compareReadWriteJSON(jsonMarshaller, cls, resourceLocation, null, null);
    }

    public static <T> void compareReadWriteJSON(JSONMarshaller<T> jsonMarshaller, Class<?> cls, String readResourceLocation, @Nullable String compareResourceLocation) throws Exception {
        compareReadWriteJSON(jsonMarshaller, cls, readResourceLocation, compareResourceLocation, null);
    }

    public static <T> void compareReadWriteJSON(JSONMarshaller<T> jsonMarshaller, Class<?> cls, String readResourceLocation, @Nullable String compareResourceLocation, @Nullable String message) throws Exception {
        JSONObject originalJSONObject = new JSONObject(Resources.toString(cls.getResource(readResourceLocation), Charset.defaultCharset()));
        T obj = jsonMarshaller.fromJSONObject(originalJSONObject);

        JSONObject newJSONObject = jsonMarshaller.toJSONObject(obj);
        String newJSONText = JSONTestHelper.toString(newJSONObject);

        String compareJSONObject;
        if (compareResourceLocation != null) {
            compareJSONObject = JSONTestHelper.toString(new JSONObject(Resources.toString(cls.getResource(compareResourceLocation), Charset.defaultCharset())));
        } else {
            compareJSONObject = JSONTestHelper.toString(originalJSONObject);
        }

        JSONAssert.assertEquals(compareJSONObject, newJSONText, true);
    }

    public static String normalizeJSON(String jsonText) {
        return toString(new JSONObject(jsonText));
    }

    public static String toString(JSONObject jsonObject) {
        return jsonObject.toString(2);
    }
}
