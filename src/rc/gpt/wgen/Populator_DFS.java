package rc.gpt.wgen;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.material.Sign;

import rc.ubt.SimplexImpl;

public class Populator_DFS extends BlockPopulator 
{
	static final double SCALE = 0.0625d;
	static final Random RND   = new Random(0);
	
	//
	//SEED = SEED * 5 + 1;
	//SEED %= 256;
	
	 public void PackedIce1(World world, Random random, int i, int j, int k) {
	        while (world.isEmpty(i, j, k) && j > 2) {
	            --j;
	        }

	        if (world.getType(i, j, k) != Blocks.SNOW_BLOCK) {
	            return false;
	        } else {
	            int l = random.nextInt(this.b - 2) + 2;
	            byte b0 = 1;

	            for (int i1 = i - l; i1 <= i + l; ++i1) {
	                for (int j1 = k - l; j1 <= k + l; ++j1) {
	                    int k1 = i1 - i;
	                    int l1 = j1 - k;

	                    if (k1 * k1 + l1 * l1 <= l * l) {
	                        for (int i2 = j - b0; i2 <= j + b0; ++i2) {
	                            Block block = world.getType(i1, i2, j1);

	                            if (block == Blocks.DIRT || block == Blocks.SNOW_BLOCK || block == Blocks.ICE) {
	                                world.setTypeAndData(i1, i2, j1, this.a, 0, 2);
	                            }
	                        }
	                    }
	                }
	            }

	        }
	
	public void SpawnSpike(World w, int X, int Z) {
		
	}
	
	public void SpawnShrub(World w, int X, int Z) {
		
		if (w.getBiome(X, Z) != Biome.JUNGLE)
			return;
		
		if (w.getBlockAt(X, 64, Z).getLightFromSky() != 15)
			return;

		w.getBlockAt(X, 64, Z).setTypeIdAndData(17, (byte) 3, true);

            for (int var8 = 65; var8 <= 67; ++var8)
            {
                int var9 = var8 - 65;
                int var10 = 2 - var9;

                for (int var11 = X - var10; var11 <= X + var10; ++var11)
                {
                    int var12 = var11 - X;

                    for (int var13 = Z - var10; var13 <= Z + var10; ++var13)
                    {
                        int var14 = var13 - Z;

                        if ((Math.abs(var12) != var10 || Math.abs(var14) != var10 || RND.nextInt(2) != 0) && !w.getBlockAt(var11, var8, var13).isEmpty())
                        {
                        	w.getBlockAt(var11, var8, var13).setTypeIdAndData(18, (byte) 3, true);
                        }
                    }
                }
            }
	}
	
	public void SpawnObelisk(World w, int X, int Z) {
		
		if (w.getBiome(X, Z) != Biome.ICE_PLAINS_SPIKES)
			return;
		
		int Height 	= RND.nextInt(28) + 10;
		int Size 	= RND.nextInt(3 ) + 2 ;
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
	}
    
    public void populate(World world, Random random, Chunk source) {
    	int X = source.getX();
    	int Z = source.getZ();
		RND.setSeed((long)(X)*341873128712L + (long)(Z)*132897987541L);
    	double R = SimplexImpl.noise(X*SCALE, Z*SCALE);
    	X *= 16;
    	Z *= 16;
    	
    	if (RND.nextInt(64) == 0)
    	{
    		SpawnObelisk(world,X + RND.nextInt(16),Z + RND.nextInt(16));
    	}
    	
    	SpawnShrub(world,X + 8,Z + 8);
    	
    	//16 surface hits to spawn spikes and trees (and probably other stuff)
    	//64 is fixed surface offset indicating normal surface
    	
		for (int i = 0 ; i < 16 ; i++)
		{
			
		}
            int centerX = (source.getX() << 4) + random.nextInt(16);
            int centerZ = (source.getZ() << 4) + random.nextInt(16);
            int centerY = world.getHighestBlockYAt(centerX, centerZ);
            //world.getBlockAt(centerX, centerY, centerZ).setTypeId(7);
    }
}