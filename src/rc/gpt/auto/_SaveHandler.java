package rc.gpt.auto;
//automation
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import ru.rawcode.dev.Core;

public class _SaveHandler implements Listener, Runnable {
	
	static long     LOCAL_TIME_OFFSET = System.currentTimeMillis();
	static int 		FRAME_SIZE = 1000*60*5;
	static boolean S1 = true;
	static boolean S2 = true;
	static boolean S3 = true;
	
	public _SaveHandler()
	{
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTaskTimer(Core.INSTANCE, new _SaveHandler(null),0,10);
	}
	public _SaveHandler(Object o){}
	
	public void run() 
	{
		int i = (int)(System.currentTimeMillis() - LOCAL_TIME_OFFSET);

		if (S3 && i >= FRAME_SIZE)
		{
			S3 = false;
			Bukkit.broadcastMessage(ChatColor.RED + "Saving now");
			int start = (int)(System.currentTimeMillis() - LOCAL_TIME_OFFSET);
			
	        Bukkit.savePlayers();
	        for (World world : Bukkit.getWorlds()) {
	            world.save();
	        }
	        int end = (int)(System.currentTimeMillis() - LOCAL_TIME_OFFSET);
	        Bukkit.broadcastMessage(ChatColor.RED + "Saving complete in " + (end-start) + " ms.");
	        S3 = true;
	        S2 = true;
	        S1 = true;
	        LOCAL_TIME_OFFSET = System.currentTimeMillis();
	        return;
		}
		
		if (S2 && i >= FRAME_SIZE - 10000)
		{
			S2 = false;
			Bukkit.broadcastMessage(ChatColor.RED + "Saving in 10 seconds.");
			return;
		}
		
		if (S1 && i >= FRAME_SIZE - 60000)
		{
			S1 = false;
			Bukkit.broadcastMessage(ChatColor.RED + "Saving in 60 seconds.");
			return;
		}
		
	}
}