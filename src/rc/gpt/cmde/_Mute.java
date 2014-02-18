package rc.gpt.cmde;
//command executor
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class _Mute implements Listener {

	static Map<String,Wrapper> Registry = new HashMap<String,Wrapper>();
	static WeakHashMap<Player,String> Chat = new WeakHashMap<Player,String>();
	
	private class Wrapper
	{
		String Admin;
		long   Expiration;
		String Reason;
		int Options;
		
		private Wrapper(String Admin,long Expiration,String Reason,int Options){
			this.Admin = Admin;
			this.Expiration = Expiration;
			this.Reason = Reason;
			this.Options = Options;
		}
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)  {
		String[] Data   = event.getMessage().toLowerCase().split("\\s+");
		if (Data.length == 0) return;
		String Order = Data[0].substring(1);

		/**
		 * 0 ���� �������
		 * 1 ��� ������
		 * 2 �����
		 * 3 �������
		 * 4-999 �������
		 * 
		 * ���� ������ ������ ��� - ��������
		 * ���� ������� ������ ����� - ��� �������
		 */
		event.setCancelled(true);
		if (Order.equals("mute"))
		{
			if (Data.length < 3)
			{
				event.getPlayer().sendMessage(ChatColor.RED + "��� ������ ���� ���� � �����������");
				return;
			}
			PermissionUser user = PermissionsEx.getUser(event.getPlayer());
			if (!user.has("RawDev.Mute")){
				event.getPlayer().sendMessage(ChatColor.RED + "��������� ���� RawDev.Mute");
				return;
			}

			long Time = 60000;
			try{Time = Integer.parseInt(Data[2])*1000;} catch (Exception e)
			{
				event.getPlayer().sendMessage(ChatColor.RED + "�������� �� ������, ������ ���� ����� ���������� �������� �������");
				return;
			}
			
	        StringBuilder message = new StringBuilder();
	        byte c = 0;
	        for (String arg : Data) {
	        	c++;
	        	if (c <= 3)continue;
	            message.append(arg);
	            message.append(" ");
	        }
			
			Bukkit.broadcastMessage(event.getPlayer().getName() + ChatColor.RED + " ������� " + Data[1] + " �� " + Time/1000 + " ������.");
			
	        Time += System.currentTimeMillis();
	        
	        Wrapper w = new Wrapper(event.getPlayer().getName(),Time,message.toString().trim(),0);
			Registry.put(Data[1], w);
			return;
		}
		
		
		if (Order.equals("fmute"))
		{
			if (Data.length < 2)
			{
				event.getPlayer().sendMessage(ChatColor.RED + "��� ������ � ���� �������� ����");
				return;
			}
			PermissionUser user = PermissionsEx.getUser(event.getPlayer());
			if (!user.has("RawDev.fMute")){
				event.getPlayer().sendMessage(ChatColor.RED + "��������� ���� RawDev.fMute");
				return;
			}

			long Time = 60000;
			try{Time = Integer.parseInt(Data[2])*1000;} catch (Exception e)
			{
				event.getPlayer().sendMessage(ChatColor.RED + "�������� �� ������, ������ ���� �����");
				return;
			}
			
			event.getPlayer().sendMessage(ChatColor.RED + Data[1] + " ������ ������� �� " + Time/1000 + " ������.");
			event.getPlayer().sendMessage(ChatColor.RED + "����������� � ���� ���� �� �����, ��� ��� � ����");
			
	        Time += System.currentTimeMillis();
	        
	        Wrapper w = new Wrapper(event.getPlayer().getName(),Time,"",1);
			Registry.put(Data[1], w);
			return;
		}
		
		if (Order.equals("mlist"))
		{
			PermissionUser user = PermissionsEx.getUser(event.getPlayer());
			if (!user.has("RawDev.mList")){
				event.getPlayer().sendMessage(ChatColor.RED + "��������� ���� RawDev.mList");
				return;
			}
			
			if (Registry.isEmpty())
			{
				event.getPlayer().sendMessage("������� � �������� ���. ���� �����, ���� ��������� ���������?");
				return;
			}
			
			event.getPlayer().sendMessage("������ ����� ����:");
			for (Map.Entry<String, Wrapper> entry : Registry.entrySet())
			{
				StringBuilder sb = new StringBuilder();
				if (entry.getValue().Options == 1 && user.has("RawDev.mList.fat"))
				{
					sb.append(ChatColor.BOLD + "(FAT) ");
				}
				sb.append(ChatColor.RED + entry.getKey() + " - " + (entry.getValue().Expiration - System.currentTimeMillis()) / 1000 + " ���. ��������. ����� "
				+ entry.getValue().Admin + ": " + entry.getValue().Reason);
				event.getPlayer().sendMessage(sb.toString());
			}
			return;
		}
		event.setCancelled(false);
	}
	
	@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
	public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event)  {
		
		String Name = event.getPlayer().getName().toLowerCase();
		Wrapper w = Registry.get(Name);
		
		String last = Chat.get(event.getPlayer());
		
		if (last != null)
			if (last.equalsIgnoreCase(event.getMessage()))
			{
				event.setCancelled(true);
				return;
			}
		Chat.put(event.getPlayer(), event.getMessage());
		
		if (w == null)return;
		
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