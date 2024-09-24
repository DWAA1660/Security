package david.security.commands;


import david.security.events.onCameraChange;
import david.security.objects.mongo;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.logging.Logger;

public class setCammera implements CommandExecutor {
    private static final Logger LOGGER = Logger.getLogger(setCammera.class.getName());
//    MongoCollection collection = mongo.connect("cameras");
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

            try {
                onCameraChange.spawnSlime(location, senderdude.getWorld(), senderdude.getUniqueId().toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }
}

