package ru.rawcode.dev;

import java.net.URLClassLoader;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import ru.rawcode.dev.commands._Mute;
import ru.rawcode.dev.events._AutoRespawn;
import ru.rawcode.dev.events._PvP;
import ru.rawcode.dev.events._SaveHandler;

public class Core extends JavaPlugin
{
	public static String     NMS   = Bukkit.getServer().getClass().getName().split("\\.")[3];
	public static byte       MODE  = 0;
	public static boolean    DEBUG = true;
	public static JavaPlugin INSTANCE;
	
	static
	{
		String[] a = ((URLClassLoader)Core.class.getClassLoader()).getURLs()[0].getFile().split("/");
		if (a[a.length-1].equalsIgnoreCase("RawDevA.jar")) /** Generic server mode */
			MODE = 1;
		if (a[a.length-1].equalsIgnoreCase("RawDevB.jar")) /** Generic server mode no debug */
			MODE = 2;
		if (a[a.length-1].equalsIgnoreCase("RawDevC.jar")) /** Sky server mode */
			MODE = 3;
		if (a[a.length-1].equalsIgnoreCase("RawDevD.jar")) /** Sky server mode no debug */
			MODE = 4;
														   /** Disabled mode */
		if (MODE == 2 || MODE == 4)DEBUG = false;
		System.out.println("RawDev construted with mode " + MODE + " debug is " + DEBUG);
	}
	
	public static void Echo(String S){
		if (DEBUG)System.out.println(S);
	}
	
	public void Activate(Listener l)
	{
		Bukkit.getPluginManager().registerEvents(l, INSTANCE);
	}
	
    public void onEnable() 
    {
    	if (MODE == 0)
    	{
    		System.out.println("RawDev disabled by JAR name");
    		return;
    	}
    	INSTANCE = this;
    	
    	Activate(new _Mute());
    	Activate(new _AutoRespawn());
    	Activate(new _PvP());
    	Activate(new _SaveHandler());
    }
    
    /** debug version */
    /*
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
    	System.out.println("TEST");
    	System.out.println(e.getMessage());
    	if (e.getMessage().equalsIgnoreCase("/az84")){
    		System.out.println("INSIDE");
    		World wz = MinecraftServer.getServer().getWorld();
    		wz.isStatic = ! wz.isStatic;
    	}
    	
    }*/
    
	/*
    
    /*
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
    	this.getLogger().log(Level.INFO, "Starting world generation now!");
        return new Generator();
    }*/
}
