package rc.ubt.impl;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

import org.bukkit.Bukkit;

import sun.misc.Unsafe;
import sun.reflect.Reflection;

/**
  * sandbox key for obfuscated stuff -Djava.security.manager
  */

/**TODO 
 * Generic update to allow more actions on fields and objects without lots of calls
 * Benchmark testing for hashmap caching and unchached version to get proper results about speed
 * If cached version faster - move everything to cache.
 * @author RawCode
 *
 */
@SuppressWarnings("all")
public class UnsafeImpl
{
	/**
	 * Java process IO
	 * This class allows to read and write java process memory directly via unsafe natives
	 * Same stuff already implemented in jillegal but i dont evedrop on it's sources for fun
	 * 
	 * Unsafe internally used by Reflections, basically this is reflections with all security and safety removed
	 * Java objects is API over internal OOPs
	 * 
	 * !!!Platform\version dependant!!!
	 * Tested platform Windows 7 - x64 - CompressedOOPS (less then 32gb memory)
	 */
	
	public static Unsafe unsafe;
	static Object anchor;
	static long   offset;
	static
	{
		try
			{
			Field f = Class.forName("sun.misc.Unsafe").getDeclaredFields()[0];
			f.setAccessible(true);
			unsafe = (Unsafe) f.get(null);
			offset = unsafe.staticFieldOffset(fetchField(UnsafeImpl.class,"anchor"));
			}
		catch(Throwable t)
		{
			t.printStackTrace();
			System.exit(42);
		}
	}
	
	static public long Object2ID(Object O){
		anchor = O;
		return unsafe.getInt(UnsafeImpl.class, offset) & 0xFFFFFFFFL;
	}
	
	static public Object ID2Object(long ID){
		unsafe.putLong(UnsafeImpl.class, offset, ID);
		return anchor;
	}
	
	static public void Object2Trace(Object O){
		Object2Trace(O,64);
	}
	
	static public void Object2Trace(Object O,int L){
		System.out.println("Trace of " + Long.toHexString(Object2ID(O)));
        for (int i = 0; i < L; i++){
            System.out.print(String.format("%02X ", unsafe.getByte(O,(long)i)));
            	if (i % 4 == 3)
            		System.out.println();
        }
        System.out.println();
	}
	
	//result are extremely easy to cache
	//we need from field only two params - type (static or not) and offset, nothing more nothing less
	//this can be cached easy by Source+Name
	//since currently there is no real need of high perfomance this feature postnoted
	static private Field fetchField(Class Source,String... Names)
	{
		for(;Source != Object.class;Source = Source.getSuperclass())
		{
			for (Field f : Source.getDeclaredFields())
				for (String s : Names)
					if (f.getName().equals(s))
						return f;
		}
		return null;
	}
	
	//primitive cast method for getting stuff from slot
	
	//put methods are word and dword
	
	
	static public void putObjectStatic(Class Owner, Object Value,String... Names)
	{
		Field Target = fetchField(Owner,Names);
		if ((Target.getModifiers() & 8) == 0)
		{
			return;
		}
		unsafe.putObject(Owner,unsafe.staticFieldOffset(Target),Value);
	}
	
	static public void putObject(Object Owner, Object Value,String... Names)
	{
		Field Target = fetchField(Owner.getClass(),Names);
		if ((Target.getModifiers() & 8) == 0)
		{
			unsafe.putObject(Owner,unsafe.objectFieldOffset(Target),Value);
			return;
		}
		unsafe.putObject(Owner.getClass(),unsafe.staticFieldOffset(Target),Value);
	}
	
	static public Object getObject(Object Owner, String... Names)
	{
		Field Target = fetchField(Owner.getClass(),Names);
		if ((Target.getModifiers() & 8) == 0)
		{
			return unsafe.getObject(Owner,unsafe.objectFieldOffset(Target));
		}
		return unsafe.getObject(Owner.getClass(),unsafe.staticFieldOffset(Target));
	}
	
	public static void putInt(Object Owner, int Value,String... Names)
	{
		Field Target = fetchField(Owner.getClass(),Names);
		if ((Target.getModifiers() & 8) == 0)
		{
			unsafe.putInt(Owner,unsafe.objectFieldOffset(Target),Value);
			return;
		}
		unsafe.putInt(Owner.getClass(),unsafe.staticFieldOffset(Target),Value);
	}
	
