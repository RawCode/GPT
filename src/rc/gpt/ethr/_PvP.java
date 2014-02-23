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
	public _PvP(){};
	
	static HashMap<String,_PvP> HASH_STORAGE = new HashMap<String,_PvP>();
	static byte DOCOLLECT = 127;
	static int  COUNTDOWN = 10000;
	
	
	
	public long TimeStamp;
	public String Source;
	
	
	
	public _PvP(long Offset,String Source){
		this.TimeStamp = Offset;
		this.Source = Source;};
	
	static void PERFORM_GC_EVENT(){
		DOCOLLECT = 127;
		Iterator<_PvP> Source = HASH_STORAGE.values().iterator();
		long TIME = System.currentTimeMillis();
		while (Source.hasNext())
		{
			if (TIME - Source.next().TimeStamp >= COUNTDOWN){
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
	public void onPlayerDeathEvent(PlayerDeathEvent event){
		String Name = event.getEntity().getName();
		if (Bukkit.getPlayer(Name) != null)
			HASH_STORAGE.remove(Name);
		//To prevent doublekilling and inability to use commands after legit death
	}
	
	@EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		String Key = event.getPlayer().getName().toLowerCase();
		_PvP Container = HASH_STORAGE.get(Key);
		if (Container == null) return;
		long TIME = System.currentTimeMillis();
		
		if (TIME - Container.TimeStamp <= COUNTDOWN){
			event.getPlayer().damage(19d);
			Bukkit.broadcastMessage(ChatColor.RED + event.getPlayer().getDisplayName() + 
			" покинул игру во время боя с " + Container.Source + " и был наказан!");
		}
		HASH_STORAGE.remove(Key);
	}

	@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (!event.getPlayer().isDead())return;
		String Key = event.getPlayer().getName().toLowerCase();
		_PvP Container = HASH_STORAGE.get(Key);
		if (Container == null) return;
		
		event.setJoinMessage(ChatColor.RED + event.getPlayer().getDisplayName() + 
				" зашел в игру мёртвым, так как ранее вышел из боя с " + Container.Source);
		HASH_STORAGE.remove(Key);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)  
	{
		
		String Key = event.getPlayer().getName().toLowerCase();
		_PvP Container = HASH_STORAGE.get(Key);
		if (Container == null) return;
		long TIME = System.currentTimeMillis();
		
		if (TIME - Container.TimeStamp <= COUNTDOWN) 
		{
			event.getPlayer().sendMessage(ChatColor.RED + 
					"Нельзя использовать команды во время боя с " + Container.Source);
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
		long TIME = System.currentTimeMillis();
		HASH_STORAGE.put(Key,new _PvP(TIME,((Player) event.getDamager()).getDisplayName()));
		
		DOCOLLECT--;
		if (DOCOLLECT == 0)
			PERFORM_GC_EVENT();
	}
}