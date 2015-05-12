package com.bazaarvoice.commons.data.dao.json.schema;

import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaNamedProperty;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaPatternProperty;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemaTypes;
import com.bazaarvoice.commons.data.model.json.schema.JSONSchemas;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaArrayType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaBooleanType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaIntegerType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaNumberType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaObjectType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaStringType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaTextFormat;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaUnionType;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.Test;

import java.nio.charset.Charset;

public class JSONSchemaMarshallerTest {
    private JSONSchemaMarshaller _jsonSchemaMarshaller = new JSONSchemaMarshaller(new JSONSchemaMarshaller.SchemaJSONRetriever() {
        @Override
        public JSONObject retrieveSchemaJSON(String schemaID)
                throws Exception {
            return new JSONObject(Resources.toString(getClass().getResource("data/" + schemaID), Charset.defaultCharset()));
        }
    });

    @Test
    public void testBasicJSONSchema() throws Throwable {
        String id = "basic.jsonschema";
        JSONSchema schema = JSONSchemas.newObjectSchema(
                new JSONSchemaNamedProperty().name("sProp1").valueSchema(JSONSchemas.newStringSchema().title("Required String").description("String that is required").required()),
                new JSONSchemaNamedProperty().name("sProp2").valueSchema(JSONSchemas.newStringSchema().title("Optional String")),
                new JSONSchemaNamedProperty().name("nProp1").valueSchema(JSONSchemas.newNumberSchema().title("Required Number").required()),
                new JSONSchemaNamedProperty().name("nProp2").valueSchema(JSONSchemas.newNumberSchema().title("Optional Number")),
                new JSONSchemaNamedProperty().name("iProp1").valueSchema(JSONSchemas.newIntegerSchema().title("Required Integer").required()),
                new JSONSchemaNamedProperty().name("iProp2").valueSchema(JSONSchemas.newIntegerSchema().title("Optional Integer")),
                new JSONSchemaNamedProperty().name("bProp1").valueSchema(JSONSchemas.newBooleanSchema().title("Required Boolean").required()),
                new JSONSchemaNamedProperty().name("bProp2").valueSchema(JSONSchemas.newBooleanSchema().title("Optional Boolean")),
                new JSONSchemaNamedProperty().name("oProp1").valueSchema(JSONSchemas.newObjectSchema(new JSONSchemaNamedProperty().name("sSubProp1").valueSchema(JSONSchemas.newStringSchema())).title("Required Object").required()),
                new JSONSchemaNamedProperty().name("oProp2").valueSchema(JSONSchemas.newObjectSchema(new JSONSchemaNamedProperty().name("sSubProp2").valueSchema(JSONSchemas.newStringSchema())).title("Optional Object")),
                new JSONSchemaNamedProperty().name("arrayProp1").valueSchema(JSONSchemas.newObjectArraySchema(new JSONSchemaNamedProperty().name("sSubProp1").valueSchema(JSONSchemas.newStringSchema())).title("Required Array").required()),
                new JSONSchemaNamedProperty().name("arrayProp2").valueSchema(JSONSchemas.newObjectArraySchema(new JSONSchemaNamedProperty().name("sSubProp2").valueSchema(JSONSchemas.newStringSchema())).title("Optional Array")),
                new JSONSchemaNamedProperty().name("anyProp1").valueSchema(JSONSchemas.newSimpleSchema(JSONSchemaTypes.ANY).title("Required Any").required()),
                new JSONSchemaNamedProperty().name("anyProp2").valueSchema(JSONSchemas.newSimpleSchema(JSONSchemaTypes.ANY).title("Optional Any")),
                new JSONSchemaNamedProperty().name("nullProp1").valueSchema(JSONSchemas.newSimpleSchema(JSONSchemaTypes.NULL).title("Required Null").required()),
                new JSONSchemaNamedProperty().name("nullProp2").valueSchema(JSONSchemas.newSimpleSchema(JSONSchemaTypes.NULL).title("Optional Null")),
                new JSONSchemaNamedProperty().name("nullableProp").valueSchema(new JSONSchema().title("Nullable").nullable())
        ).id(id);

        compareSchemaWithResource(schema, "data/" + id);
    }

