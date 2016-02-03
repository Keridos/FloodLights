package de.keridos.floodlights.client.render.block;

import de.keridos.floodlights.client.render.model.TileEntitySmallFluorescentLightModel;
import de.keridos.floodlights.client.render.model.TileEntitySquareFluorescentLightModel;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import de.keridos.floodlights.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Keridos on 05.05.2015.
 * This Class
 */
public class TileEntitySmallFoodlightRenderer extends TileEntitySpecialRenderer {

    //The model of your block
    private final TileEntitySmallFluorescentLightModel modelSmallFluorescent;
    private final TileEntitySquareFluorescentLightModel modelSquareFluorescent;

    public TileEntitySmallFoodlightRenderer() {
        super();
        this.modelSmallFluorescent = new TileEntitySmallFluorescentLightModel();
        this.modelSquareFluorescent = new TileEntitySquareFluorescentLightModel();
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z,  float partialTicks, int destroyStage) {
        ResourceLocation textures;
        TileEntitySmallFloodlight tileEntitySmallFloodlight = (TileEntitySmallFloodlight) te;
        int metadata = te.getBlockMetadata();
        //The PushMatrix tells the renderer to "start" doing something.
        GL11.glPushMatrix();

        //This is setting the initial location.
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        switch (metadata) {
            case 0:
                if (tileEntitySmallFloodlight.getWasActive()) {
                    textures = (new ResourceLocation(Textures.Block.SMALL_FLUORESCENT_FLOODLIGHT_TEXTURE_ON));
                } else {
                    textures = (new ResourceLocation(Textures.Block.SMALL_FLUORESCENT_FLOODLIGHT_TEXTURE_OFF));
                }
                Minecraft.getMinecraft().renderEngine.bindTexture(textures);
                break;
            case 1:
                if (tileEntitySmallFloodlight.getWasActive()) {
                    textures = (new ResourceLocation(Textures.Block.SQUARE_FLUORESCENT_FLOODLIGHT_TEXTURE_ON));
                } else {
                    textures = (new ResourceLocation(Textures.Block.SQUARE_FLUORESCENT_FLOODLIGHT_TEXTURE_OFF));
                }
                Minecraft.getMinecraft().renderEngine.bindTexture(textures);
                break;
        }

        //This rotation part is very important! Without it, your modelSmallFluorescent will render upside-down! And for some reason you DO need PushMatrix again!
        GL11.glPushMatrix();

        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        //A reference to your Model file. Again, very important.
        float xRotation = 0.0F;
        float yRotation = 0.0F;
        float zRotation = 0.0F;
        switch (tileEntitySmallFloodlight.getOrientation()) {
            case DOWN:
                xRotation = (float) Math.PI;
                yRotation = (tileEntitySmallFloodlight.getRotationState() ? 0.0F : (float) Math.PI / 2);
                break;
            case UP:
                yRotation = (tileEntitySmallFloodlight.getRotationState() ? 0.0F : (float) Math.PI / 2);
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
                zRotation = (tileEntitySmallFloodlight.getRotationState() ? (float) Math.PI / 2 : 0.0F);
                break;
            case NORTH:
                xRotation = (float) Math.PI / 2;
                zRotation = (tileEntitySmallFloodlight.getRotationState() ? (float) Math.PI / 2 : 0.0F);
                break;
        }
        int c = tileEntitySmallFloodlight.getColor();
        if (c != 16) {
            float cMult = 1.21342F;
            GL11.glColor3f(RenderUtil.r[c] * cMult, RenderUtil.g[c] * cMult, RenderUtil.b[c] * cMult);
        }
        switch (metadata) {
            case 0:
                this.modelSmallFluorescent.setRotateAngle(this.modelSmallFluorescent.shape1, xRotation, yRotation, zRotation);
                this.modelSmallFluorescent.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
                break;
            case 1:
                this.modelSquareFluorescent.setRotateAngle(this.modelSquareFluorescent.shape1, xRotation, yRotation, zRotation);
                this.modelSquareFluorescent.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
                break;
        }
        //Tell it to stop rendering for both the PushMatrix's
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}

