package me.temaflex.bbt.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.temaflex.bbt.Button;
import me.temaflex.bbt.Main;
import me.temaflex.bbt.Utils;

public class BungeeButtonTeleport
implements CommandExecutor {
	Main main = Main.getInstance();
	FileConfiguration config = main.getConfig();
	FileConfiguration dataconfig = main.getDataConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1) {
        	
        	//AddServer
            if (args[0].equalsIgnoreCase("addserver")) {
                if (!sender.hasPermission(config.getString("permissions.addserver"))) {
                	Utils.sendMessage(sender, config.get("messages.noperm"), cmd.getName());
                    return true;
                }
                if (args.length == 1) {
                	Utils.sendMessage(sender, config.get("messages.noarg.button_name"), cmd.getName());
                	return false;
                }
        		String name = args[1];
                if (args.length == 2) {
                	Utils.sendMessage(sender, config.get("messages.noarg.server_name"), cmd.getName());
                	return false;
                }
        		String server = args[2];
            	Block block = ((Player)sender).getTargetBlock((Set<Material>) null, config.getInt("distance_from_button"));
            	if (block.getType().toString().contains("BUTTON")) {
        		    boolean checkSection = false;
        		    try {
        		    	checkSection = dataconfig.getConfigurationSection(name) != null;
        		    }
        		    catch(Exception e) {}
        		    Location location = block.getLocation();
        		    Button button = null;
        		    for (Button btn : main.getButtons()) {
        		    	if (btn.getLocation().equals(location)) {
        		    		button = btn;
        		    		break;
        		    	}
        		    }
        		    if (!checkSection) {
            		    if (button != null) {
                        	Utils.sendMessage(sender, config.getString("messages.button_exist.cordinate").replace("{name}", button.getName()), cmd.getName());
            		    	return false;
            		    }
        		    	Location loc = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ());
        		    	main.getButtons().add(new Button(name, loc, server));
        		    	dataconfig.createSection(name);
        		    	dataconfig.set(name+".location", loc.getWorld().getName()+", "+loc.getX()+", "+loc.getY()+", "+loc.getZ());
        		    	dataconfig.set(name+".server", server);
        		    	main.saveDataConfig();
        		    	Utils.sendMessage(sender, config.getString("messages.added_server").replace("{name}", name).replace("{server}", server), cmd.getName());
        		    	return true;
        		    }
            		Utils.sendMessage(sender, config.getString("messages.button_exist.true").replace("{name}", name), cmd.getName());
            		return false;
            	}
            	Utils.sendMessage(sender, config.get("messages.is_not_button"), cmd.getName());
            	return false;
            }
            
            //Remove
            if (args[0].equalsIgnoreCase("remove")) {
                if (!sender.hasPermission(config.getString("permissions.remove"))) {
                	Utils.sendMessage(sender, config.get("messages.noperm"), cmd.getName());
                    return true;
                }
                if (args.length == 1) {
                	Utils.sendMessage(sender, config.get("messages.noarg.button_name"), cmd.getName());
                	return false;
                }
        		String name = args[1];
    		    boolean checkSection = false;
    		    try {
    		    	checkSection = dataconfig.getConfigurationSection(name) != null;
    		    }
    		    catch(Exception e) {}
    		    if (checkSection) {
    		    	String server = main.getButton(name).getServer();
    		    	main.getButtons().remove(main.getButton(name));
    		    	dataconfig.set(name, null);
    		    	main.saveDataConfig();
    		    	Utils.sendMessage(sender, config.getString("messages.removed_server").replace("{name}", name).replace("{server}", server), cmd.getName());
    		    	return true;
    		    }
            	Utils.sendMessage(sender, config.getString("messages.button_exist.false").replace("{name}", name), cmd.getName());
            	return false;
            }
            
            //List
            if (args[0].equalsIgnoreCase("list")) {
                if (!sender.hasPermission(config.getString("permissions.list"))) {
                	Utils.sendMessage(sender, config.get("messages.noperm"), cmd.getName());
                    return true;
                }
                int pagechoose = 1;
                try {
                	pagechoose = Integer.valueOf(args[1]);
                }
                catch (Exception ee) {}
                try {
	        		int page = args.length > 0 ? pagechoose : 1;
	
	        		if (page < 1) {
	        			Utils.sendMessage(sender, config.getString("messages.list.numberminus"), cmd.getName());
	        			return false;
	        		}
	        		
	        		int sectionsize = 0;
	        		try {
	        			sectionsize = dataconfig.getKeys(false).size();
	        		}
	        		catch (NullPointerException e) {}
	        		
	        		int list_per_page = config.getInt("list_per_page", 10);
	        		int totalPages = sectionsize / list_per_page;
	        		if (sectionsize % list_per_page != 0) {
	        			totalPages++;
	        		}
	        		
	        		if (sectionsize == 0) {
	        			Utils.sendMessage(sender, config.getString("messages.list.nobuttons"), cmd.getName());
	        			return false;
	        		}
	        		
	        		if (page > totalPages) {
	        			Utils.sendMessage(sender, config.getString("messages.list.page_notfound"), cmd.getName());
	        			return false;
	        		}
	        		
	        		Utils.sendMessage(sender, config.getString("messages.list.title").replace("{page}", page+"").replace("{total}", totalPages+""), cmd.getName());
	        		int fromIndex = (page - 1) * list_per_page;
	        		int toIndex = fromIndex + list_per_page;
	        		
	        		List<Button> Buttons = new ArrayList<Button>();
	        		Buttons.addAll(main.getButtons());
	
	        		for (int i = fromIndex; i < toIndex; i++) {
	        			if (i < sectionsize) {
	        				Button button = Buttons.get(i);
	        				Utils.sendMessage(sender, config.getString("messages.list.info")
	        						.replace("{name}", button.getName())
	        						.replace("{x}", (int)button.getX()+"")
	        						.replace("{y}", (int)button.getY()+"")
	        						.replace("{z}", (int)button.getZ()+"")
	        						.replace("{server}", button.getServer()+"")
	        						.replace("{world}", button.getWorld().getName()),
	        						cmd.getName());
	        			}
	        		}
	        		if (page < totalPages) {
	        			Utils.sendMessage(sender, config.getString("messages.list.pages").replace("{next}", (page + 1)+""), cmd.getName());
	        		}
	        		return true;
                }
                catch (Exception e) {
                	e.printStackTrace();
                }
                return false;
            }
            
            //Reload
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission(config.getString("permissions.reload"))) {
                	Utils.sendMessage(sender, config.get("messages.noperm"), cmd.getName());
                    return true;
                }
                main.reloadConfig();
                main.loadConfig();
                Utils.sendMessage(sender, config.get("messages.reload"), cmd.getName());
                return true;
            }
        }
        
        //Help
        if (!sender.hasPermission(config.getString("permissions.help"))) {
        	Utils.sendMessage(sender, config.get("messages.noperm"), cmd.getName());
            return true;
        }
        Utils.sendMessage(sender, config.get("messages.help"), cmd.getName());
        return false;
	}
}
