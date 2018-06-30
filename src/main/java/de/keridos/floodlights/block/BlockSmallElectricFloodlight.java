package de.keridos.floodlights.block;

import de.keridos.floodlights.handler.GuiHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import de.keridos.floodlights.util.MathUtil;
import de.keridos.floodlights.util.PropertiesEnum;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keridos on 01.10.14.
 * This Class defines the block properties of the electric floodlight.
 */
@SuppressWarnings({"NullableProblems", "deprecation"})
public class BlockSmallElectricFloodlight extends BlockFLColorableMachine implements ITileEntityProvider {

    public static final PropertyEnum MODEL = PropertyEnum.create("model", PropertiesEnum.EnumModelSmallLight.class);
    public static final PropertyBool ROTATIONSTATE = PropertyBool.create("rotationstate");

    @SuppressWarnings("unchecked")
    public BlockSmallElectricFloodlight() {
        super(Names.Blocks.SMALL_ELECTRIC_FLOODLIGHT, Material.ROCK, SoundType.METAL, 2.5F);
        setHarvestLevel("pickaxe", 1);
        guiId = GuiHandler.GUI_ELECTIC_FLOODLIGHT;

        setDefaultState(getDefaultState()
                .withProperty(MODEL, PropertiesEnum.EnumModelSmallLight.values()[0])
                .withProperty(ROTATIONSTATE, false));

        // TODO: allow to change ROTATIONSTATE of this block (for example a shift click with a tool)
    }

    @SuppressWarnings("unchecked")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MODEL, PropertiesEnum.EnumModelSmallLight.values()[meta]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getMetaFromState(IBlockState state) {
        return (((PropertiesEnum.EnumModelSmallLight) state.getValue(MODEL)).getID());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntitySmallFloodlight) {
            return super.getActualState(state, worldIn, pos)
                    .withProperty(ROTATIONSTATE, ((TileEntitySmallFloodlight) tile).getRotationState());
        }

