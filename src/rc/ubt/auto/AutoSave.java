package rc.ubt.auto;

import java.io.UnsupportedEncodingException;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_7_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitScheduler;

import rc.ubt.Loader;

public class AutoSave implements Runnable, Listener
{
	static long    LAST_SAVE  = System.currentTimeMillis();
	static int 	   FRAME_SIZE = 1000*60*10;
	static boolean PROCESS    = false;
	static CraftChunk[] TOSAVE= null;
	static int     STEP       = 0;
	static boolean DISABLED   = false;
	static long    SAVECOUNT  = 0;
	
	static Chunk TESTAZ = null;
	
	
	public AutoSave()
	{
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTaskTimer(Loader.INSTANCE, new AutoSave(null),0,1L);
		Bukkit.getPluginManager().registerEvents(this, Loader.INSTANCE);
	}
	public AutoSave(Object o){}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) throws UnsupportedEncodingException  
	{
		String[] Data   = event.getMessage().toLowerCase().split("\\s+");
		if (Data.length == 0) return;
		String Order = Data[0].substring(1);
		
		if (Order.equals("ztsm"))
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
		if (Order.equals("zfsm"))
		{
			//restart saving if needed.
			DISABLED = false;
			PROCESS = false;
			LAST_SAVE = 0;
			event.getPlayer().sendMessage("FORCED SAVING PROCEDURE");
			event.setCancelled(true);
		}
	}
	/*
	String S1 = new String("проверка".getBytes("CP866"),"CP866"); //valid for logfile
	String S2 = new String("проверка".getBytes(),"CP866"); //not valid completely
	String S3 = new String("проверка".getBytes("CP866")); //valid for console
	String S4 = new String("проверка"); //valid for logfile*/
	
	//String S1 = new String("проверка".getBytes("CP1252"));
	//String S2 = new String("проверка".getBytes("CP1251")); //this is logfile encoding
	//String S3 = new String("проверка".getBytes("CP866")); //this is console encoding
	//String S4 = new String("проверка".getBytes("UTF-8"));
	//System.out.println(S1);
	//System.out.println(S2);
	//System.out.println(S3);
	//System.out.println(S4);
	//this check indicate that console window encoding is CP866 and internal encoding NOT cp866

	public void run()
	{
		if (DISABLED) return;
		
		if (PROCESS)
		{
((CraftWorld)Bukkit.getWorld("world")).getHandle().chunkProviderServer.saveChunk(TOSAVE[STEP].getHandle());
			TOSAVE[STEP] = null;
			if (STEP++ == TOSAVE.length-1)
			{
				PROCESS = false;
				LAST_SAVE = System.currentTimeMillis();
				Bukkit.savePlayers();
				TOSAVE = null;
				Bukkit.broadcastMessage("AVZX SAVE PROCESS FINISHED " + System.currentTimeMillis()/1000);
				Bukkit.broadcastMessage("SAVED " + STEP + " CHUNKS");
				Bukkit.broadcastMessage("PASSED " + (System.currentTimeMillis() - SAVECOUNT)/1000 + " SECONDS");
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
			Bukkit.broadcastMessage("AVZX SAVE PROCESS STARTED " + SAVECOUNT/1000);
			Bukkit.broadcastMessage("WILL SAVE " + TOSAVE.length + " CHUNKS");
			Bukkit.broadcastMessage("ESTIMATED TIME " + TOSAVE.length/20 + " SECONDS");
		}
		
	}
}