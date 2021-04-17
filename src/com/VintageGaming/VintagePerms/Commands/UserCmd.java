package com.VintageGaming.VintagePerms.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.VintageGaming.VintagePerms.SettingsManager;

public class UserCmd extends PermsCommand {

	public static Permission addperm = new Permission("vperms.user.addperm", PermissionDefault.FALSE);
	public static Permission removeperm = new Permission("vperms.user.removeperm", PermissionDefault.FALSE);
	public static Permission listperms = new Permission("vperms.user.listperms", PermissionDefault.FALSE);
	
	public void run(CommandSender sender, String[] args) {
		if (!(sender.hasPermission("vperms.user.addperm") || sender.hasPermission("vperms.user.removeperm") || sender.hasPermission("vperms.user.listperms") || sender.hasPermission("vperms.*") || !(sender instanceof Player))) {
			sender.sendMessage(ChatColor.RED + "Insufficient Permissions!");
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "You didn't enter a user.");
			return;
		}
		
		String p = args[0];
		
		if (args.length < 2) {
			if (sender.hasPermission("vperms.user.listperms") || sender.hasPermission("vperms.*") || !(sender instanceof Player)) {
				if (SettingsManager.getInstance().getPerms(p).size() == 0) {
					sender.sendMessage(ChatColor.YELLOW + "No permissions for " + p);
					return;
				}
				sender.sendMessage(ChatColor.GOLD+"<"+ChatColor.AQUA+"Player: "+p+ChatColor.GOLD+">");
				String groups = "";
				int amount = SettingsManager.getInstance().getGroups(p).size();
				int where = 0;
				for (String group : SettingsManager.getInstance().getGroups(p)) {
					where += 1;
					if (where == amount)
					groups = groups + group;
					else
						groups = groups + group + ", ";
				}
				sender.sendMessage(ChatColor.GOLD+"Groups: " + ChatColor.RESET + groups);
				int perms = 0;
				for (String perm : SettingsManager.getInstance().getPerms(p)) {
					perms +=1;
					sender.sendMessage(String.valueOf(perms) + ") "+ ChatColor.YELLOW + perm);
				}
				perms = 0;
				return;
			}
			sender.sendMessage(ChatColor.RED + "Insufficient Permissions!");
			return;
		}
		
		else {
			if (args[1].equalsIgnoreCase("addperm")) {
				if (sender.hasPermission("vperms.user.addperm") || sender.hasPermission("vperms.*") || !(sender instanceof Player) || sender.hasPermission("vperms.user.*")) {
					StringBuilder perm = new StringBuilder();
					for(int i = 2; i < args.length; i++) {
		            	if (i > 2) perm.append(" ");
		                perm.append(args[i]);
		            }
					SettingsManager.getInstance().addPPerm(p, perm.toString());
					sender.sendMessage(ChatColor.GREEN + "Added " + perm.toString() + " to " + p);
					return;
				}
				sender.sendMessage(ChatColor.RED + "Insufficient Permissions!");
				return;
			}
			
			else if (args[1].equalsIgnoreCase("removeperm")) {
				if (sender.hasPermission("vperms.user.removeperm") || sender.hasPermission("vperms.*") || !(sender instanceof Player) || sender.hasPermission("vperms.user.*")) {
					StringBuilder perm = new StringBuilder();
					for(int i = 2; i < args.length; i++) {
		            	if (i > 2) perm.append(" ");
		                perm.append(args[i]);
		            }
					SettingsManager.getInstance().remPPerm(p, perm.toString());
					sender.sendMessage(ChatColor.GREEN + "Removed " + perm.toString() + " from " + p);
					return;
				}
				sender.sendMessage(ChatColor.RED + "Insufficient Permissions!");
				return;
			}
		}
	}
	
	public UserCmd() {
		super("user", "<name> [<addperm | removeper> <perm>]");
	}
}