    @Test
    public void testUnionJSONSchema() throws Throwable {
        String id = "union.jsonschema";
        JSONSchema schema = JSONSchemas.newObjectSchema(
                new JSONSchemaNamedProperty().name("snProp").valueSchema(new JSONSchema().types(new JSONSchemaStringType(), new JSONSchemaNumberType()).nullable().required()),
                new JSONSchemaNamedProperty().name("allProp").valueSchema(new JSONSchema().types(
                        new JSONSchemaStringType(),
                        new JSONSchemaNumberType(),
                        new JSONSchemaIntegerType(),
                        new JSONSchemaBooleanType(),
                        new JSONSchemaObjectType().namedProperties(new JSONSchemaNamedProperty().name("sSubProp").valueSchema(JSONSchemas.newStringSchema())),
                        new JSONSchemaArrayType().item(new JSONSchema().types(new JSONSchemaStringType(), new JSONSchemaNumberType())),
                        JSONSchemaTypes.NULL,
                        JSONSchemaTypes.ANY
                ).required()),
                new JSONSchemaNamedProperty().name("complexProp").valueSchema(new JSONSchema().types(
                        new JSONSchemaStringType().enumValues("a", "b", "c"),
                        new JSONSchemaNumberType().enumValues(1, 2, 3),
                        new JSONSchemaUnionType().schemas(
                                JSONSchemas.newStrictObjectSchema(new JSONSchemaNamedProperty().name("sSubProp").valueSchema(JSONSchemas.newStringSchema())).id("sSubSchema"),
                                JSONSchemas.newStrictObjectSchema(new JSONSchemaNamedProperty().name("nSubProp").valueSchema(JSONSchemas.newNumberSchema())).id("nSubSchema")
                        )
                ).required())
        ).id(id);

        compareSchemaWithResource(schema, "data/" + id);
    }

