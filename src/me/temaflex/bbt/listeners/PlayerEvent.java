package me.temaflex.bbt.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.temaflex.bbt.Button;
import me.temaflex.bbt.Main;

public class PlayerEvent
implements Listener {
	Main main = Main.getInstance();
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void Interact(PlayerInteractEvent e) {
		Block block = e.getClickedBlock();
		// �������� ���� �� � ������ ������ �������� � �������� ����� �� �� ���
		if (block.getType().toString().contains("BUTTON") && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		    Location location = block.getLocation();
		    Button button = null;
		    for (Button btn : main.getButtons()) {
		    	if (btn.getLocation().equals(location)) {
		    		button = btn;
		    		break;
		    	}
		    }
			if (button == null) return;
			//�������� ������ ������
		    ByteArrayDataOutput out = ByteStreams.newDataOutput();
		    out.writeUTF("Connect");
		    //������ � �������� �� ������ �����������
		    out.writeUTF(button.getServer());
		    //��������� �������
		    JavaPlugin plugin = (JavaPlugin)main;
		    //��������� ������ � �������� ������ �� ��� �����.
		    e.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
		}
	}
}
