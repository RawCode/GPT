package ru.rawcode.dev.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class FlatGenerator extends ChunkGenerator {
	
	public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
        return new short[16][];
    }

    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator)new FlatPopulator());
    }
    
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }
    
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0,0,0);
    }
}