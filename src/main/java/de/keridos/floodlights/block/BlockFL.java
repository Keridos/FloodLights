package de.keridos.floodlights.block;


import de.keridos.floodlights.client.gui.CreativeTabFloodlight;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityFL;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Keridos on 28.02.14.
 * This Class describes the generic block this mod uses.
 */
@SuppressWarnings({"WeakerAccess", "NullableProblems"})
public class BlockFL extends Block {

    protected BlockFL(String unlocName, Material material, SoundType type, float hardness) {
        super(material);
        setHardness(hardness);
        setSoundType(type);
        setNames(unlocName);
        if (!unlocName.equals(Names.Blocks.PHANTOM_LIGHT) && !unlocName.equals(Names.Blocks.PHANTOM_UV_LIGHT)) {
            this.setCreativeTab(CreativeTabFloodlight.FL_TAB);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (willHarvest)
            return true;
        return super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityFL)) {
            super.getDrops(drops, world, pos, state, fortune);
            return;
        }

        NBTTagCompound nbtTagCompound = ((TileEntityFL) tileEntity).writeOwnToNBT(new NBTTagCompound());
        ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, 0, nbtTagCompound);
        stack.setTagCompound(nbtTagCompound);
        drops.add(stack);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // This method must be called on both client and server in order to prevent orientation from being
        // quickly changed from default to correct value when synchronization packet arrives to the tile entity.
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileEntityFL))
            return;

        TileEntityFL tileFL = (TileEntityFL) tile;

        if (stack.hasTagCompound()) {
            //noinspection ConstantConditions
            tileFL.readOwnFromNBT(stack.getTagCompound());
        }

        if (stack.hasDisplayName())
            tileFL.setCustomName(stack.getDisplayName());

        tileFL.setOrientation(getFacing(placer));
    }

    public EnumFacing getFacing(EntityLivingBase entityLiving) {
        float rotationYaw = MathHelper.wrapDegrees(entityLiving.rotationYaw);
        float rotationPitch = (entityLiving.rotationPitch);
        int result = (rotationPitch < -45.0F ? 1 : (rotationPitch > 45.0F
                ? 0 : ((MathHelper.floor(rotationYaw * 4.0F / 360.0F + 0.5D) & 3) + 2)));
        EnumFacing[] direction = {EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH,
                EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
        return direction[result];
    }


    /**
     * Sets unlocalized name {@link Item#setUnlocalizedName(String)} and registryName {@link Item#setRegistryName(String)}.
     * <br>
     * Naming convention:
     * <li><b>camelCase</b> - unlocalized name</li>
     * <li><b>unserscore_based</b> - registry name (automatic conversion)</li>
     */
    private void setNames(String unlocalizedName) {
        setUnlocalizedName(unlocalizedName);
        setRegistryName(Names.MOD_ID, Names.convertToUnderscore(unlocalizedName));
    }


    private String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}