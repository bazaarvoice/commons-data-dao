package com.bazaarvoice.commons.data.dao.mongo.dbo;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.BSONObject;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MongoDB {@link DBObject} with some extra functionality over {@link BasicDBObject}
 */
public class MongoDBObject<D extends MongoDBObject<D>> implements DBObject, Cloneable, Serializable {
    public static final String ID_FIELD = "_id";

    private final BasicDBObject _dbObject;

    /**
     * Creates an empty object.
     */
    public MongoDBObject() {
        this(new BasicDBObject());
    }

    /**
     * Creates an object with the given {@link BasicDBObject}.  This wraps the given DBObject so any changes to this object will affect the wrapped DBObject.
     */
    public MongoDBObject(BasicDBObject dbObject) {
        _dbObject = dbObject;
    }

    /**
     * Creates an object with the given {@link BSONObject}'s properties.  Only wraps the given BSONObject if it is a BasicDBObject.
     */
    public MongoDBObject(BSONObject bsonObject) {
        if (bsonObject instanceof BasicDBObject) {
            _dbObject = (BasicDBObject) bsonObject;
        } else {
            _dbObject = new BasicDBObject();
            _dbObject.putAll(bsonObject);
        }
    }

    public Object getID() {
        return get(ID_FIELD);
    }

    public String getIDString() {
        return getString(ID_FIELD);
    }

    public D append(String key, Object val) {
        put(key, val);

        //noinspection unchecked
        return (D) this;
    }

    public Object put(String key, Object val) {
        return _dbObject.put(key, convertIfEnum(val));
    }

    /**
     * If the given value is an enum, converts it to a string value.  If a collection, optionally converts the collection by calling {@link #convertIfEnums(Collection)}.
     */
    protected Object convertIfEnum(Object val) {
        if (val instanceof Enum) {
            val = ((Enum) val).name();
        } else if (val instanceof Collection) {
            val = convertIfEnums((Collection) val);
        }
        return val;
    }

    /**
     * Converts any enum values in the collection to strings.
     */
    protected Collection<Object> convertIfEnums(Collection<?> values) {
        return Collections2.transform(values, new Function<Object, Object>() {
            @Override
            public Object apply(Object input) {
                return convertIfEnum(input);
            }
        });
    }

    /**
     * Returns the property as an enumeration value.
     */
    public <E extends Enum<E>> E getEnum(Class<E> enumType, String propertyName) {
        String enumValue = getString(propertyName);
        if (enumValue == null) {
            return null;
        }

        return Enum.valueOf(enumType, enumValue);
    }

    /**
     * Returns the property as a date.
     */
    public Date getDate(String propertyName) {
        return (Date) get(propertyName);
    }

    /**
     * Returns the property as a MongoDBObject.
     * <p>
     * This requires setting the object class on the DBCollection: {@link com.mongodb.DBCollection#setObjectClass(Class)}.
     */
    public MongoDBObject getDBObject(String propertyName) {
        return DBObjectToMongoDBObjectFunction.Instance.apply((DBObject) get(propertyName));
    }

    /**
     * Returns the property as a list.
     *
     * This will return null if the property does not exist.
     */
    public List<MongoDBObject> getDBObjects(String propertyName) {
        List<DBObject> dbObjectList = getList(DBObject.class, propertyName);
        if(dbObjectList == null) {
            return null;
        }
        return Lists.transform(dbObjectList, DBObjectToMongoDBObjectFunction.Instance);
    }

    /**
     * Returns the property as a list.
     */
    public <T> List<T> getList(Class<T> itemType, String propertyName) {
        Object currentValue = get(propertyName);
        if (currentValue == null) {
            return null;
        }

        if (currentValue instanceof List) {
            //noinspection unchecked
            return (List<T>) currentValue;
        }

        if (currentValue instanceof Iterable) {
            //noinspection unchecked
            return Lists.newArrayList((Iterable<T>) currentValue);
        }

        throw new IllegalArgumentException("Value is not a list");
    }

    /**
     * Returns the property as a {@link Set}}.
     */
    public <E extends Enum<E>> Set<E> getEnumSet(final Class<E> enumType, String propertyName) {
        Object currentValue = get(propertyName);
        if (currentValue == null) {
            return null;
        }

        if (currentValue instanceof Collection) {
            //noinspection unchecked
            final Collection<String> values = (Collection<String>) currentValue;
            Collection<E> enums = Collections2.transform(values, new Function<String, E>() {
                @Override
                public E apply(String value) {
                    return Enum.valueOf(enumType, value);
                }
            });
            return Sets.newLinkedHashSet(enums);
        }

        throw new IllegalArgumentException("Value is not a collection");
    }

