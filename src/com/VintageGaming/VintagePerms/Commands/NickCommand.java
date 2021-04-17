package com.VintageGaming.VintagePerms.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.VintageGaming.VintagePerms.SettingsManager;

public class NickCommand implements CommandExecutor {
	
	public static Permission Admin = new Permission("vperms.nick.admin", PermissionDefault.FALSE);
	public static Permission nick = new Permission("vperms.nick", PermissionDefault.FALSE);
	public static Permission realnames = new Permission("vperms.realnames", PermissionDefault.FALSE);
	public static Permission RemoteChange = new Permission("vperms.remotechange", PermissionDefault.FALSE);

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!SettingsManager.getInstance().getPConfig().getBoolean("Nick_Command")) return true;
		
		if (cmd.getName().equalsIgnoreCase("nick") && (sender.hasPermission("vperms.nick.admin") || sender.hasPermission("vperms.nick") || sender.hasPermission("vperms.*"))) {
			if (!(sender instanceof Player)) return false;
			Player p = (Player) sender;
			if (args.length == 0) {
				SettingsManager.getInstance().getPConfig().set(p.getName() + ".nick", null);
				SettingsManager.getInstance().Psave();
				p.setDisplayName(p.getName());
				return true;
			}
			StringBuilder message = new StringBuilder();
            for(int i = 0; i < args.length; i++) {
            	if (message.length() >= 20) {
            		
            	}
            	else if (i > 0) message.append(" ");
            	if (!(message.length() >= 20))
                message.append(args[i]);
            }
			SettingsManager.getInstance().setPrefix(p.getName(), message.toString());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Nickname Changed to " + message.toString() + "&2."));
		}
		if (cmd.getName().equalsIgnoreCase("realnames") && (sender.hasPermission("vperms.nick.admin") || sender.hasPermission("vperms.realnames") || sender.hasPermission("vperms.*") || !(sender instanceof Player))) {
			sender.sendMessage(ChatColor.GOLD + "<" + ChatColor.AQUA + "RealNames" + ChatColor.GOLD + ">");
			for (String player : SettingsManager.getInstance().getPConfig().getKeys(false)) {
				if (Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(player)) && (SettingsManager.getInstance().getPConfig().get(player + ".prefix") != null || SettingsManager.getInstance().getPConfig().get(player + ".prefix") != " ")) {
					sender.sendMessage(player + " is " + ChatColor.translateAlternateColorCodes('&', SettingsManager.getInstance().getPConfig().getString(player + ".prefix")));
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("nickchange") && (sender.hasPermission("vperms.nick.remotechange") || sender.hasPermission("vperms.nick.admin") || sender.hasPermission("vperms.*") || !(sender instanceof Player))) {
			if (args.length < 1) return true;
			Player p = null;
			try {
			p = Bukkit.getServer().getPlayer(args[0]);
			}
			catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Try a different Player!");
				return true;
			}
			if (args.length == 1) {
				p.setDisplayName(p.getName());
				return true;
			}
			StringBuilder message = new StringBuilder();
            for(int i = 0; i < args.length; i++) {
            	if (message.length() >= 20) {
            		
            	}
            	else if (i > 0) message.append(" ");
            	if (!(message.length() >= 20))
                message.append(args[i]);
            }
			SettingsManager.getInstance().setPrefix(p.getName(), message.toString());
		}
		return true;
	}
	
}
