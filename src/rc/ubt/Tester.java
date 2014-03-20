package rc.ubt;

import net.minecraft.server.v1_7_R1.NBTTagCompound;
import net.minecraft.server.v1_7_R1.NBTTagEnd;
import net.minecraft.server.v1_7_R1.NBTTagList;
import net.minecraft.server.v1_7_R1.NBTTagString;

import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import rc.ubt.impl.UnsafeImpl;

public class Tester implements Listener
{
	//this is test class, loader class will be static and everything esle will be modular
	
	//when loader class code finished, it wont change on updates, i will dump classfiles (or ever plain source texts)
	//into defined folder and system will load given classes
	//Tester class unlike all other classes can be defined on it's own
	//in my case i will load directly from workspace
	@EventHandler()
	public void OnChat(AsyncPlayerChatEvent tt)
	{
		CraftItemStack tz = (CraftItemStack) tt.getPlayer().getItemInHand();
		if (tz == null)return;
		net.minecraft.server.v1_7_R1.ItemStack handle = (net.minecraft.server.v1_7_R1.ItemStack) UnsafeImpl.getObject(tz, "handle");
		
		handle.setTag(new NBTTagCompound());
		
		NBTTagList ttz = new NBTTagList();
		NBTTagCompound data = new NBTTagCompound();
		
		data.setByte("Id", (byte) 2);
		data.setByte("Amplifier", (byte) 1);
		data.setInt("Duration", 1);
		data.setBoolean("Ambient", true);
        
		ttz.add(data);
		handle.tag.set("CustomPotionEffects", ttz);
	}
}

