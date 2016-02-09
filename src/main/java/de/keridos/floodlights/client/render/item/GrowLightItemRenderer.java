package de.keridos.floodlights.client.render.item;

import de.keridos.floodlights.client.render.block.TileEntityGrowLightRenderer;
import de.keridos.floodlights.client.render.model.TileEntitySquareFluorescentLightModel;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityGrowLight;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Keridos on 30.06.2015.
 * This Class
 */
public class GrowLightItemRenderer implements IItemRenderer {

    private final TileEntitySquareFluorescentLightModel modelGrowLight;

    public GrowLightItemRenderer(TileEntityGrowLightRenderer tileEntityGrowLightRenderer, TileEntityGrowLight tileEntityGrowLight) {
        this.modelGrowLight = new TileEntitySquareFluorescentLightModel();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        if (type == ItemRenderType.EQUIPPED) {
            GL11.glRotatef(-90, 0, 1, 0);
            GL11.glTranslated(0.5, 0, -0.5);
        } else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glRotatef(-90, 0, 1, 0);
            GL11.glRotatef(-20, 0, 1, 0);
            GL11.glTranslated(-0.5, 0.2, -0.5);
        } else if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY) {
            GL11.glRotatef(-90, 1, 0, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);
        }
        ResourceLocation textures;
        //The PushMatrix tells the renderer to "start" doing something.
        GL11.glPushMatrix();

        //This is setting the initial location.
        GL11.glTranslatef(0.5F, 1.5F, 0.5F);

        textures = (new ResourceLocation(Textures.Block.GROW_LIGHT_TEXTURE_OFF));
        Minecraft.getMinecraft().renderEngine.bindTexture(textures);


        //This rotation part is very important! Without it, your modelSmallFluorescent will render upside-down! And for some reason you DO need PushMatrix again!
        GL11.glPushMatrix();

        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        //A reference to your Model file. Again, very important.
        float xRotation = -(float) Math.PI / 2;
        float yRotation = 0.0F;
        float zRotation = 0.0F;
        this.modelGrowLight.setRotateAngle(this.modelGrowLight.shape1, xRotation, yRotation, zRotation);
        this.modelGrowLight.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        //Tell it to stop rendering for all the PushMatrix's
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }


}