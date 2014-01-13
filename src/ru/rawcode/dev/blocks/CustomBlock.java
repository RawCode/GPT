package ru.rawcode.dev.blocks;

import net.minecraft.server.v1_7_R1.Block;
import net.minecraft.server.v1_7_R1.Material;

public class CustomBlock extends Block {
	
	public CustomBlock(Material material) {
        super(material);
    }
	
	public CustomBlock b(float f) {
        this.durability = f * 3.0F;
        return this;
    }
	
	public CustomBlock c(float f) {
        this.strength = f;
        if (this.durability < f * 5.0F) {
            this.durability = f * 5.0F;
        }
        return this;
    }
}