package rc.gpt.auto;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitScheduler;

import rc.gpt.Loader;

public class AutoSave implements Runnable, Listener
{
	static long    LAST_SAVE  = System.currentTimeMillis();
	static int 	   FRAME_SIZE = 1000*60*14;
	static boolean PROCESS    = false;
	static CraftChunk[] TOSAVE= null;
	static int     STEP       = 0;
	static boolean DISABLED   = false;
	static long    SAVECOUNT  = 0;
	
	
	public AutoSave()
	{
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTaskTimer(Loader.INSTANCE, new AutoSave(null),0,2L);
		Bukkit.getPluginManager().registerEvents(this, Loader.INSTANCE);
	}
	public AutoSave(Object o){}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)  
	{
		String[] Data   = event.getMessage().toLowerCase().split("\\s+");
		if (Data.length == 0) return;
		String Order = Data[0].substring(1);


		if (Order.equals("togglesave"))
		{
			if (DISABLED)
			{
				//save currently disabled, it will be enabled
				//we set last save to now, in other case it will start save instantly
				LAST_SAVE = System.currentTimeMillis();
			}else
			{
				//save enabled, will stop it
				PROCESS = false;
				TOSAVE = null;
			}
			DISABLED = !DISABLED;
			event.getPlayer().sendMessage("SAVE STATE IS " + DISABLED);
			event.setCancelled(true);
		}
		if (Order.equals("forcesave"))
		{
			//restart saving if needed.
			PROCESS = false;
			LAST_SAVE = 0;
			event.getPlayer().sendMessage("FORCED SAVING PROCEDURE");
			event.setCancelled(true);
		}
	}
	
	public void run()
	{
		if (DISABLED) return;
		
		if (PROCESS)
		{
((CraftWorld)Bukkit.getWorld("world")).getHandle().chunkProviderServer.saveChunk(TOSAVE[STEP].getHandle());
			if (STEP++ == TOSAVE.length-1)
			{
				PROCESS = false;
				LAST_SAVE = System.currentTimeMillis();
				Bukkit.savePlayers();
				TOSAVE = null;
				Bukkit.broadcastMessage(ChatColor.RED + "SAVE PROCESS FINISHED " + System.currentTimeMillis()/1000);
				Bukkit.broadcastMessage(ChatColor.RED + "SAVED " + STEP + " CHUNKS");
				Bukkit.broadcastMessage(ChatColor.RED + "PASSED " + (System.currentTimeMillis() - SAVECOUNT)/1000 + " SECONDS");
			}
			return;
		}
		long off = System.currentTimeMillis() - LAST_SAVE;
		if (off >= FRAME_SIZE)
		{
			PROCESS = true;
			TOSAVE = (CraftChunk[]) Bukkit.getWorld("world").getLoadedChunks();
			STEP = 0;
			SAVECOUNT = System.currentTimeMillis();
			Bukkit.broadcastMessage(ChatColor.RED + "SAVE PROCESS STARTED " + SAVECOUNT/1000);
			Bukkit.broadcastMessage(ChatColor.RED + "WILL SAVE " + TOSAVE.length + " CHUNKS");
			Bukkit.broadcastMessage(ChatColor.RED + "ESTIMATED TIME " + TOSAVE.length/10 + " SECONDS");
		}
		
	}
}