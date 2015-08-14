package com.bazaarvoice.commons.data.dao.mongo;

public abstract class AbstractMongoDataMarshaller<T> implements MongoDataMarshaller<T> {
    /**
     * Returns the field name for the sort field associated with the given field.  Mostly used for enumerations.
     */
    protected String getSortFieldName(String field) {
        return field + "Sort";
    }

    /**
     * Returns the value used to sort the given enumeration.  Uses {@link Enum#ordinal()}.
     */
    public Integer getSortValue(Enum<?> e) {
        return e != null ? e.ordinal() : null;
    }
}
