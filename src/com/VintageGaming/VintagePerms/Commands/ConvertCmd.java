package com.VintageGaming.VintagePerms.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.VintageGaming.VintagePerms.SettingsManager;

public class ConvertCmd implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player) || sender.hasPermission("vperms.*") || sender.hasPermission("vperms.convert")) {
			FileConfiguration config = SettingsManager.getInstance().getConfig();
			for (String group : SettingsManager.getInstance().getConfig().getKeys(false)) {
				if (config.get(group + ".default") == null && config.get(group + ".prefix") == null) {
					sender.sendMessage(ChatColor.RED + "Already Converted!");
					return true;
				}
				List<String> permissions = config.getStringList(group + ".permissions");
				config.set(group + ".permissions", null);
				if (config.get(group + ".default") != null) {
					config.set(group + ".options.default", config.getBoolean(group + ".default"));
					config.set(group + ".default", null);
				}
				if (config.get(group + ".prefix") != null) {
					config.set(group + ".options.prefix", config.getString(group + ".prefix"));
					config.set(group + ".prefix", null);
				}
				config.set(group + ".permissions", permissions);
			}
			SettingsManager.getInstance().save();
			sender.sendMessage(ChatColor.GREEN + "Complete!");
			return true;
		}
		sender.sendMessage(ChatColor.RED + "Insufficient Permissions!");
		return true;
	}
}
