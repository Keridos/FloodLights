package de.keridos.floodlights.client.render.block;

import de.keridos.floodlights.item.ItemLightDebugTool;
import de.keridos.floodlights.tileentity.TileEntityUVLightBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

/**
 * Created by Keridos on 05.05.2015.
 * This Class
 */
public class TileEntityPhantomLightRenderer extends TileEntitySpecialRenderer {

    public TileEntityPhantomLightRenderer() {
        super();
    }

    public static void drawCube(AxisAlignedBB cube) {
        double xa = cube.minX;
        double xb = cube.maxX;
        double ya = cube.minY;
        double yb = cube.maxY;
        double za = cube.minZ;
        double zb = cube.maxZ;


        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINE_STRIP);
        tessellator.addVertex(xa, ya, za);
        tessellator.addVertex(xa, yb, za);
        tessellator.addVertex(xb, yb, za);
        tessellator.addVertex(xb, ya, za);
        tessellator.addVertex(xa, ya, za);

        tessellator.addVertex(xa, ya, zb);
        tessellator.addVertex(xa, yb, zb);
        tessellator.addVertex(xb, yb, zb);
        tessellator.addVertex(xb, ya, zb);
        tessellator.addVertex(xa, ya, zb);
        tessellator.draw();

        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.addVertex(xa, ya, za);
        tessellator.addVertex(xa, ya, zb);

        tessellator.addVertex(xa, yb, za);
        tessellator.addVertex(xa, yb, zb);

        tessellator.addVertex(xb, ya, za);
        tessellator.addVertex(xb, ya, zb);

        tessellator.addVertex(xb, yb, za);
        tessellator.addVertex(xb, yb, zb);
        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemLightDebugTool) || te instanceof TileEntityUVLightBlock) {
            return;
        }
        try {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glPushMatrix();
            GL11.glColor3f(1F, 1F, 0.0F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glTranslatef((float) x, (float) y, (float) z);
            AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.002D, 0.002D, 0.002D, 0.998D, 0.998D, 0.998D);
            drawCube(boundingBox);
        } finally {
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }

    }
}

