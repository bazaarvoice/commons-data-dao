package com.bazaarvoice.commons.data.dao.mongo.audit;

import com.bazaarvoice.commons.data.model.AuditAction;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Required;

import java.lang.reflect.Method;
import java.util.Map;

public class DefaultAuditActionClassToTypeConverter<T extends Enum & AuditActionType> implements AuditActionClassToTypeConverter<T> {
    private Class<T> _typeEnumClass;
    private Map<Class<? extends AuditAction>, T> _typesByClass;

    @Required
    public void setTypeEnumClass(Class<T> typeEnumClass) {
        _typeEnumClass = typeEnumClass;
        _typesByClass = Maps.newHashMap();
        try {
            Method valuesMethod = typeEnumClass.getMethod("values");
            if (valuesMethod == null) {
                throw new IllegalArgumentException("Not an enum class: " + typeEnumClass);
            }

            //noinspection unchecked
            T[] values = (T[]) valuesMethod.invoke(null);
            for (T value : values) {
                _typesByClass.put(value.getActionClass(), value);
            }
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public T apply(Class<? extends AuditAction> actionClass) {
        T type = _typesByClass.get(actionClass);
        if (type == null) {
            throw new IllegalArgumentException("Action type not found for class: " + actionClass);
        }
        return type;
    }

    @Override
    public Class<T> getTypeEnumClass() {
        return _typeEnumClass;
    }
}
