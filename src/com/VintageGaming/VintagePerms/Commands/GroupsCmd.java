package com.VintageGaming.VintagePerms.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.VintageGaming.VintagePerms.SettingsManager;

public class GroupsCmd implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("groups") && sender instanceof Player && sender.hasPermission("vperms.groups.*")) {
			Player p = (Player) sender;
			Inventory groups = Bukkit.createInventory(null, 54, "Groups");
			ArrayList<String> groupNames = (ArrayList<String>) SettingsManager.getInstance().getConfig().getKeys(false);
			for (int i=0;i<groupNames.size();i++) {
				ItemStack group = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
				ItemMeta name = group.getItemMeta();
				name.setDisplayName(ChatColor.DARK_PURPLE + groupNames.get(i));
				group.setItemMeta(name);
				groups.setItem(i, group);
			}
		}
		return true;
	}
}
