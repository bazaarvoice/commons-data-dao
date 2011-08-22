package com.bazaarvoice.commons.data.dao;

import java.util.Collection;

/**
 * Data Access Object (DAO) interface for creating, updating and deleting objects in the database.
 */
public interface ModelDAORW<T> extends ModelDAO<T> {
    /** Inserts or updates the specified object in the database. */
    void save(T object);

    /** Inserts or updates the specified objects in the database */
    void saveAll(Collection<T> objects);

    /** Inserts the specified objects in the database. Performance improvements over saveAll */
    void create(T... objects);

    /** Inserts the specified objects in the database. Performance improvements over saveAll */
    void create(Collection<T> objects);

    /** Deletes the specified object from the database. */
    void delete(T object);

    /** Deletes the specified object by ID from the database. */
    void deleteByID(String objectID);

    /** Deletes the specified objects from the database. */
    void deleteAll(Collection<T> objects);

    /** Deletes the specified objects by ID from the database. */
    void deleteAllByID(Collection<String> objectIDs);
}
