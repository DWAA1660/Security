package david.security.events;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import david.security.objects.mongo;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class OnCameraChange implements Listener {
    MongoCollection collection = mongo.connect("cameras");

    @EventHandler
    public void onPlaceCamera(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.PETRIFIED_OAK_SLAB) {
            Block block = event.getBlock();
            Location location = block.getLocation();
            String message = String.valueOf(event.getPlayer().getUniqueId()) + location.getBlockX() + location.getBlockY() + location.getBlockZ();
            Document data = new Document("_id", message)
                    .append("x", location.getBlockX())
                    .append("y", location.getBlockY() + 1.1)
                    .append("y2", location.getBlockY() -1.01)
                    .append("z", location.getBlockZ())
                    .append("world", block.getWorld().getName())
                    .append("uuid", event.getPlayer().getUniqueId().toString());
            OnCameraChange.spawnSlime(location, data, block.getWorld(), collection);
        }
    }

    public static void spawnSlime(Location location, Document data, World world, MongoCollection<Document> collection) {
        Location location1 = new Location(world, location.getBlockX(), location.getBlockY() + 1.1, location.getBlockZ());
        Slime cameraSlime = world.spawn(location1, Slime.class);
        cameraSlime.setInvisible(true);
        cameraSlime.setInvulnerable(true);
        cameraSlime.setSilent(true);
        cameraSlime.setSize(2);
        cameraSlime.setAI(false);
        cameraSlime.setGravity(false);
        cameraSlime.setCollidable(false);

        Location location2 = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() -1.01, location.getBlockZ());
        Slime cameraSlime2 = world.spawn(location2, Slime.class);
        cameraSlime2.setInvisible(true);
        cameraSlime2.setInvulnerable(true);
        cameraSlime2.setSilent(true);
        cameraSlime2.setSize(2);
        cameraSlime2.setAI(false);
        cameraSlime2.setGravity(false);
        cameraSlime2.setCollidable(false);

        collection.insertOne(data);
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(ChatColor.RED + "Welcome to geeksmp you bozo (from DWAA please give me money so i dont go broke hosting this)");
    }

    @EventHandler
    public void cameraBreakEvent(BlockBreakEvent event) {


        Block block = event.getBlock();
        if (block.getType() == Material.PETRIFIED_OAK_SLAB) {
            event.setDropItems(false);
            ItemStack camera = new ItemStack(Material.PETRIFIED_OAK_SLAB);
            ItemMeta meta = camera.getItemMeta();
            meta.setDisplayName("Camera");
            camera.setItemMeta(meta);
            Objects.requireNonNull(block.getLocation().getWorld()).dropItem(block.getLocation(), camera);

            DBObject data = new BasicDBObject("x", block.getLocation().getBlockX())
                    .append("y", block.getLocation().getBlockY() + 1.1)
                    .append("z", block.getLocation().getBlockZ())
                    .append("world", block.getLocation().getWorld().getName());

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
            collection.deleteOne((Bson) data);
        }


    }
}
