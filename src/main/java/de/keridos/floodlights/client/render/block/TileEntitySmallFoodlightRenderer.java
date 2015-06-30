package de.keridos.floodlights.client.render.block;

import de.keridos.floodlights.client.render.model.TileEntitySmallFluorescentLightModel;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * Created by Keridos on 05.05.2015.
 * This Class
 */
public class TileEntitySmallFoodlightRenderer extends TileEntitySpecialRenderer {

    //The model of your block
    private final TileEntitySmallFluorescentLightModel model;

    public TileEntitySmallFoodlightRenderer() {
        super();
        this.model = new TileEntitySmallFluorescentLightModel();
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
        ResourceLocation textures;
        TileEntitySmallFloodlight tileEntitySmallFloodlight = (TileEntitySmallFloodlight) te;
        //The PushMatrix tells the renderer to "start" doing something.
        GL11.glPushMatrix();

        //This is setting the initial location.
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        if (tileEntitySmallFloodlight.getState() != 0) {
            textures = (new ResourceLocation(Textures.Block.FLUORESCENT_FLOODLIGHT_TEXTURE_ON));
        } else {
            textures = (new ResourceLocation(Textures.Block.FLUORESCENT_FLOODLIGHT_TEXTURE_OFF));
        }
        //binding the textures
        Minecraft.getMinecraft().renderEngine.bindTexture(textures);

        //This rotation part is very important! Without it, your model will render upside-down! And for some reason you DO need PushMatrix again!
        GL11.glPushMatrix();

        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        //A reference to your Model file. Again, very important.
        float xRotation = 0.0F;
        float yRotation = 0.0F;
        float zRotation = 0.0F;
        switch (tileEntitySmallFloodlight.getOrientation()) {
            case DOWN:
                xRotation = (float) Math.PI;
                yRotation = (tileEntitySmallFloodlight.getRotationState() ? (float) Math.PI / 2 : 0.0F);
                break;
            case UP:
                yRotation = (tileEntitySmallFloodlight.getRotationState() ? (float) Math.PI / 2 : 0.0F);
                break;
            case WEST:
                zRotation = (float) Math.PI / 2;
                yRotation = (tileEntitySmallFloodlight.getRotationState() ? 0.0F : (float) Math.PI / 2);
                break;
            case EAST:
                zRotation = -(float) Math.PI / 2;
                yRotation = (tileEntitySmallFloodlight.getRotationState() ? 0.0F : (float) Math.PI / 2);
                break;
            case SOUTH:
                xRotation = -(float) Math.PI / 2;
                yRotation = (tileEntitySmallFloodlight.getRotationState() ? (float) Math.PI / 2 : 0.0F);
                break;
            case NORTH:
                xRotation = (float) Math.PI / 2;
                yRotation = (tileEntitySmallFloodlight.getRotationState() ? (float) Math.PI / 2 : 0.0F);
                break;
        }
        this.model.setRotateAngle(this.model.shape1, xRotation, yRotation, zRotation);

        this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        //Tell it to stop rendering for both the PushMatrix's
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    //Set the lighting stuff, so it changes it's brightness properly.
    private void adjustLightFixture(World world, int i, int j, int k, Block block) {
        Tessellator tess = Tessellator.instance;
        //float brightness = block.getBlockBrightness(world, i, j, k);
        //As of MC 1.7+ block.getBlockBrightness() has become block.getLightValue():
        float brightness = block.getLightValue(world, i, j, k);
        int skyLight = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int modulousModifier = skyLight % 65536;
        int divModifier = skyLight / 65536;
        tess.setColorOpaque_F(brightness, brightness, brightness);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) modulousModifier, divModifier);
    }
}

