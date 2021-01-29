package com.bazaarvoice.commons.data.dao.mongo;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.DBDecoderFactory;
import com.mongodb.DBEncoderFactory;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCompressor;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.CommandListener;
import com.mongodb.selector.ServerSelector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.util.List;

/**
 * Spring factory bean for construction of a {@link MongoClientOptions} instance
 * Deprecated properties are stubbed out for backward compatibility
 */
public class MongoClientOptionsFactoryBean extends AbstractFactoryBean<MongoClientOptions> {

    private static final Log _sLog = LogFactory.getLog(MongoClientOptionsFactoryBean.class);

    private static final MongoClientOptions DEFAULT_MONGO_OPTIONS = MongoClientOptions
            .builder()
            .build();

    private String applicationName = DEFAULT_MONGO_OPTIONS.getApplicationName();
    private List<com.mongodb.MongoCompressor> compressorList = DEFAULT_MONGO_OPTIONS.getCompressorList();
    private ReadPreference readPreference = DEFAULT_MONGO_OPTIONS.getReadPreference();
    private WriteConcern writeConcern = DEFAULT_MONGO_OPTIONS.getWriteConcern();
    private boolean retryWrites = DEFAULT_MONGO_OPTIONS.getRetryWrites();
    private boolean retryReads = DEFAULT_MONGO_OPTIONS.getRetryReads();
    private ReadConcern readConcern = DEFAULT_MONGO_OPTIONS.getReadConcern();
    private CodecRegistry codecRegistry = DEFAULT_MONGO_OPTIONS.getCodecRegistry();
    private UuidRepresentation uuidRepresentation = DEFAULT_MONGO_OPTIONS.getUuidRepresentation();
    private ServerSelector serverSelector = DEFAULT_MONGO_OPTIONS.getServerSelector();
    private int minConnectionsPerHost = DEFAULT_MONGO_OPTIONS.getMinConnectionsPerHost();
    private int maxConnectionsPerHost = DEFAULT_MONGO_OPTIONS.getConnectionsPerHost();
    private int serverSelectionTimeout = DEFAULT_MONGO_OPTIONS.getServerSelectionTimeout();
    private int maxWaitTime = DEFAULT_MONGO_OPTIONS.getMaxWaitTime();
    private int maxConnectionIdleTime = DEFAULT_MONGO_OPTIONS.getMaxConnectionIdleTime();
    private int maxConnectionLifeTime = DEFAULT_MONGO_OPTIONS.getMaxConnectionLifeTime();
    private int connectTimeout = DEFAULT_MONGO_OPTIONS.getConnectTimeout();
    private int socketTimeout = DEFAULT_MONGO_OPTIONS.getSocketTimeout();
    private boolean sslEnabled = DEFAULT_MONGO_OPTIONS.isSslEnabled();
    private boolean sslInvalidHostNameAllowed = DEFAULT_MONGO_OPTIONS.isSslInvalidHostNameAllowed();
    private SSLContext sslContext = DEFAULT_MONGO_OPTIONS.getSslContext();
    private int heartbeatFrequency = DEFAULT_MONGO_OPTIONS.getHeartbeatFrequency();
    private int minHeartbeatFrequency = DEFAULT_MONGO_OPTIONS.getMinHeartbeatFrequency();
    private int heartbeatConnectTimeout = DEFAULT_MONGO_OPTIONS.getHeartbeatConnectTimeout();
    private int heartbeatSocketTimeout = DEFAULT_MONGO_OPTIONS.getHeartbeatSocketTimeout();
    private int localThreshold = DEFAULT_MONGO_OPTIONS.getLocalThreshold();
    private String requiredReplicaSetName = DEFAULT_MONGO_OPTIONS.getRequiredReplicaSetName();
    private DBDecoderFactory dbDecoderFactory = DEFAULT_MONGO_OPTIONS.getDbDecoderFactory();
    private DBEncoderFactory dbEncoderFactory = DEFAULT_MONGO_OPTIONS.getDbEncoderFactory();
    private boolean cursorFinalizerEnabled = DEFAULT_MONGO_OPTIONS.isCursorFinalizerEnabled();
    private List<com.mongodb.event.ClusterListener> clusterListeners = DEFAULT_MONGO_OPTIONS.getClusterListeners();
    private List<com.mongodb.event.CommandListener> commandListeners = DEFAULT_MONGO_OPTIONS.getCommandListeners();
    private AutoEncryptionSettings autoEncryptionSettings = DEFAULT_MONGO_OPTIONS.getAutoEncryptionSettings();


