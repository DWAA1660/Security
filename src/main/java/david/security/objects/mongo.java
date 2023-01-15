package david.security.objects;


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import david.security.Security;
import org.bukkit.plugin.java.JavaPlugin;

public class mongo {


    public static DBCollection connect(String collname){
        JavaPlugin plugin = Security.instance;


        // Get the value of the greeting option
        String username = plugin.getConfig().getString("username");
        String password = plugin.getConfig().getString("password");
        String seed = username + password;

        MongoClient mongoClient;
        mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://DWAA:Pinta123@schedulercluster.sfos0.mongodb.net/"));

        DB database = mongoClient.getDB("Security-" + seed);
        DBCollection collection = database.getCollection(collname);
        return collection;
    }
}
