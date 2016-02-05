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

    public static void drawCube(AxisAlignedBB cube) {
        double xa = cube.minX;
        double xb = cube.maxX;
        double ya = cube.minY;
        double yb = cube.maxY;
        double za = cube.minZ;
        double zb = cube.maxZ;


        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        tessellator.addVertex(xa, ya, za);
        tessellator.addVertex(xa, yb, za);
        tessellator.addVertex(xb, yb, za);
        tessellator.addVertex(xb, ya, za);
        tessellator.draw();

        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        tessellator.addVertex(xa, ya, zb);
        tessellator.addVertex(xa, yb, zb);
        tessellator.addVertex(xb, yb, zb);
        tessellator.addVertex(xb, ya, zb);
        tessellator.draw();

        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.addVertex(xa, ya, za);
        tessellator.addVertex(xa, ya, zb);
        tessellator.draw();

        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.addVertex(xa, yb, za);
        tessellator.addVertex(xa, yb, zb);
        tessellator.draw();

        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.addVertex(xb, ya, za);
        tessellator.addVertex(xb, ya, zb);
        tessellator.draw();

        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.addVertex(xb, yb, za);
        tessellator.addVertex(xb, yb, zb);
        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemLightDebugTool)) {
            return;
        }
        try {
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            if (te instanceof TileEntityUVLightBlock) {
                GL11.glColor3f(1F, 0.0F, 1F);
            } else {
                GL11.glColor3f(1F, 1F, 0.0F);
            }
            GL11.glLineWidth(1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            GL11.glTranslatef((float) x, (float) y, (float) z);
            AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.000D, 0.000D, 0.000D, 1D, 1D, 1D);
            drawCube(boundingBox);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        } finally {
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }

    }
}

