package org.myftp.p_productions.spigot.CubeDisplay;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class CubeDeleteTabExecutor implements TabExecutor {
	CubeDisplay plugin;
	
	public CubeDeleteTabExecutor(CubeDisplay plugin) {
		this.plugin=plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==1){
			Cube cube = plugin.cubes.remove(args[0]);
			if(cube==null){
				sender.sendMessage("Cube couldn't be found");
				return true;
			}
			cube.cancel();
			sender.sendMessage(ChatColor.RED+"Deleted "+ChatColor.AQUA+args[0]);
		}
		else {
			int c=0;
			for (Entry<String, Cube> cubeEntry : plugin.cubes.entrySet()) {
				cubeEntry.getValue().cancel();
				//plugin.cubes.remove(cubeEntry.getKey());
				sender.sendMessage(ChatColor.RED+"Deleted "+ChatColor.AQUA+cubeEntry.getKey());
				c++;
			}
			plugin.cubes.clear();
			sender.sendMessage(ChatColor.GOLD+"Total: "+ChatColor.GREEN+c);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		Set<String> regions = plugin.cubes.keySet();
		return regions.stream().filter(region->region.toLowerCase().startsWith(args[args.length-1].toLowerCase())).collect(Collectors.toList());
	}

}
