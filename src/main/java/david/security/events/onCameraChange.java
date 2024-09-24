package david.security.events;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import david.security.Security;
import david.security.objects.mongo;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static david.security.Security.connection;

public class onCameraChange implements Listener {

    @EventHandler
    public void onPlaceCamera(BlockPlaceEvent event) throws SQLException {
        if (event.getBlockPlaced().getType() == Material.PETRIFIED_OAK_SLAB) {
            Block block = event.getBlock();
            Location location = block.getLocation();

            onCameraChange.spawnSlime(location, block.getWorld(), event.getPlayer().getUniqueId().toString());
        }
    }

    public static void spawnSlime(Location location, World world, String uuid) throws SQLException {
        Location location1 = new Location(world, location.getBlockX(), location.getBlockY() + 1.1, location.getBlockZ());
        Slime cameraSlime = world.spawn(location1, Slime.class);
        cameraSlime.setInvisible(true);
        cameraSlime.setInvulnerable(true);
        cameraSlime.setSilent(true);
        cameraSlime.setSize(2);
        cameraSlime.setAI(false);
        cameraSlime.setGravity(false);
        cameraSlime.setCollidable(false);

        Location location2 = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1.01, location.getBlockZ());
        Slime cameraSlime2 = world.spawn(location2, Slime.class);
        cameraSlime2.setInvisible(true);
        cameraSlime2.setInvulnerable(true);
        cameraSlime2.setSilent(true);
        cameraSlime2.setSize(2);
        cameraSlime2.setAI(false);
        cameraSlime2.setGravity(false);
        cameraSlime2.setCollidable(false);

        String insertCameraDataSQL = "INSERT INTO cameras (x, y1, y2, z, world, uuid) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertCameraDataSQL)) {
            stmt.setDouble(1, location.getBlockX());
            stmt.setDouble(2, location.getBlockY() + 1.1);
            stmt.setDouble(3, location.getBlockY() - 1.01);
            stmt.setDouble(4, location.getBlockZ());
            stmt.setString(5, world.getName());
            stmt.setString(6, uuid); // Replace with the player's UUID

            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger("Security").info(e.toString());
        }

    }
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        }

    @EventHandler
    public void cameraBreakEvent(BlockBreakEvent event) throws SQLException {


        Block block = event.getBlock();
        if (block.getType() == Material.PETRIFIED_OAK_SLAB) {
            event.setDropItems(false);
            ItemStack camera = new ItemStack(Material.PETRIFIED_OAK_SLAB);
            ItemMeta meta = camera.getItemMeta();
            assert meta != null;
            meta.setDisplayName("Camera");
            camera.setItemMeta(meta);
            Objects.requireNonNull(block.getLocation().getWorld()).dropItem(block.getLocation(), camera);

            List<Entity> entities = block.getWorld().getEntities();

            // Find the entity at the specific location
            Entity slime1 = null;
            for (Entity worldEntity : entities) {
                Location newLoc = new Location(worldEntity.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1.1, block.getLocation().getBlockZ());
                if (worldEntity.getLocation().equals(newLoc)) {
                    slime1 = worldEntity;
                    Slime slime = (Slime) slime1;
                    slime.setInvulnerable(false);
                    slime.setSize(1);
                    slime.teleport(new Location(worldEntity.getWorld(), 0, -10, 0));
                    slime.setHealth(0);
                }
            }

            Entity slime2 = null;
            for (Entity worldEntity : entities) {
                Location newLoc2 = new Location(worldEntity.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() - 1.01, block.getLocation().getBlockZ());
                if (worldEntity.getLocation().equals(newLoc2)) {
                    slime2 = worldEntity;
                    Slime slime = (Slime) slime2;
                    slime.setInvulnerable(false);
                    slime.setSize(1);
                    slime.teleport(new Location(worldEntity.getWorld(), 0, -10, 0));
                    slime.setHealth(0);
                }
            }
            String deleteSQL = "DELETE FROM cameras WHERE x = ? AND y1 = ? AND z = ?"; // Replace with your table name and column name

            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                // Set the parameter for the DELETE statement
                preparedStatement.setDouble(1, block.getLocation().getBlockX());
                preparedStatement.setDouble(2, block.getLocation().getBlockY() + 1.1);
                preparedStatement.setDouble(3, block.getLocation().getBlockZ());

                preparedStatement.executeUpdate();

            }
        }

    }
}