    /**
     * If the given value is a list, returns it. If any other type of {@link Iterable}, creates a list from it. If a non-null single value, returns a list with that value. Otherwise creates a new list.
     */
    protected List<Object> convertOrCreateList(Object currentValue) {
        if (currentValue == null) {
            return Lists.newArrayList();
        }

        if (currentValue instanceof List) {
            //noinspection unchecked
            return (List<Object>) currentValue;
        }

        if (currentValue instanceof Iterable) {
            //noinspection unchecked
            return Lists.newArrayList((Iterable<Object>) currentValue);
        }

        return Lists.newArrayList(currentValue);
    }

    /**
     * Add a value to the given subkey on a DBObject within the given key.  Uses {@link #getOrCreateDBObjectProperty(Class, String)}.
     */
    protected D appendToEnhancedProperty(String key, String subkey, Object value) {
        return appendToEnhancedProperty(MongoDBObject.class, key, subkey, value);
    }

    /**
     * Add a value to the given subkey on a DBObject within the given key.  Uses {@link #getOrCreateDBObjectProperty(Class, String)}.
     */
    protected D appendToEnhancedProperty(Class<? extends DBObject> dbObjectType, String key, String subkey, Object value) {
        getOrCreateDBObjectProperty(dbObjectType, key).put(subkey, value);

        //noinspection unchecked
        return (D) this;
    }

    /**
     * Returns a subobject for the given property.  If null, will create, add and return a new object.  If non-null, must be of the given type.
     */
    protected MongoDBObject getOrCreateDBObjectProperty(String key) {
        //noinspection unchecked
        return getOrCreateDBObjectProperty(MongoDBObject.class, key);
    }

    /**
     * Returns a subobject for the given property.  If null, will create, add and return a new object of the given type.  If non-null, must be of the given type.
     */
    protected <T extends DBObject> T getOrCreateDBObjectProperty(Class<T> dbObjectType, String key) {
        T modifier = getDBObjectProperty(dbObjectType, key);
        if (modifier == null) {
            try {
                modifier = dbObjectType.newInstance();
            } catch (Throwable t) {
                Throwables.throwIfUnchecked(t);
                throw new RuntimeException(t);
            }
            put(key, modifier);
        }
        return modifier;
    }

    /**
     * Returns a subobject for the given property, which must be null or of the given type.
     */
    public <T extends DBObject> T getDBObjectProperty(Class<T> dbObjectType, String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }

        Assert.isInstanceOf(dbObjectType, value);

        //noinspection unchecked
        return (T) value;
    }

    public int size() {
        return _dbObject.size();
    }

    public boolean isEmpty() {
        return _dbObject.isEmpty();
    }

    public void clear() {
        _dbObject.clear();
    }

    public Set<String> keySet() {
        return _dbObject.keySet();
    }

    public Collection<Object> values() {
        return _dbObject.values();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return _dbObject.entrySet();
    }

    public int hashCode() {
        return _dbObject.hashCode();
    }

    public Map toMap() {
        return _dbObject.toMap();
    }

    public Object removeField(String key) {
        return _dbObject.removeField(key);
    }

    public boolean containsField(String field) {
        return _dbObject.containsField(field);
    }

    public boolean containsKey(String key) {
        return _dbObject.containsField(key);
    }

    public Object get(String key) {
        return _dbObject.get(key);
    }

    public int getInt(String key) {
        return _dbObject.getInt(key);
    }

    public int getInt(String key, int def) {
        return _dbObject.getInt(key, def);
    }

    public long getLong(String key) {
        return _dbObject.getLong(key);
    }

    public double getDouble(String key) {
        return _dbObject.getDouble(key);
    }

    public String getString(String key) {
        return _dbObject.getString(key);
    }

    public boolean getBoolean(String key) {
        return _dbObject.getBoolean(key);
    }

    public boolean getBoolean(String key, boolean def) {
        return _dbObject.getBoolean(key, def);
    }

    public void putAll(Map m) {
        _dbObject.putAll(m);
    }

    public void putAll(BSONObject o) {
        _dbObject.putAll(o);
    }

    public boolean isPartialObject() {
        return _dbObject.isPartialObject();
    }

    public void markAsPartialObject() {
        _dbObject.markAsPartialObject();
    }

    public String toString() {
        return super.toString() + _dbObject.toString();
    }

    @SuppressWarnings ( {"CloneDoesntDeclareCloneNotSupportedException"})
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class DBObjectToMongoDBObjectFunction implements Function<DBObject, MongoDBObject> {
        public static final DBObjectToMongoDBObjectFunction Instance = new DBObjectToMongoDBObjectFunction();

        @Override
        public MongoDBObject apply(DBObject dbObject) {
            if(dbObject == null) {
                return null;
            }
            
            if (dbObject instanceof MongoDBObject) {
                return (MongoDBObject) dbObject;
            }

            if (dbObject instanceof BasicDBObject) {
                return new MongoDBObject((BasicDBObject) dbObject);
            }

            return new MongoDBObject(dbObject);
        }
    }
}
