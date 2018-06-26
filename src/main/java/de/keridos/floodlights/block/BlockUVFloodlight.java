package de.keridos.floodlights.block;

import de.keridos.floodlights.handler.GuiHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityUVFloodlight;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
@SuppressWarnings({"deprecation", "NullableProblems"})
public class BlockUVFloodlight extends BlockFLColorableMachine implements ITileEntityProvider {

    public BlockUVFloodlight() {
        super(Names.Blocks.UV_FLOODLIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setHarvestLevel("pickaxe", 1);
        guiId = GuiHandler.GUI_ELECTIC_FLOODLIGHT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return true;
    }

    @Override
    public TileEntityUVFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntityUVFloodlight();
    }
}
