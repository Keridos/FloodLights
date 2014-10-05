package de.keridos.floodlights.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by Nico on 04/10/2014.
 */
@SideOnly(Side.CLIENT)
public class CustomBlockRenderer extends TileEntitySpecialRenderer {
    private static BlockModel blockModel = new BlockModel();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
        if (tileEntity instanceof TileEntityElectricFloodlight) {
            TileEntityElectricFloodlight tileEntityElectricFloodlight = (TileEntityElectricFloodlight) tileEntity;
            ForgeDirection direction = null;

            if (tileEntity.getWorldObj() != null) {
                direction = tileEntityElectricFloodlight.getOrientation();
            }

            //Tessellator tessellator = Tessellator.instance;
            //This will make your block brightness dependent from surroundings lighting.
            //float brightness = tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord).getLightValue((IBlockAccess)tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            //int l = tileEntity.getWorldObj().getLightBrightnessForSkyBlocks(tileEntity.xCoord,tileEntity.yCoord, tileEntity.zCoord, 0);
            //int l1 = l % 65536;
            //int l2 = l / 65536;
            // tessellator.setColorOpaque_F(brightness, brightness, brightness);
            //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)l1, (float)l2);

            GL11.glPushMatrix();
            GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            Minecraft.getMinecraft().renderEngine.bindTexture(Textures.Block.ELECTRICFLOODLIGHT);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            short angle = 0;
            short angle2 = 0;
            if (direction != null) {
                if (direction == ForgeDirection.NORTH) {
                    angle = 0;
                } else if (direction == ForgeDirection.SOUTH) {
                    angle = 180;
                } else if (direction == ForgeDirection.WEST) {
                    angle = -90;
                } else if (direction == ForgeDirection.EAST) {
                    angle = 90;
                } else if (direction == ForgeDirection.DOWN) {
                    angle2 = 90;
                    GL11.glTranslatef(0.0F, 1.0F, -1.0F);
                } else if (direction == ForgeDirection.UP) {
                    angle2 = -90;
                    GL11.glTranslatef(0.0F, 1.0F, 1.0F);
                }

            }
            GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(angle2, 1.0F, 0.0F, 0.0F);
            this.blockModel.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }


    }

}
