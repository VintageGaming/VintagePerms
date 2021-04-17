package com.VintageGaming.VintagePerms;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;

import com.VintageGaming.VintagePerms.injection.Groups;

public class SettingsManager {

	private SettingsManager() { }
	private static SettingsManager instance = new SettingsManager();
	
	public static SettingsManager getInstance() {
		return instance;
	}
	
	private Plugin p;
	private FileConfiguration config;
	private FileConfiguration pConfig;
	private File cFile;
	private File pFile;
	
	private HashMap<String, PermissionAttachment> attachments = new HashMap<String, PermissionAttachment>();
	public static ArrayList<Groups> groups = new ArrayList<Groups>();
	
	public void makeConfig(Plugin p) {
		this.p = p;
		
		if (!p.getDataFolder().exists()) p.getDataFolder().mkdir();
		
		cFile = new File(p.getDataFolder(), "groups.yml");
		pFile = new File(p.getDataFolder(), "player.yml");
		
		if (!cFile.exists()) {
			try {cFile.createNewFile();}
			catch(Exception e) { e.printStackTrace(); }
			config = YamlConfiguration.loadConfiguration(cFile);
			ArrayList<String> exampleG = new ArrayList<String>();
			ArrayList<String> defaultG = new ArrayList<String>();
			ArrayList<String> ownerG = new ArrayList<String>();
			ArrayList<String> ownerI = new ArrayList<String>();
			exampleG.add("essentials.kick");
			defaultG.add("essentials.kill");
			ownerG.add("essentials.ban");
			ownerI.add("example");
			ownerI.add("default");
			config.set("example.options.default", false);
			config.set("example.permissions", exampleG);
			config.set("default.options.default", true);
			config.set("default.options.prefix", "&a<&2Guest&a>&f");
			config.set("default.permissions", defaultG);
			config.set("owner.options.default", false);
			config.set("owner.options.prefix", "[&4Owner&f]");
			config.set("owner.permissions", ownerG);
			config.set("owner.inheritance", ownerI);
			save();
		}
		config = YamlConfiguration.loadConfiguration(cFile);
		
		if(!pFile.exists()) {
			try {pFile.createNewFile();}
			catch (Exception e) {e.printStackTrace();}
			pConfig = YamlConfiguration.loadConfiguration(pFile);
			ArrayList<String> tutPerms = new ArrayList<String>();
			ArrayList<String> tutGroup = new ArrayList<String>();
			tutPerms.add("minecraft.command.gamemode");
			tutPerms.add("- minecraft.command.gamemode");
			tutGroup.add("default");
			pConfig.set("Nick_Command", false);
			pConfig.set("VintageGaming.permissions", tutPerms);
			pConfig.set("VintageGaming.groups", tutGroup);
			Psave();
		}
		pConfig = YamlConfiguration.loadConfiguration(pFile);
		
		//for (String groupName : config.getKeys(false)) {
		//	groups.add(new Groups(groupName));
		//}
	}

	public FileConfiguration getConfig() {
		return config;
	}
	
	public FileConfiguration getPConfig() {
		return pConfig;
	}
	
