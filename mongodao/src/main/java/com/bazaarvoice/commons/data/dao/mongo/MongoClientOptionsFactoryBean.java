package com.bazaarvoice.commons.data.dao.mongo;

import com.mongodb.DBDecoderFactory;
import com.mongodb.DBEncoderFactory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

/**
 * A factory bean for construction of a {@link MongoClientOptions} instance.
 * Source: http://docs.spring.io/spring-data/data-mongodb/docs/current/api/org/springframework/data/mongodb/core/MongoClientOptionsFactoryBean.html
 * Updated with additional configurational configuratoin properties.
 */
public class MongoClientOptionsFactoryBean extends AbstractFactoryBean<MongoClientOptions> {

    private static final MongoClientOptions DEFAULT_MONGO_OPTIONS = MongoClientOptions.builder().build();

    private String description = DEFAULT_MONGO_OPTIONS.getDescription();
    private int minConnectionsPerHost = DEFAULT_MONGO_OPTIONS.getMinConnectionsPerHost();
    private int connectionsPerHost = DEFAULT_MONGO_OPTIONS.getConnectionsPerHost();
    private int threadsAllowedToBlockForConnectionMultiplier = DEFAULT_MONGO_OPTIONS
            .getThreadsAllowedToBlockForConnectionMultiplier();
    private int maxWaitTime = DEFAULT_MONGO_OPTIONS.getMaxWaitTime();
    private int maxConnectionIdleTime = DEFAULT_MONGO_OPTIONS.getMaxConnectionIdleTime();
    private int maxConnectionLifeTime = DEFAULT_MONGO_OPTIONS.getMaxConnectionLifeTime();
    private int connectTimeout = DEFAULT_MONGO_OPTIONS.getConnectTimeout();
    private int socketTimeout = DEFAULT_MONGO_OPTIONS.getSocketTimeout();
    private boolean socketKeepAlive = DEFAULT_MONGO_OPTIONS.isSocketKeepAlive();
    private ReadPreference readPreference = DEFAULT_MONGO_OPTIONS.getReadPreference();
    private DBDecoderFactory dbDecoderFactory = DEFAULT_MONGO_OPTIONS.getDbDecoderFactory();
    private DBEncoderFactory dbEncoderFactory = DEFAULT_MONGO_OPTIONS.getDbEncoderFactory();
    private WriteConcern writeConcern = DEFAULT_MONGO_OPTIONS.getWriteConcern();
    private SocketFactory socketFactory = DEFAULT_MONGO_OPTIONS.getSocketFactory();
    private boolean cursorFinalizerEnabled = DEFAULT_MONGO_OPTIONS.isCursorFinalizerEnabled();
    private boolean alwaysUseMBeans = DEFAULT_MONGO_OPTIONS.isAlwaysUseMBeans();
    private int heartbeatFrequency = DEFAULT_MONGO_OPTIONS.getHeartbeatFrequency();
    private int minHeartbeatFrequency = DEFAULT_MONGO_OPTIONS.getMinHeartbeatFrequency();
    private int heartbeatConnectTimeout = DEFAULT_MONGO_OPTIONS.getHeartbeatConnectTimeout();
    private int heartbeatSocketTimeout = DEFAULT_MONGO_OPTIONS.getHeartbeatSocketTimeout();
    private String requiredReplicaSetName = DEFAULT_MONGO_OPTIONS.getRequiredReplicaSetName();

    private boolean ssl;
    private SSLSocketFactory sslSocketFactory;

    /**
     * Set the {@link MongoClient} description.
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the minimum number of connections per host.
     *
     * @param minConnectionsPerHost
     */
    public void setMinConnectionsPerHost(int minConnectionsPerHost) {
        this.minConnectionsPerHost = minConnectionsPerHost;
    }

    /**
     * Set the number of connections allowed per host. Will block if run out. Default is 10. System property
     * {@code MONGO.POOLSIZE} can override
     *
     * @param connectionsPerHost
     */
    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    /**
     * Set the multiplier for connectionsPerHost for # of threads that can block. Default is 5. If connectionsPerHost is
     * 10, and threadsAllowedToBlockForConnectionMultiplier is 5, then 50 threads can block more than that and an
     * exception will be thrown.
     *
     * @param threadsAllowedToBlockForConnectionMultiplier
     */
    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    /**
     * Set the max wait time of a blocking thread for a connection. Default is 12000 ms (2 minutes)
     *
     * @param maxWaitTime
     */
    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    /**
     * The maximum idle time for a pooled connection.
     *
     * @param maxConnectionIdleTime
     */
    public void setMaxConnectionIdleTime(int maxConnectionIdleTime) {
        this.maxConnectionIdleTime = maxConnectionIdleTime;
    }

