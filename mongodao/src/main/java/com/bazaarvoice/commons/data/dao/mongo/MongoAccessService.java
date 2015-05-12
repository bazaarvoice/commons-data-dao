package com.bazaarvoice.commons.data.dao.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;

public interface MongoAccessService {
    /**
     * Retrieve the Mongo database for low-level actions.
     */
    public Mongo getMongo();

    /**
     * Retrieve the default MongoDB
     */
    public DB getDB();

    /**
     * Retrieve the GridFS accessor for saving and retrieving large binary data.
     */
    public GridFS getGridFS();

    /**
     * Retrieves the collection used for sequences.
     */
    DBCollection getSequenceCollection();

    /**
     * Returns the next value in the given sequence, starting at 1 if the sequence hasn't been created yet.
     */
    int nextSequenceValue(String sequenceName);
}
