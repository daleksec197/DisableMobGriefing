package org.loyal0713.disablemobgriefing;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

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
        Objects.requireNonNull(this.getCommand("mobgriefing")).setExecutor(new CommandManager());
    }

    private boolean isNotAllowedToGrief(EntityType entityType, boolean gameRuleValue) {
        boolean ruleExists = config.contains(entityType.toString().toLowerCase()+"_griefing");
        boolean allowedToGrief = ruleExists ? config.getBoolean(entityType.toString().toLowerCase() + "_griefing") : gameRuleValue; //use GameRule instead if no config exists
        boolean isVerbose = config.getBoolean("verbose");
        if (!allowedToGrief) {
            if (isVerbose) {
                getLogger().info(entityType + " griefing is disabled.");
            }
        }
        return !allowedToGrief;
    }
    /*
    Most mobs that can grief are handled here.
     */
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        EntityType entityType = event.getEntityType();
        World world = event.getBlock().getWorld();
        boolean gameRuleValue = Boolean.TRUE.equals(world.getGameRuleValue(GameRule.MOB_GRIEFING));

        if (isNotAllowedToGrief(entityType, gameRuleValue))
            event.setCancelled(true);
    }

    /*
    Special case for things like ender crystals, fireballs, wither skulls, creepers, etc.
     */
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        EntityType entityType = event.getEntityType();
        World world = event.getEntity().getWorld();
        boolean gameRuleValue = Boolean.TRUE.equals(world.getGameRuleValue(GameRule.MOB_GRIEFING));

        if (isNotAllowedToGrief(entityType,gameRuleValue))
            event.setCancelled(true);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }
}
