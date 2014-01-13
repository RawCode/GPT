package ru.rawcode.dev.blocks;

import java.util.Random;

import net.minecraft.server.v1_7_R1.CreativeModeTab;
import net.minecraft.server.v1_7_R1.Material;


public class CustomCobble extends CustomBlock {

	public CustomCobble() {
        super(Material.GRASS);
        a(true);
        a(CreativeModeTab.b);
    }

    public void a(net.minecraft.server.v1_7_R1.World world, int i, int j, int k, Random random)
    {
        if(!world.isStatic){}
    }
}