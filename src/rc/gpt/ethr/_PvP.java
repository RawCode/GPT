package rc.gpt.ethr;

import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class _PvP implements Listener {
	
	static long EPOCH_TIME_OFFSET = System.currentTimeMillis();
	static HashMap<String,_PvP_Wrapper> HASH_STORAGE = new HashMap<String,_PvP_Wrapper>();
	static byte GC_EVENT = 100;
	static int COUNTDOWN = 10000;
	
	static class _PvP_Wrapper
	{
		public int TimeStamp;
		public String Source;
		
		public _PvP_Wrapper(int i, String s){
			TimeStamp = i;
			Source = s;
		}
	}
	
	static void PERFORM_GC_EVENT(){
		int TimeOffset = (int)(System.currentTimeMillis() - EPOCH_TIME_OFFSET);
		Iterator<_PvP_Wrapper> Source = HASH_STORAGE.values().iterator();
		
		int LocalTime = 0;
		while (Source.hasNext())
		{
			LocalTime = Source.next().TimeStamp;
			if (TimeOffset - LocalTime >= COUNTDOWN){
				Source.remove();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
	public void onPlayerKickEvent(PlayerKickEvent event){
		String Name = event.getPlayer().getName().toLowerCase();
		HASH_STORAGE.remove(Name);
		//To prevent kick from killing players.
	}
	
	@EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		String Key = event.getPlayer().getName().toLowerCase();
		_PvP_Wrapper Container = HASH_STORAGE.get(Key);
		
		int TimeOffset = (int)(System.currentTimeMillis() - EPOCH_TIME_OFFSET);
		if (Container == null) return;
		
		if (TimeOffset - Container.TimeStamp <= COUNTDOWN){
			event.getPlayer().damage(19);
			Bukkit.broadcastMessage(ChatColor.RED + event.getPlayer().getDisplayName() + 
			" покинул игру во время боя с " + Container.Source + " и был наказан!");
			HASH_STORAGE.remove(Key);
		}
	}
	@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		String Key = event.getEntity().getName().toLowerCase();
		HASH_STORAGE.remove(Key);
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (!event.getPlayer().isDead())return;
		String Key = event.getPlayer().getName().toLowerCase();
		_PvP_Wrapper Container = HASH_STORAGE.get(Key);
		
		if (Container == null) return;
		
		event.setJoinMessage(ChatColor.RED + event.getPlayer().getDisplayName() + 
				" зашел в игру мёртвым, так как ранее вышел из боя с " + Container.Source);
		HASH_STORAGE.remove(Key);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)  
	{
		
		String Key = event.getPlayer().getName().toLowerCase();
		_PvP_Wrapper Container = HASH_STORAGE.get(Key);
		if (Container == null) return;
		
		int TimeOffset = (int)(System.currentTimeMillis() - EPOCH_TIME_OFFSET);
		
		if (TimeOffset - Container.TimeStamp <= COUNTDOWN) 
		{
			event.getPlayer().sendMessage(ChatColor.RED + 
					"You cannot use any commands when fighting with " + Container.Source);
			event.setCancelled(true);
			return;
		}
		HASH_STORAGE.remove(Key);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void EntityDamageEvent(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		
		String Key = ((Player) event.getEntity()).getName().toLowerCase();
		int TimeOffset = (int)(System.currentTimeMillis() - EPOCH_TIME_OFFSET);
		
		HASH_STORAGE.put(Key,new _PvP_Wrapper(TimeOffset,((Player) event.getDamager()).getDisplayName()));

		if (GC_EVENT-- == 0)PERFORM_GC_EVENT();
	}
}