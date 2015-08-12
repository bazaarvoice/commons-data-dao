package com.bazaarvoice.commons.data.dao.json;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Map;

public class EnumDelegateItemClassToTypeConverter<E extends Enum & DelegateItemType<?>> implements DelegateItemClassToTypeConverter<E> {
    private final Class<E> _typeEnumClass;
    private final Map<Class<?>, E> _typesByClass;

    public static <E extends Enum & DelegateItemType<?>> EnumDelegateItemClassToTypeConverter<E> createConverter(Class<E> typeEnumClass) {
        return new EnumDelegateItemClassToTypeConverter<E>(typeEnumClass);
    }

    public EnumDelegateItemClassToTypeConverter(Class<E> typeEnumClass) {
        _typeEnumClass = typeEnumClass;
        _typesByClass = Maps.newHashMap();

        try {
            Method valuesMethod = typeEnumClass.getMethod("values");
            if (valuesMethod == null) {
                throw new IllegalArgumentException("Not an enum class: " + typeEnumClass);
            }

            //noinspection unchecked
            E[] values = (E[]) valuesMethod.invoke(null);
            for (E value : values) {
                _typesByClass.put(value.getDelegateItemClass(), value);
            }
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public E apply(Class<?> delegateItemClass) {
        E type = _typesByClass.get(delegateItemClass);
        if (type == null) {
            throw new IllegalArgumentException("Delegate item type not found for class: " + delegateItemClass);
        }
        return type;
    }

    @Override
    public Class<E> getTypeEnumClass() {
        return _typeEnumClass;
    }
}
