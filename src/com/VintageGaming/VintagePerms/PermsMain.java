package com.VintageGaming.VintagePerms;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.VintageGaming.VintagePerms.Commands.ConvertCmd;
import com.VintageGaming.VintagePerms.Commands.GroupCmd;
import com.VintageGaming.VintagePerms.Commands.MainCommands;
import com.VintageGaming.VintagePerms.Commands.NickCommand;
import com.VintageGaming.VintagePerms.Commands.UserCmd;
import com.VintageGaming.VintagePerms.injection.Groups;
import com.VintageGaming.VintagePerms.injection.InjectEvents;
import com.VintageGaming.VintagePerms.injection.ScoreBoardPrefixes;
import com.VintageGaming.VintagePerms.injection.VVHook;

//1.9 Update: NEEDS TESTING

public class PermsMain extends JavaPlugin {
	public static Scoreboard ranks;
	
	Permission GodPerm = new Permission("vperms.*");
	Permission GodUserPerm = new Permission("vperms.user.*");
	Permission GodGroupPerm = new Permission("vperms.groups.*");
	
	static PluginManager pm = Bukkit.getServer().getPluginManager();
	public static Plugin instance;
	public ServicesManager sm = null;
	
	public void onEnable() {
		ranks = getServer().getScoreboardManager().getNewScoreboard();
		sm = getServer().getServicesManager();
		Logger logger = Logger.getLogger("Minecraft");
		instance = this;
		registerPermissions();
		SettingsManager.getInstance().makeConfig(this);
		pm.registerEvents(new InjectEvents(), this);
		getCommand("vperms").setExecutor(new MainCommands());
		getCommand("nick").setExecutor(new NickCommand());
		getCommand("realnames").setExecutor(new NickCommand());
		getCommand("vconvert").setExecutor(new ConvertCmd());
		SettingsManager.getInstance().registerGroups();
		createScoreBoardRanks();
		ScoreBoardPrefixes.updatePrefixes();
		
        logger.info("VintagePerms Has been Enabled!");
        if (getServer().getOnlinePlayers().size()>0) {
        	SettingsManager.getInstance().injectPlayer(getServer().getOnlinePlayers());
        }
}
	public void onDisable() {
		for (Player p : getServer().getOnlinePlayers()) {
			SettingsManager.getInstance().unInjectPlayer(p);
		}
        Logger logger = Logger.getLogger("Minecraft");
        logger.info("VintagePerms Has been Disabled!");
}
	public void registerPermissions() {
		pm.addPermission(GodPerm);
		pm.addPermission(GodUserPerm);
		pm.addPermission(GodGroupPerm);
		pm.addPermission(UserCmd.addperm);
		pm.addPermission(UserCmd.removeperm);
		pm.addPermission(UserCmd.listperms);
		pm.addPermission(MainCommands.usage);
		pm.addPermission(GroupCmd.createGroup);
		pm.addPermission(GroupCmd.removeGroup);
		pm.addPermission(GroupCmd.addGroupPerm);
		pm.addPermission(GroupCmd.removeGroupPerm);
		pm.addPermission(GroupCmd.listGroupPerms);
		pm.addPermission(GroupCmd.setGroupPrefix);
		pm.addPermission(NickCommand.nick);
		pm.addPermission(NickCommand.realnames);
		pm.addPermission(NickCommand.Admin);
		pm.addPermission(NickCommand.RemoteChange);
	}
	public static void registerPermission(Permission perm) {
		pm.addPermission(perm);
	}

	public void createScoreBoardRanks() {
		for (Groups rank : SettingsManager.groups) {
			if (SettingsManager.getInstance().getConfig().getString(rank.getName() + ".options.prefix") == null || SettingsManager.getInstance().getConfig().getString(rank.getName() + ".options.prefix") == "")
				continue;
			Team CRank = ranks.getTeam(rank.getName());
			if (CRank == null) {
				CRank = ranks.registerNewTeam(rank.getName());
			}
			CRank.setPrefix(ChatColor.translateAlternateColorCodes('&', SettingsManager.getInstance().getConfig().getString(rank.getName() + ".options.prefix")) + " ");
		}
	}
	//All Permission Methods
	public VVHook VVPermission() {
		return new VVHook();
	}
}
