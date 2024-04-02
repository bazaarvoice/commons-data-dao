package com.bazaarvoice.commons.data.dao;

import com.bazaarvoice.commons.data.model.QueryResultsBatch;
import com.bazaarvoice.commons.data.model.QueryResults;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) interface for querying objects in the database.
 */
public interface CriteriaDAO<T, C extends Criteria<T, C>, S extends SortOrder<T, S>> extends ModelDAO<T> {
    /** Creates a new criteria object that is compatible with the rest of the criteria methods. */
    C newCriteria();

    /** Creates a new sort order object that is compatible with the rest of the criteria methods. */
    S newSortOrder();

    /** Returns the number of objects that match the criteria. */
    int findCount(@Nullable C criteria);

    /** Returns all objects that match the criteria, but only return their IDs. */
    Iterable<String> findIDs(@Nullable C criteria, @Nullable S sortOrder);

    /** Returns first object that matches the criteria. */
    T findOne(@Nullable C criteria, @Nullable S sortOrder);

    /** Returns first object that matches the criteria. */
    T findOne(@Nullable C criteria, @Nullable S sortOrder, @Nullable Map<String, Integer> keys);

    /** Returns all objects that match the criteria. */
    Iterable<T> find(@Nullable C criteria, @Nullable S sortOrder);

    Iterable<T> find(@Nullable C criteria, @Nullable S sortOrder, @Nullable Map<String, Integer> keys);

    Iterable<T> findResultsForPage(@Nullable C criteria, @Nullable S sortOrder, @Nullable Map<String, Integer> keys, int startIndex, int maxResults);

    QueryResultsBatch<T> findBatch(@Nullable C criteria, @Nullable S sortOrder, @Nullable Map<String, Integer> keys, int startIndex, int maxResults);

    /** Returns a limited set of objects that match the criteria. */
    QueryResults<T> find(@Nullable C criteria, @Nullable S sortOrder, int startIndex, int maxResults);

    QueryResults<T> find(@Nullable C criteria, @Nullable S sortOrder, int startIndex, int maxResults, @Nullable Map<String, Integer> keys);

    /** Returns the objects that match the criteria, indexed by their primary key. */
    Map<String, T> findIndexed(@Nullable C criteria, @Nullable S sortOrder);

    List<String> findDistinct(String propertyName, @Nullable C criteria);

    String getCollectionName();

}
