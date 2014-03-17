package rc.ubt.hnde;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import rc.ubt.impl.PsExImpl;

public class CustomLogin implements Listener {
	
	static public HashMap<String,Long> MAP   = new HashMap<String,Long>();
	static long 					   DELAY = 1000 * 20;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void CountdownQuit(PlayerQuitEvent event){
		
		if (PsExImpl.has(event.getPlayer(), "UBT.Bypass")) return;
		
		MAP.put(event.getPlayer().getName().toLowerCase(), new Long(System.currentTimeMillis()));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void CountdownLogin(PlayerLoginEvent event) {
		
		Long offset = MAP.get(event.getPlayer().getName().toLowerCase());
		if (offset == null) return;
		
		long passed = System.currentTimeMillis() - offset;
		if (passed <= DELAY){
			event.disallow(Result.KICK_OTHER, ChatColor.RED + "�������� " + (DELAY - passed)/1000 + " ������");
			return;
		}
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void WhitelistLogin(PlayerLoginEvent event) {
		if (event.getResult() == Result.KICK_WHITELIST)
		{
			if (PsExImpl.has(event.getPlayer(), "UBT.Bypass"))
			{
				event.allow();
				return;
			}
			event.setKickMessage(ChatColor.RED+"���������");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void WhitelistPing(ServerListPingEvent event)  {
		
		if (Bukkit.getServer().hasWhitelist())
		{
			event.setMotd("���������");
			event.setMaxPlayers(0);
			return;
		}
	}
}