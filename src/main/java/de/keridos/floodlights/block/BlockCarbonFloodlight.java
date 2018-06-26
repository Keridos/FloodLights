package de.keridos.floodlights.block;

import de.keridos.floodlights.handler.GuiHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityCarbonFloodlight;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the carbon floodlight.
 */
@SuppressWarnings({"deprecation", "NullableProblems"})
public class BlockCarbonFloodlight extends BlockFLColorableMachine implements ITileEntityProvider {

    public BlockCarbonFloodlight() {
        super(Names.Blocks.CARBON_FLOODLIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setHarvestLevel("pickaxe", 1);
        guiId = GuiHandler.GUI_CARBON_FLOODLIGHT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return true;
    }

    @Override
    public TileEntityCarbonFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntityCarbonFloodlight();
    }
}