    @Deprecated
    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        _sLog.warn("Use of setThreadsAllowedToBlockForConnectionMultiplier is deprecated, this setting will have no affect!");
    }

    @Deprecated
    public void setSocketKeepAlive(boolean socketKeepAlive) {
        _sLog.warn("Use of setSocketKeepAlive is deprecated, this setting will have no affect!");
    }

    @Deprecated
    public void setSocketFactory(SocketFactory socketFactory) {
        _sLog.warn("Use of setSocketFactory is deprecated, this setting will have no affect!");
    }

    @Deprecated
    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        _sLog.warn("Use of setSslSocketFactory is deprecated, this setting will have no affect!");
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param applicationName
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param compressorList
     */
    public void setCompressorList(List<MongoCompressor> compressorList) {
        this.compressorList = compressorList;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param readPreference
     */
    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param writeConcern
     */
    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param retryWrites
     */
    public void setRetryWrites(boolean retryWrites) {
        this.retryWrites = retryWrites;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param retryReads
     */
    public void setRetryReads(boolean retryReads) {
        this.retryReads = retryReads;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param readConcern
     */
    public void setReadConcern(ReadConcern readConcern) {
        this.readConcern = readConcern;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param codecRegistry
     */
    public void setCodecRegistry(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param uuidRepresentation
     */
    public void setUuidRepresentation(UuidRepresentation uuidRepresentation) {
        this.uuidRepresentation = uuidRepresentation;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param serverSelector
     */
    public void setServerSelector(ServerSelector serverSelector) {
        this.serverSelector = serverSelector;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param minConnectionsPerHost
     */
    public void setMinConnectionsPerHost(int minConnectionsPerHost) {
        this.minConnectionsPerHost = minConnectionsPerHost;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param maxConnectionsPerHost
     */
    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param serverSelectionTimeout
     */
    public void setServerSelectionTimeout(int serverSelectionTimeout) {
        this.serverSelectionTimeout = serverSelectionTimeout;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param maxWaitTime
     */
    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param maxConnectionIdleTime
     */
    public void setMaxConnectionIdleTime(int maxConnectionIdleTime) {
        this.maxConnectionIdleTime = maxConnectionIdleTime;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param maxConnectionLifeTime
     */
    public void setMaxConnectionLifeTime(int maxConnectionLifeTime) {
        this.maxConnectionLifeTime = maxConnectionLifeTime;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param connectTimeout
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param socketTimeout
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param sslEnabled
     */
    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param sslInvalidHostNameAllowed
     */
    public void setSslInvalidHostNameAllowed(boolean sslInvalidHostNameAllowed) {
        this.sslInvalidHostNameAllowed = sslInvalidHostNameAllowed;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param sslContext
     */
    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param heartbeatFrequency
     */
    public void setHeartbeatFrequency(int heartbeatFrequency) {
        this.heartbeatFrequency = heartbeatFrequency;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param minHeartbeatFrequency
     */
    public void setMinHeartbeatFrequency(int minHeartbeatFrequency) {
        this.minHeartbeatFrequency = minHeartbeatFrequency;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param heartbeatConnectTimeout
     */
    public void setHeartbeatConnectTimeout(int heartbeatConnectTimeout) {
        this.heartbeatConnectTimeout = heartbeatConnectTimeout;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param heartbeatSocketTimeout
     */
    public void setHeartbeatSocketTimeout(int heartbeatSocketTimeout) {
        this.heartbeatSocketTimeout = heartbeatSocketTimeout;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param localThreshold
     */
    public void setLocalThreshold(int localThreshold) {
        this.localThreshold = localThreshold;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param requiredReplicaSetName
     */
    public void setRequiredReplicaSetName(String requiredReplicaSetName) {
        this.requiredReplicaSetName = requiredReplicaSetName;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param dbDecoderFactory
     */
    public void setDbDecoderFactory(DBDecoderFactory dbDecoderFactory) {
        this.dbDecoderFactory = dbDecoderFactory;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param dbEncoderFactory
     */
    public void setDbEncoderFactory(DBEncoderFactory dbEncoderFactory) {
        this.dbEncoderFactory = dbEncoderFactory;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param cursorFinalizerEnabled
     */
    public void setCursorFinalizerEnabled(boolean cursorFinalizerEnabled) {
        this.cursorFinalizerEnabled = cursorFinalizerEnabled;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param clusterListeners
     */
    public void setClusterListeners(List<ClusterListener> clusterListeners) {
        this.clusterListeners = clusterListeners;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param commandListeners
     */
    public void setCommandListeners(List<CommandListener> commandListeners) {
        this.commandListeners = commandListeners;
    }

    /**
     * @see com.mongodb.MongoClientOptions
     * @param autoEncryptionSettings
     */
    public void setAutoEncryptionSettings(AutoEncryptionSettings autoEncryptionSettings) {
        this.autoEncryptionSettings = autoEncryptionSettings;
    }

    /**
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
     */
    @Override
    protected MongoClientOptions createInstance() {

        return MongoClientOptions.builder()
                .applicationName(this.applicationName)
                .compressorList(this.compressorList)
                .readPreference(this.readPreference)
                .writeConcern(this.writeConcern)
                .retryWrites(this.retryWrites)
                .retryReads(this.retryReads)
                .readConcern(this.readConcern)
                .codecRegistry(this.codecRegistry)
                .uuidRepresentation(this.uuidRepresentation)
                .serverSelector(this.serverSelector)
                .minConnectionsPerHost(this.minConnectionsPerHost)
                .connectionsPerHost(this.maxConnectionsPerHost)
                .serverSelectionTimeout(this.serverSelectionTimeout)
                .maxWaitTime(this.maxWaitTime)
                .maxConnectionIdleTime(this.maxConnectionIdleTime)
                .maxConnectionLifeTime(this.maxConnectionLifeTime)
                .connectTimeout(this.connectTimeout)
                .socketTimeout(this.socketTimeout)
                .sslEnabled(this.sslEnabled)
                .sslInvalidHostNameAllowed(this.sslInvalidHostNameAllowed)
                .sslContext(this.sslContext)
                .heartbeatFrequency(this.heartbeatFrequency)
                .minHeartbeatFrequency(this.minHeartbeatFrequency)
                .heartbeatConnectTimeout(this.heartbeatConnectTimeout)
                .heartbeatSocketTimeout(this.heartbeatSocketTimeout)
                .localThreshold(this.localThreshold)
                .requiredReplicaSetName(this.requiredReplicaSetName)
                .dbDecoderFactory(this.dbDecoderFactory)
                .dbEncoderFactory(this.dbEncoderFactory)
                .cursorFinalizerEnabled(this.cursorFinalizerEnabled)
                .autoEncryptionSettings(this.autoEncryptionSettings)
                .build();
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return MongoClientOptions.class;
    }
}
