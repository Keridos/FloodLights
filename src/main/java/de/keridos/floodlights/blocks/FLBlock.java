package de.keridos.floodlights.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by Nico on 28.02.14.
 */
public class FLBlock extends Block {
    protected String unlocName;

    protected FLBlock(String unlocName, Material material, SoundType type, float hardness) {
        super(material);
        setStepSound(type);
        setHardness(hardness);
        setBlockName(unlocName);
        this.unlocName = unlocName;
    }


}