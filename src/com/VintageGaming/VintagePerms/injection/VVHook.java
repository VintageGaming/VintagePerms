package com.VintageGaming.VintagePerms.injection;

import java.util.List;

import com.VintageGaming.VintagePerms.SettingsManager;

public class VVHook {

	
	public String getName() {
		return "VintagePerms";
	}
	
	public boolean addPlayerPermission(String player, String permission) {
		SettingsManager.getInstance().addPPerm(player, permission);
		return true;
	}
	
	public boolean removePlayerPermission(String player, String permission) {
		SettingsManager.getInstance().remPPerm(player, permission);
		return true;
	}
	
	public List<String> getPlayersPermissions(String player) {
		return SettingsManager.getInstance().getPerms(player);
	}
	
	public boolean setPlayerGroup(String player, String group) {
		SettingsManager.getInstance().setGroup(player, group);
		return true;
	}
	
	public boolean createGroup(String group) {
		SettingsManager.getInstance().createGroup(group);
		return true;
	}
	
	public boolean addGroupPermission(String group, String permission) {
		SettingsManager.getInstance().getGroup(group.toLowerCase()).addPerm(permission);
		return true;
	}
	
	public boolean removeGroupPermission(String group, String permission) {
		SettingsManager.getInstance().getGroup(group.toLowerCase()).remPerm(permission);
		return true;
	}
	
	
}
