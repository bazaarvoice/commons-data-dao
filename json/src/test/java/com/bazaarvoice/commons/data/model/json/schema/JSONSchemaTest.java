package com.bazaarvoice.commons.data.model.json.schema;

import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaArrayType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaIntegerType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaNumberType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaObjectType;
import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaStringType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JSONSchemaTest {
    @Test
    public void testClone() {
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
        );

        JSONSchema clonedSchema = schema.clone();

        JSONSchemaObjectType clonedSchemaType = (JSONSchemaObjectType) clonedSchema.getType();
        Assert.assertNotNull(clonedSchemaType, "clonedSchema.type");

        JSONSchema sProp2Schema = clonedSchemaType.getNamedProperty("sProp2").getValueSchema();
        Assert.assertFalse(sProp2Schema.isRequired(), "clonedSchema.sProp2.isRequired");

        sProp2Schema.required();
        Assert.assertTrue(sProp2Schema.isRequired(), "clonedSchema.sProp2.isRequired after set");
        Assert.assertFalse(((JSONSchemaObjectType) schema.getType()).getNamedProperty("sProp2").getValueSchema().isRequired(), "schema.sProp2.isRequired after set");
    }

    @Test
    public void testMerge() {
        JSONSchema parentSchema = new JSONSchema().types(
                new JSONSchemaObjectType().
                        namedProperties(
                                new JSONSchemaNamedProperty().name("sProp1").valueSchema(JSONSchemas.newStringSchema().title("A string")),
                                new JSONSchemaNamedProperty().name("nProp2").valueSchema(JSONSchemas.newNumberSchema().required()),
                                new JSONSchemaNamedProperty().name("bProp3").valueSchema(JSONSchemas.newBooleanSchema().title("Other Boolean"))
                        ),
                new JSONSchemaStringType().minimumLength(1).maximumLength(20).enumValues("abc", "def", "ghi", "jkl"),
                new JSONSchemaIntegerType().minimum(5).maximum(100).maximumExclusive().enumValues(5, 10, 15, 20),
                new JSONSchemaArrayType().item(new JSONSchema().types(
                        new JSONSchemaStringType().pattern("ack.*bar"),
                        new JSONSchemaNumberType().minimum(2.5).maximum(1000)
                ))
        );

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
        ).extendsSchema(parentSchema);

        JSONSchema flattenedSchema = schema.clone().flatten();

        Assert.assertEquals(flattenedSchema.getTypes().size(), parentSchema.getTypes().size(), "flattenedSchema.size");

        JSONSchemaObjectType flattenedObjectType = flattenedSchema.getType(JSONSchemaObjectType.class);
        Assert.assertEquals(flattenedObjectType.getNamedProperties().size(), ((JSONSchemaObjectType) schema.getType()).getNamedProperties().size()+1, "flattenedObjectType.properties.size");

        Assert.assertTrue(flattenedObjectType.getNamedProperty("sProp1").getValueSchema().isRequired(), "sProp1.isRequired");
        Assert.assertEquals(flattenedObjectType.getNamedProperty("sProp1").getValueSchema().getTitle(), "Required String", "sProp1.title");
        Assert.assertTrue(flattenedObjectType.getNamedProperty("nProp2").getValueSchema().isRequired(), "nProp2.isRequired");
        Assert.assertEquals(flattenedObjectType.getNamedProperty("bProp3").getValueSchema().getTitle(), "Other Boolean", "bProp3.title");

    }
}
