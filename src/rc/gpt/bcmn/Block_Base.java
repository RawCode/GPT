package rc.gpt.bcmn;
//base class modification
import net.minecraft.server.v1_7_R1.Block;
import net.minecraft.server.v1_7_R1.Material;

public class Block_Base extends Block {
	
	public Block_Base(Material material) {
        super(material);
    }
	
	public Block_Base b(float f) {
        this.durability = f * 3.0F;
        return this;
    }
	
	public Block_Base c(float f) {
        this.strength = f;
        if (this.durability < f * 5.0F) {
            this.durability = f * 5.0F;
        }
        return this;
    }
}