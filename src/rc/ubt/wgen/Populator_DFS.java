package rc.ubt.wgen;

import java.util.Random;

import net.minecraft.server.v1_7_R1.MathHelper;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

import rc.ubt.impl.SimplexImpl;

public class Populator_DFS extends BlockPopulator 
{
	static final double SCALE = 0.0625d;
	static final Random RND   = new Random(0);
	
    public static int abs_int(int par0)
    {
        return par0 >= 0 ? par0 : -par0;
    }
	
	public void SpawnSpike(World w, int X, int Z) {
		{
			if (w.getBiome(X, Z) != Biome.ICE_PLAINS_SPIKES)
				return;
			if (RND.nextInt(30) != 0)
				return;
			
			int par4 = RND.nextInt(4) + 64;
			int var6 = RND.nextInt(4) + 7;
			int var7 = var6 / 4 + RND.nextInt(2);

			int var8;
			int var10;
			int var11;

			for (var8 = 0; var8 < var6; ++var8)
			{
				float var9 = (1.0F - (float) var8 / (float) var6) * (float) var7;
				var10 = fastfloor(var9);

				for (var11 = -var10; var11 <= var10; ++var11)
				{
					float var12 = (float) abs_int(var11) - 0.25F;

					for (int var13 = -var10; var13 <= var10; ++var13)
					{
						float var14 = (float) abs_int(var13) - 0.25F;

						if ((var11 == 0 && var13 == 0 || var12 * var12 + var14 * var14 <= var9 * var9)
								&& (var11 != -var10 && var11 != var10 && var13 != -var10 && var13 != var10 || RND
										.nextFloat() <= 0.75F))
						{
							w.getBlockAt(X + var11, par4 + var8, Z + var13).setType(Material.PACKED_ICE);
							if (var8 != 0 && var10 > 1)
							{
								w.getBlockAt(X + var11, par4 - var8, Z + var13).setType(Material.PACKED_ICE);
							}
						}
					}
				}
			}

			var8 = var7 - 1;

			if (var8 < 0)
			{
				var8 = 0;
			} else if (var8 > 1)
			{
				var8 = 1;
			}

			for (int var16 = -var8; var16 <= var8; ++var16)
			{
				var10 = -var8;

				while (var10 <= var8)
				{
					var11 = par4 - 1;
					int var17 = 50;

					if (Math.abs(var16) == 1 && Math.abs(var10) == 1)
					{
						var17 = RND.nextInt(5);
					}

					while (true)
					{
						if (var11 > 50)
						{
							w.getBlockAt(X + var16, var11, Z + var10).setType(Material.PACKED_ICE);
							--var11;
							--var17;

							if (var17 <= 0)
							{
								var11 -= RND.nextInt(5) + 1;
								var17 = RND.nextInt(5);
							}

							continue;
						}

						++var10;
						break;
					}
				}
			}
		}
	}
	
	
	
	public void SpawnShrub(World w, int X, int Z){
		if (w.getBiome(X, Z) != Biome.JUNGLE)
			return;
		
		for (int var8 = 64; var8 <= 67; ++var8)
		{
			int var9 = var8 - 64;
			int var10 = 2 - var9;

			for (int var11 = X - var10; var11 <= X + var10; ++var11)
			{
				int var12 = var11 - X;

				for (int var13 = Z - var10; var13 <= Z + var10; ++var13)
				{
					int var14 = var13 - Z;

					if ((Math.abs(var12) != var10 || Math.abs(var14) != var10 || RND.nextInt(2) != 0))
					{
						w.getBlockAt(var11, var8, var13).setTypeIdAndData(18,(byte) 3,true);
					}
				}
			}
		}
		w.getBlockAt(X, 64, Z).setTypeIdAndData(17,(byte) 3,true);
	}
	
	public void SpawnObelisk(World w, int X, int Z) {
		
		if (w.getBiome(X, Z) != Biome.ICE_PLAINS_SPIKES)
			return;
		
		int Height 	= RND.nextInt(28) + 10;
		int Size 	= RND.nextInt(2 ) + 3 ;
		int Base    = 60;
		
		//select 3-6-9 locations inside given boundary and
		//cast ores into given locations
		//replace obsidian with diamond
		//air with glowstone
		
		//orecast range:
		//y from base to base+height
		//X-size to X+size
		//Z-size to Z+size
		//basically simple
		
		for (int ly = Base; ly <= Base + Height ; ly++)
		{
			for (int lx = X - Size; lx <= X + Size ; lx++)
			{
				for (int lz = Z - Size; lz <= Z + Size ; lz++)
				{
					int ex = lx - X;
					int ez = lz - Z;
					if (ex * ex + ez * ez >= Size * Size + 1)
						continue;
					if (ex * ex + ez * ez <= 1)
						continue;
					
					w.getBlockAt(lx, ly, lz).setType(Material.OBSIDIAN);
				}
			}
		}
		
		for (int i = Size-1 ; i > 0 ; i--)
		{
			int bx = X - Size + RND.nextInt(Size*2);
			int by = Base + RND.nextInt(Height);
			int bz = Z - Size + RND.nextInt(Size*2);
			SpawnOre(w,bx,by,bz,Material.OBSIDIAN,Material.DIAMOND_ORE);
		}
	}
	
