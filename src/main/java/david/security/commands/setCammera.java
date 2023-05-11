package david.security.commands;


import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import david.security.events.OnCameraChange;
import david.security.objects.mongo;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class setCammera implements CommandExecutor {
    private static final Logger LOGGER = Logger.getLogger(setCammera.class.getName());
    MongoCollection collection = mongo.connect("cameras");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.isOp()) {
                sender.sendMessage("You need to be an OP to use this command!");
                return true;
            }
            Player senderdude = (Player) sender;
            LOGGER.info(String.valueOf(senderdude.getLocation().getBlockX()));

            LOGGER.info(String.valueOf(senderdude.getLocation().getBlockY()));
            Location location = senderdude.getLocation();
            String message = String.valueOf(senderdude.getUniqueId()) + location.getBlockX() + location.getBlockY() + location.getBlockZ();
            Document data = new Document ("_id", message)
                    .append("x", location.getBlockX() + 0.01)
                    .append("y", location.getBlockY() + 1.1)
                    .append("y2", location.getBlockY() -1.01)
                    .append("z", location.getBlockZ() + 0.01)
                    .append("world", senderdude.getWorld().getName())
                    .append("uuid", senderdude.getUniqueId().toString());

            OnCameraChange.spawnSlime(location, data, senderdude.getWorld(), collection);
            return true;
        }
        return false;
    }
}

