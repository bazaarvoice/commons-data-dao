package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.mongo.dbo.MongoDBObject;
import com.google.common.base.Function;
import com.mongodb.DBObject;

public class MongoDataMarshallerFunctions {
    public static <T> Function<T, DBObject> to(final MongoDataMarshaller<T> marshaller) {
        return new Function<T, DBObject>() {
            @Override
            public DBObject apply(T input) {
                return marshaller.toDBObject(input);
            }
        };
    }

    public static <T> Function<DBObject, T> from(final MongoDataMarshaller<T> marshaller) {
        return new Function<DBObject, T>() {
            @Override
            public T apply(DBObject input) {
                if (input instanceof MongoDBObject) {
                    return marshaller.fromDBObject((MongoDBObject)input);
                } else {
                    return marshaller.fromDBObject(new MongoDBObject(input));
                }
            }
        };
    }
}
