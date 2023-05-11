package david.security.tasks;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import david.security.objects.mongo;
import com.mongodb.*;
import david.security.Security;
import david.security.commands.setCammera;
import github.scarsz.discordsrv.DiscordSRV;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

import java.util.logging.Logger;


public class getNearPlayers extends BukkitRunnable {

    public boolean shouldFilterBlock(Block block) {
        return block.getType() != Material.PETRIFIED_OAK_SLAB;
    }

    private static final Logger LOGGER = Logger.getLogger(setCammera.class.getName());
    MongoCollection collection = mongo.connect("cameras");
    Security plugin;

    public getNearPlayers(Security plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        int people = Bukkit.getServer().getOnlinePlayers().size();
        if (people == 0) {
            return;
        }
        List<Document> results = new ArrayList<>();
        FindIterable<Document> cursor = collection.find();
        MongoCursor<Document> mongoCursor = cursor.iterator();
        try {
            while (mongoCursor.hasNext()) {
                Document res = mongoCursor.next();
                int x = (int) res.get("x");
                double y = (double) res.get("y");
                int z = (int) res.get("z");
                double y2 = (double) res.get("y2");
                String ownerId = (String) res.get("ownerId");
                World world = Bukkit.getWorld(res.get("world").toString());
                Player owner = Bukkit.getPlayer(UUID.fromString(res.get("uuid").toString()));
                Location cameralocation = new Location(world, x, y, z);
                Location cameralocation2 = new Location(world, x, y2, z);
                Collection<Entity> possible = Objects.requireNonNull(cameralocation.getWorld()).getNearbyEntities(cameralocation, 10.0, 10.0, 10.0);
                for (Entity entity : possible) {
                    if (entity instanceof Player) {
//                        if(entity != owner || owner == null){
                        if (1 == 1) {
                            // Get a list of all the entities in the world
                            assert world != null;
                            List<Entity> entities = world.getEntities();

                            // Find the entity at the specific location
                            Entity entityAtLocation = null;
                            for (Entity worldEntity : entities) {
                                if (worldEntity.getLocation().equals(cameralocation)) {
                                    entityAtLocation = worldEntity;
                                    LivingEntity slime = (LivingEntity) entityAtLocation;
                                    if (slime.hasLineOfSight(entity)) {
                                        String userid = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(UUID.fromString(res.get("uuid").toString()));
                                        Objects.requireNonNull(DiscordSRV.getPlugin().getJda().getUserById(userid))
                                                .openPrivateChannel().flatMap(channel -> channel.
                                                        sendMessage(String.format("%s Was spotted near your camera at (%s, %s, %s)", entity.getName(), String.valueOf(x), String.valueOf(y), String.valueOf(z)))).queue();

                                    }
                                    break;
                                } else if (worldEntity.getLocation().equals(cameralocation2)) {
                                    entityAtLocation = worldEntity;
                                    LivingEntity slime = (LivingEntity) entityAtLocation;
                                    if (slime.hasLineOfSight(entity)) {
                                        String userid = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(UUID.fromString(res.get("uuid").toString()));
                                        Objects.requireNonNull(DiscordSRV.getPlugin().getJda().getUserById(userid))
                                                .openPrivateChannel().flatMap(channel -> channel.
                                                        sendMessage(String.format("%s Was spotted near your camera at (%s, %s, %s)", entity.getName(), String.valueOf(x), String.valueOf(y), String.valueOf(z)))).queue();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            mongoCursor.close();
        }
    }
}