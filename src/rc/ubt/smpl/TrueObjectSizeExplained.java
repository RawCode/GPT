package rc.ubt.smpl;

import rc.ubt.impl.UnsafeImpl;

public class TrueObjectSizeExplained
{
	static boolean testbool = false;
	static public void main(String[] args) throws Throwable {
		System.out.println(testbool);
		UnsafeImpl.putInt(new TrueObjectSizeExplained(),999999,"testbool");
		System.out.println(testbool);
		System.out.println(UnsafeImpl.getInt(new TrueObjectSizeExplained(),"testbool"));
		testbool = true;
		System.out.println(UnsafeImpl.getInt(new TrueObjectSizeExplained(),"testbool"));
		testbool = false;
		System.out.println(UnsafeImpl.getInt(new TrueObjectSizeExplained(),"testbool"));
		System.out.println(testbool);
		
		//make conclusion yourself
	}
}