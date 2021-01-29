package com.bazaarvoice.commons.data.dao.mongo;

import com.mongodb.MongoCredential;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Spring factory bean for construction of a {@link MongoCredential} instance
 */
public class MongoCredentialsFactoryBean extends AbstractFactoryBean<MongoCredential> {

    private final String username;
    private final String password;
    private final String authDatabaseName;

    public MongoCredentialsFactoryBean(String username, String password, String authDatabaseName) {
        this.username = username;
        this.password = password;
        this.authDatabaseName = authDatabaseName;
    }

    @Override
    protected MongoCredential createInstance() {
        return MongoCredential.createCredential(username, authDatabaseName, password.toCharArray());
    }

    @Override
    public Class<?> getObjectType() {
        return MongoCredential.class;
    }
}
