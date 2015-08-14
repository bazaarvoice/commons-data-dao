package com.bazaarvoice.commons.data.dao.json;

import com.bazaarvoice.commons.data.model.AbstractModel;

public abstract class AbstractModelJSONMarshaller<C extends AbstractModel<C>> extends AbstractMappingJSONMarshaller<C> {

    @Override
    protected String getKey(C object) {
        return object.getID();
    }

    @Override
    protected C setKey(C object, String key) {
        return object.id(key);
    }

    protected String getIDField() {
        return "id";
    }

}
