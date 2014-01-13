package ru.rawcode.dev;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import sun.misc.Unsafe;
import sun.reflect.ConstantPool;


@SuppressWarnings({ "restriction", "rawtypes","unused" })
public class UnsafeImpl //aka java process IO
{
	
	//Reflections are just one big wrapped over Unsafe
	//Java objects are wrappers over JVM OOPs
	
	//Now you know everything required to work with this code

	//This fields are for a reason, dont change or remove them;
	
	//Tested platform Windows 7 x64 CompressedOOPS
	//OOP size == word
	
	//some methods marked private, think twice before using them
	
	int FIELD_A = 0xffffffff;
	int FIELD_B = 0xaaaaaaaa;
	String s = "TESTIFITESTIFI";
	static int FIELD_Y = 0xffffffff;
	static int FIELD_Z = 0xaaaaaaaa;
	

	static Unsafe unsafe 	   = null;
	static Object anchor 	   = null;
	static long   endoftheline = -1L;
	static 
	{
		try
			{
			Field f = Class.forName("sun.misc.Unsafe").getDeclaredFields()[0];
			f.setAccessible(true);
			unsafe = (Unsafe) f.get(null);
			endoftheline = forField_offset(forName_Field(UnsafeImpl.class,"anchor"));
			}
		catch(Throwable t){}
	};
	
	static public Field forName_Field(Class Source, String... Names)
	{
		Field[] f = Source.getDeclaredFields();
		for (Field f0 : f)
		{
			for (String s0 : Names)
			{
				if (f0.getName().equals(s0))
					return f0;
			}
		}
		if (Source.getSuperclass() == null)
			return null;
		return forName_Field(Source.getSuperclass(),Names);
	}
	
	static private long forField_offset(Field F) {
		return ((F.getModifiers() & 8) == 0) ?
				unsafe.objectFieldOffset(F)  :
				unsafe.staticFieldOffset(F)  ;
	}
	
	static private int forObject_OOP(Object O){
		anchor = O;
		return unsafe.getInt(UnsafeImpl.class, endoftheline);
	}
	
	static private Object forOOP_Object(int ID){
		unsafe.putInt(UnsafeImpl.class, endoftheline, ID);
		return anchor;
	}
	
	static public void setField_int (Object Owner,Field Target,int I) throws Throwable
	{
		Object o =(Target.getModifiers() & 8) != 0 ? unsafe.staticFieldBase(Target) : Owner;
		
		if ((Target.getModifiers() & 8) != 0)
			unsafe.putInt(o,unsafe.staticFieldOffset(Target),I);
		
		if ((Target.getModifiers() & 8) == 0)
			unsafe.putInt(o,unsafe.objectFieldOffset(Target),I);
	}
	
	static private void forObject_Dump(Object O){
	        for (int i = 0; i < 8*8; i++){
	            if (i % 4 == 0)
	            	System.out.println();
	            	System.out.print(String.format("%02X ", unsafe.getByte(O,(long)i)));
	        }
	}
	
	public static long normalize(int value) {
		   if(value >= 0) return value;
		   return (~0L >>> 32) & value;
		}
	
	final String ss;
	
	public UnsafeImpl(){
		ss = new String("TEST");
	}
	
