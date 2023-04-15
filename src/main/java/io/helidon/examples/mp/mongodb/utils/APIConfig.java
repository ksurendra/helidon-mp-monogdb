package io.helidon.examples.mp.mongodb.utils;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Program to map and read config key-values.
 *
 *  @author Suren K
 *  @since Oct 2021
 */
@ApplicationScoped
public class APIConfig {
    private final AtomicReference<String> datastoreUrl = new AtomicReference<>();
    private final AtomicReference<String> databaseName = new AtomicReference<>();
    private final AtomicReference<String> connectionString = new AtomicReference<>();

    @Inject
    public APIConfig(
            @ConfigProperty(name = "datastore.url") String datastoreUrl,
            @ConfigProperty(name = "datastore.databasename") String databaseName,
            @ConfigProperty(name = "mongo.connectionstring") String connectionString) {
        this.datastoreUrl.set(datastoreUrl);
        this.databaseName.set(databaseName);
        this.connectionString.set(connectionString);
    }

    public String getDatastoreUrl() {
        return datastoreUrl.get();
    }

    public String getDatabaseName() {
        return databaseName.get();
    }

    public String getConnectionString() {
        return connectionString.get();
    }
}