    @Test
    public void testReferencesJSONSchema() throws Throwable {
        String id = "references.jsonschema";
        JSONSchema schema = JSONSchemas.newObjectSchema(
                new JSONSchemaNamedProperty().name("oPropToRef").valueSchema(
                        JSONSchemas.newObjectSchema(
                                new JSONSchemaNamedProperty().name("oPropRef1").valueSchema(new JSONSchema().referencesSchemaID("schemaToBeReferenced"))
                        ).id("schemaToBeReferenced")
                ),
                new JSONSchemaNamedProperty().name("oPropRef2").valueSchema(new JSONSchema().referencesSchemaID("basic.jsonschema")),
                new JSONSchemaNamedProperty().name("oPropRef3").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaObjectType().additionalPropertiesSchema(new JSONSchema().referencesSchemaID("strings.jsonschema")))),
                new JSONSchemaNamedProperty().name("oPropExtend1").valueSchema(
                        JSONSchemas.newObjectSchema(
                                new JSONSchemaNamedProperty().name("sExtended1").valueSchema(JSONSchemas.newStringSchema())
                        ).extendsSchemaID("strings.jsonschema")
                ),
                new JSONSchemaNamedProperty().name("oPropExtend2").valueSchema(JSONSchemas.newSimpleSchema(new JSONSchemaObjectType().additionalPropertiesSchema(new JSONSchema().extendsSchemaID("basic.jsonschema"))))
        ).id(id);

        compareSchemaWithResource(schema, "data/" + id);
    }

    @Test
    public void testJSONSchemaBooleanType() throws Throwable {
        String id = "booleans.jsonschema";
        JSONSchema schema = JSONSchemas.newObjectSchema(
                new JSONSchemaNamedProperty().name("bProp").valueSchema(new JSONSchema().types(new JSONSchemaBooleanType())),
                new JSONSchemaNamedProperty().name("bPropDefaultTrue").valueSchema(new JSONSchema().types(new JSONSchemaBooleanType().defaultValue(true))),
                new JSONSchemaNamedProperty().name("bPropDefaultFalse").valueSchema(new JSONSchema().types(new JSONSchemaBooleanType().defaultValue(false)))
        ).id(id);

        compareSchemaWithResource(schema, "data/" + id);
    }

    @Test
    public void testJSONSchemaStringType() throws Throwable {
        String id = "strings.jsonschema";
        JSONSchema schema = JSONSchemas.newObjectSchema(
                new JSONSchemaNamedProperty().name("sProp").valueSchema(new JSONSchema().types(new JSONSchemaStringType())),
                new JSONSchemaNamedProperty().name("sPropDefault").valueSchema(new JSONSchema().types(new JSONSchemaStringType().defaultValue("theDefault"))),
                new JSONSchemaNamedProperty().name("sPropMin").valueSchema(new JSONSchema().types(new JSONSchemaStringType().minimumLength(10))),
                new JSONSchemaNamedProperty().name("sPropMax").valueSchema(new JSONSchema().types(new JSONSchemaStringType().maximumLength(100))),
                new JSONSchemaNamedProperty().name("sPropPattern").valueSchema(new JSONSchema().types(new JSONSchemaStringType().pattern("[0-9]+"))),
                new JSONSchemaNamedProperty().name("sPropEnum1").valueSchema(new JSONSchema().types(new JSONSchemaStringType().enumValues("single"))),
                new JSONSchemaNamedProperty().name("sPropEnum2").valueSchema(new JSONSchema().types(new JSONSchemaStringType().enumValues("one", "two", "three"))),
                new JSONSchemaNamedProperty().name("sPropFormatCSSColor").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.CSS_COLOR))),
                new JSONSchemaNamedProperty().name("sPropFormatCSSStyle").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.CSS_STYLE))),
                new JSONSchemaNamedProperty().name("sPropFormatDate").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.DATE))),
                new JSONSchemaNamedProperty().name("sPropFormatDateTime").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.DATE_TIME))),
                new JSONSchemaNamedProperty().name("sPropFormatTime").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.TIME))),
                new JSONSchemaNamedProperty().name("sPropFormatEmail").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.EMAIL_ADDRESS))),
                new JSONSchemaNamedProperty().name("sPropFormatHostname").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.HOSTNAME))),
                new JSONSchemaNamedProperty().name("sPropFormatPhone").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.PHONE_NUMBER))),
                new JSONSchemaNamedProperty().name("sPropFormatIPV4").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.IPV4_ADDRESS))),
                new JSONSchemaNamedProperty().name("sPropFormatIPV6").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.IPV6_ADDRESS))),
                new JSONSchemaNamedProperty().name("sPropFormatRegEx").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.REGEX))),
                new JSONSchemaNamedProperty().name("sPropFormatURI").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.URI))),
                new JSONSchemaNamedProperty().name("sPropFormatUTCMillis").valueSchema(new JSONSchema().types(new JSONSchemaStringType().format(JSONSchemaTextFormat.UTC_MILLISECONDS))),
                new JSONSchemaNamedProperty().name("sPropFormatCustom").valueSchema(new JSONSchema().types(new JSONSchemaStringType().customFormat("http://dev.bv.com/myschema.jsonschema"))),
                new JSONSchemaNamedProperty().name("sPropMulti").valueSchema(new JSONSchema().types(new JSONSchemaStringType().minimumLength(5).maximumLength(50).defaultValue("aDefault")))
        ).id(id);

        compareSchemaWithResource(schema, "data/" + id);
    }

    @Test
    public void testJSONSchemaNumberTypes() throws Throwable {
        String id = "numbers.jsonschema";
        JSONSchema schema = JSONSchemas.newObjectSchema(
                new JSONSchemaNamedProperty().name("nProp").valueSchema(new JSONSchema().types(new JSONSchemaNumberType())),
                new JSONSchemaNamedProperty().name("nPropDefault").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().defaultValue(101.5))),
                new JSONSchemaNamedProperty().name("nPropMin").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().minimum(1.5))),
                new JSONSchemaNamedProperty().name("nPropMax").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().maximum(150.5))),
                new JSONSchemaNamedProperty().name("nPropMinInclusive").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().minimum(1.6).minimumInclusive())),
                new JSONSchemaNamedProperty().name("nPropMinExclusive").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().minimum(1.7).minimumExclusive())),
                new JSONSchemaNamedProperty().name("nPropMaxInclusive").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().maximum(150.6).maximumInclusive())),
                new JSONSchemaNamedProperty().name("nPropMaxExclusive").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().maximum(150.7).maximumExclusive())),
                new JSONSchemaNamedProperty().name("nPropDivisibleBy").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().divisibleBy(2.5))),
                new JSONSchemaNamedProperty().name("nPropEnums").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().enumValues(1.1, 1.2, 1.3, 1.4))),
                new JSONSchemaNamedProperty().name("nPropMulti").valueSchema(new JSONSchema().types(new JSONSchemaNumberType().defaultValue(102.5).minimum(2.5).minimumInclusive().maximum(200.5).maximumExclusive().divisibleBy(2.5))),
                new JSONSchemaNamedProperty().name("iProp").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType())),
                new JSONSchemaNamedProperty().name("iPropDefault").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().defaultValue(42))),
                new JSONSchemaNamedProperty().name("iPropMin").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().minimum(20))),
                new JSONSchemaNamedProperty().name("iPropMax").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().maximum(200))),
                new JSONSchemaNamedProperty().name("iPropMinInclusive").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().minimum(21).minimumInclusive())),
                new JSONSchemaNamedProperty().name("iPropMinExclusive").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().minimum(22).minimumExclusive())),
                new JSONSchemaNamedProperty().name("iPropMaxInclusive").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().maximum(201).maximumInclusive())),
                new JSONSchemaNamedProperty().name("iPropMaxExclusive").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().maximum(202).maximumExclusive())),
                new JSONSchemaNamedProperty().name("iPropDivisibleBy").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().divisibleBy(10))),
                new JSONSchemaNamedProperty().name("iPropEnums").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().enumValues(1, 2, 3, 5, 8, 13, 21))),
                new JSONSchemaNamedProperty().name("iPropMulti").valueSchema(new JSONSchema().types(new JSONSchemaIntegerType().defaultValue(101).minimum(10).minimumInclusive().maximum(1000).maximumExclusive().divisibleBy(5)))
        ).id(id);

        compareSchemaWithResource(schema, "data/" + id);
    }

    @Test
    public void testJSONSchemaObjectType() throws Throwable {
        String id = "objects.jsonschema";
        JSONSchema schema = JSONSchemas.newObjectSchema(
                new JSONSchemaNamedProperty().name("oProp").valueSchema(new JSONSchema().types(new JSONSchemaObjectType())),
                new JSONSchemaNamedProperty().name("oPropProperties").valueSchema(new JSONSchema().types(new JSONSchemaObjectType().
                        namedProperties(
                                new JSONSchemaNamedProperty().name("sSubProp").valueSchema(JSONSchemas.newStringSchema()),
                                new JSONSchemaNamedProperty().name("nSubProp").valueSchema(JSONSchemas.newNumberSchema())
                        )
                )),
                new JSONSchemaNamedProperty().name("oPropPatternProperties").valueSchema(new JSONSchema().types(new JSONSchemaObjectType().
                        patternProperties(
                                new JSONSchemaPatternProperty().pattern("s.+").valueSchema(JSONSchemas.newStringSchema()),
                                new JSONSchemaPatternProperty().pattern("n.+").valueSchema(JSONSchemas.newNumberSchema())
                        )
                )),
                new JSONSchemaNamedProperty().name("oPropAdditionalProperties").valueSchema(new JSONSchema().types(new JSONSchemaObjectType().
                        additionalPropertiesSchema(JSONSchemas.newStringSchema())
                )),
                new JSONSchemaNamedProperty().name("oPropNoAdditionalProperties").valueSchema(new JSONSchema().types(new JSONSchemaObjectType().
                        additionalPropertiesSchema(null)
                )),
                new JSONSchemaNamedProperty().name("oPropMultiProperties").valueSchema(new JSONSchema().types(new JSONSchemaObjectType().
                        namedProperties(
                                new JSONSchemaNamedProperty().name("sSubProp").valueSchema(JSONSchemas.newStringSchema()),
                                new JSONSchemaNamedProperty().name("nSubProp").valueSchema(JSONSchemas.newNumberSchema())
                        ).
                        patternProperties(
                                new JSONSchemaPatternProperty().pattern("s.+").valueSchema(JSONSchemas.newStringSchema()),
                                new JSONSchemaPatternProperty().pattern("n.+").valueSchema(JSONSchemas.newNumberSchema())
                        ).
                        additionalPropertiesSchema(JSONSchemas.newStringSchema())
                ))
        ).id(id);

        compareSchemaWithResource(schema, "data/" + id);
    }

    @Test
    public void testJSONSchemaArraysType() throws Throwable {
        String id = "arrays.jsonschema";
        JSONSchema schema = JSONSchemas.newObjectSchema(
                new JSONSchemaNamedProperty().name("aProp").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().item(JSONSchemas.newAnySchema()))),
                new JSONSchemaNamedProperty().name("aPropStrings").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        item(JSONSchemas.newStringSchema())
                )),
                new JSONSchemaNamedProperty().name("aPropStringsMinMax").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        item(JSONSchemas.newStringSchema()).minimumItems(4).maximumItems(8)
                )),
                new JSONSchemaNamedProperty().name("aPropStringsNoDupes").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        item(JSONSchemas.newStringSchema()).disallowDuplicateItems()
                )),
                new JSONSchemaNamedProperty().name("aPropUnion").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        item(new JSONSchema().types(new JSONSchemaStringType(), new JSONSchemaNumberType()).nullable())
                )),
                new JSONSchemaNamedProperty().name("aProp2Tuple").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        items(JSONSchemas.newStringSchema(), JSONSchemas.newNumberSchema()).additionalItemsSchema(new JSONSchema())
                )),
                new JSONSchemaNamedProperty().name("aProp3Tuple").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        items(JSONSchemas.newStringSchema(), JSONSchemas.newNumberSchema(), JSONSchemas.newBooleanSchema())
                )),
                new JSONSchemaNamedProperty().name("aProp2TupleMinMax").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        items(JSONSchemas.newStringSchema(), JSONSchemas.newNumberSchema()).additionalItemsSchema(new JSONSchema()).minimumItems(5).maximumItems(11)
                )),
                new JSONSchemaNamedProperty().name("aProp2TupleNoDupes").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        items(JSONSchemas.newStringSchema(), JSONSchemas.newNumberSchema()).additionalItemsSchema(new JSONSchema()).disallowDuplicateItems()
                )),
                new JSONSchemaNamedProperty().name("aPropAdditionalItems").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        items(JSONSchemas.newStringSchema(), JSONSchemas.newNumberSchema()).
                        additionalItemsSchema(JSONSchemas.newStringSchema())
                )),
                new JSONSchemaNamedProperty().name("aPropObjects").valueSchema(new JSONSchema().types(new JSONSchemaArrayType().
                        item(JSONSchemas.newObjectSchema(
                                new JSONSchemaNamedProperty().name("sSubProp").valueSchema(JSONSchemas.newStringSchema()),
                                new JSONSchemaNamedProperty().name("nSubProp").valueSchema(JSONSchemas.newNumberSchema())
                        ))
                ))
        ).id(id);

        compareSchemaWithResource(schema, "data/" + id);
    }

    @Test
    public void testJSONSchemaComplexType() throws Throwable {
        String id = "complex.jsonschema";
        JSONSchema schema = new JSONSchema().types(
                new JSONSchemaObjectType().
                        namedProperties(
                                new JSONSchemaNamedProperty().name("sProp").valueSchema(JSONSchemas.newStringSchema()),
                                new JSONSchemaNamedProperty().name("nProp").valueSchema(JSONSchemas.newNumberSchema())
                        ),
                new JSONSchemaStringType().minimumLength(1).maximumLength(20).enumValues("abc", "def", "ghi", "jkl"),
                new JSONSchemaIntegerType().minimum(5).maximum(100).maximumExclusive().enumValues(5, 10, 15, 20),
                new JSONSchemaArrayType().item(new JSONSchema().types(
                        new JSONSchemaStringType().pattern("ack.*bar"),
                        new JSONSchemaNumberType().minimum(2.5).maximum(1000)
                ))
        ).id(id);

        compareSchemaWithResource(schema, "data/" + id);
    }

    private void compareSchemaWithResource(JSONSchema schema, String resourceLocation) throws Exception {
        String resourceJson = IOUtils.toString(getClass().getResourceAsStream(resourceLocation));

        // To JSON
        JSONObject schemaJSON = _jsonSchemaMarshaller.toJSONObject(schema);
        JSONAssert.assertEquals(resourceJson, schemaJSON, JSONCompareMode.STRICT);


        // fromJSON -> toJSON
        JSONSchema schema2 = _jsonSchemaMarshaller.fromJSONObject(schemaJSON);
        JSONObject schemaJson2 = _jsonSchemaMarshaller.toJSONObject(schema2);
        JSONAssert.assertEquals(resourceJson, schemaJson2, JSONCompareMode.LENIENT);
    }
}
