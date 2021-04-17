package com.VintageGaming.VintagePerms.injection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.VintageGaming.VintagePerms.SettingsManager;

public class InjectEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (SettingsManager.getInstance().getPConfig().getStringList(e.getPlayer().getName() + ".groups").size() <1) {
			String group = "";
			//if Player has no Group
			for (String g : SettingsManager.getInstance().getConfig().getKeys(false)) {
				if (SettingsManager.getInstance().getConfig().get(g + ".options.default") != null && SettingsManager.getInstance().getConfig().getBoolean(g + ".options.default")) {
					group = g;
				}
				//Old Method
				/*for (String key : SettingsManager.getInstance().getGroupSection(g).getKeys(false)) {
					if (key.equalsIgnoreCase("default") && SettingsManager.getInstance().getConfig().getBoolean(g + "." + key)) {
						group = g;
					}
				}*/
			}
			//End If Player Has No Group
			if (group != ""){
			SettingsManager.getInstance().setGroup(e.getPlayer().getName(), group); //checked
			}
		}
		List<Player> p = new ArrayList<Player>();
		p.add(e.getPlayer());
		SettingsManager.getInstance().injectPlayer(p);
		//if (SettingsManager.getInstance().getPConfig().getString(e.getPlayer().getName() + ".nick") != null && SettingsManager.getInstance().getPConfig().getBoolean("Nick_Command") && (e.getPlayer().hasPermission("vperms.nick") || e.getPlayer().hasPermission("vperms.nick.admin") || e.getPlayer().hasPermission("vperms.*"))) {
		//	SettingsManager.getInstance().setPrefix(e.getPlayer().getName(), SettingsManager.getInstance().getPConfig().getString(e.getPlayer().getName() + ".nick"));
		//} Old Method for Nicknames
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		SettingsManager.getInstance().unInjectPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		//New Nickname Method
		if (!p.hasPermission("vperms.nick") && !p.hasPermission("vperms.*") && !p.hasPermission("vperms.nick.admin")) {
			SettingsManager.getInstance().setPrefix(p.getName(), p.getName());
		}
		else if (SettingsManager.getInstance().getPConfig().getString(p.getName() + ".nick") != null && SettingsManager.getInstance().getPConfig().getBoolean("Nick_Command") && (p.hasPermission("vperms.nick") || p.hasPermission("vperms.*") || p.hasPermission("vperms.nick.admin"))){
			SettingsManager.getInstance().setPrefix(p.getName(), SettingsManager.getInstance().getPConfig().getString(p.getName() + ".nick"));
		}
		//End of Method
		if (SettingsManager.getInstance().getGroups(p.getName()).size() <1) {
			return;
		}
		List<String> groups = SettingsManager.getInstance().getGroups(p.getName());
		String GPrefix;
		if (SettingsManager.getInstance().getConfig().getString(groups.get(0) + ".options.prefix") != null) {
			GPrefix = SettingsManager.getInstance().getConfig().getString(groups.get(0) + ".options.prefix");
			event.setFormat(ChatColor.translateAlternateColorCodes('&', GPrefix) + " " + "%s: %s");
		}
	}

}
