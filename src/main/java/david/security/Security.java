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

import david.security.events.OnCameraChange;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Logger;

public class Security extends JavaPlugin {
    public static Security instance;

    @Override
    public void onEnable() {
        instance = this;
        final Logger LOGGER = Logger.getLogger(setCammera.class.getName());

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        this.getCommand("setcamera").setExecutor(new setCammera());
        System.out.println("DWAAS PLUGIN GOT ENABLED AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHh");
        getServer().getPluginManager().registerEvents(new OnCameraChange(), this);
        BukkitTask getNearPlayers = new getNearPlayers(this).runTaskTimer(this, 100L, 100);


        ItemStack item = new ItemStack(Material.PETRIFIED_OAK_SLAB);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Camera");
        item.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "camera"), item);
        recipe.shape("CCC", "REG", "CCC");
        recipe.setIngredient('C', Material.COBBLESTONE);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('E', Material.ENDER_EYE);
        recipe.setIngredient('G', Material.GLASS);
        Bukkit.addRecipe(recipe);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
