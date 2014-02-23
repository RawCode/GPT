package rc.gpt;

import java.net.URLClassLoader;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import rc.gpt.wgen.Generator_DFS;
import rc.ubt.UnsafeImpl;

public class Loader extends JavaPlugin implements Listener
{
	
	//This method is constructor hijack, it will set static variable to latest constructed object of this type;
	//You expected to never construct multiple instances of this class at same time;
	public static JavaPlugin 	INSTANCE = null;
	{
		INSTANCE = this;
	}
	
	//this method is class constructor hijack, it executed at moment of class initialization (once in most cases);
	static String 		FOLDER	 = null;
	static String 		NMS		 = Bukkit.getServer().getClass().getName().split("\\.")[3];
	static
	{
		String[] a = ((URLClassLoader)Loader.class.getClassLoader()).getURLs()[0].getFile().split("/");
		FOLDER = a[a.length-1].substring(4,a[a.length-1].length()-4);
		LogManager.getLogger().info("Unsafe Bukkit Toolkit " + NMS + " will load from " + FOLDER);
		//it does not load classes from folder currently, this is TBI feature for fast debug deployment;
		
		//world generator hijack
		YamlConfiguration YC = (YamlConfiguration) UnsafeImpl.getObject(Bukkit.getServer(), "configuration");
		ConfigurationSection ss = YC.createSection("worlds");
		ss = ss.createSection("world");
		ss.set("generator", "GPToolkit");
	}
	
	static void Notify(Listener L)
	{
		Bukkit.getPluginManager().registerEvents(L, INSTANCE);
	}

    public void onEnable() 
    {
    	Notify(this);
    	//Notify something
    }
    
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
    	LogManager.getLogger().info("Dome Fuji Survival generator will be used for " + worldName);
        return new Generator_DFS();
    }
}
