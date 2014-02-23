package rc.gpt.ethr;
//event handler
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import rc.gpt.Loader;

public class _AutoRespawn implements Listener,Runnable {
	public _AutoRespawn(){}
	
	public String target;
	
	public _AutoRespawn(String e)
	{
		this.target = e;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDeathEvent (PlayerDeathEvent e){
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTask(Loader.INSTANCE, new _AutoRespawn(e.getEntity().getName()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerSome (AsyncPlayerChatEvent e){
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTaskLater(Loader.INSTANCE, new _AutoRespawn(e.getPlayer().getName()), 0L);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoinEvent (PlayerJoinEvent e){
		if (!e.getPlayer().isDead())
			return;
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTask(Loader.INSTANCE, new _AutoRespawn(e.getPlayer().getName()));
	}
	
	public void run() {
		CraftPlayer CP = (CraftPlayer) Bukkit.getPlayer(target);
		if (CP == null){
			return;
		}
		EntityPlayer EP = CP.getHandle();
		if (EP.isAlive() || EP.getHealth() > 0.)
		{
			return;
		}
    	PlayerConnection PC = EP.playerConnection;
    	PC.player = MinecraftServer.getServer().getPlayerList().moveToWorld(EP, 0, false);
	}
}