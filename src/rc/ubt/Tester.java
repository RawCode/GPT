package rc.ubt;

public class Tester
{
	//this is test class, loader class will be static and everything esle will be modular
	
	//when loader class code finished, it wont change on updates, i will dump classfiles (or ever plain source texts)
	//into defined folder and system will load given classes
	//Tester class unlike all other classes can be defined on it's own
	//in my case i will load directly from workspace
}

/**@EventHandler()
public void OnChat(AsyncPlayerChatEvent tt)
{
	CraftItemStack tz = (CraftItemStack) tt.getPlayer().getItemInHand();
	if (tz == null)return;
	net.minecraft.server.v1_7_R1.ItemStack handle = (net.minecraft.server.v1_7_R1.ItemStack) UnsafeImpl.getObject(tz, "handle");
	handle.setTag(new NBTTagCompound());
	NBTTagList ttz = new NBTTagList();
	ttz.add(new NBTTagCompound());
	handle.tag.set("ench", ttz);
}*/