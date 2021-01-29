package com.bazaarvoice.commons.data.dao.mongo.impl;

import com.bazaarvoice.commons.data.dao.mongo.MongoAccessService;
import com.bazaarvoice.commons.data.dao.mongo.dbo.MongoDBObject;
import com.bazaarvoice.commons.data.dao.mongo.dbo.QueryMongoDBObject;
import com.bazaarvoice.commons.data.dao.mongo.dbo.UpdateMongoDBObject;
import com.google.common.collect.Lists;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.gridfs.GridFS;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;
import java.util.List;

public class MongoAccessServiceImpl implements MongoAccessService {
    private static final Log _sLog = LogFactory.getLog(MongoAccessServiceImpl.class);

    private static final String DEFAULT_SEQUENCE_COLLECTION_NAME = "sequences";
    private static final String SEQUENCE_VALUE_PROPERTY_NAME = "current";

    private String _serverAuthorities;

    private String _databaseName;
    private boolean _useGridFS = false;
    private String _gridFSDatabaseName;
    private MongoClientOptions _mongoClientOptions = MongoClientOptions.builder().build();
    private MongoCredential _mongoCredentials;

    private String _sequenceCollectionName = DEFAULT_SEQUENCE_COLLECTION_NAME;

    private MongoClient _mongoClient;
    private GridFS _gridFS;

    @Required
    public void setServerAuthorities(String serverAuthorities) {
        _serverAuthorities = serverAuthorities;
    }

    @Required
    public void setDatabaseName(String databaseName) {
        _databaseName = databaseName;
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
     * MongoClientOptions to pass into the mongo client constructor
     * @see com.mongodb.MongoClientOptions
     * @param mongoClientOptions
     */
    public void setMongoClientOptions(MongoClientOptions mongoClientOptions) {
        _mongoClientOptions = mongoClientOptions;
    }

    /**
     * Optional credentials to configure on the mongo client
     * @see com.mongodb.MongoCredential
     * @param credentials
     */
    public void setMongoCredential(MongoCredential credentials) {
        _mongoCredentials = credentials;
    }

    @PostConstruct
    public void initMongoDB() {
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


        if(_mongoCredentials == null) {
            _mongoClient = new MongoClient(serverAddressList, _mongoClientOptions);
        } else {
            _mongoClient = new MongoClient(serverAddressList, _mongoCredentials, _mongoClientOptions);
        }

        if (_useGridFS) {
            if (StringUtils.isNotBlank(_gridFSDatabaseName)) {
                _sLog.info("Using grid FS with DB " + _gridFSDatabaseName);
                _gridFS = new GridFS(_mongoClient.getDB(_gridFSDatabaseName));
            } else {
                _sLog.info("Using grid FS with DB " + _databaseName);
                _gridFS = new GridFS(_mongoClient.getDB(_databaseName));
            }
        }
    }

    @Override
    public MongoClient getMongoClient() {
        return _mongoClient;
    }

    @Override
    public DB getDB() {
        return _mongoClient.getDB(_databaseName);
    }

    @Override
    public GridFS getGridFS() {
        return _gridFS;
    }

    @Override
    public DBCollection getSequenceCollection() {
        return _mongoClient.getDB(_databaseName).getCollection(_sequenceCollectionName);
    }

    @Override
    public int nextSequenceValue(String sequenceName) {
        QueryMongoDBObject query = new QueryMongoDBObject().forID(sequenceName);
        UpdateMongoDBObject update = new UpdateMongoDBObject().$inc(SEQUENCE_VALUE_PROPERTY_NAME);

        MongoDBObject modifiedObject = new MongoDBObject(getSequenceCollection().findAndModify(query, null, null, false, update, true, true));
        return modifiedObject.getInt(SEQUENCE_VALUE_PROPERTY_NAME);
    }
}
