package me.temaflex.bbt;

import java.util.List;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Utils {
    public static void sendMessage(CommandSender sender, Object msg, String cmd, String... replace) {
        if (msg instanceof List) {
            for (Object str : ((List<?>)msg)) {
                String out = String.valueOf(str).replace("{cmd}", cmd);
                sender.sendMessage(parseColor(out));
            }
        }
        if (msg instanceof String) {
            String out = String.valueOf(msg).replace("{cmd}", cmd);
            sender.sendMessage(parseColor(out));
        }
    }
    
    public static String parseColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
