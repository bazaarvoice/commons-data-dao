package com.bazaarvoice.commons.data.model.json.schema;

import com.bazaarvoice.commons.data.dao.json.schema.ValidationResultMarshaller;
import com.bazaarvoice.commons.data.dao.json.schema.ValidationResultsMarshaller;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaArrayType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaIntegerType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaNumberType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaObjectType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaStringType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaTextFormat;
import com.bazaarvoice.commons.data.model.json.schema.validation.ResultType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResult;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;
import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JSONSchemaValidationTest {
    private final ValidationResultsMarshaller _validationResultsMarshaller = new ValidationResultsMarshaller();
    private final ValidationResultMarshaller _validationResultMarshaller = new ValidationResultMarshaller();

    @Test
    public void testRequiredValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newObjectSchema(
                new JSONSchemaNamedProperty().name("sProp1").valueSchema(JSONSchemas.newStringSchema()),
                new JSONSchemaNamedProperty().name("sProp2").valueSchema(JSONSchemas.newStringSchema().required()),
                new JSONSchemaPatternProperty().pattern("sProp3.*").valueSchema(JSONSchemas.newStringSchema()),
                new JSONSchemaPatternProperty().pattern("sProp4.*").valueSchema(JSONSchemas.newStringSchema().required())
        );

        validateNoErrors(schema, new JSONObject().put("sProp1", "value1").put("sProp2", "value2").put("sProp3a", "value3").put("sProp4b", "value4"), "should be valid");
        validateNoErrors(schema, new JSONObject().put("sProp2", "value1").put("sProp4.2", "value2").put("sProp5", "value5"), "extras should be allowed");
        validateWithResults(schema, new JSONObject().put("sProp1", "value1").put("sProp3d", "value3"), "Should indicate missing required fields", ResultType.REQUIRED_VALUE, ResultType.REQUIRED_VALUE);
        validateWithResults(schema, new JSONObject().put("sProp1", "value1").put("sProp3d", "value3").put("sProp4xa", "value4"), "Should indicate missing required field sProp2", ResultType.REQUIRED_VALUE);
        validateWithResults(schema, new JSONObject().put("sProp1", "value1").put("sProp2", "value2").put("sProp3d", "value3"), "Should indicate missing required field sProp4.*", ResultType.REQUIRED_VALUE);
    }

    @Test
    public void testNoExtrasValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newStrictObjectSchema(
                new JSONSchemaNamedProperty().name("sProp1").valueSchema(JSONSchemas.newStringSchema()),
                new JSONSchemaPatternProperty().pattern("sProp2.*").valueSchema(JSONSchemas.newStringSchema())
        );

        validateNoErrors(schema, new JSONObject().put("sProp1", "value1").put("sProp2a", "value2"), "should be valid");
        validateWithResults(schema, new JSONObject().put("sProp1", "value1").put("sProp2.2", "value2").put("sProp3", "value3"), "extras should not be allowed", ResultType.CONSTRAINT_VIOLATION);
    }

    @Test
    public void testSpecificExtraValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newSimpleSchema(new JSONSchemaObjectType().allProperties(
                new JSONSchemaNamedProperty().name("sProp1").valueSchema(JSONSchemas.newStringSchema())
        ).additionalPropertiesSchema(JSONSchemas.newStringSchema()));

        validateNoErrors(schema, new JSONObject().put("sProp1", "value1"), "should be valid");
        validateNoErrors(schema, new JSONObject().put("sProp1", "value1").put("sProp2", "value2"), "extra strings should be allowed");
        validateWithResults(schema, new JSONObject().put("sProp1", "value1").put("sProp2", 2), "other types of extras should not be allowed", ResultType.INCOMPATIBLE_TYPE);
    }

    @Test
    public void testStringValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newStrictObjectSchema(
                new JSONSchemaPatternProperty().pattern("sProp1.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().minimumLength(10).maximumLength(20))),
                new JSONSchemaPatternProperty().pattern("sProp2.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().pattern("^\\d+a*b*c?$"))),
                new JSONSchemaPatternProperty().pattern("sProp3dt.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.DATE_TIME))),
                new JSONSchemaPatternProperty().pattern("sProp3date.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.DATE))),
                new JSONSchemaPatternProperty().pattern("sProp3time.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.TIME))),
                new JSONSchemaPatternProperty().pattern("sProp3utc.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.UTC_MILLISECONDS))),
                new JSONSchemaPatternProperty().pattern("sProp3regex.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.REGEX))),
                new JSONSchemaPatternProperty().pattern("sProp3cssColor.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.CSS_COLOR))),
                new JSONSchemaPatternProperty().pattern("sProp3cssStyle.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.CSS_STYLE))),
                new JSONSchemaPatternProperty().pattern("sProp3phoneNumber.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.PHONE_NUMBER))),
                new JSONSchemaPatternProperty().pattern("sProp3uri.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.URI))),
                new JSONSchemaPatternProperty().pattern("sProp3emailAddress.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.EMAIL_ADDRESS))),
                new JSONSchemaPatternProperty().pattern("sProp3ipv4address.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.IPV4_ADDRESS))),
                new JSONSchemaPatternProperty().pattern("sProp3ipv6address.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.IPV6_ADDRESS))),
                new JSONSchemaPatternProperty().pattern("sProp3hostname.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaStringType().format(JSONSchemaTextFormat.HOSTNAME)))
        );

        validateNoErrors(schema,
                new JSONObject().put("sProp1a", "a long enough value").put("sProp1b", "1234567890").put("sProp1c", "12345678901234567890"),
                "sProp1 should be valid");
        validateWithResults(schema,
                new JSONObject().put("sProp1a", "too short").put("sProp1b", ""),
                "sProp1 should be too short", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);
        validateWithResults(schema,
                new JSONObject().put("sProp1a", "this should be far too long of a string").put("sProp1b", "123456789012345678901"),
                "sProp1 should be too long", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateNoErrors(schema,
                new JSONObject().
                        put("sProp2a", "1234abc").
                        put("sProp2b", "12a").
                        put("sProp2c", "1bc").
                        put("sProp2d", "1b"),
                "sProp2 should be valid");
        validateWithResults(schema,
                new JSONObject().
                        put("sProp2a", "1234ad").
                        put("sProp2b", "a1234a").
                        put("sProp2c", "ad"),
                "sProp2 should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateNoErrors(schema,
                new JSONObject().
                        put("sProp3dt", "2011-01-01T11:22:33+0000").put("sProp3date", "2011-02-02").put("sProp3time", "22:33:44").
                        put("sProp3utc1", "1000").put("sProp3utc2", "1000.5").put("sProp3utc3", "100.5E5").put("sProp3utc3", "100e5").
                        put("sProp3regex1", "^\\d+a*b*c?$").put("sProp3regex2", "\\..*[abc]").
                        put("sProp3cssColor1", "#aaBB33").put("sProp3cssColor2", "#aB3").put("sProp3cssColor3", "rgb(100%,20%,20%)").put("sProp3cssColor4", "rgb(255,123,103)").
                        put("sProp3cssColor5", "rgb(100%,123,103)").put("sProp3cssColor6", "blue").put("sProp3cssColor6", "red").put("sProp3cssColor6", "black").put("sProp3cssColor6", "white").
                        put("sProp3cssStyle1", "color: blue; background: black").put("sProp3cssStyle1", "width: 100%; height: 100px").
                        put("sProp3phoneNumber1", "1-800-123-4567").put("sProp3phoneNumber2", "+1 (800) 456-7890").put("sProp3phoneNumber3", "+011 1234 (0800) 456-780").
                        put("sProp3phoneNumber4", "1234 x1234").put("sProp3phoneNumber5", "1-800-555-1234 x1234").put("sProp3phoneNumber6", "1/800/555/1234 x1234").
                        put("sProp3uri1", "http://www.example.com/").put("sProp3uri2", "file:///some/file").put("sProp3uri3", "some:db:url?user=ack&pass=bar").
                        put("sProp3emailAddress1", "aperson@example.com").put("sProp3emailAddress2", "another.person@an-example.com").put("sProp3emailAddress3", "a.person+special@mail.example.com").
                        put("sProp3ipv4address1", "128.125.253.140").put("sProp3ipv4address2", "255.255.255.255").put("sProp3ipv4address3", "10.10.10.1").
                        put("sProp3ipv6address1", "0123:4567:89ab:cdef:0123:4567:89ab:cdef").put("sProp3ipv6address2", "0123::cdef:0123:4567:89ab:cdef").put("sProp3ipv6address3", "0123::cdef").
                        put("sProp3hostname1", "www.example.com").put("sProp3hostname2", "example.com").put("sProp3hostname3", "example").put("sProp3hostname4", "example-name"),
                "sProp3 should be valid");

        validateWithResults(schema,
                new JSONObject().put("sProp3dt1", "abcd").put("sProp3dt2", "2011-31-01T").put("sProp3dt3", "2011-31-01T12:31:01+0000").put("sProp3dt4", "2011-01-31T12:31:01AM+0000"),
                "sProp3dt should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3date1", "abcd").put("sProp3date2", "2011-31-01"),
                "sProp3date should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3time1", "abcd").put("sProp3time2", "33:11:22").put("sProp3time3", "2011-01-31T01:22:33+0000"),
                "sProp3date should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3utc1", "abcd").put("sProp3utc2", "1.2.3").put("sProp3utc3", "1-2-3"),
                "sProp3date should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3regex1", "\\x").put("sProp3regex", "(abcd"),
                "sProp3regex should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3cssColor1", "rgb(a,b,c)").put("sProp3cssColor2", "aabbcc").put("sProp3cssColor3", "#aaaa").put("sProp3cssColor4", "#a").
                        put("sProp3cssColor5", "aquamarine").put("sProp3cssColor6", "grey"),
                "sProp3cssColor should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3cssStyle1", "color: 12345; style: asdf;;").put("sProp3cssStyle2", "color#4: #fff;"),
                "sProp3cssStyle should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3phoneNumber1", "abcd").put("sProp3phoneNumber2", "1234-abcd").put("sProp3phoneNumber3", "1234 x5678 x9012").put("sProp3phoneNumber4", "+1234 +5678 9012").
                        put("sProp3phoneNumber5", "1 (800) 1234 (def)").put("sProp3phoneNumber6", "1- 800/-1245"),
                "sProp3phoneNumber should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3uri1", "ack\\www.example.com").put("sProp3uri2", "&!(\\)!&)").put("sProp3uri3", "a?asdF\\?asdf?"),
                "sProp3uri should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3emailAddress1", "www.example.com").put("sProp3emailAddress2", "aperson").put("sProp3emailAddress2", "$&!()$@a.com").put("sProp3emailAddress3", "another.person@a"),
                "sProp3emailAddress should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3ipv4address1", "www.example.com").put("sProp3ipv4address2", "111.222.333.444").put("sProp3ipv4address3", "1111.2222.3333").put("sProp3ipv4address4", "1111.2222"),
                "sProp3ipv4address should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3ipv6address1", "www.example.com").put("sProp3ipv6address2", "111.222.111.222").put("sProp3ipv6address3", "0123::cdef:0123:4567::cdef").
                        put("sProp3ipv6address4", "01231234:4567:89ab:cdef:0123:4567:89ab:cdef").put("sProp3ipv6address5", "0123:cdef"),
                "sProp3ipv6address should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);

        validateWithResults(schema,
                new JSONObject().put("sProp3hostname1", "http://www.example.com").put("sProp3hostname2", "&!()!&)"),
                "sProp3hostname should not be valid", ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH);
    }

    @Test
    public void testNumberValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newStrictObjectSchema(
                new JSONSchemaPatternProperty().pattern("nProp1.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaNumberType().minimum(12.5).maximum(100.5))),
                new JSONSchemaPatternProperty().pattern("nProp2.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaNumberType().minimum(13.5).minimumExclusive().maximum(125.5).maximumExclusive())),
                new JSONSchemaPatternProperty().pattern("nProp3.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaNumberType().divisibleBy(1.2)))
        );

        validateNoErrors(schema,
                new JSONObject().put("nProp1a", 15).put("nProp1b", 12.5).put("nProp1b", 100.5).
                        put("nProp2a", 102).put("nProp2b", 13.6).put("nProp2c", 125.4).
                        put("nProp3a", 1.2).put("nProp3b", 2.4).put("nProp3c", 12),
                "should be valid");

        validateWithResults(schema,
                new JSONObject().put("nProp1a", 1).put("nProp1b", 12).put("nProp1c", 12.4).put("nProp1d", 100.6).put("nProp1e", 1000),
                "nProp1 should be invalid", ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);

        validateWithResults(schema,
                new JSONObject().put("nProp2a", 1).put("nProp2b", 12).put("nProp2c", 13.5).put("nProp2d", 125.5).put("nProp2e", 1000),
                "nProp2 should be invalid", ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);

        validateWithResults(schema,
                new JSONObject().put("nProp3a", 1).put("nProp3b", 121).put("nProp3c", 1.3).put("nProp3d", 12.1).put("nProp3e", 12.0001),
                "nProp3 should be invalid", ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);
    }

    @Test
    public void testIntegerValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newStrictObjectSchema(
                new JSONSchemaPatternProperty().pattern("nProp1.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaIntegerType().minimum(12).maximum(100))),
                new JSONSchemaPatternProperty().pattern("nProp2.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaIntegerType().minimum(13).minimumExclusive().maximum(125).maximumExclusive())),
                new JSONSchemaPatternProperty().pattern("nProp3.*").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaIntegerType().divisibleBy(2)))
        );

        validateNoErrors(schema,
                new JSONObject().put("nProp1a", 15).put("nProp1b", 12).put("nProp1b", 100).
                        put("nProp2a", 102).put("nProp2b", 14).put("nProp2c", 124).
                        put("nProp3a", 2).put("nProp3b", 4).put("nProp3c", 12),
                "should be valid");

        validateWithResults(schema,
                new JSONObject().put("nProp1a", 1).put("nProp1b", 10).put("nProp1c", 11).put("nProp1d", 101).put("nProp1e", 1000),
                "nProp1 should be invalid", ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);

        validateWithResults(schema,
                new JSONObject().put("nProp2a", 1).put("nProp2b", 12).put("nProp2c", 13).put("nProp2d", 125).put("nProp2e", 1000),
                "nProp2 should be invalid", ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);

        validateWithResults(schema,
                new JSONObject().put("nProp3a", 1).put("nProp3b", 3).put("nProp3c", 5).put("nProp3d", 81).put("nProp3e", 100001),
                "nProp3 should be invalid", ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);
    }

    @Test
    public void testSimpleArrayValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newArraySchema(JSONSchemas.newStringSchema());

        validateNoErrors(schema, new JSONArray().put(0, "abc").put(1, "def"), "valid json array");
        validateNoErrors(schema, Lists.newArrayList("abc", "def"), "valid list");
        validateNoErrors(schema, Lists.newArrayList("abc", "def", "def"), "valid list with dupes");

        validateWithResults(schema, new JSONArray().put(0, 1).put(1, 2), "invalid json array", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE);
        validateWithResults(schema, Lists.newArrayList(3, 4), "invalid list", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE);
    }

    @Test
    public void testArrayWithMinMaxValidation() throws Exception {
        JSONSchema schema = new JSONSchema().addType(new JSONSchemaArrayType().item(JSONSchemas.newStringSchema()).minimumItems(3).maximumItems(6));

        validateNoErrors(schema, Lists.newArrayList("abc", "def", "ghi"), "valid minimum");
        validateNoErrors(schema, Lists.newArrayList("abc", "def", "ghi", "jkl"), "valid middle");
        validateNoErrors(schema, Lists.newArrayList("abc", "def", "ghi", "jkl", "mno", "pqr"), "valid maximum");

        validateWithResults(schema, Lists.newArrayList("abc", "def"), "too small", ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, Lists.newArrayList("abc", "def", "ghi", "jkl", "mno", "pqr", "stu"), "too big", ResultType.CONSTRAINT_VIOLATION);
    }

    @Test
    public void testArrayWithDuplicateValidation() throws Exception {
        JSONSchema schema = new JSONSchema().addType(new JSONSchemaArrayType().item(JSONSchemas.newStringSchema()).disallowDuplicateItems());

        validateNoErrors(schema, Lists.newArrayList("abc", "def", "ghi"), "valid list");

        validateWithResults(schema, Lists.newArrayList("abc", "abc", "def"), "duplicate at beginning", ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, Lists.newArrayList("abc", "def", "def", "ghi"), "duplicate in middle", ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, Lists.newArrayList("abc", "def", "def"), "duplicate at end", ResultType.CONSTRAINT_VIOLATION);
    }

    @Test
    public void testTupleArrayValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newArraySchema(JSONSchemas.newStringSchema(), JSONSchemas.newIntegerSchema(), JSONSchemas.newBooleanSchema());

        validateNoErrors(schema, new JSONArray().put(0, "abc").put(1, 10).put(2, true), "valid json array");
        validateNoErrors(schema, Lists.newArrayList("abc", 10, true), "valid list");

        validateWithResults(schema, new JSONArray().put(0, "abc").put(1, "10").put(2, "true"), "invalid json array", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE);
        validateWithResults(schema, Lists.newArrayList("abc", "10", "true"), "invalid list", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE);

        validateWithResults(schema, new JSONArray().put(0, "abc").put(1, 10).put(2, true).put(3, "abc"), "invalid json array with extras", ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, Lists.newArrayList("abc", 10, true, "abc"), "invalid list with extras", ResultType.CONSTRAINT_VIOLATION);
    }

    @Test
    public void testTupleArrayWithExtras() throws Exception {
        JSONSchema schema = JSONSchemas.newSimpleSchema(new JSONSchemaArrayType().items(
                JSONSchemas.newStringSchema(), JSONSchemas.newIntegerSchema(), JSONSchemas.newBooleanSchema()
        ).allowAdditionalItems());

        validateNoErrors(schema, new JSONArray().put(0, "abc").put(1, 10).put(2, true), "valid json array");
        validateNoErrors(schema, new JSONArray().put(0, "abc").put(1, 10).put(2, true).put(3, "abc"), "valid json array with extras");

        validateNoErrors(schema, Lists.newArrayList("abc", 10, true), "valid list");
        validateNoErrors(schema, Lists.newArrayList("abc", 10, true, "abc"), "valid list with extras");
    }

    @Test
    public void testTupleArrayWithMinMaxValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newSimpleSchema(new JSONSchemaArrayType().items(
                JSONSchemas.newStringSchema(), JSONSchemas.newIntegerSchema()
        ).allowAdditionalItems().minimumItems(4).maximumItems(7));

        validateNoErrors(schema, Lists.newArrayList("abc", 10, 3, 4), "valid minimum");
        validateNoErrors(schema, Lists.newArrayList("abc", 10, 3, 4, 5, 6), "valid middle");
        validateNoErrors(schema, Lists.newArrayList("abc", 10, 3, 4, 5, 6, 7), "valid maximum");

        validateWithResults(schema, Lists.newArrayList("abc", 10, 3), "too small", ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, Lists.newArrayList("abc", 10, 3, 4, 5, 6, 7, 8), "too big", ResultType.CONSTRAINT_VIOLATION);
    }

    @Test
    public void testTupleArrayWithDuplicateValidation() throws Exception {
        JSONSchema schema = JSONSchemas.newSimpleSchema(new JSONSchemaArrayType().items(
                JSONSchemas.newStringSchema(), JSONSchemas.newStringSchema()
        ).allowAdditionalItems().disallowDuplicateItems());

        validateNoErrors(schema, Lists.newArrayList("abc", "def", 3, 4), "valid list");

        validateWithResults(schema, Lists.newArrayList("abc", "abc", 3, 4), "duplicate inside tuple", ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, Lists.newArrayList("abc", "def", 3, 3), "duplicate outside tuple", ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, Lists.newArrayList("abc", "def", "ghi", "abc"), "duplicate inside/outside tuple", ResultType.CONSTRAINT_VIOLATION);
    }

    @Test
    public void testTupleArrayWithSpecificExtras() throws Exception {
        JSONSchema schema = JSONSchemas.newSimpleSchema(new JSONSchemaArrayType().items(
                JSONSchemas.newStringSchema(), JSONSchemas.newIntegerSchema(), JSONSchemas.newBooleanSchema()
        ).additionalItemsSchema(JSONSchemas.newNumberSchema()));

        validateNoErrors(schema, new JSONArray().put(0, "abc").put(1, 10).put(2, true), "valid json array");
        validateNoErrors(schema, new JSONArray().put(0, "abc").put(1, 10).put(2, true).put(3, 10), "valid json array with extras");

        validateNoErrors(schema, Lists.newArrayList("abc", 10, true), "valid list");
        validateNoErrors(schema, Lists.newArrayList("abc", 10, true, 10), "valid list with extras");

        validateWithResults(schema, new JSONArray().put(0, "abc").put(1, 10).put(2, true).put(3, "abc"), "invalid json array with extras", ResultType.INCOMPATIBLE_TYPE);
        validateWithResults(schema, Lists.newArrayList("abc", 10, true, "abc"), "invalid list with extras", ResultType.INCOMPATIBLE_TYPE);
    }

    @Test
    public void testUnionWithObjects() throws Exception {
        JSONSchema schema = JSONSchemas.newUnionSchema(
                JSONSchemas.newStrictObjectSchema(new JSONSchemaPatternProperty().pattern("sProp.*").valueSchema(JSONSchemas.newStringSchema())).id("StringSchema"),
                JSONSchemas.newStrictObjectSchema(new JSONSchemaPatternProperty().pattern("nProp.*").valueSchema(JSONSchemas.newNumberSchema())).id("NumberSchema")
        );

        validateNoErrors(schema, new JSONObject().put("$schema", "StringSchema").put("sProp1", "abc").put("sProp2", "def"), "valid string object with $schema");
        validateNoErrors(schema, new JSONObject().put("$schema", "NumberSchema").put("nProp1", 123).put("nProp2", 456), "valid number object with $schema");

        validateWithResults(schema, new JSONObject().put("sProp1", "abc").put("sProp2", "def"), "valid string object without $schema", ResultType.MISSING_SCHEMA);
        validateWithResults(schema, new JSONObject().put("nProp1", 123).put("nProp2", 456), "valid number object without $schema", ResultType.MISSING_SCHEMA);

        validateWithResults(schema, new JSONObject().put("$schema", "StringSchema").put("sProp1", "abc").put("sProp2", 456), "invalid string object with $schema", ResultType.INCOMPATIBLE_TYPE);
        validateWithResults(schema, new JSONObject().put("$schema", "NumberSchema").put("nProp1", 123).put("nProp2", "def"), "invalid number object with $schema", ResultType.INCOMPATIBLE_TYPE);
        validateWithResults(schema, new JSONObject().put("$schema", "OtherSchema").put("sProp1", "abc").put("sProp2", 456), "invalid object with unknown $schema", ResultType.CONSTRAINT_VIOLATION);
    }

    @Test
    public void testUnionWithSimpleTypes() throws Exception {
        JSONSchema schema = JSONSchemas.newStrictObjectSchema(new JSONSchemaPatternProperty().pattern("uProp.*").valueSchema(JSONSchemas.newUnionSchema(
                JSONSchemas.newSimpleSchema(new JSONSchemaStringType().enumValues("A", "B", "C")),
                JSONSchemas.newSimpleSchema(new JSONSchemaStringType().minimumLength(10).maximumLength(20)),
                JSONSchemas.newSimpleSchema(new JSONSchemaStringType().minimumLength(6).maximumLength(7)),
                JSONSchemas.newSimpleSchema(new JSONSchemaStringType().pattern("http|https")),
                JSONSchemas.newSimpleSchema(new JSONSchemaIntegerType().enumValues(1, 2, 3)),
                JSONSchemas.newSimpleSchema(new JSONSchemaIntegerType().minimum(20).maximum(50)),
                JSONSchemas.newSimpleSchema(new JSONSchemaNumberType().minimum(100).maximum(200))
        )));

        validateNoErrors(schema,
                new JSONObject().
                        put("uProp1s", "B").
                        put("uProp2s", "http").
                        put("uProp3s", "https").
                        put("uProp4s", "abcdef").
                        put("uProp5s", "abcdefghijklmnop").
                        put("uProp6i", 2).
                        put("uProp7i", 25).
                        put("uProp8n", 175.51),
                "valid object");

        // invalid strings
        validateWithResults(schema, new JSONObject().put("uProp1s", "D"), "invalid small string", ResultType.CONSTRAINT_VIOLATION, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH,
                ResultType.FORMAT_MISMATCH, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE);
        validateWithResults(schema, new JSONObject().put("uProp2s", "abcdefgh"), "invalid medium string", ResultType.CONSTRAINT_VIOLATION, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH,
                ResultType.FORMAT_MISMATCH, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE);
        validateWithResults(schema, new JSONObject().put("uProp3s", "abcdefghijklmnopqrstuvwxyz"), "invalid long string", ResultType.CONSTRAINT_VIOLATION, ResultType.FORMAT_MISMATCH, ResultType.FORMAT_MISMATCH,
                ResultType.FORMAT_MISMATCH, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE);

        // invalid integers
        validateWithResults(schema, new JSONObject().put("uProp4i", 0), "invalid low integer", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE,
                ResultType.INCOMPATIBLE_TYPE, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, new JSONObject().put("uProp5i", 15), "invalid low-mid integer", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE,
                ResultType.INCOMPATIBLE_TYPE, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, new JSONObject().put("uProp6i", 55), "invalid high-mid integer", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE,
                ResultType.INCOMPATIBLE_TYPE, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, new JSONObject().put("uProp7i", 300), "invalid high integer", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE,
                ResultType.INCOMPATIBLE_TYPE, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION, ResultType.CONSTRAINT_VIOLATION);

        // invalid floating-point numbers
        validateWithResults(schema, new JSONObject().put("uProp8n", 25.5), "invalid mid-mid number", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE,
                ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, new JSONObject().put("uProp9n", 60.5), "invalid high-mid number", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE,
                ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.CONSTRAINT_VIOLATION);
        validateWithResults(schema, new JSONObject().put("uProp10n", 375.51), "invalid high number", ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE,
                ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.INCOMPATIBLE_TYPE, ResultType.CONSTRAINT_VIOLATION);
    }

    private void validateNoErrors(JSONSchema schema, Object obj, String test) throws Exception {
        validateResults(schema, obj, test, 0);
    }

    private void validateWithResults(JSONSchema schema, Object obj, String test, ResultType... resultTypes) throws Exception {
        ValidationResults validationResults = validateResults(schema, obj, test, resultTypes.length);

        for (int index = 0; index < resultTypes.length; index++) {
            ResultType resultType = resultTypes[index];
            ValidationResult validationResult = validationResults.getResults().get(index);
            Assert.assertEquals(validationResult.getType(), resultType, test + " -- incorrect result type: " + _validationResultMarshaller.toJSONObject(validationResult).toString(2));
        }
    }

    private ValidationResults validateResults(JSONSchema schema, Object obj, String test, int expectedResultCount) throws Exception {
        return validateResults(schema, obj, test, expectedResultCount, expectedResultCount == 0 ? "got validation errors" : "incorrect validation errors");
    }

    private ValidationResults validateResults(JSONSchema schema, Object obj, String test, int expectedResultCount, String message) throws Exception {
        ValidationResults validationResults = validateResults(schema, obj);
        Assert.assertEquals(validationResults.getResults().size(), expectedResultCount, test + " -- " + message + ": " + _validationResultsMarshaller.toJSONObject(validationResults).toString(2));
        return validationResults;
    }

    private ValidationResults validateResults(JSONSchema schema, Object obj) {
        ValidationResults validationResults = new ValidationResults();
        schema.validate(obj, "obj", validationResults);
        return validationResults;
    }
}
