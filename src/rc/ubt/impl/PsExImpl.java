package rc.ubt.impl;

import java.lang.reflect.Method;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class PsExImpl
{
	static Method xhas = null;
	static Object xpex = null;
	static public void init()
	{
		try
		{
			Class c = Class.forName("ru.tehkode.permissions.bukkit.PermissionsEx");
			if (c == null) return;
			xhas = c.getDeclaredMethod("has", Player.class,String.class);
			xpex = c.getDeclaredMethod("getPlugin", null).invoke(null, null);
		}
		catch(Throwable t){t.printStackTrace();};
	}
	
	static public boolean has(Player player, String permission)
	{
		if (xhas != null)
		{
			try
			{
				return (boolean) xhas.invoke(xpex, player,permission);
			}catch (Exception e){e.printStackTrace(); return false;}
		}
		return player.isOp();
	}
}
