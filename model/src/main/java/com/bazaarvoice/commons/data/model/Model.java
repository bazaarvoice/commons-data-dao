package com.bazaarvoice.commons.data.model;

/**
 * Basic interface for most domain model objects.  Provides a primary key identifier for persistence.
 */
public interface Model {
    String getID();
}
