package de.keridos.floodlights.client.render.block;

import de.keridos.floodlights.item.ItemLightDebugTool;
import de.keridos.floodlights.tileentity.TileEntityPhantomUVLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
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

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = Tessellator.getInstance().getBuffer();
        worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(xa, ya, za).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xa, yb, za).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xb, yb, za).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xb, ya, za).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xa, ya, za).color(1F, 1F, 0.0F,1.0F).endVertex();

        worldRenderer.pos(xa, ya, zb).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xa, yb, zb).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xb, yb, zb).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xb, ya, zb).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xa, ya, zb).color(1F, 1F, 0.0F,1.0F).endVertex();
        tessellator.draw();

        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(xa, ya, za).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xa, ya, zb).color(1F, 1F, 0.0F,1.0F).endVertex();

        worldRenderer.pos(xa, yb, za).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xa, yb, zb).color(1F, 1F, 0.0F,1.0F).endVertex();

        worldRenderer.pos(xb, ya, za).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xb, ya, zb).color(1F, 1F, 0.0F,1.0F).endVertex();

        worldRenderer.pos(xb, yb, za).color(1F, 1F, 0.0F,1.0F).endVertex();
        worldRenderer.pos(xb, yb, zb).color(1F, 1F, 0.0F,1.0F).endVertex();
        tessellator.draw();
    }


    @Override
    public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player.getHeldItem(EnumHand.MAIN_HAND) == null || !(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemLightDebugTool) || te instanceof TileEntityPhantomUVLight) {
            return;
        }
        try {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glPushMatrix();
            GL11.glColor3f(1F, 1F, 0.0F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glTranslatef((float) x, (float) y, (float) z);
            AxisAlignedBB boundingBox = new AxisAlignedBB(0.002D, 0.002D, 0.002D, 0.998D, 0.998D, 0.998D);
            drawCube(boundingBox);
        } finally {
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }

    }
}

