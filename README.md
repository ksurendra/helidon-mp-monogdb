# Helidon MP - MongoDB Example

Sample Helidon MP project to demonstrate connection and usage of MongoDB.

Though Helidon has a [DBClient](https://helidon.io/docs/v2/apidocs/io.helidon.dbclient/io/helidon/dbclient/DbClient.html) class, the intent of this example is to use MongoDB's Java drivers. 

## Prerequisites
1. Java JDK 11 or higher (OpenJDK or Oracle JDK)
2. MongoDB instance. Running locally or remotely
   1. A database (preferred `helidon-samples`) added to the above mongoDB instance.
   2. A collection (named `users`) added to the above database.

### Database connection properties
The above details in (2) should be entered in `microprofile-config.properties` located in `src/main/resources/META-INF` as:

```
# MongoDB related connection properties
datastore.url=mongodb://localhost:27017
datastore.databasename=helidon-samples
```

_For this example, i ran MongodDB instance on my computer, running on localhost and default port.
`mongodb://localhost:27017`_

## Build and run

With JDK11+
```bash
mvn package
java -jar target/helidon-mp-mongodb.jar
```

## Usage
This example comes with two end points.  

### 1. Add a User
The endpoint `/user/add` defined in `DataService` adds a new User to the collection. 

```
http://localhost:8080/datastore/user/add

Success! Inserted User: User{id=61785236a187a53337573b5f, userId='1', userName='surenk', firstName='Suren', lastName='K'}
 with document id:BsonObjectId{value=61785236a187a53337573b5f}
```

### 2. Find a User
The endpoint `/user/find` defined in `DataService` finds a User in the `users` collection.

```
http://localhost:8080/datastore/user/find?username=surenk

User with username=surenk is Found! Details: {"userName": "surenk"}
```

## Details
This sample has been built with the following:

### 1. Helidon MP Framework

[Helidon MP 2.3.4](https://helidon.io/docs/latest/#/mp/guides/02_quickstart) was used to build this sample.

```xml
mvn -U archetype:generate -DinteractiveMode=false \
    -DarchetypeGroupId=io.helidon.archetypes \
    -DarchetypeArtifactId=helidon-quickstart-mp \
    -DarchetypeVersion=2.3.4 \
    -DgroupId=io.helidon.examples \
    -DartifactId=helidon-mp-mongodb \
    -Dpackage=io.helidon.examples.mp.mongodb
```

### 2. MongoDB Drivers

Used [MongodDB Java Driver 4.3.3](https://docs.mongodb.com/drivers/java/sync/current/) and added as a maven dependency.

```xml
 <dependency>
   <groupId>org.mongodb</groupId>
   <artifactId>mongodb-driver-sync</artifactId>
   <version>4.3.3</version>
</dependency>
```

