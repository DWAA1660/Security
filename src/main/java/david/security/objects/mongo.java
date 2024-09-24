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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class mongo {


    public static Connection connect() {
        String url = "jdbc:sqlite:./plugins/Security/database.db";

        try {
            Connection connection = DriverManager.getConnection(url);
            return  connection;
        } catch (SQLException e) {
            Logger.getLogger("Security").warning(e.toString());
        }

        return null;
    }
}
