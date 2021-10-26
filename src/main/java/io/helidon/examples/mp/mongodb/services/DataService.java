package io.helidon.examples.mp.mongodb.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import io.helidon.examples.mp.mongodb.models.User;
import io.helidon.examples.mp.mongodb.utils.DataConnect;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

import javax.enterprise.context.RequestScoped;

import javax.ws.rs.Produces;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

/**
 * Program to interact with data store.
 *
 *  @author Suren K
 *  @since Oct 2021
 */
@Path("/datastore/user")
@RequestScoped
public class DataService {
    private static final Logger LOG = Logger.getLogger(DataService.class.getName());
    private static final Gson gsonObj = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final DataConnect dataConnect;
    private final  MongoDatabase database;

    @Inject
    public DataService(DataConnect dataConnectConfig) {
        this.dataConnect = dataConnectConfig;
        this.database = this.dataConnect.getDataStore();
    }

    /**
     * Method to add a user to data store.
     * @return Message of the data operation
    */
    @Path("/add")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String insertUser() {
        StringBuffer message = new StringBuffer();

        MongoCollection<User> collection = database.getCollection("users", User.class);
        LOG.info("collection = "+ collection.toString());

        User user = new User("1", "surenk", "Suren", "K");

        try {
            InsertOneResult result = collection.insertOne(user);

            message.append("Success! Inserted User: " + user.toString());
            message.append("\n with document id:" + result.getInsertedId());
            LOG.info("Success! Inserted document id: " + result.getInsertedId());
        } catch (MongoException me) {
            message.append("Unable to insert due to an error");
            LOG.info("Unable to insert due to an error: " + me);
        }

        return message.toString();
    }

    /**
     * Method to find user from the input in the data store.
     * @param userName - User name to be found
     * @return Message of the data operation
     */
    @Path("/find")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findUser(@QueryParam("username") String userName) {
        StringBuffer message = new StringBuffer();

        try {
            MongoCollection<Document> collection = database.getCollection("users");
            LOG.info("collection = "+ collection.toString());

            Bson projectionFields = Projections.fields(
                    Projections.include("userName"),
                    Projections.excludeId());

            Document docUser = collection.find(eq("userName", userName))
                    .projection(projectionFields)
                    .first();

            if (docUser == null) {
                message.append("User with username=" + userName + " is not found!");
            } else {
                message.append("User with username=" + userName + " is Found!");
                message.append("Details: " + docUser.toJson());
            }

        }  catch (MongoException me) {
            message.append("Unable to Find User due to an error");
            LOG.info("Unable to Find User due to an error: " + me);
        }
        return message.toString();
    }
}
