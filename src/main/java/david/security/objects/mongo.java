package david.security.objects;


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import david.security.Security;
import david.security.commands.setCammera;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class mongo {


    public static MongoCollection connect(String collname){
        JavaPlugin plugin = Security.instance;

        final Logger LOGGER = Logger.getLogger(setCammera.class.getName());
        // Get the value of the greeting option
        String username = plugin.getConfig().getString("username");
        String password = plugin.getConfig().getString("password");
        String mongo_connection_string = plugin.getConfig().getString("mongo_connection_string");
        String seed = username + password;

        MongoClient mongoClient;
        assert mongo_connection_string != null;
        try {
            mongoClient = new MongoClient(new MongoClientURI(mongo_connection_string));
            MongoDatabase database = mongoClient.getDatabase("Security-" + seed);
            MongoCollection collection = database.getCollection(collname);
            return collection;
        }
        catch (Exception e) {
            LOGGER.warning("!!!!!-Could not connect to your mongo database please make sure your connection string is correct!--!!!!!");
        }

        return null;
    }
}
