package org.myftp.p_productions.spigot.CubeDisplay;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class CubeTabExecutor implements TabExecutor {
	CubeDisplay plugin;
	
	public CubeTabExecutor(CubeDisplay plugin) {
		this.plugin=plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("You're no Player");
			return true;
		}
		Player player = (Player) sender;
		switch(args.length){
			case 7:
			case 6:
				doCube(player, new Location(player.getWorld(), Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2])),
								new Location(player.getWorld(), Double.valueOf(args[3]), Double.valueOf(args[4]), Double.valueOf(args[5])),
								args.length==7?args[args.length-1]:getHexTime());
				return true;
			case 2:
			case 1:
				if(!plugin.hasWorldGuard())
					sender.sendMessage("WorldGuard not found!");
				else {
					ProtectedRegion region = plugin.getWorldGuard().getRegionManager(((Player)sender).getWorld()).getRegion(args[0]);
					if(region==null)
						sender.sendMessage("Region couldn't be found");
					else if(!(region instanceof ProtectedCuboidRegion))
						sender.sendMessage("Region isn't a cube");
					else {
						BlockVector max = region.getMaximumPoint();
						BlockVector min = region.getMinimumPoint();
						doCube(player,
								new Location(player.getWorld(), max.getX(), max.getY(), max.getZ()), 
								new Location(player.getWorld(), min.getX(), min.getY(), min.getZ()),
								args.length==2?args[args.length-1]:getHexTime());
						return true;
					}
				}
			case 0:
				if(!plugin.hasWorldEdit())
					sender.sendMessage("WorldEdit not found!");
				else {
					Selection sel = plugin.getWorldEdit().getSelection(player);
					if(sel==null)
						sender.sendMessage("You first have to select something");
					else {
						doCube(player, sel.getMaximumPoint(), sel.getMinimumPoint());
						return true;
					}
				}
		}
		return false;
	}
	
	private void doCube(Player player, Location start, Location end, String id){
		Cube cube = new Cube(start, end, plugin);
		cube.start();
		plugin.cubes.put(id, cube);
		player.sendMessage(ChatColor.GREEN+"Created "+ChatColor.AQUA+id+": \n"+
							ChatColor.GOLD+"    "+CubeListExecutor.basicLocString(start)+
							ChatColor.RED+" -> "+ChatColor.GOLD+CubeListExecutor.basicLocString(end));
	}
	
	private void doCube(Player player, Location start, Location end){
		doCube(player, start, end, getHexTime());
	}
	
	private String getHexTime(){
		return Long.toHexString(System.currentTimeMillis()).toUpperCase();
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		Set<String> regions = plugin.getWorldGuard().getRegionManager(((Player)sender).getWorld()).getRegions().keySet();				
		return regions.stream().filter(region->region.toLowerCase().startsWith(args[args.length-1].toLowerCase())).collect(Collectors.toList());
	}

}
