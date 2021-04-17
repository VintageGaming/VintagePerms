package com.VintageGaming.VintagePerms.injection;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import com.VintageGaming.VintagePerms.SettingsManager;

public class Groups {

	private String name;
	private ArrayList<String> perms;
	
	public Groups(String name) {
		this.name = name;
		
		ConfigurationSection s = SettingsManager.getInstance().getGroupSection(name);
		
		if (!s.contains("permissions")) s.set("permissions", new ArrayList<String>());
		
		this.perms = new ArrayList<String>(s.getStringList("permissions"));
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasPerm(String perm) {
		return perms.contains(perm);
	}
	
	public void addPerm(String perm) {
		perms.add(perm);
		SettingsManager.getInstance().getGroupSection(name).set("permissions", perms);
		SettingsManager.getInstance().save();
		SettingsManager.getInstance().injectPlayer(Bukkit.getServer().getOnlinePlayers());
	}
	
	public void remPerm(String perm) {
		perms.remove(perm);
		SettingsManager.getInstance().getGroupSection(name).set("permissions", perms);
		SettingsManager.getInstance().save();
		SettingsManager.getInstance().injectPlayer(Bukkit.getServer().getOnlinePlayers());
	}
	
	public ArrayList<String> getPerms() {
		return perms;
	}
	
	public ArrayList<String> getIncludedGroups() {
		ArrayList<String> inherited = new ArrayList<String>();
		for (String g : SettingsManager.getInstance().getGroupSection(name).getStringList("inheritance")) {
			inherited.add(g);
		}
		return inherited;
	}
}
