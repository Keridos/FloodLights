package de.keridos.floodlights.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by Keridos on 05.05.2015.
 * Small Floodlight - Keridos
 * Created using Tabula 5.0.0
 */
public class TileEntitySmallFluorescentLightModel extends ModelBase {
    public ModelRenderer shape1;

    public TileEntitySmallFluorescentLightModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.shape1.addBox(-8.0F, 5.0F, -3.0F, 16, 3, 6, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.shape1.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