	static public Object getInt(Object Owner, String... Names)
	{
		Field Target = fetchField(Owner.getClass(),Names);
		if ((Target.getModifiers() & 8) == 0)
		{
			return unsafe.getInt(Owner,unsafe.objectFieldOffset(Target));
		}
		return unsafe.getInt(Owner.getClass(),unsafe.staticFieldOffset(Target));
	}
	

	
	//I need complex all in one with caching and other stuff method backed by hashtable or something similar
	//This method shoud allow me to read and write any field without any additional calls
	//since memory and calculations footprint not soo heavy, i will need to benchmark hashtable backed version
	//probably it will be faster to just reseak, then asking hashtable about values
	
	//integer have 4 words size
	//string have 6 words size
	//final File f = new File(MyClass.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	
	//1091        FileInputStream fdIn = new FileInputStream(FileDescriptor.in);
	//1092        FileOutputStream fdOut = new FileOutputStream(FileDescriptor.out);
	//1093        FileOutputStream fdErr = new FileOutputStream(FileDescriptor.err);
	
	
	
	
	static private Thread mainref = null;

	static public void RandomLockingMethodAPI(final boolean Forced) throws Throwable
	{
		
		if (Thread.currentThread() == mainref && Forced)
			new Thread()
		{
			public void run()
			{
				try
				{
					RandomLockingMethodAPI(Forced);
				} catch (Throwable e){}
			}
		}.start();
		else
			RandomLockingMethodImpl();
	}
	
	static public void RandomLockingMethodImpl() throws Throwable
	{
		Thread.sleep(1000);
		System.out.println("Message second later from " + Thread.currentThread());
	}

	static public int[] Key2Value = new int[256];
	public static void Initialize()
	{
		Arrays.fill(Key2Value, 1488);
		Key2Value['0'] = 0;
		Key2Value['1'] = 1;
		Key2Value['2'] = 2;
		Key2Value['3'] = 3;
		Key2Value['4'] = 4;
		Key2Value['5'] = 5;
		Key2Value['6'] = 6;
		Key2Value['7'] = 7;
		Key2Value['8'] = 8;
		Key2Value['9'] = 9;
		Key2Value['m'] = 60;
		Key2Value['h'] = 3600;
		Key2Value['d'] = 86400;
	}
	
	public static long ProcessDelay(String Input)
	{
		byte[] RawData = Input.getBytes();
		
		int buffer   = 0;
		int brate    = 1;
		int multiply = 1;
		int result   = 0;
		int temp     = 0;
		int step     = RawData.length-1;
		
		
		for(;;)
		{
			temp = Key2Value[RawData[step]];
			
			if (temp == 1488) return -1;
			
			if (temp > 10)
			{
				result+= buffer * multiply;
				multiply = temp;
				buffer = 0;
				brate = 1;
			}
			else
			{
				buffer+= temp * brate;
				brate*= 10;
			}
			
			step--;
			
			if (step == -1)
			{
				result+= buffer * multiply;
				return result;
			}
		}
	}
	
	public static String ReverseDelay(long Input)
	{
		long now = System.currentTimeMillis();
		long tmp = ( Input - now ) / 1000;
		
		if (tmp < 1)
			return "expired";
		
		long d = tmp / 86400;
		if (d > 0)
			tmp-= d * 86400;
		
		long h = tmp / 3600;
		if (h > 0)
			tmp-= h * 3600;
		
		long m = tmp / 60;
		
		return m+"m"+(h>0 ? h+"h" : "")+(d>0 ? d+"d" : "");
	}
	
