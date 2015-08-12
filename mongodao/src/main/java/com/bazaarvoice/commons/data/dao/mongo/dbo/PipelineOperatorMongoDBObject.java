package com.bazaarvoice.commons.data.dao.mongo.dbo;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Used to query MongoDB via the Aggregate Pipeline in a fluent style.
 *
 * <p/>
 * <pre>
 * {@code
 * List<DBObject> pipeline = pipelineOf(
 *      $match(matchDbObject),
 *      $unwind("$fieldToUnwind"),
 *      $group(new BasicDBObject().
 *                  append(ID_FIELD, "$" + ID_FIELD).
 *                  append("compositeGroupingField", "$fieldFromMatch"),
 *             $last("lastFieldToUnwindValue", "$fieldToUnwind")
 *      ),
 *      $group("$" + ID_FIELD + ".compositeGroupingField",
 *          $sum("count", 1),
 *          $sum("lastFieldToUnwindSum", "$lastFieldToUnwindValue")
 *      ),
 *      $project(
 *          exclude(ID_FIELD),
 *          alias("field1", "$" + ID_FIELD),
 *          alias("fieldSum", "$lastFieldToUnwindSum')
 *      )
 * );
 *
 * Iterable<DBObject> results = getDBCollection().aggregate(pipeline).results();
 *
 * }
 * </pre>
 */
public class PipelineOperatorMongoDBObject extends MongoDBObject<PipelineOperatorMongoDBObject> {
    public static PipelineOperatorMongoDBObject $match(DBObject dbObject) {
        return new PipelineOperatorMongoDBObject().append("$match", dbObject);
    }

    public static PipelineOperatorMongoDBObject $unwind(String key) {
        return new PipelineOperatorMongoDBObject().append("$unwind", key);
    }

    public static PipelineOperatorMongoDBObject $group(String idField, ComputedAccumulator... computedFields) {
        return $group(idField, Arrays.asList(computedFields));
    }

    public static PipelineOperatorMongoDBObject $group(String idField, Collection<ComputedAccumulator> computedFields) {
        BasicDBObject group = new BasicDBObject().append(ID_FIELD, idField);
        for (ComputedAccumulator computedField : computedFields) {
            group.putAll((Map) computedField);
        }
        return new PipelineOperatorMongoDBObject().append("$group", group);
    }

    public static PipelineOperatorMongoDBObject $group(DBObject compoundId, ComputedAccumulator... computedFields) {
        return $group(compoundId, Arrays.asList(computedFields));
    }

    public static PipelineOperatorMongoDBObject $group(DBObject compoundId, Collection<ComputedAccumulator> computedFields) {
        BasicDBObject group = new BasicDBObject().append(ID_FIELD, compoundId);
        for (ComputedAccumulator computedField : computedFields) {
            group.putAll((Map) computedField);
        }
        return new PipelineOperatorMongoDBObject().append("$group", group);
    }

    public static PipelineOperatorMongoDBObject $project(ProjectedField... projectedFields) {
        return $project(Arrays.asList(projectedFields));
    }

    public static PipelineOperatorMongoDBObject $project(Collection<ProjectedField> projectedFields) {
        BasicDBObject project = new BasicDBObject();
        for (ProjectedField projectedField : projectedFields) {
            project.putAll((Map) projectedField);
        }
        return new PipelineOperatorMongoDBObject().append("$project", project);
    }

    public static List<DBObject> pipelineOf(PipelineOperatorMongoDBObject... operators) {
        return Lists.<DBObject>newArrayList(operators);
    }

    public static class ComputedAccumulator extends BasicDBObject {
        public static ComputedAccumulator $count(String computedFieldName, String fieldToGroup) {
            return newComputedField(computedFieldName, "$count", fieldToGroup);
        }

        public static ComputedAccumulator $addToSet(String computedFieldName, String fieldToGroup) {
            return newComputedField(computedFieldName, "$addToSet", fieldToGroup);
        }

        public static ComputedAccumulator $first(String computedFieldName, String fieldToGroup) {
            return newComputedField(computedFieldName, "$first", fieldToGroup);
        }

        public static ComputedAccumulator $last(String computedFieldName, String fieldToGroup) {
            return newComputedField(computedFieldName, "$last", fieldToGroup);
        }

        public static ComputedAccumulator $max(String computedFieldName, String fieldToGroup) {
            return newComputedField(computedFieldName, "$max", fieldToGroup);
        }

        public static ComputedAccumulator $min(String computedFieldName, String fieldToGroup) {
            return newComputedField(computedFieldName, "$min", fieldToGroup);
        }

        public static ComputedAccumulator $avg(String computedFieldName, String fieldToGroup) {
            return newComputedField(computedFieldName, "$avg", fieldToGroup);
        }

        public static ComputedAccumulator $push(String computedFieldName, String fieldToGroup) {
            return newComputedField(computedFieldName, "$push", fieldToGroup);
        }

        public static ComputedAccumulator $push(String computedFieldName, DBObject dbObject) {
            return newComputedField(computedFieldName, "$push", dbObject);
        }

        public static ComputedAccumulator $sum(String computedFieldName, String fieldToGroup) {
            return newComputedField(computedFieldName, "$sum", fieldToGroup);
        }

        public static ComputedAccumulator $sum(String computedFieldName, int literal) {
            return newComputedField(computedFieldName, "$sum", literal);
        }

        private static ComputedAccumulator newComputedField(String computedFieldName, String operator, Object toGroup) {
            return (ComputedAccumulator) new ComputedAccumulator().append(computedFieldName, new BasicDBObject().append(operator, toGroup));
        }
    }

    public static class ProjectedField extends BasicDBObject {
        public static final int INCLUDE_FIELD = 1;
        public static final int EXCLUDE_FIELD = 0;

        public static ProjectedField include(String fieldName) {
            return newProjectedField(fieldName, INCLUDE_FIELD);
        }

        public static ProjectedField exclude(String fieldName) {
            return newProjectedField(fieldName, EXCLUDE_FIELD);
        }

        public static ProjectedField alias(String computedFieldName, String fieldName) {
            return newProjectedField(computedFieldName, fieldName);
        }

        public static ProjectedField alias(String computedFieldName, DBObject fieldObject) {
            return newProjectedField(computedFieldName, fieldObject);
        }

        private static ProjectedField newProjectedField(String computedFieldName, Object toProject) {
            return (ProjectedField) new ProjectedField().append(computedFieldName, toProject);
        }
    }
}
