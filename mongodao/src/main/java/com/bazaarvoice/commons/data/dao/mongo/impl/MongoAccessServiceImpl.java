package com.bazaarvoice.commons.data.dao.mongo.impl;

import com.bazaarvoice.commons.data.dao.mongo.MongoAccessService;
import com.bazaarvoice.commons.data.dao.mongo.dbo.MongoDBObject;
import com.bazaarvoice.commons.data.dao.mongo.dbo.QueryMongoDBObject;
import com.bazaarvoice.commons.data.dao.mongo.dbo.UpdateMongoDBObject;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.gridfs.GridFS;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.List;

public class MongoAccessServiceImpl implements MongoAccessService {
    private static final Log _sLog = LogFactory.getLog(MongoAccessServiceImpl.class);

    private static final String DEFAULT_SEQUENCE_COLLECTION_NAME = "sequences";
    private static final String SEQUENCE_VALUE_PROPERTY_NAME = "current";

    private String _serverAuthorities;

    private String _databaseName;
    private boolean _isSlaveOK = false;
    private boolean _useGridFS = false;
    private String _gridFSDatabaseName;
    private MongoOptions _mongoOptions = new MongoOptions();

    private String _sequenceCollectionName = DEFAULT_SEQUENCE_COLLECTION_NAME;

    private Mongo _mongo;
    private GridFS _gridFS;

    @Required
    public void setServerAuthorities(String serverAuthorities) {
        _serverAuthorities = serverAuthorities;
    }

    @Required
    public void setDatabaseName(String databaseName) {
        _databaseName = databaseName;
    }

    /**
     * makes it possible to run read queries on secondary nodes
     *
     * @param slaveOK if you want to use secondaries or not
     * @deprecated 10/22/2012 Replaced with {@code setMongoOptions()}
     * @see MongoAccessServiceImpl#setMongoOptions(com.mongodb.MongoOptions)
     */
    @Deprecated
    public void setSlaveOK(boolean slaveOK) {
        _isSlaveOK = slaveOK;
    }

    public void setUseGridFS(boolean useGridFS) {
        _useGridFS = useGridFS;
    }

    public void setGridFSDatabaseName(String gridFSDatabaseName) {
        _gridFSDatabaseName = gridFSDatabaseName;
    }

    public void setSequenceCollectionName(String sequenceCollectionName) {
        _sequenceCollectionName = sequenceCollectionName;
    }

    /**
     * MongoOptions to pass into the mongo constructor
     * @see com.mongodb.MongoOptions
     *
     * @param mongoOptions the mongo options to use
     */
    public void setMongoOptions(MongoOptions mongoOptions) {
        _mongoOptions = mongoOptions;
    }

    @PostConstruct
    public void initMongoDB() {
        try {
            List<ServerAddress> serverAddressList = Lists.newArrayList();

            for (String serverAuthority : StringUtils.split(_serverAuthorities, ',')) {
                String[] serverAuthorityParts = StringUtils.split(StringUtils.trimToEmpty(serverAuthority), ":");

                if (serverAuthorityParts.length == 1) {
                    serverAddressList.add(new ServerAddress(serverAuthorityParts[0]));
                } else {
                    serverAddressList.add(new ServerAddress(serverAuthorityParts[0], Integer.parseInt(serverAuthorityParts[1])));
                }
            }

            if (_sLog.isInfoEnabled()) {
                _sLog.info("Initializing mongoDB '" + _databaseName + "' with servers: " + serverAddressList);
            }

            if (_isSlaveOK) {
                _mongoOptions.readPreference = ReadPreference.secondaryPreferred();
            }

            _mongo = new Mongo(serverAddressList, _mongoOptions);

        } catch (UnknownHostException e) {
            throw Throwables.propagate(e);
        }

        if (_useGridFS) {
            if (StringUtils.isNotBlank(_gridFSDatabaseName)) {
                _sLog.info("Using grid FS with DB " + _gridFSDatabaseName);
                _gridFS = new GridFS(_mongo.getDB(_gridFSDatabaseName));
            } else {
                _sLog.info("Using grid FS with DB " + _databaseName);
                _gridFS = new GridFS(_mongo.getDB(_databaseName));
            }
        }
    }

    @Override
    public Mongo getMongo() {
        return _mongo;
    }

    @Override
    public DB getDB() {
        return _mongo.getDB(_databaseName);
    }

    @Override
    public GridFS getGridFS() {
        return _gridFS;
    }

    @Override
    public DBCollection getSequenceCollection() {
        return getDB().getCollection(_sequenceCollectionName);
    }

    @Override
    public int nextSequenceValue(String sequenceName) {
        QueryMongoDBObject query = new QueryMongoDBObject().forID(sequenceName);
        UpdateMongoDBObject update = new UpdateMongoDBObject().$inc(SEQUENCE_VALUE_PROPERTY_NAME);

        MongoDBObject modifiedObject = new MongoDBObject(getSequenceCollection().findAndModify(query, null, null, false, update, true, true));
        return modifiedObject.getInt(SEQUENCE_VALUE_PROPERTY_NAME);
    }
}
