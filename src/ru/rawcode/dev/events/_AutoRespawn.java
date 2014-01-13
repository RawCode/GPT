package ru.rawcode.dev.events;

import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import ru.rawcode.dev.Core;

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
		scheduler.runTask(Core.INSTANCE, new _AutoRespawn(e.getEntity().getName()));
		Core.Echo("ON DEATH PASSED CONTROL INTO RUN");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoinEvent (PlayerJoinEvent e){
		Core.Echo("ONJOIN INIT");
		if (!e.getPlayer().isDead())
		{
			Core.Echo("ONJOIN PLAYER IS ALIVE RETURNING");
			return; //We return from this method if player is alive.
		}
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTask(Core.INSTANCE, new _AutoRespawn(e.getPlayer().getName()));
		Core.Echo("ONJOIN PASSED CONTROL INTO RUN");
	}
	
	public void run() {
		Core.Echo("RUN INIT");
		CraftPlayer CP = (CraftPlayer) Bukkit.getPlayer(target);
		if (CP == null){
			Core.Echo("PLAYER NULL");
			return; //We return from this method if player killed on kick\disconnect and no longer present on server.
		}
		EntityPlayer EP = CP.getHandle();
		if (EP.isAlive())
		{
			Core.Echo("EP NOT DEAD FOR SOME REASON");
			return; //We return if player is alive, shoud never happen.
		}
		Core.Echo("RUN PASSED BOTH CHECKS");
    	EP.w();
    	PlayerConnection PC = EP.playerConnection;
    	PC.player = MinecraftServer.getServer().getPlayerList().moveToWorld(EP, 0, false);
		Core.Echo("RUN FINISHED");
	}
}