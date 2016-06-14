package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class TileEntityUVLightBlock extends TileEntityPhantomLight implements ITickable {

    public TileEntityUVLightBlock() {
        super();
    }

    private AxisAlignedBB getBoundingBox() {
        return new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 1, this.pos.getZ() + 1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update() {
        if (!worldObj.isRemote && worldObj.getWorldTime() % 20 == 5) {
            AxisAlignedBB axisAlignedBB = this.getBoundingBox();
            List<EntityLivingBase> entityList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB);
            ListIterator iterator = entityList.listIterator();
            while (iterator.hasNext()) {
                ((EntityLivingBase) iterator.next()).attackEntityFrom(DamageSource.cactus, ConfigHandler.damageUVFloodlight);
            }
        }
    }
}
