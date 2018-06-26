package de.keridos.floodlights.block;

import de.keridos.floodlights.handler.GuiHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntityGrowLight;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockGrowLight extends BlockFLColorableMachine {

    public BlockGrowLight() {
        super(Names.Blocks.GROW_LIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setHarvestLevel("pickaxe", 1);
        guiId = GuiHandler.GUI_ELECTIC_FLOODLIGHT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySmallFloodlight) {
            TileEntitySmallFloodlight te = (TileEntitySmallFloodlight) worldIn.getTileEntity(pos);
            return state.withProperty(ACTIVE, te.getWasActive());
        } else
            return state.withProperty(COLOR, 16);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World w, @Nonnull IBlockState state) {
        return new TileEntityGrowLight();
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {

        double minX = 0;
        double minY = 0.8125;
        double minZ = 0;
        double maxX = 1;
        double maxY = 1;
        double maxZ = 1;

        return new AxisAlignedBB((double) pos.getX() + minX,
                (double) pos.getY() + minY,
                (double) pos.getZ() + minZ,
                (double) pos.getX() + maxX,
                (double) pos.getY() + maxY,
                (double) pos.getZ() + maxZ);

    }
}
