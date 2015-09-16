package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class TileEntityUVLightBlock extends TileEntityPhantomLight {

    public TileEntityUVLightBlock() {
        super();
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    public AxisAlignedBB getBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateEntity() {
        if (!worldObj.isRemote && worldObj.getWorldTime() % 20 == 0) {
            AxisAlignedBB axisAlignedBB = this.getBoundingBox();
            List<EntityLivingBase> entityList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB);
            ListIterator iterator = entityList.listIterator();
            while (iterator.hasNext()) {
                ((EntityLivingBase) iterator.next()).attackEntityFrom(DamageSource.inFire, ConfigHandler.damageUVFloodlight);
            }
        }
    }
}
