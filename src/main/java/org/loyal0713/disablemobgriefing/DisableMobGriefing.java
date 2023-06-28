package org.loyal0713.disablemobgriefing;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DisableMobGriefing extends JavaPlugin implements Listener {

    static FileConfiguration config = null;

    @Override
    public void onEnable() {

        config = getConfig();
        config.options().copyDefaults(false);
        config.addDefault("require_op", true);
        config.addDefault("verbose", false);
        config.addDefault("creeper_griefing", true);
        config.addDefault("ender_crystal_griefing", true);
        config.addDefault("ender_dragon_griefing", true);
        config.addDefault("enderman_griefing", true);
        config.addDefault("falling_block_griefing", true);
        config.addDefault("fireball_griefing", true);
        config.addDefault("player_griefing", true);
        config.addDefault("rabbit_griefing", true);
        config.addDefault("sheep_griefing", true);
        config.addDefault("silverfish_griefing", true);
        config.addDefault("villager_griefing", true);
        config.addDefault("wither_griefing", true);
        config.addDefault("wither_skull_griefing", true);
        config.options().copyDefaults(true);
        saveConfig();

        // register event
        Bukkit.getPluginManager().registerEvents(this, this);

        // register command
        this.getCommand("mobgriefing").setExecutor(new CommandManager());
    }

    /*
    Most mobs that can grief are handled here.
     */
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        EntityType entityType = event.getEntityType();
        boolean allowedToGrief = config.getBoolean(entityType.toString().toLowerCase() + "_griefing");
        boolean isVerbose = config.getBoolean("verbose");
        if (!allowedToGrief) {
            if (isVerbose) {
                getLogger().info(entityType + " griefing is disabled.");
            }
            event.setCancelled(true);
        }
    }

    /*
    Special case for things like ender crystals, fireballs, wither skulls, creepers, etc.
     */
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        EntityType entityType = event.getEntityType();
        boolean allowedToGrief = config.getBoolean(entityType.toString().toLowerCase() + "_griefing");
        boolean isVerbose = config.getBoolean("verbose");
        if (!allowedToGrief) {
            if (isVerbose) {
                getLogger().info(entityType + " griefing is disabled.");
            }
            event.setCancelled(true);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }
}