	public static void main(String[] args) throws Throwable {
		
		UnsafeImpl test = new UnsafeImpl();
		System.out.println(test.ss);
		
		Field f = test.getClass().getDeclaredField("ss");
		f.setAccessible(true);
		f.set(test, new String("REPLACED"));
		
		System.out.println(test.ss);
		/*
		long A = 0xffffffff;
		long[] b = new long[0xff];
		System.out.println(Integer.toHexString(forObject_OOP(b)));
		String f = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";
		
		forObject_Dump(Thread.currentThread());
		
		for(;;){}
		
		
		//System.out.println(SimplexNoise.noise(0, 08));
		/*
		Field f = forName_Field(ConstantPool.class,"constantPoolOop");
		
		long l = forField_offset(f);
		
		ConstantPool cp = sun.misc.SharedSecrets.getJavaLangAccess().getConstantPool(UnsafeImpl.class);
		ConstantPool cp2 = sun.misc.SharedSecrets.getJavaLangAccess().getConstantPool(Object.class);
		
		Object o = unsafe.getObject(cp, l);
		Object o2 = unsafe.getObject(cp2, l);
		
		long o22 = unsafe.getLong(cp2, l);
		int o33 = unsafe.getInt(cp2,l);
		
		System.out.println(Long.toHexString(o22*8));
		System.out.println(Long.toHexString(normalize(o33)*8));
		
		//System.out.println(Long.toHexString(o22*8));
		
		
		//System.out.println(Integer.toHexString(forObject_OOP(o)));
		//System.out.println(Integer.toHexString(forObject_OOP(o2)*8));
		
		
		
		//for(;;){}
		/*
		if (true)return;

		if (checked){
			checked = false;
			tz = Thread.currentThread();

			new Thread(){
				public void run() {

					for(;;)
					{
						if (!tz.isAlive()){
							System.out.println("DEAD");
							new Thread(){
								public void run(){
									try
									{
										tz = Thread.currentThread();
										UnsafeImpl.main(null);
									} catch (Throwable e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}.start();
						}
						try
						{
							Thread.sleep(100l);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}

			}.start();
		}

		System.out.println(System.currentTimeMillis());

		for(;;)
		{
			if (new Random().nextInt(10) == 5)unsafe.throwException(new Exception("MAIN INTERRUPT"));
			System.out.println(System.currentTimeMillis());
			Thread.sleep(100l);
		}
		
		
		
		
		/*
	        PrintWriter writer = new PrintWriter(System.out);
	        System.setOut(null);
	        writer.println("Method 2");
	        writer.flush();
	        writer.close();
	        System.out.println("test");
		/*
	    ItemStack chestRed = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
	    LeatherArmorMeta metaRed = (LeatherArmorMeta) chestRed.getItemMeta();
	    metaRed.setColor(Color.RED);
		
		//long a = 0x7aaf14d90l;
		long b = 0x6fba00000l;
		
		//long c = a-b;
		
		long tt = forObject_OOP(new UnsafeImpl());
		
		long addr = tt*32*-1+b;
		//tt = tt << 3;
		
		System.out.println(Long.toHexString(addr));
		
		//System.out.println(Long.toHexString(c));
		
		//af514d90
		//55e29b0
		
		
		/*
		URL input = new URL("http://www.minecraftforum.net/topic/1003192-prominecrafthostcom-us-and-europe-locations-save-25-off-with-promo-code-thanks13/");
		URLConnection uc = input.openConnection();
		InputStream raw = uc.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		int[] data = new int[9999];
		int i = 0;
		
		while(in.available() != 0)
		{
			i = in.read();
			if (i == 118)System.out.println();
			System.out.print(String.valueOf((char)i));
		}
		*/
		
		//forObject_Dump(new UnsafeImpl());
		
		//for(;;);
		//System.out.println(8<<3);
		//0xfc2fff8 //max accessable adress
		//f55e2aea
		//System.out.println(0x7aaf15758L);
		//0x6fba00000 //heap base adress
		
		//long adr = 0x6fba00000L - 0x7aaf15758L;
		//System.out.println(Long.toHexString(adr));
		//int a = 0;
		//long ttz = unsafe.getLong(0x7aaf15758L+7);

		//for(;;)
		//	{
		//	System.out.println(Long.toHexString(unsafe.getLong(0x7aaf15758L+a)));
		//	a++;
		//	if (a == 10)return;
		//	}
		//System.out.println(Long.toHexString(ttz));
		
		//System.out.println(Long.toHexString(ttz));
		
		//Object test = forInt_OOP(tt);
		//Object tests = forInt_OOP(ttz);
		
		//System.out.println(ttz);
		
		//System.out.println(Integer.toHexString(unsafe.getInt(anchor, 4L)));
		
		//System.out.println(unsafe.getAddress(unsafe.getInt(anchor, 4L)));
        
        //long [] test = new long[99999999];
        
        //unsafe.getAddress(-1);
		//for(;;);
		
		/*
		long azz = forOOP_Int(anchor);
		
		System.out.println(Long.toHexString(azz));
		System.out.println(Long.toHexString(forOOP_Intnp(anchor) << 32 >>> 32));
		
		//unsafe.getAddress(azz);
		
		//int atz = unsafe.getInt(anchor, 8);
		//System.out.println(atz);
		
		
		Object zz = forInt_OOP((int) azz);
		System.out.println(zz.getClass());
		
		Object zzz = forInt_OOP((int) forOOP_Intnp(anchor));
		System.out.println(zzz.getClass());
		
		unsafe.getInt(zz, 0);
		
		//System.out.println(zz.getClass());
		
		//System.out.println(Long.toHexString(addressOf(UnsafeImpl.class)));
		//System.out.println(Integer.toHexString(forOOP_Int(UnsafeImpl.class)));
		
		
		//long a1 = unsafe.getByte(o, 8l);
	//	long a2 = unsafe.getByte(o, 9l)  << 8  ;
	//	long a3 = unsafe.getByte(o, 10l) << 16 ;
	//	long a4 = unsafe.getByte(o, 11l) << 24 ;
		
	//	long azx = normalizez(a1+a2+a3+a4);
		
		
		
		
		//System.out.println(String.format("%02X ", tz));
		
	//	System.out.println(Long.toHexString(azx));
	//	System.out.println(azx);
		
		//long tt = unsafe.getAddress(azx);
		
		//System.out.println(Long.toHexString(tt));
		
		//System.out.println(unsafe.getObject(null,0x7aaeb72f0L).getClass());
		
		//for(;;);
		*/
	}

}