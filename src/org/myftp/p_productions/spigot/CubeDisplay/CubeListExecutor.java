package org.myftp.p_productions.spigot.CubeDisplay;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CubeListExecutor implements CommandExecutor {
	CubeDisplay plugin;
	
	public CubeListExecutor(CubeDisplay plugin) {
		this.plugin=plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(plugin.cubes.isEmpty())
			sender.sendMessage(ChatColor.GOLD+"No cubes present");
		else {
			for (Entry<String, Cube> cubesEntry : plugin.cubes.entrySet()) {
				Cube cube = cubesEntry.getValue();
				Location from = cube.getFrom();
				Location to = cube.getTo();
				sender.sendMessage(ChatColor.AQUA+cubesEntry.getKey()+":\n"+
								ChatColor.GOLD+"    "+basicLocString(from)+
								ChatColor.RED+" -> "+ChatColor.GOLD+basicLocString(to));
			}
		}
		return true;
	}

	public static String basicLocString(Location loc){
		return String.format("(%s/%s/%s)", loc.getX(), loc.getY(), loc.getZ());
	}
}
