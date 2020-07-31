package me.temaflex.bbt;

import org.bukkit.Location;
import org.bukkit.World;

public class Button {
	String name;
	Location location;
	String server;
	
	public Button(String name, Location location, String server) {
		this.name = name;
		this.location = location;
		this.server = server;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public double getX() {
		return this.location.getX();
	}
	
	public double getY() {
		return this.location.getY();
	}
	
	public double getZ() {
		return this.location.getZ();
	}
	
	public World getWorld() {
		return this.location.getWorld();
	}
	
	public String getServer() {
		return this.server;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
}