	public void save() {
		try { config.save(cFile); }
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public void Psave() {
		try { pConfig.save(pFile); }
		catch(Exception e) { e.printStackTrace(); }
	}

	public Plugin getPlugin() {
		return p;
	}
	//AddPerm to player
	public void addPPerm(String player, String perm) {
        Player p = Bukkit.getServer().getPlayer(player);
		
		if (p != null) {
			List<Player> players = new ArrayList<Player>();
			players.add(p);
			injectPlayer(players);
			attachments.get(p.getName()).setPermission(perm, true);
		}
		
		List<String> perms = getPerms(player);
		perms.add(perm);
		pConfig.set(player + ".permissions", perms);
		Psave();
		if (perm.contains("-")){
			List<Player> pl = new ArrayList<Player>();
			pl.add(p);
			injectPlayer(pl);
			attachments.get(player).setPermission(perm.replace("- ", ""), false);
		}
		else
				attachments.get(player).setPermission(perm, true);
	}
	//Remove perm from player
	public void remPPerm(String player, String perm) {
        Player p = Bukkit.getServer().getPlayer(player);
		
		List<String> perms = getPerms(player);
		if (perms.contains(perm)){
		perms.remove(perm);
		pConfig.set(player + ".permissions", perms);
	    }
		else {
			perms.add("- " + perm);
			pConfig.set(player + ".permissions", perms);
		}
		Psave();
		if (p != null) {
			List<Player> players = new ArrayList<Player>();
			players.add(p);
			if (perm.contains("-")){
				injectPlayer(players);
			}
			else {
			injectPlayer(players);
			attachments.get(p.getName()).setPermission(perm, false);
			}
		}
	}
	//Get players perms
	public List<String> getPerms(String player) {
		if (!pConfig.contains(player)) {
			pConfig.set(player + ".permissions", new ArrayList<String>());
			Psave();
		}
		return pConfig.getStringList(player + ".permissions");
	}
	//add group to player
	public void setGroup(String player, String group) {
		List<String> groups = getGroups(player);
		//New Method
		for (String g : groups) {
			if (g.equalsIgnoreCase(group)) return;
		}
		for (Groups g : this.groups) {
			if (g.getName().equalsIgnoreCase(group) && !groups.contains(g.getName()))
				groups.add(g.getName());
		}
		//End
		if (groups.size()>1) {
			List<String> groups2 = groups;
		for (int g=groups.size()-1;g>=0;g--) {
			if (g==groups.size()-1) {
			groups2.set(0, groups.get(groups.size()-1));
			continue;
			}
			if (g==0) continue;
			groups2.set(g, groups.get(g-1));
		}
		groups = groups2;
		}
		pConfig.set(player + ".groups", groups);
		Psave();
		Player p = Bukkit.getServer().getPlayer(player);
		
		if (p != null) {
			List<Player> players = new ArrayList<Player>();
			players.add(p);
			injectPlayer(players);
		}
	}
	//remove a group in game
	public void remGroup(String player, String group) {
		List<String> groups = getGroups(player);
		if (!groups.contains(group)) return;
		groups.remove(group);
		pConfig.set(player + ".groups", groups);
		Psave();
		Player p = Bukkit.getServer().getPlayer(player);
		
		if (p != null) {
			List<Player> players = new ArrayList<Player>();
			players.add(p);
			unInjectPlayer(p);
			injectPlayer(players);
		}
	}
	//Add new group in game
	public void createGroup(String group) {
		if (groups.contains(group.toLowerCase())) return;
		config.set(group.toLowerCase() + ".options.default", false);
		config.set(group.toLowerCase() + ".permissions", new ArrayList<String>());
		save();
		groups.add(new Groups(group.toLowerCase()));
		Groups g = getGroup(group);
			Permission perm = new Permission("vperms.groups." + g.getName().toLowerCase() + ".addplayers", PermissionDefault.FALSE);
			PermsMain.registerPermission(perm);
			perm = new Permission("vperms.groups." + g.getName().toLowerCase() + ".removeplayers", PermissionDefault.FALSE);
			PermsMain.registerPermission(perm);
			perm = new Permission("vperms.groups." + g.getName().toLowerCase() + ".*", PermissionDefault.FALSE);
			PermsMain.registerPermission(perm);
			perm = new Permission("vperms.groups." + g.getName().toLowerCase() + ".addperm", PermissionDefault.FALSE);
			PermsMain.registerPermission(perm);
			perm = new Permission("vperms.groups." + g.getName().toLowerCase() + ".removeperm", PermissionDefault.FALSE);
			PermsMain.registerPermission(perm);
			//FINISH PERMISSIONS -- (Few months later)Don't remember what is needed?
	}
	
	public Groups getGroup(String name) {
		for (Groups g : groups) {
			if (g.getName().equalsIgnoreCase(name)) return g;
		}
		
		return null;
	}
	//Get the Groups a player is in
	public List<String> getGroups(String player) {
		if (!pConfig.contains(player)) {
			pConfig.set(player + ".permissions", new ArrayList<String>());
			pConfig.set(player + ".groups", new ArrayList<String>());
			Psave();
		}
		return pConfig.getStringList(player + ".groups");
	}
	//Add the perms to the player
	public void injectPlayer(Collection<? extends Player> collection) {
		for (Player p : collection) {
			if (attachments.get(p.getName()) == null) attachments.put(p.getName(), p.addAttachment(getPlugin()));
		
		    for (String pGroup : pConfig.getStringList(p.getName() + ".groups")) {
		    	for (Groups g : groups) {
		    		if (g.getName().equalsIgnoreCase(pGroup)) {
		    			for (String perm : getGroup(g.getName()).getPerms()) {
		    				attachments.get(p.getName()).setPermission(perm, true);
		    			}
		    			if (groupInheritance(g) == null) continue;
		    			for (String perm : groupInheritance(g)) {
		    				attachments.get(p.getName()).setPermission(perm, true);
		    			}
		    		}
		    	}
		    }
		    for (String perm : getPerms(p.getName())) {
		    	if (perm.contains("-")){
		    		attachments.get(p.getName()).unsetPermission(perm.replace("-", ""));
		    	}
		    	else
				attachments.get(p.getName()).setPermission(perm, true);
			}
		    
		    if (getGroups(p.getName()).size() <1) {
				return;
			}
			List<String> groups = getGroups(p.getName());
			if (getConfig().getString(groups.get(0) + ".options.prefix") != null) {
				PermsMain.ranks.getTeam(groups.get(0)).addEntry(p.getName());
			}
			p.setScoreboard(PermsMain.ranks);
		}
	}
	
	public void unInjectPlayer(Player pl) {
		pl.removeAttachment(attachments.get(pl.getName()));
		attachments.remove(pl.getName());
		PermsMain.ranks.getEntryTeam(pl.getName()).removeEntry(pl.getName());
	}
	
	public ConfigurationSection getGroupSection(String name) {
		return config.getConfigurationSection(name);
	}
	
	//Nick command
	public void setPrefix(String player, String prefix) {
		Player p = Bukkit.getServer().getPlayer(player);
		for (String nick : pConfig.getKeys(false)) {
			if (nick.equalsIgnoreCase(player) || nick.equalsIgnoreCase("nick_command")) continue;
			if (pConfig.getString(nick + ".nick").contains(prefix)) {
				p.sendMessage(ChatColor.RED + "Try not to have a name similar to someone else!");
				return;
			}
		}
		pConfig.set(player + ".nick", prefix);
		Psave();
		p.setDisplayName(ChatColor.translateAlternateColorCodes('&', prefix + "&F"));
	}
	
	public void removePrefix(String player) {
		pConfig.set(player + ".nick", null);
		Player p = Bukkit.getPlayer(player);
		p.setDisplayName(p.getName());
	}
	//REAL PREFIXES
	public void addGroupPrefix(String group, String prefix) {
		for (Groups g : groups) {
			if (g.getName().equalsIgnoreCase(group)){
				boolean create = false;
				if (config.get(g.getName()+ ".options.prefix") != null || config.get(g.getName()+ ".options.prefix") != "") {
					PermsMain.ranks.getTeam(g.getName()).setPrefix(ChatColor.translateAlternateColorCodes('&', prefix) + " ");
				}
				else {
					create = true;
				}
				config.set(g.getName() + ".options.prefix", prefix);
				save();
				if (create) {
				Team CRank = PermsMain.ranks.registerNewTeam(g.getName().toLowerCase());
				CRank.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix) + " ");
				}
			}
		}
	}
	
	public void registerGroups() {
		for (String group : config.getKeys(false)) {
			Groups g = new Groups(group);
			groups.add(g);
				Permission perm = new Permission("vperms.groups." + g.getName() + ".addplayers", PermissionDefault.FALSE);
				PermsMain.registerPermission(perm);
				perm = new Permission("vperms.groups." + g.getName() + ".removeplayers", PermissionDefault.FALSE);
				PermsMain.registerPermission(perm);
				perm = new Permission("vperms.groups." + g.getName() + ".*", PermissionDefault.FALSE);
				PermsMain.registerPermission(perm);
				perm = new Permission("vperms.groups." + g.getName() + ".addperm", PermissionDefault.FALSE);
				PermsMain.registerPermission(perm);
				perm = new Permission("vperms.groups." + g.getName() + ".removeperm", PermissionDefault.FALSE);
				PermsMain.registerPermission(perm);
		}
	}
	
	public ArrayList<String> groupInheritance(Groups g) {
		ArrayList<String> perms = new ArrayList<String>();
		for (String group : g.getIncludedGroups()) {
			if (getGroup(group) == null) {
				PermsMain.instance.getLogger().info(ChatColor.RED + "Invalid Group Inheritence!");
				return null;
			}
			for (String perm : getGroup(group).getPerms()) {
				perms.add(perm);
			}
		}
		return perms;
	}
	
	/*UPDATED FOR GROUP INHERITANCE:
	 * 
	 * example:
	 *   options:
	 *     default: false
	 *   permissions:
	 *   - essentials.kick
	 * 
	 * default:
	 *   options:
	 *     default: true
	 *     prefix: <Default>
	 *   permissions:
	 *   - essentials.kill
	 *   
	 * owner:
	 *   options:
	 *     prefix: '[&4Owner&f]'
	 *   permissions:
	 *   - essentials.ban
	 *   inheritance:
	 *   - example
	 *   - default
	 */
}
