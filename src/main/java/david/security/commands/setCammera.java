package david.security.commands;


import com.mongodb.*;
import david.security.events.OnCameraChange;
import david.security.objects.mongo;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class setCammera implements CommandExecutor {
    private static final Logger LOGGER = Logger.getLogger(setCammera.class.getName());
    DBCollection collection = mongo.connect("cameras");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player senderdude = (Player) sender;
            LOGGER.info(String.valueOf(senderdude.getLocation().getBlockX()));

            LOGGER.info(String.valueOf(senderdude.getLocation().getBlockY()));
            Location location = senderdude.getLocation();
            String message = String.valueOf(senderdude.getUniqueId()) + location.getBlockX() + location.getBlockY() + location.getBlockZ();
            DBObject data = new BasicDBObject("_id", message)
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

