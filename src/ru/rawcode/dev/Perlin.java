package ru.rawcode.dev;
/**
 * Perlin sample Applet
 *
 * Sample 2D perlin noise generator with sample applet
 *
 * @author bruno augier
 * @email bruno.augier@dzzd.net
 * @website http://dzzd.net/
 * @version 1.00 2004/08/06
 * @updated 2007/11/28
 */

import java.awt.*;
import java.awt.image.*;
import java.applet.*;

public class Perlin extends Applet implements Runnable 
{
    private Thread anim;
	private MemoryImageSource imgsrc;
	private Image memimg;
	private int[] pixels;
	private boolean running;
	private int nbStart;
	
	public void init() 
	{
	    this.pixels = new int[128 * 128];
	    this.imgsrc = new MemoryImageSource(128, 128, pixels, 0, 128);
	    this.imgsrc.setAnimated(true);
	   	this.memimg = createImage(imgsrc);
	   	this.running=false;
	   	this.nbStart=0;
	}

    public void start() 
    {
    	if(this.nbStart>0)
    		return;
    	this.nbStart++;
	    anim = new Thread(this);
	    this.running=true;
	    anim.start();
	}

	public void destroy() 
	{
	    this.running=false;
	    try
	    {
	    	Thread.sleep(1000);
	    }
	    catch(InterruptedException ie)
	    {
	    }
	}
	
	/**
	 * Brut noise generator using pseudo-random
	 */
	public double noise(int x,int y)
	{
		x=x + y * 57;
		x=((x<<13) ^ x);
		double t=(x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff;
		return 1-t*0.000000000931322574615478515625;
		
	}
	
	/**
	 * Smoothed noise generator using 9 brut noise
	 */
	public double sNoise(int x,int y)
	{
		double corners = ( noise(x-1, y-1)+noise(x+1, y-1)+noise(x-1, y+1)+noise(x+1, y+1) ) * 0.0625;
	    double sides   = ( noise(x-1, y)  +noise(x+1, y)  +noise(x, y-1)  +noise(x, y+1) ) *0.125;
	    double center  =  noise(x, y) *0.25;
		return corners + sides + center;		
	}

	/**
	 * Linear Interpolator
	 *
	 * @param a value 1
	 * @param b value 2
	 * @param x interpolator factor
	 * 
	 * @return value interpolated from a to b using x factor by linear interpolation
	 */
	public double lInterpoleLin(double a,double b,double x)
	{
		return  a*(1-x) + b*x;		
	}

	
	/**
	 * Cosine Interpolator
	 *
	 * @param a value 1
	 * @param b value 2
	 * @param x interpolator factor
	 * 
	 * @return value interpolated from a to b using x factor by cosin interpolation
	 */
	public double lInterpoleCos(double a,double b,double x)
	{
		
		double ft = x * 3.1415927;
		double f = (1 - Math.cos(ft)) * .5;
		return  a*(1-f) + b*f;
	}
	
	/**
	 * Smooth noise generator with two input 2D
	 * <br>
	 *  You may change the interpolation method : cosin , linear , cubic 
	 * </br>
	 * @param x x parameter
	 * @param y y parameter
	 *
	 * @return value of smoothed noise for 2d value x,y
	 */
	public double iNoise(double x,double y)
	{
		int iX=(int)x;
		int iY=(int)y;
		double dX=x-iX;
		double dY=y-iY;
		double p1=sNoise(iX,iY);
		double p2=sNoise(iX+1,iY);
		double p3=sNoise(iX,iY+1);
		double p4=sNoise(iX+1,iY+1);
		double i1=lInterpoleLin(p1,p2,dX);
		double i2=lInterpoleLin(p3,p4,dX);
		return lInterpoleLin(i1,i2,dY);	
	} 	
	
	/**
	 * Perlin noise generator for two input 2D
	 * 
	 * @param x x parameter
	 * @param y y parameter
	 * @param octave maximum octave/harmonic
	 * @param persistence noise persitence
	 * @return perlin noise value for given entry
	 */
	public double pNoise(double x,double y,double persistence,int octave)
	{
		double result;
		double amplitude=1;
		int frequence=1;
		result=0;
		for(int n=0;n<octave;n++)
		{
			result+=iNoise(x*frequence,y*frequence)*amplitude;
			frequence<<=1;
			amplitude*=persistence;
		}
		return result;	
	}

	
	public void run() 
	{
		int nbLoop=0;
		
	    while (this.running) 
	    {
	    	double zoom=0.3+0.1*Math.cos((nbLoop*Math.PI)/180.0);
			//For each pixels			
			for(int y=0;y<128;y++)
			for(int x=0;x<128;x++)
			{	
				//Compute perlin noise for given pos (modify a little the input value to animate)
				double c=pNoise(((double)x-128*zoom*0.5+nbLoop)*zoom,((double)y-128*zoom*0.5)*zoom,0.3,2);
				
				
				//Range value between [0-255]
				c*=128.0;
				c+=127.0;
				if(c>255.0)
					c=255.0;
				if(c<0.0)
					c=0.0;
				
				//here is where code maybe added to produce marble or such effect
				//TODO: implements code here					
				int r=(int)c;
				int v=(int)c;
				int b=(int)c;
				
				if(c>128)
				 r>>=1;
				if(c>128)
				 b>>=1;
				 

				//Draw pixel
				pixels[y* 128 + x]=0xFF000000|r<<16|v<<8 |b;
			}
			nbLoop++;
			imgsrc.newPixels();
			this.update(this.getGraphics());
			try{Thread.sleep(10);}
			catch(InterruptedException ie){}
	    }
	}
	public void paint(Graphics g) 
	{
	}
	public void update(Graphics g) 
	{
	    g.drawImage(memimg, 0, 0, this);
	}
}
