package org.myftp.p_productions.spigot.CubeDisplay;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class CubeDisplay extends JavaPlugin {
	private WorldGuardPlugin worldGuardPlugin;
	private boolean worldGuard;
	
	private WorldEditPlugin worldEditPlugin;
	private boolean worldEdit;
	
	Map<String, Cube> cubes;
	
	@Override
	public void onEnable() {
		
		Plugin wgplugin = getServer().getPluginManager().getPlugin("WorldGuard");

	    // WorldGuard may not be loaded
	    if (wgplugin != null && (wgplugin instanceof WorldGuardPlugin)) {
	        worldGuard=true;
	        worldGuardPlugin=(WorldGuardPlugin) wgplugin;
	        getLogger().info("WorldGuard found");
	    }
	    
	    Plugin weplugin = getServer().getPluginManager().getPlugin("WorldEdit");

	    // WorldEdit may not be loaded
	    if (weplugin != null && (weplugin instanceof WorldEditPlugin)) {
	        worldEdit=true;
	        worldEditPlugin=(WorldEditPlugin) weplugin;
	        getLogger().info("WorldEdit found");
	    }
		
	    cubes=new HashMap<>();
	    
		getCommand("cube").setExecutor(new CubeTabExecutor(this));
		if(worldGuard)
			getCommand("cube").setTabCompleter(new CubeTabExecutor(this));
		getCommand("cubes").setExecutor(new CubeListExecutor(this));
		getCommand("decube").setExecutor(new CubeDeleteTabExecutor(this));
		getCommand("decube").setTabCompleter(new CubeDeleteTabExecutor(this));
		
		
		
		getLogger().info("CubeDisplay activated");
	}
	
	public WorldGuardPlugin getWorldGuard(){
		return worldGuardPlugin;
	}
	
	public boolean hasWorldGuard(){
		return worldGuard;
	}
	
	public WorldEditPlugin getWorldEdit(){
		return worldEditPlugin;
	}
	
	public boolean hasWorldEdit(){
		return worldEdit;
	}
}
