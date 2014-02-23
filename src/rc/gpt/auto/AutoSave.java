package rc.gpt.auto;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.scheduler.BukkitScheduler;

import rc.gpt.Loader;

public class AutoSave implements Runnable
{
	static long    LAST_SAVE  = System.currentTimeMillis();
	static int 	   FRAME_SIZE = 1000*60*1;
	static boolean PROCESS    = false;
	static CraftChunk[] TOSAVE= null;
	static int     STEP       = 0;
	
	public AutoSave()
	{
		System.out.println("AUTOSAVE PREINIT");
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTaskTimer(Loader.INSTANCE, new AutoSave(null),0,2L);
		System.out.println("AUTOSAVE INIT");
	}
	public AutoSave(Object o){}
	
	public void run()
	{
		System.out.println("SAVE TICK");
		if (PROCESS)
		{
			System.out.println("SAVE TICK IN PROCESS");
((CraftWorld)Bukkit.getWorld("world")).getHandle().chunkProviderServer.saveChunk(TOSAVE[STEP].getHandle());
			STEP++;
			if (STEP >= TOSAVE.length)
			{
				PROCESS = false;
				LAST_SAVE = System.currentTimeMillis();
				System.out.println("SAVE EXIT");
				Bukkit.savePlayers();
			}
			return;
		}
		System.out.println("SAVE TICK AFTER PROCESS");
		long off = System.currentTimeMillis() - LAST_SAVE;
		if (off >= FRAME_SIZE)
		{
			System.out.println("SAVE INITIALIZE");
			PROCESS = true;
			TOSAVE = (CraftChunk[]) Bukkit.getWorld("world").getLoadedChunks();
			STEP = 0;
		}
		
	}
}