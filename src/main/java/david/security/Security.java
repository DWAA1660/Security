package david.security;

import david.security.commands.setCammera;
import david.security.tasks.getNearPlayers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import david.security.events.onCameraChange;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Security extends JavaPlugin {

    public static Connection connection;


    public static Security instance;

    @Override
    public void onEnable() {
        instance = this;
        final Logger LOGGER = Logger.getLogger(setCammera.class.getName());

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:./plugins/Security/database.db");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


//        this.getCommand("setcamera").setExecutor(new setCammera());
        LOGGER.info("Security cameras got enabled");
        getServer().getPluginManager().registerEvents(new onCameraChange(), this);
        BukkitTask getNearPlayers = new getNearPlayers(this).runTaskTimer(this, 100L, 100);


        ItemStack item = new ItemStack(Material.PETRIFIED_OAK_SLAB);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Camera");
        item.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "camera"), item);
        recipe.shape("CCC", "REG", "CCC");
        recipe.setIngredient('C', Material.COBBLESTONE);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('E', Material.ENDER_EYE);
        recipe.setIngredient('G', Material.GLASS);
        Bukkit.addRecipe(recipe);


        try {

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS cameras (x REAL, y1 REAL, y2 REAL, z REAL, world TEXT, uuid TEXT)");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