    /**
     * Set the maximum life time for a pooled connection.
     *
     * @param maxConnectionLifeTime
     */
    public void setMaxConnectionLifeTime(int maxConnectionLifeTime) {
        this.maxConnectionLifeTime = maxConnectionLifeTime;
    }

    /**
     * Set the connect timeout in milliseconds. 0 is default and infinite.
     *
     * @param connectTimeout
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Set the socket timeout. 0 is default and infinite.
     *
     * @param socketTimeout
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * Set the keep alive flag, controls whether or not to have socket keep alive timeout. Defaults to false.
     *
     * @param socketKeepAlive
     */
    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    /**
     * Set the {@link ReadPreference}.
     *
     * @param readPreference
     */
    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    /**
     * Set the {@link WriteConcern} that will be the default value used when asking the MongoDbFactory for a DB
     * object.
     *
     * @param writeConcern
     */
    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }

    /**
     * @param socketFactory
     */
    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }


    public void setCursorFinalizerEnabled(boolean cursorFinalizerEnabled) {
        this.cursorFinalizerEnabled = cursorFinalizerEnabled;
    }

    /**
     * Set the frequency that the driver will attempt to determine the current state of each server in the cluster.
     *
     * @param heartbeatFrequency
     */
    public void setHeartbeatFrequency(int heartbeatFrequency) {
        this.heartbeatFrequency = heartbeatFrequency;
    }

    /**
     * In the event that the driver has to frequently re-check a server's availability, it will wait at least this long
     * since the previous check to avoid wasted effort.
     *
     * @param minHeartbeatFrequency
     */
    public void setMinHeartbeatFrequency(int minHeartbeatFrequency) {
        this.minHeartbeatFrequency = minHeartbeatFrequency;
    }

    /**
     * Set the connect timeout for connections used for the cluster heartbeat.
     *
     * @param heartbeatConnectTimeout
     */
    public void setHeartbeatConnectTimeout(int heartbeatConnectTimeout) {
        this.heartbeatConnectTimeout = heartbeatConnectTimeout;
    }

    /**
     * Set the socket timeout for connections used for the cluster heartbeat.
     *
     * @param heartbeatSocketTimeout
     */
    public void setHeartbeatSocketTimeout(int heartbeatSocketTimeout) {
        this.heartbeatSocketTimeout = heartbeatSocketTimeout;
    }

    /**
     * Configures the name of the replica set.
     *
     * @param requiredReplicaSetName
     */
    public void setRequiredReplicaSetName(String requiredReplicaSetName) {
        this.requiredReplicaSetName = requiredReplicaSetName;
    }

    /**
     * This controls if the driver should us an SSL connection. Defaults to |@literal false}.
     *
     * @param ssl
     */
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    /**
     * Set the {@link SSLSocketFactory} to use for the {@literal SSL} connection. If none is configured here,
     * {@link SSLSocketFactory#getDefault()} will be used.
     *
     * @param sslSocketFactory
     */
    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
     */
    @Override
    protected MongoClientOptions createInstance() throws Exception {

        SocketFactory socketFactoryToUse = ssl ? (sslSocketFactory != null ? sslSocketFactory : SSLSocketFactory
                .getDefault()) : this.socketFactory;

        return MongoClientOptions.builder() //
                .alwaysUseMBeans(this.alwaysUseMBeans) //
                .connectionsPerHost(this.connectionsPerHost) //
                .connectTimeout(connectTimeout) //
                .cursorFinalizerEnabled(cursorFinalizerEnabled) //
                .dbDecoderFactory(dbDecoderFactory) //
                .dbEncoderFactory(dbEncoderFactory) //
                .description(description) //
                .heartbeatConnectTimeout(heartbeatConnectTimeout) //
                .heartbeatFrequency(heartbeatFrequency) //
                .heartbeatSocketTimeout(heartbeatSocketTimeout) //
                .maxConnectionIdleTime(maxConnectionIdleTime) //
                .maxConnectionLifeTime(maxConnectionLifeTime) //
                .maxWaitTime(maxWaitTime) //
                .minConnectionsPerHost(minConnectionsPerHost) //
                .minHeartbeatFrequency(minHeartbeatFrequency) //
                .readPreference(readPreference) //
                .requiredReplicaSetName(requiredReplicaSetName) //
                .socketFactory(socketFactoryToUse) //
                .socketKeepAlive(socketKeepAlive) //
                .socketTimeout(socketTimeout) //
                .threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier) //
                .writeConcern(writeConcern).build();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    public Class<?> getObjectType() {
        return MongoClientOptions.class;
    }
}
