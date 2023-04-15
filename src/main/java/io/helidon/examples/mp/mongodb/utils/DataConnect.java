package io.helidon.examples.mp.mongodb.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.logging.Logger;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Program to connect with the data store using config key-values.
 *
 * @author Suren K
 * @since Oct 2021
 */
@ApplicationScoped
public class DataConnect {
    private static final Logger LOG = Logger.getLogger(DataConnect.class.getName());

    private APIConfig apiConfig;

    private MongoDatabase database;

    @Inject
    public DataConnect(APIConfig apiConfig) {
        this.apiConfig = apiConfig;
    }
   
    /**
     * Connect to local database
     * @return
     */
    public MongoDatabase getLocalDataStore() {
        try {
            // Replace the uri string with your MongoDB deployment's connection string.
            ConnectionString connString = new ConnectionString(apiConfig.getDatastoreUrl());
            CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
            CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .retryWrites(true)
                    .codecRegistry(codecRegistry)
                    .build();
            MongoClient mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(apiConfig.getDatabaseName());
        } catch (MongoException mongoException) {
            LOG.info("Error while making a datastore connection. Mongo Exception() {}" + mongoException);
        } catch (Exception exception) {
            LOG.info("Error while making a datastore connection. Exception() {}" + exception);
        }

        return database;
    }

    /**
     * Connect to external mongodb
     * @return
     */
    public MongoDatabase getDataStore() {
        System.out.println("getDataStore() {}");

        ServerApi serverApi = ServerApi.builder()
                                        .version(ServerApiVersion.V1)
                                        .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(apiConfig.getConnectionString()))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("frisco-business");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException mongoException) {
                LOG.info("Error while making a datastore connection. Mongo Exception() {}" + mongoException);
            } catch (Exception exception) {
                LOG.info("Error while making a datastore connection. Exception() {}" + exception);
            }
    
            return database;
        }
    }
}
