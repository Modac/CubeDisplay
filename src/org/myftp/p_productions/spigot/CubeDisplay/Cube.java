package org.myftp.p_productions.spigot.CubeDisplay;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Cube extends BukkitRunnable {
	private Location from;
	private Location to;
	private CubeDisplay plugin;
	private boolean xCor;
	private boolean yCor;
	private boolean zCor;
	private Location loc3;
	private Location loc4;
	private Location loc5;
	private Location loc6;
	
	// TODO: make scheduling async but reliable so that the cubes don't flicker
	
	public Cube(Location from, Location to, CubeDisplay plugin) {
		this.from=from;
		this.to=to;
		this.plugin=plugin;
		xCor=false;
		yCor=false;
		zCor=false;
		alignBoundsToBlock();
		initCornerLocs();
	}
	
	
	private void initCornerLocs(){
		loc3 = new Location(from.getWorld(), from.getX(), from.getY(), to.getZ());
		loc4 = new Location(from.getWorld(), to.getX(), from.getY(), to.getZ());
		loc5 = new Location(from.getWorld(), from.getX(), to.getY(), from.getZ());
		loc6 = new Location(from.getWorld(), to.getX(), from.getY(), from.getZ());
		
	}
	//MAYBE: More efficient (e.g. caching of exact particle locs)
	
	@Override
	public void run() {
		
		//long start=System.nanoTime();
		
		double toX = to.getX();
		double toY = to.getY();
		double toZ = to.getZ();
		
		double fromX = from.getX();
		double fromY = from.getY();
		double fromZ = from.getZ();
		
		drawLineX(from, toX-fromX);
		drawLineY(from, toY-fromY);
		drawLineZ(from, toZ-fromZ);
		
		drawLineX(to, fromX-toX);
		drawLineY(to, fromY-toY);
		drawLineZ(to, fromZ-toZ);
		
		drawLineY(loc3, toY-fromY);
		
		drawLineX(loc4, fromX-toX);
		drawLineZ(loc4, fromZ-toZ);
		
		drawLineZ(loc5, toZ-fromZ);
		drawLineX(loc5, toX-fromX);
		
		drawLineY(loc6, toY-fromY);
		
		//plugin.getServer().broadcast("Run-Time: " + (System.nanoTime()-start), "cubedisplay.debugMsg");
		
	}
	
	public BukkitTask start(){
		// return runTaskTimer(plugin, 0, 10); 
		return runTaskTimerAsynchronously(plugin, 0, 10);
	}
	
	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}
	
	private void alignBoundsToBlock(){
		if(from.getBlockX()>=to.getBlockX() && !xCor){
			from.setX(from.getBlockX()+1);
			xCor=true;
		} else if(!xCor){
			to.setX(to.getBlockX()+1);
			xCor=true;
		}
		if(from.getBlockY()>=to.getBlockY() && !yCor){
			from.setY(from.getBlockY()+1);
			yCor=true;
		} else if(!yCor){
			to.setY(to.getBlockY()+1);
			yCor=true;
		}
		if(from.getBlockZ()>=to.getBlockZ() && !zCor){
			from.setZ(from.getBlockZ()+1);
			zCor=true;
		} else if(!zCor){
			to.setZ(to.getBlockZ()+1);
			zCor=true;
		}
	}
	
	private void broadcastEffect(Location loc){
		//plugin.getServer().broadcast(ChatColor.DARK_BLUE+"Players: "+plugin.getServer().getOnlinePlayers().size(), "cubedisplay.debugMsg");
		for(Player player : plugin.getServer().getOnlinePlayers()){
			player.spawnParticle(Particle.REDSTONE, loc, 3);
			//plugin.getServer().broadcast("Particle to " + player.getName() + " at " + simpleLocString(loc), "cubedisplay.debugMsg");
		}
	}
	
	private void drawLine(Location start, Vector line){
		double lengthSqr = line.lengthSquared();
		//plugin.getServer().broadcast(ChatColor.RED+"LINE"+ChatColor.GREEN+"-Start: " + line.toString() + lengthSqr, "cubedisplay.debugMsg");
		double delta = Math.sqrt(0.25/(square(line.getX())+square(line.getY())+square(line.getZ())));
		Vector stepVec = new Vector(line.getX()*delta, line.getY()*delta, line.getZ()*delta);
		Location step = start.clone();
		int c=0;
		while(square(c*0.5)<lengthSqr){
			broadcastEffect(step);
			step.add(stepVec);
			c++;
			//if(c>100) break;
		}
		//plugin.getServer().broadcast(ChatColor.RED+"LINE"+ChatColor.YELLOW+"-End", "cubedisplay.debugMsg");
		
	}
	
	private void drawLineX(Location start, double xLength){
		drawLine(start, new Vector(xLength, 0, 0));
	}
	
	private void drawLineY(Location start, double yLength){
		drawLine(start, new Vector(0, yLength, 0));
	}
	
	private void drawLineZ(Location start, double zLength){
		drawLine(start, new Vector(0, 0, zLength));
	}
	
	private static double square(double exp){
		//return Math.pow(exp, 2);
		return exp*exp;
	}
	
	public String simpleLocString(Location loc){
		return String.format("%f / %f / %f in %s", loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName());
	}

}
