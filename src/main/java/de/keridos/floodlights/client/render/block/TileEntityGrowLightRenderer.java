package de.keridos.floodlights.client.render.block;

import de.keridos.floodlights.client.render.model.TileEntitySquareFluorescentLightModel;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityGrowLight;
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
public class TileEntityGrowLightRenderer extends TileEntitySpecialRenderer {

    //The model of your block
    private final TileEntitySquareFluorescentLightModel modelGrowLight;

    public TileEntityGrowLightRenderer() {
        super();
        this.modelGrowLight = new TileEntitySquareFluorescentLightModel();
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
        ResourceLocation textures;
        TileEntityGrowLight tileEntityGrowLight = (TileEntityGrowLight) te;
        //The PushMatrix tells the renderer to "start" doing something.
        GL11.glPushMatrix();

        //This is setting the initial location.
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        if (tileEntityGrowLight.getWasActive()) {
            textures = (new ResourceLocation(Textures.Block.GROW_LIGHT_TEXTURE_ON));
        } else {
            textures = (new ResourceLocation(Textures.Block.GROW_LIGHT_TEXTURE_OFF));
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(textures);

        //This rotation part is very important! Without it, your modelSmallFluorescent will render upside-down! And for some reason you DO need PushMatrix again!
        GL11.glPushMatrix();

        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        //A reference to your Model file. Again, very important.
        float xRotation = 0.0F;
        float yRotation = 0.0F;
        float zRotation = 0.0F;
        switch (tileEntityGrowLight.getOrientation()) {
            case DOWN:
                xRotation = (float) Math.PI;
                break;
            case UP:
                break;
            case WEST:
                zRotation = (float) Math.PI / 2;
                break;
            case EAST:
                zRotation = -(float) Math.PI / 2;
                break;
            case SOUTH:
                xRotation = -(float) Math.PI / 2;
                break;
            case NORTH:
                xRotation = (float) Math.PI / 2;
                break;
        }
        int c = tileEntityGrowLight.getColor();
        if (c != 16) {
            float cMult = 1.21342F;
            GL11.glColor3f(RenderUtil.r[c] * cMult, RenderUtil.g[c] * cMult, RenderUtil.b[c] * cMult);
        }
        this.modelGrowLight.setRotateAngle(this.modelGrowLight.shape1, xRotation, yRotation, zRotation);
        this.modelGrowLight.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        //Tell it to stop rendering for both the PushMatrix's
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}


