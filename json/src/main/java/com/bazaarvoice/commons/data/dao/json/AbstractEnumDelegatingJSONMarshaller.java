package com.bazaarvoice.commons.data.dao.json;

public abstract class AbstractEnumDelegatingJSONMarshaller<T, E extends Enum & DelegateItemType<?>> extends AbstractDelegatingJSONMarshaller<T,E> {
    private final DelegateItemClassToTypeConverter<E> _delegateItemClassToTypeConverter = EnumDelegateItemClassToTypeConverter.createConverter(getDelegateItemTypeEnumClass());

    @Override
    protected E getDelegateItemType(Class<?> delegateItemClass)  {
        return _delegateItemClassToTypeConverter.apply(delegateItemClass);
    }

    protected final void addDelegateMarshaller(DelegateJSONMarshaller<?, E> marshaller) {
        for (final E type : marshaller.getSupportedDelegateItemTypes()) {
            addDelegateMarshaller(type, marshaller);
        }
    }

    protected abstract Class<E> getDelegateItemTypeEnumClass();

    @Override
    protected String convertFromDelegateItemType(E delegateItemType) {
        return convertFromEnum(delegateItemType);
    }

    @Override
    protected E convertToDelegateItemType(String delegateItemTypeValue) {
        return (E) convertToEnum(_delegateItemClassToTypeConverter.getTypeEnumClass(), delegateItemTypeValue);
    }

}
