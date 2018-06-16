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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Keridos on 28.02.14.
 * This Class describes the generic block this mod uses.
 */
public class BlockFL extends Block {

    protected BlockFL(String unlocName, Material material, SoundType type, float hardness) {
        super(material);
        setHardness(hardness);
        setNames(unlocName);
        if (!unlocName.equals(Names.Blocks.PHANTOM_LIGHT) && !unlocName.equals(Names.Blocks.UV_LIGHTBLOCK)) {
            this.setCreativeTab(CreativeTabFloodlight.FL_TAB);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        dropInventory(world, pos);
        super.breakBlock(world, pos, blockState);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityFL) {
            if (stack.hasDisplayName()) {
                ((TileEntityFL) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());
            }
            ((TileEntityFL) world.getTileEntity(pos)).setOrientation(getFacing(placer));
            world.setBlockState(pos, this.getStateFromMeta(getFacing(placer).ordinal()), 2);
        }
    }


    protected void dropInventory(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.getCount() > 0) {
                Random rand = new Random();
                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityItem = new EntityItem
                        (world, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ, itemStack.copy());
                if (itemStack.hasTagCompound()) {
                    itemStack.setTagCompound(itemStack.getTagCompound().copy());

                }
                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntity(entityItem);
                itemStack.setCount(0);
            }
        }
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