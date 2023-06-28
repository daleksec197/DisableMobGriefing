package org.loyal0713.disablemobgriefing;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean success = false;
        if(!sender.isOp() && DisableMobGriefing.config.getBoolean("require_op")) {
            sender.sendMessage("You must be an op to use this command.");
            return success;
        }

        if(command.getLabel().equalsIgnoreCase("mobgriefing")) {
            if(args.length == 0) {
                sender.sendMessage("Usage: /mobgriefing <minecraft:name> <true/false>");
                sender.sendMessage("Example: /mobgriefing creeper false");
                sender.sendMessage("Example: /mobgriefing creeper");
                success = true;
            }

            String entityName = args[0];
            boolean isInConfig = DisableMobGriefing.config.contains(entityName + "_griefing");

            if(!isInConfig) {
                sender.sendMessage(entityName + " is not a valid entity.");
                return success;
            }

            if(args.length == 1) {
                boolean allowedToGrief = DisableMobGriefing.config.getBoolean(entityName + "_griefing");
                sender.sendMessage(entityName + " griefing is " + (allowedToGrief ? "enabled" : "disabled"));
                success = true;
            }

            if(args.length == 2) {
                boolean allowedToGrief = Boolean.parseBoolean(args[1]);
                DisableMobGriefing.config.set(entityName + "_griefing", allowedToGrief);
                DisableMobGriefing.config.options().copyDefaults(true);
                success = true;
            }
        }
        return success;
    }
}
