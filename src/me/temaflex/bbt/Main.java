package me.temaflex.bbt;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.temaflex.bbt.commands.BungeeButtonTeleport;
import me.temaflex.bbt.listeners.PlayerEvent;

public class Main
extends JavaPlugin {
    private static Main instance;
    List<Button> buttons = new ArrayList<Button>();
    FileConfiguration config;
    FileConfiguration database;
    File datafile;
    
    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        // Load database
        loadData();
    	for (String x : database.getKeys(false)) {
    		String server = database.getString(x+"."+".server");
			String[] loc = database.getString(x+"."+".location").split(", ");
			World world = Bukkit.getWorld(loc[0]);
			Double cord_x = Double.parseDouble(loc[1]);
			Double cord_y = Double.parseDouble(loc[2]);
			Double cord_z = Double.parseDouble(loc[3]);
			Location location = new Location(world, cord_x, cord_y, cord_z);
			buttons.add(new Button(x, location, server));
    	}
    	// Register channels
    	if (!Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
    	    Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    	}
    	// Commands
        this.getCommand("bungeebuttonteleport").setExecutor(new BungeeButtonTeleport());
    	// Listeners
    	registerListeners(new PlayerEvent());
    }
    
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        super.onDisable();
    }
    
    public void loadConfig() {
        if (!this.getDataFolder().exists() && !this.getDataFolder().mkdir()) {
            throw new RuntimeException("Can't create data folder!");
        }
        File file = new File(this.getDataFolder(), "config.yml");
        if (file.exists()) {
        	config = this.getConfig();
        } else {
            this.saveDefaultConfig();
            config = this.getConfig();
        }
    }
    
    public void loadData() {
        File databaseFile = new File(this.getDataFolder(), "data.yml");
        if (!databaseFile.exists()) {
        	try {
				databaseFile.createNewFile();
			} catch (IOException e) {
            	StringWriter errors = new StringWriter();
            	e.printStackTrace(new PrintWriter(errors));
            	this.getLogger().severe(errors.toString());
			}
        }
        this.database = getDataConfig();
    }
    
    public void reloadDataConfig() {
        if (datafile == null) {
        	datafile = new File(getDataFolder(), "data.yml");
        }
        database = YamlConfiguration.loadConfiguration(datafile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
		try {
			defConfigStream = new InputStreamReader(this.getResource("data.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            database.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getDataConfig() {
        if (database == null) {
            reloadDataConfig();
        }
        return database;
    }
    
    public void saveDataConfig() {
        if (database == null || datafile == null) {
            return;
        }
        try {
            getDataConfig().save(datafile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + datafile, ex);
        }
    }
    
    public void saveDefaultdatabase() {
        if (datafile == null) {
        	datafile = new File(getDataFolder(), "data.yml");
        }
        if (!datafile.exists()) {            
             this.saveResource("data.yml", false);
         }
    }
    
	public Button getButton(String name) {
		for (Button button : buttons) {
			if (button.getName().equals(name)) {
				return button;
			}
		}
		return null;
	}
	
	public List<Button> getButtons() {
		return this.buttons;
	}
    
    public static Main getInstance() {
        return instance;
    }
    
    public void registerListeners(Listener... listener) {
        for (Listener l : listener) {
            this.getServer().getPluginManager().registerEvents(l, this);
        }
    }
}
