package com.bazaarvoice.commons.data.dao.json;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import java.util.Map;

public class GenericDelegatingJSONMarshaller<T> extends AbstractDelegatingJSONMarshaller<T, GenericDelegatingJSONMarshaller.GenericDelegateItemType> {
    private Map<String, GenericDelegateItemType> _delegateItemTypesByName = Maps.newHashMap();
    private Map<Class<?>, GenericDelegateItemType> _delegateItemTypesByClass = Maps.newHashMap();

    private String _typeField = "_type";

    public void setDelegateJSONMarshallers(Map<GenericDelegateItemType, JSONMarshaller<?>> delegateJSONMarshallers) {
        for (Map.Entry<GenericDelegateItemType, JSONMarshaller<?>> delegateJSONMarshallersEntry : delegateJSONMarshallers.entrySet()) {
            GenericDelegateItemType delegateItemType = delegateJSONMarshallersEntry.getKey();
            addDelegateMarshaller(delegateItemType, delegateJSONMarshallersEntry.getValue());
            _delegateItemTypesByName.put(delegateItemType.getName(), delegateItemType);
            _delegateItemTypesByClass.put(delegateItemType.getDelegateItemClass(), delegateItemType);
        }
    }

    @Override
    protected GenericDelegateItemType getDelegateItemType(Class<?> delegateItemClass) {
        return _delegateItemTypesByClass.get(delegateItemClass);
    }

    @Override
    protected String convertFromDelegateItemType(GenericDelegateItemType delegateItemType) {
        return delegateItemType.getName();
    }

    @Override
    protected GenericDelegateItemType convertToDelegateItemType(String delegateItemTypeValue) {
        return _delegateItemTypesByName.get(delegateItemTypeValue);
    }

    public void setTypeField(String typeField) {
        _typeField = typeField;
    }

    @Override
    protected String getTypeField() {
        return _typeField;
    }

    public static class GenericDelegateItemType implements DelegateItemType<Object> {
        private String _name;
        private Class<?> _delegateItemClass;

        public String getName() {
            return _name;
        }

        public void setName(String name) {
            _name = name;
        }

        public GenericDelegateItemType name(String name) {
            setName(name);
            return this;
        }

        @Override
        public Class<?> getDelegateItemClass() {
            return _delegateItemClass;
        }

        public void setDelegateItemClass(Class<?> delegateItemClass) {
            _delegateItemClass = delegateItemClass;
        }

        public GenericDelegateItemType delegateItemClass(Class<?> delegateItemClass) {
            setDelegateItemClass(delegateItemClass);
            return this;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).
                    add("_name", _name).
                    add("_delegateItemClass", _delegateItemClass).
                    toString();
        }
    }
}
