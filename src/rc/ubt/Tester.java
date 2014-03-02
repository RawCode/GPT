package rc.ubt;

public class Tester
{
	//this is test class, loader class will be static and everything esle will be modular
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