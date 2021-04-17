package com.VintageGaming.VintagePerms.Gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Group implements Listener {
	
	private static HashMap<String, String> GroupChanges = new HashMap<String, String>();
	
	@EventHandler
	public static void inventoryclick(InventoryClickEvent e) {
		if (!e.getView().getTitle().contains("Groups") || e.getCurrentItem() == null || e.getCurrentItem().equals(Material.AIR)) return;
		Player p = (Player) e.getWhoClicked();
		ItemStack clicked = e.getCurrentItem();
		if (e.getView().getTitle() == "Groups") {
			if (clicked.equals(Material.GREEN_STAINED_GLASS_PANE)) {
				GroupChanges.put(p.getDisplayName(), ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
				InventoryChange("group", ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
			}
		}
	}
	
	public static void InventoryChange(String type, String group) {
		Inventory change;
		
		if (type == "group") {
			change = Bukkit.createInventory(null, 54, "Groups > " + group);
			//insert Group Options (Add Permissions, Delete group, Inheritance, Prefix)
		}
	}
}