	public static int fastfloor(double x) 
	{
		return x >= 0 ? (int) x : (int) x - 1;
	}
	
    public void SpawnOre(World w, int X, int Y,int Z,Material Source,Material Ore)
    {
        float var6 = RND.nextFloat() * (float)Math.PI;
        double var7 = (double)((float)(X) + MathHelper.sin(var6) * (float)8 / 8.0F);
        double var9 = (double)((float)(X) - MathHelper.sin(var6) * (float)8 / 8.0F);
        double var11 = (double)((float)(Z) + MathHelper.cos(var6) * (float)8 / 8.0F);
        double var13 = (double)((float)(Z) - MathHelper.cos(var6) * (float)8 / 8.0F);
        double var15 = (double)(Y + RND.nextInt(3) - 2);
        double var17 = (double)(Y + RND.nextInt(3) - 2);

        for (int var19 = 0; var19 <= 8; ++var19)
        {
            double var20 = var7 + (var9 - var7) * (double)var19 / (double)8;
            double var22 = var15 + (var17 - var15) * (double)var19 / (double)8;
            double var24 = var11 + (var13 - var11) * (double)var19 / (double)8;
            double var26 = RND.nextDouble() * (double)8 / 16.0D;
            double var28 = (double)(MathHelper.sin((float)var19 * (float)Math.PI / (float)8) + 1.0F) * var26 + 1.0D;
            double var30 = (double)(MathHelper.sin((float)var19 * (float)Math.PI / (float)8) + 1.0F) * var26 + 1.0D;
            int var32 = fastfloor(var20 - var28 / 2.0D);
            int var33 = fastfloor(var22 - var30 / 2.0D);
            int var34 = fastfloor(var24 - var28 / 2.0D);
            int var35 = fastfloor(var20 + var28 / 2.0D);
            int var36 = fastfloor(var22 + var30 / 2.0D);
            int var37 = fastfloor(var24 + var28 / 2.0D);

            for (int var38 = var32; var38 <= var35; ++var38)
            {
                double var39 = ((double)var38 + 0.5D - var20) / (var28 / 2.0D);

                if (var39 * var39 < 1.0D)
                {
                    for (int var41 = var33; var41 <= var36; ++var41)
                    {
                        double var42 = ((double)var41 + 0.5D - var22) / (var30 / 2.0D);

                        if (var39 * var39 + var42 * var42 < 1.0D)
                        {
                            for (int var44 = var34; var44 <= var37; ++var44)
                            {
                                double var45 = ((double)var44 + 0.5D - var24) / (var28 / 2.0D);
                                if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0D)
                                {
                                	if (w.getBlockAt(var38, var41, var44).getType() == Source)
                                		w.getBlockAt(var38, var41, var44).setType(Ore);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void populate(World w, Random random, Chunk source) {
    	int X = source.getX();
    	int Z = source.getZ();
		RND.setSeed((long)(X)*341873128712L + (long)(Z)*132897987541L);
    	double R = (SimplexImpl.noise(X*SCALE, Z*SCALE)+1.0d)*2;
    	X *= 16;
    	Z *= 16;

    	if (RND.nextInt(64) == 0)
    	{
    		SpawnObelisk(w,X + RND.nextInt(16),Z + RND.nextInt(16));
    	}
    	
    	for (int p = 16 ; p > 0 ; p--)
    	{
    		SpawnShrub(w,X+RND.nextInt(16),Z+RND.nextInt(16));
    		SpawnSpike(w,X+RND.nextInt(16),Z+RND.nextInt(16));
    	}
    	X += 2;
    	Z += 2;
    	Material m = Material.STONE;
    	for (int p = (int) (32/R) ; p > 0 ; p--)
    	{
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(40),Z+RND.nextInt(12),m,Material.CLAY);
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(40),Z+RND.nextInt(12),m,Material.SAND);
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(40),Z+RND.nextInt(12),m,Material.GRAVEL);
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(40),Z+RND.nextInt(12),m,Material.DIRT);
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(40),Z+RND.nextInt(12),m,Material.COAL_ORE);
    		
    		SpawnOre(w,X+RND.nextInt(12),3+RND.nextInt(20),Z+RND.nextInt(12),Material.OBSIDIAN,Material.GLOWSTONE);
    		SpawnOre(w,X+RND.nextInt(12),3+RND.nextInt(20),Z+RND.nextInt(12),Material.NETHERRACK,Material.QUARTZ_ORE);
    	}

    	for (int p = (int) (16/R) ; p > 0 ; p--)
    	{
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(30),Z+RND.nextInt(12),m,Material.IRON_ORE);
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(20),Z+RND.nextInt(12),m,Material.GOLD_ORE);
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(20),Z+RND.nextInt(12),m,Material.LAPIS_ORE);

    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(15),Z+RND.nextInt(12),m,Material.REDSTONE_ORE);
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(10),Z+RND.nextInt(12),m,Material.DIAMOND_ORE);
    		SpawnOre(w,X+RND.nextInt(12),20+RND.nextInt(5),Z+RND.nextInt(12),m,Material.EMERALD_ORE);
    	}
    }
}