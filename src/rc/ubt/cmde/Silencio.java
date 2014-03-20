package rc.ubt.cmde;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Silencio
{
	public Silencio(){};
	
	String Source;
	long   Stamp;
	String Reason;
	byte   Mode;
	
	static Map<String,Silencio> MAP = new HashMap<String,Silencio>();
	public Silencio(String Target, String Source,long Stamp,String Reason,byte Mode)
	{
		this.Source = Source;
		this.Stamp  = Stamp;
		this.Reason = Reason;
		this.Mode   = Mode;
		MAP.put(Target, this);
	};
	
	
	@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
	public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event)  {
		
		String Name = event.getPlayer().getName().toLowerCase();
		Silencio s = MAP.get(Name);
		
		if (s == null) return;
		
		if (w.Expiration < System.currentTimeMillis())
		{
			Registry.remove(Name);
			Bukkit.getPlayer(w.Admin).sendMessage(ChatColor.RED + "��� ������ " + Name + " ����");
			return;
		}
		
		if (w.Options == 1)
		{
			event.getRecipients().clear();
			event.getRecipients().add(event.getPlayer());
			return;
		}

		event.getPlayer().sendMessage(ChatColor.RED + "��� ��������� ������������ ��� ��������������� " 
		+ w.Admin + " �� ������� " + w.Reason + " �������� " + (w.Expiration - System.currentTimeMillis()) / 1000 + " ������");
		event.setCancelled(true); 
	}
}
