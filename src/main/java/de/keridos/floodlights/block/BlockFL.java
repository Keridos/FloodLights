package de.keridos.floodlights.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.keridos.floodlights.client.gui.CreativeTabFloodlight;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityFL;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by Keridos on 28.02.14.
 * This Class describes the generic block this mod uses.
 */
public class BlockFL extends Block {
    protected String unlocName;
    public IIcon topIcon;
    public IIcon topOnIcon;
    public IIcon sideIcon;
    public IIcon botIcon;

    protected BlockFL(String unlocName, Material material, SoundType type, float hardness) {
        super(material);
        setStepSound(type);
        setHardness(hardness);
        setBlockName(unlocName);
        this.unlocName = unlocName;
        if (unlocName != "blockLight") {
            this.setCreativeTab(CreativeTabFloodlight.FL_TAB);
        }
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        topIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName() + "_top")));
        topOnIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName() + "_top_on")));
        botIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName() + "_bot")));
        sideIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName() + "_side")));
    }

    // For rotatable block, topIcon is front, botIcon is rear.
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == meta - 6) {
            return topOnIcon;
        } else if (meta > 5) {
            meta -= 6;
        }
        if (side == meta) {
            return topIcon;
        } else if (side == ForgeDirection.getOrientation(meta).getOpposite().ordinal()) {
            return botIcon;
        } else {
            return sideIcon;
        }
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        dropInventory(world, x, y, z);
        super.breakBlock(world, x, y, z, block, 0);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityFL) {
            if (itemStack.hasDisplayName()) {
                ((TileEntityFL) world.getTileEntity(x, y, z)).setCustomName(itemStack.getDisplayName());
            }
            ((TileEntityFL) world.getTileEntity(x, y, z)).setOrientation(ForgeDirection.getOrientation(getFacing(entityLiving)));
            world.setBlockMetadataWithNotify(x, y, z, getFacing(entityLiving), 2);
        }
    }


    protected void dropInventory(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.stackSize > 0) {
                Random rand = new Random();
                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, itemStack.copy());
                if (itemStack.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }
                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                itemStack.stackSize = 0;
            }
        }
    }

    public int getFacing(EntityLivingBase entityLiving) {
        float rotationYaw = MathHelper.wrapAngleTo180_float(entityLiving.rotationYaw);
        float rotationPitch = (entityLiving.rotationPitch);
        int result = (rotationPitch < -45.0F ? 1 : (rotationPitch > 45.0F ? 0 : ((MathHelper.floor_double(rotationYaw * 4.0F / 360.0F + 0.5D) & 3) + 2)));
        ForgeDirection[] direction = {ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
        return direction[result].ordinal();
    }
}