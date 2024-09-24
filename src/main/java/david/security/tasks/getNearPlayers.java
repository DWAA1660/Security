package david.security.tasks;

import david.security.Security;

import david.security.objects.mongo;
import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import static david.security.Security.connection;

public class getNearPlayers extends BukkitRunnable {

    private Security plugin;

    public getNearPlayers(Security plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        int people = Bukkit.getServer().getOnlinePlayers().size();
        if (people == 0) {
            return;
        }

        try {

            Statement statement = connection.createStatement();
            // Get the value of the greeting option
            boolean triggerOnOwner = plugin.getConfig().getBoolean("owner-triggers-camera");


            ResultSet resultSet = statement.executeQuery("SELECT * from cameras");
            while (resultSet.next()) {
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y1");
                double z = resultSet.getDouble("z");
                double y2 = resultSet.getDouble("y2");
                String uuid = resultSet.getString("uuid");

                World world = Bukkit.getWorld(resultSet.getString("world"));
                Player owner = Bukkit.getPlayer(UUID.fromString(uuid));

                Location cameralocation = new Location(world, x, y, z);
                Location cameralocation2 = new Location(world, x, y2, z);

                List<Entity> entities = (List<Entity>) Objects.requireNonNull(cameralocation.getWorld()).getNearbyEntities(cameralocation, 10.0, 10.0, 10.0);
                for (Entity entity : entities) {
                    if (entity instanceof Player && owner != null) {
                        if (triggerOnOwner) {
                            for (Location cameraLocation : List.of(cameralocation, cameralocation2)) {

                                Entity entityAtLocation = getEntityAtLocation(cameraLocation);
                                if (entityAtLocation instanceof LivingEntity) {
                                    LivingEntity slime = (LivingEntity) entityAtLocation;
                                    if (slime.hasLineOfSight(entity)) {
                                        String userId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(UUID.fromString(uuid));
                                        Objects.requireNonNull(DiscordSRV.getPlugin().getJda().getUserById(userId))
                                                .openPrivateChannel().flatMap(channel -> channel.sendMessage(
                                                        String.format("%s was spotted near your camera at (%s, %s, %s)",
                                                                entity.getName(), x, y, z))).queue();
                                    }
                                }
                            }
                        }
                        else {
                            if(owner != entity){
                                for (Location cameraLocation : List.of(cameralocation, cameralocation2)) {

                                    Entity entityAtLocation = getEntityAtLocation(cameraLocation);
                                    if (entityAtLocation instanceof LivingEntity) {
                                        LivingEntity slime = (LivingEntity) entityAtLocation;
                                        if (slime.hasLineOfSight(entity)) {
                                            String userId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(UUID.fromString(uuid));
                                            Objects.requireNonNull(DiscordSRV.getPlugin().getJda().getUserById(userId))
                                                    .openPrivateChannel().flatMap(channel -> channel.sendMessage(
                                                            String.format("%s was spotted near your camera at (%s, %s, %s)",
                                                                    entity.getName(), x, y, z))).queue();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            Logger.getLogger("Security").warning(e.toString());

        }
    }

    private Entity getEntityAtLocation(Location location) {
        for (Entity worldEntity : Objects.requireNonNull(location.getWorld()).getEntities()) {
            if (worldEntity.getLocation().equals(location)) {
                return worldEntity;
            }
        }
        return null;
    }
}
