package com.VintageGaming.VintagePerms.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.VintageGaming.VintagePerms.SettingsManager;

public class GroupCmd extends PermsCommand {

	public static Permission createGroup = new Permission("vperms.groups.create");
	public static Permission removeGroup = new Permission("vperms.groups.remove");
	public static Permission addGroupPerm = new Permission("vperms.groups.addperm");
	public static Permission removeGroupPerm = new Permission("vperms.groups.removeperm");
	public static Permission listGroupPerms = new Permission("vperms.groups.listperms");
	public static Permission setGroupPrefix = new Permission("vperms.groups.setprefix");
	
	public void run(CommandSender sender, String[] args) {
		if (args.length == 0) {
	      if ((!(sender instanceof Player) || sender.hasPermission("vperms.groups.listgroups") || sender.hasPermission("vperms.groups.*") || sender.hasPermission("vperms.*"))) {
			if (SettingsManager.groups.size() == 0)
			sender.sendMessage(ChatColor.RED + "There are no Groups Created!");
			else {
				int amount = SettingsManager.groups.size();
				sender.sendMessage(ChatColor.GOLD+"<"+ChatColor.AQUA+"Groups: "+amount+ChatColor.GOLD+">");
				for (int g=0;g<amount;g++) {
					sender.sendMessage(ChatColor.GOLD + String.valueOf(g+1) + ChatColor.YELLOW + ". " + SettingsManager.groups.get(g).getName());
				}
			}
		  }
		  else
			sender.sendMessage(ChatColor.RED + "Insufficient Permissions!");
			return;
		}
		
		String g = args[0].toLowerCase();
		
		if (args.length < 2) {
			if (SettingsManager.getInstance().getGroup(g.toLowerCase()) != null && (sender.hasPermission("vperms.groups.listperms") || !(sender instanceof Player) || sender.hasPermission("vperms.groups.*") || sender.hasPermission("vperms.*"))) {
				Boolean isDefault = false;
				if (SettingsManager.getInstance().getGroupSection(g.toLowerCase()).getBoolean("default") == true)
					isDefault = true;
				if (SettingsManager.getInstance().getGroup(g.toLowerCase()).getPerms().size() == 0) {
					sender.sendMessage(ChatColor.GOLD+"<"+ChatColor.AQUA+"Group: "+g.toLowerCase()+ChatColor.GOLD+">");
					sender.sendMessage(ChatColor.GOLD+"Default: " + ChatColor.RESET + isDefault);
					sender.sendMessage(ChatColor.YELLOW + "No permissions for " + g.toLowerCase());
					return;
				}
				sender.sendMessage(ChatColor.GOLD+"<"+ChatColor.AQUA+"Group: "+g.toLowerCase()+ChatColor.GOLD+">");
				sender.sendMessage(ChatColor.GOLD+"Default: " + ChatColor.RESET + isDefault);
				sender.sendMessage(ChatColor.GOLD+"<"+ChatColor.AQUA+"Permissions:"+ChatColor.GOLD+">");
				int perms = 0;
				for (String perm : SettingsManager.getInstance().getGroup(g.toLowerCase()).getPerms()) {
					perms +=1;
					sender.sendMessage(ChatColor.GOLD + String.valueOf(perms) + ") "+ ChatColor.YELLOW + perm);
				}
				perms = 0;
				return;
			}
			else { 
				if (sender.hasPermission("vperms.groups.create") || !(sender instanceof Player) || sender.hasPermission("vperms.groups.*") || sender.hasPermission("vperms.*")){
				SettingsManager.getInstance().createGroup(g.toLowerCase());
				sender.sendMessage(ChatColor.GREEN + "Created group!");
				return;
				}
				else {
					sender.sendMessage(ChatColor.RED + "Insufficient Permissions!");
					return;
				}
			}
		}
		
		else {
			if (args[1].equalsIgnoreCase("add") && (sender.hasPermission("vperms.groups." + g.toLowerCase() + ".addplayers") || !(sender instanceof Player) || sender.hasPermission("vperms.groups.*") || sender.hasPermission("vperms.groups." + g.toLowerCase() + ".*") || sender.hasPermission("vperms.*"))) {
				SettingsManager.getInstance().setGroup(args[2], g.toLowerCase());
				sender.sendMessage(ChatColor.GREEN + "Added " + args[2] + " to group " + g.toLowerCase() + ".");
				return;
			}
			else if (args[1].equalsIgnoreCase("remove") && (sender.hasPermission("vperms.groups." + g.toLowerCase() + ".removeplayers") || !(sender instanceof Player) || sender.hasPermission("vperms.groups.*") || sender.hasPermission("vperms.groups." + g.toLowerCase() + ".*") || sender.hasPermission("vperms.*"))) {
				SettingsManager.getInstance().remGroup(args[2], g.toLowerCase());
				sender.sendMessage(ChatColor.GREEN + "Removed " + args[2] + " from group " + g.toLowerCase() + ".");
				return;
			}
			else if (args[1].equalsIgnoreCase("addperm") && (sender.hasPermission("vperms.groups." + g.toLowerCase() + ".addperm") || !(sender instanceof Player) || sender.hasPermission("vperms.groups.*") || sender.hasPermission("vperms.groups." + g.toLowerCase() + ".*") || sender.hasPermission("vperms.*"))) {
				SettingsManager.getInstance().getGroup(g.toLowerCase()).addPerm(args[2]);
				sender.sendMessage(ChatColor.GREEN + "Added " + args[2] + " to group " + g.toLowerCase() + ".");
				return;
			}
			else if (args[1].equalsIgnoreCase("removeperm") && (sender.hasPermission("vperms.groups." + g.toLowerCase() + ".removeperm") || !(sender instanceof Player) || sender.hasPermission("vperms.groups.*") || sender.hasPermission("vperms.groups." + g.toLowerCase() + ".*") || sender.hasPermission("vperms.*"))) {
				SettingsManager.getInstance().getGroup(g.toLowerCase()).remPerm(args[2]);
				sender.sendMessage(ChatColor.GREEN + "Removed " + args[2] + " from group " + g.toLowerCase() + ".");
				return;
			}
			else if (args[1].equalsIgnoreCase("delete") && (sender.hasPermission("vperms.groups." + g.toLowerCase() + ".delete") || !(sender instanceof Player) || sender.hasPermission("vperms.groups.*") || sender.hasPermission("vperms.groups." + g.toLowerCase() + ".*") || sender.hasPermission("vperms.*"))) {
				SettingsManager.getInstance().getConfig().set(g.toLowerCase(), null);
				SettingsManager.getInstance().save();
				sender.sendMessage(ChatColor.GREEN + "Deleted Group " + g.toLowerCase() + ".");
				return;
			}
			else if (args[1].equalsIgnoreCase("setprefix") && (sender.hasPermission("vperms.groups.setprefix") || !(sender instanceof Player) || sender.hasPermission("vperms.groups.*") || sender.hasPermission("vperms.groups." + g.toLowerCase() + ".*") || sender.hasPermission("vperms.*"))) {
				SettingsManager.getInstance().addGroupPrefix(args[0], args[2]);
				SettingsManager.getInstance().save();
				sender.sendMessage(ChatColor.GREEN + "Set Group Prefix!");
				return;
			}
		}
	}
	
	public GroupCmd() {
		super("group", "<name> [<add | remove | delete | addperm | removeperm | setprefix> <user | perm | prefix>]");
	}
}
