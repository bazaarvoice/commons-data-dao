package com.bazaarvoice.commons.data.dao.mongo;

import com.bazaarvoice.commons.data.dao.Criteria;
import com.bazaarvoice.commons.data.dao.mongo.dbo.QueryMongoDBObject;

public interface MongoCriteria<T, C extends Criteria<T, C>> extends Criteria<T, C> {
    QueryMongoDBObject toDBObject();
}