	static public void main(String[] args) throws Throwable {
		Initialize();
		System.out.println(ProcessDelay("1d1h"));
		
		
		

		//working offheap object sample
		
		//must check rules of memory copy, since for some reason it does not copy
		//from object to object and this is not good
		
		//TEST testA = new TEST();
		
	//	testA.A = 666;
	//	testA.B = 666;
	//	testA.C = new Integer(888);
		
		//int A = unsafe.getInt(test, 4l);
		//int B = unsafe.getInt(test, 8l);
		//int C = unsafe.getInt(test, 12l);
		
		//System.out.println(A);
		//System.out.println(B);
		//System.out.println(C);
		
		//long TRY = unsafe.allocateMemory(32);
		
		//Object FabA = ID2Object(TRY/8);
		//Object FabB = ID2Object((TRY/8)+1);
		
		//System.out.println(TRY);
		
		//unsafe.copyMemory(testA, 0, null, TRY, 32);
		//unsafe.copyMemory(testA, 8, null, TRY+16, 4);
		
		//((TEST)FabA).C = null;
		
		//System.out.println(((TEST)FabA).C);
		
		
	//	Object2Trace(FabA);
		
		//Object2Trace(testA);
		//((TEST)FabA).D = null;
		
		//Object2Trace(FabB);
		
		//System.out.println(unsafe.getInt(TRY+12));
		
		//System.out.println(o);
		
		//System.out.println(FabA.getClass());
		//System.out.println(FabB.getClass());
		
		
		//Object2Trace(o);
		
		//System.out.println(unsafe.getInt(TRY+19));
	
		
		/*
		System.out.println("THIS IS TESTSTRING TO CONSOLE");
		
		FileOutputStream fis = new FileOutputStream(FileDescriptor.out);
		
		System.out.println("BEFORE TEST");
		try
		{
			fis.write('a');
			fis.write(new byte[999]);
			System.out.println("TEST");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERR");
		}
		System.out.println("OUT");
		
		

		
		System.out.println("THIS IS TESTSTRING TO CONSOLE");
		
		
		
		/*
		int[] t = {1,2,3,4};
		int step = 0;
		for(;;)
		{
			if (t[step] != 3)
				System.out.println(t[step]);
			
			if (step++ == t.length-1)break;
		}
		/*
		
		
		/*
		Object S1 = new Integer(0xAAAAAAAA);
		Object S2 = new String("");
		
		//forObject_Dump(S1);
		//forObject_Dump(S2);
		
		int C1 = unsafe.getInt(S1, 8);
		int C2 = unsafe.getInt(S2, 8);
		
		forObject_Dump(forOOP_Object(C1));
		forObject_Dump(forOOP_Object(C2));
		
		int X1 = unsafe.getInt(forOOP_Object(C1), 24); //this is layout helper OOP instance
		int X2 = unsafe.getInt(forOOP_Object(C2), 24);
		
		System.out.println(X1);
		System.out.println(X2);
		
		//forObject_Dump(forOOP_Object(X1));
		//forObject_Dump(forOOP_Object(X2));
		
		//int tt = unsafe.getInt(subject, 8);
		//System.out.println(Long.toHexString(I2L(tt)));
		//forObject_Dump(forOOP_Object(tt));
		//forObject_Dump(forObject_OOP(tt));
		/*
		Path path = Paths.get("d://1.class");
		byte[] data = Files.readAllBytes(path);
		
		for (byte b : data)
			System.out.println(b);
		
	    Path pathd = Paths.get("d://bt.txt");
	    Files.write(pathd, data); //creates, overwrites
	    
	    //string representation of byte
	    //casting byte to it's char will be implemented via replacement table
	    /*
	    challenge eh?
	    		looks like you accept entries about "let i merge everything into single line", i will provide ultimate entry with this "feature".

	    		1) I will pick very first entry of this thread - class "Give"
	    		2) I will precompile it
	    		3) With little suplementary class i will assemble byte array storage from given class
		
		/*
		B test = new B();
		
		Field f = forName_Field(B.class,"A");
		
		System.out.println(f);
		*/
		
		/*
		long before = System.nanoTime();
		for (int i = 0 ; i < 1000000 ; i++)
		{
			forName_Field(Reflection.class,"zz");
		}
		System.out.println(System.nanoTime()-before);
		
		before = System.nanoTime();
		for (int i = 0 ; i < 1000000 ; i++)
		{
			testaz(Reflection.class,"zz");
		}
		System.out.println(System.nanoTime()-before);
		//426209254
		//424380599
		//198173277
		//198173277
		//198173277
		//367406745
		//197619324
		//364876120
		
		
		/*
		short[][] data = new short[16][];
		
		short[][] test = new short[16][];
		
		System.out.println(6 | 10);
		
		
		/*
		
		URLClassLoader urs = (URLClassLoader) ClassLoader.getSystemClassLoader();
		
		Method m = URLClassLoader.class.getDeclaredMethod("addURL",new Class[]{URL.class});
		m.setAccessible(true);
		
		URL u = new File("D:\\1").toURL();
		
		//m.invoke(urs, u);
		
		Class testa = Class.forName("ru.rawcode.dev.A");
		/*
		File f = new File("D:\\1");
		File[] ff = f.listFiles();
		
		File USE = ff[0];
		
	    FileInputStream stream = new FileInputStream(USE);
	    
	    byte[] data;
	    
	    stream.read(data = new byte[stream.available()]);
	    
	    int esize = data[14]*255+data[15];
	    byte[] ename = new byte[esize];
	    
	    System.arraycopy(data, 16, ename, 0, esize);
	    
	    System.out.println(new String(ename));
	    
		Class az = unsafe.defineClass(new String(ename), data, 0, data.length);
		
		ClassLoader zz = az.getClassLoader();
		
		havemain test = (havemain) unsafe.allocateInstance(az);
		test.main();
		
		zz.loadClass(new String(ename));
		
		
		Class testa = Class.forName(new String(ename));
		System.out.println(az.getClassLoader());
		
		/*
		
		byte[] test = testLocation();
		
		//printLocation(test);
		
		long testl = Loc2Long(test);
		
		System.out.println(Long.toHexString(testl));

		for (byte b : test) {
			   System.out.format("0x%x ", b);
			}
		
		byte[] bbs = Long2Loc(testl);
		System.out.println();
		for (byte b : bbs) {
			   System.out.format("0x%x ", b);
			}
		
		//int OUT = bb[3] + (bb[2]<<8) + (bb[1]<<16) + (bb[0]<<24);
		
		//System.out.println(Integer.toHexString(OUT));
		
		/*
		Random r = new Random();
		
		byte[] setA = new byte[]{'h','e','l','l','o'};
		byte[] setB = new byte[]{'w','o','r','l','d'};
		
		//(0-19)+100
		//100-119
		/*
		long l = 0;
		int step = 0;
		
		byte[] az = new byte[5];
		
		for (;;)
		{
			r.setSeed(l);
			
			r.nextBytes(az);
			
			if (az[0] == setA[0])
				if (az[1] == setA[1])
					if (az[2] == setA[2])
						if (az[3] == setA[3])
							if (az[4] == setA[4])
								System.out.println(l);
			l++;
		}
	//	*/
		
		/*
		byte[] message = new byte[10];
		int i = 0;
		Random rnd = new Random();
		for(;;)
		{
			if (i == 0)
				rnd.setSeed(12051153);
			if (i == 5)
				rnd.setSeed(10782634);
			if (i == 10)
				break;
			message[i] = (byte) (rnd.nextInt(20)+100);
			i++;
		}
		
		System.out.println(new String(message));
		//seed A = 12051153
		//seed B = 10782634
		
		

		A testa = new A();
		B testb = new B();
		
		int aa = unsafe.getInt(testa, 4);
		int bb = unsafe.getInt(testb, 4);
		
		Object oa = forOOP_Object(aa);
		Object ob = forOOP_Object(bb);
		
		//forObject_Dump(oa);
		System.out.println();
		//forObject_Dump(ob);
		
		int offa = 82; //second key for interface
		unsafe.putInt(oa, offa*4, unsafe.getInt(ob, offa*4));
		offa = 7; //first key for interface
		unsafe.putInt(oa, offa*4, unsafe.getInt(ob, offa*4));
		
		havemain2 testing = (havemain2) new A();
		testing.have();
		
		/*
		for(;;){
		offa++;
		unsafe.putInt(oa, offa*4, unsafe.getInt(ob, offa*4));
		Thread.sleep(1);
		try
		{
			havemain2 testing = (havemain2) new A();
			testing.main();
			System.out.println("WORK WITH" + offa);
		} catch (Throwable e)
		{
		}
		}
		
		//forObject_Dump(oa);
		

		
		/*
		System.out.println(Integer.toHexString(z));
		forObject_Dump(forOOP_Object(z));
		if(true)return;
		System.out.println();
		System.out.println();
		System.out.println("FFFFFF");
		if(true)return;
		int b = unsafe.getInt(testb, 4);
		System.out.println(Integer.toHexString(b));
		
		forObject_Dump(forOOP_Object(b));
		
		
		//Class.forName("TEST");
		
		
		//UnsafeImpl test = new UnsafeImpl();
		//System.out.println(test.ss);
		
		//Field f = test.getClass().getDeclaredField("ss");
		//f.setAccessible(true);
		//f.set(test, new String("REPLACED"));
		
		//System.out.println(test.ss);
		
		
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