        return super.getActualState(state, worldIn, pos);
    }

    @Override
    protected void buildBlockState(BlockStateContainer.Builder builder) {
        super.buildBlockState(builder);
        builder.add(MODEL).add(ROTATIONSTATE);
    }

    @Override
    public TileEntitySmallFloodlight createNewTileEntity(World world, int metadata) {
        return new TileEntitySmallFloodlight();
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state.getBlock() != this)
            return super.getBoundingBox(state, world, pos);

        IBlockState actualState = state.getActualState(world, pos);
        boolean rotated = actualState.getValue(ROTATIONSTATE);

        switch (((PropertiesEnum.EnumModelSmallLight) actualState.getValue(MODEL))) {
            case SMALL_ELECTRIC_FLOODLIGHTS_STRIP:
                switch (actualState.getValue(FACING)) {
                    case NORTH:
                        return rotated ? BoundingBoxes.StripRotated.NORTH : BoundingBoxes.Strip.NORTH;
                    case SOUTH:
                        return rotated ? BoundingBoxes.StripRotated.SOUTH : BoundingBoxes.Strip.SOUTH;
                    default:
                    case EAST:
                        return rotated ? BoundingBoxes.StripRotated.EAST : BoundingBoxes.Strip.EAST;
                    case WEST:
                        return rotated ? BoundingBoxes.StripRotated.WEST : BoundingBoxes.Strip.WEST;
                    case UP:
                        return rotated ? BoundingBoxes.StripRotated.UP : BoundingBoxes.Strip.UP;
                    case DOWN:
                        return rotated ? BoundingBoxes.StripRotated.DOWN : BoundingBoxes.Strip.DOWN;
                }
            default:
            case SMALL_ELECTRIC_FLOODLIGHT_SQUARE:
                switch (actualState.getValue(FACING)) {
                    case NORTH:
                        return BoundingBoxes.Square.NORTH;
                    case SOUTH:
                        return BoundingBoxes.Square.SOUTH;
                    default:
                    case EAST:
                        return BoundingBoxes.Square.EAST;
                    case WEST:
                        return BoundingBoxes.Square.WEST;
                    case UP:
                        return BoundingBoxes.Square.UP;
                    case DOWN:
                        return BoundingBoxes.Square.DOWN;
                }
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(0, new ItemStack(ModBlocks.blockSmallElectricLight, 1, this.getMetaFromState(state)));
        return drops;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 2; i++) {
            items.add(new ItemStack(ModBlocks.blockSmallElectricLight, 1, i));
        }
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    private static class BoundingBoxes {
        static class Strip {
            static AxisAlignedBB NORTH;
            static AxisAlignedBB SOUTH;
            static AxisAlignedBB EAST;
            static AxisAlignedBB WEST;
            static AxisAlignedBB UP;
            static AxisAlignedBB DOWN;
        }

        static class StripRotated {
            static AxisAlignedBB NORTH;
            static AxisAlignedBB SOUTH;
            static AxisAlignedBB EAST;
            static AxisAlignedBB WEST;
            static AxisAlignedBB UP;
            static AxisAlignedBB DOWN;
        }

        static class Square {
            static AxisAlignedBB NORTH;
            static AxisAlignedBB SOUTH;
            static AxisAlignedBB EAST;
            static AxisAlignedBB WEST;
            static AxisAlignedBB UP;
            static AxisAlignedBB DOWN;
        }
    }

    private static AxisAlignedBB rotateAABB(AxisAlignedBB source, EnumFacing direction) {
        double[] min = MathUtil.rotateD(source.minX, source.minY, source.minZ, direction);
        double[] max = MathUtil.rotateD(source.maxX, source.maxY, source.maxZ, direction);
        //double[] finallMin = MathUtil.sortMinMaxToMin(min, max);
        //double[] finalMax = MathUtil.sortMinMaxToMax(min, max);
        //return new AxisAlignedBB(finallMin[0], finallMin[1], finallMin[2], finalMax[0], finalMax[1], finalMax[2]);
        return new AxisAlignedBB(min[0], min[1], min[2], max[0], max[1], max[2]);
    }

    static {
        BoundingBoxes.Strip.EAST = new AxisAlignedBB(0, 0.3125, 0, 0.1875, 0.6875, 1);
        BoundingBoxes.Strip.NORTH = rotateAABB(BoundingBoxes.Strip.EAST, EnumFacing.NORTH);
        BoundingBoxes.Strip.SOUTH = rotateAABB(BoundingBoxes.Strip.EAST, EnumFacing.SOUTH);
        BoundingBoxes.Strip.WEST = rotateAABB(BoundingBoxes.Strip.EAST, EnumFacing.WEST);
        BoundingBoxes.Strip.UP = rotateAABB(BoundingBoxes.Strip.EAST, EnumFacing.UP);
        BoundingBoxes.Strip.DOWN = rotateAABB(BoundingBoxes.Strip.EAST, EnumFacing.DOWN);

        BoundingBoxes.StripRotated.EAST = new AxisAlignedBB(0, 0, 0.3125, 0.1875, 1, 0.6875);
        BoundingBoxes.StripRotated.NORTH = rotateAABB(BoundingBoxes.StripRotated.EAST, EnumFacing.NORTH);
        BoundingBoxes.StripRotated.SOUTH = rotateAABB(BoundingBoxes.StripRotated.EAST, EnumFacing.SOUTH);
        BoundingBoxes.StripRotated.WEST = rotateAABB(BoundingBoxes.StripRotated.EAST, EnumFacing.WEST);
        BoundingBoxes.StripRotated.UP = rotateAABB(BoundingBoxes.StripRotated.EAST, EnumFacing.UP);
        BoundingBoxes.StripRotated.DOWN = rotateAABB(BoundingBoxes.StripRotated.EAST, EnumFacing.DOWN);

        BoundingBoxes.Square.EAST = new AxisAlignedBB(0, 0, 0, 0.1875, 1, 1);
        BoundingBoxes.Square.NORTH = rotateAABB(BoundingBoxes.Square.EAST, EnumFacing.NORTH);
        BoundingBoxes.Square.SOUTH = rotateAABB(BoundingBoxes.Square.EAST, EnumFacing.SOUTH);
        BoundingBoxes.Square.WEST = rotateAABB(BoundingBoxes.Square.EAST, EnumFacing.WEST);
        BoundingBoxes.Square.UP = rotateAABB(BoundingBoxes.Square.EAST, EnumFacing.UP);
        BoundingBoxes.Square.DOWN = rotateAABB(BoundingBoxes.Square.EAST, EnumFacing.DOWN);
    }
}
