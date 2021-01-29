Bazaarvoice Common Data DAO
======

This project contains general data DAO modules.

## Modules
```
commons-data-parent    - Parent module
commons-data-model     - The Data Model module contains generally useful classes for building up models
commons-data-dao       - The DAO module contains general DAO interfaces and base classes for accessing data using any number of data sources
commons-data-json      - Commons Data JSON contains many useful classes for working with JSON and JSON Schema
commons-data-mongodao  - The Mongo DAO module is an implementation of the general DAO for MongoDB
```

## Supported Versions
_Check [`parent/pom.xml`](parent/pom.xml) for latest version information._

At date of writing (Jan 2021) the following versions are used:
- Spring 5.3.3
- MongoDB driver (legacy version) 4.2.0
- Guava 30.1-jre
- JSON 20201115

## Usage
### Spring XML-based configuration example
- Access to Mongo server/cluster via `MongoAccessService` using username/password auth (via `MongoCredentialsFactoryBean`) and configuring `readPreference`,
`connectionsPerHost` and `writeConcern` (via `MongoClientOptionsFactoryBean`):
```xml
<bean id="MongoAccessService" class="com.bazaarvoice.commons.data.dao.mongo.impl.MongoAccessServiceImpl" abstract="true">
    <property name="serverAuthorities" value="${mongo.serverAuthorities}"/>
    <property name="mongoClientOptions">
        <bean class="com.bazaarvoice.commons.data.dao.mongo.MongoClientOptionsFactoryBean">
            <property name="readPreference">
                <bean class="com.mongodb.ReadPreference" factory-method="valueOf">
                    <constructor-arg value=""/>
                </bean>
            </property>
            <property name="connectionsPerHost" value="30"/>
            <property name="writeConcern">
                <bean class="com.mongodb.WriteConcern" factory-method="valueOf">
                    <constructor-arg value="ACKNOWLEDGED"/>
                </bean>
            </property>
        </bean>
    </property>
    <property name="mongoCredential">
        <bean class="com.bazaarvoice.commons.data.dao.mongo.MongoCredentialsFactoryBean" >
            <constructor-arg index="0" value="username"/>
            <constructor-arg index="1" value="password"/>
            <constructor-arg index="2" value="authDb"/>
        </bean>
    </property>
</bean>
```
- Access database `databaseName` via child bean: 
```xml
<bean id="DbMongoAccessService" class="com.bazaarvoice.commons.data.dao.mongo.impl.MongoAccessServiceImpl" parent="MongoAccessService">
    <property name="databaseName" value="databaseName"/>
</bean>
```
