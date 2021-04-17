package com.VintageGaming.VintagePerms.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class MainCommands implements CommandExecutor {

	private ArrayList<PermsCommand> cmds = new ArrayList<PermsCommand>();
	public static Permission usage = new Permission("vperms.usages", PermissionDefault.FALSE);
	
	public MainCommands() {
		cmds.add(new GroupCmd());
		cmds.add(new UserCmd());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("vperms")) {
			if (args.length == 0) {
				if (sender.hasPermission("vperms.usages") || !(sender instanceof Player) || sender.hasPermission("vperms.*")) {
					sender.sendMessage(ChatColor.GOLD+"<"+ChatColor.AQUA+"VintagePerms"+ChatColor.GOLD+">");
					for (PermsCommand c : cmds) {
						sender.sendMessage(ChatColor.YELLOW + "/vperms " + c.getName() + " " + c.getArgs());
					}
					return true;
				}
				sender.sendMessage(ChatColor.RED + "Insufficient Permissions!");
				return true;
			}
			
			ArrayList<String> a = new ArrayList<String>(Arrays.asList(args));
			a.remove(0);
			
			for (PermsCommand c : cmds) {
				if (c.getName().equalsIgnoreCase(args[0])) {
					try { 
						c.run(sender, a.toArray(new String[a.size()])); 
						}
					catch (Exception e) { 
						sender.sendMessage(ChatColor.RED + "An error has occurred."); 
						//sender.sendMessage(a.toArray(new String[a.size()]));
						e.printStackTrace(); 
						}
					return true;
				}
			}
			
			sender.sendMessage(ChatColor.RED + "Invalid command!");
		}
		return true;
	}
}
