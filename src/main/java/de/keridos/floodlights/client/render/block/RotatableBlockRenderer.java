package de.keridos.floodlights.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.RenderIDs;
import de.keridos.floodlights.tileentity.TileEntityFL;
import de.keridos.floodlights.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by Keridos on 04.10.14.
 * This Class renders the rotatable blocks from this mod.
 */
@SideOnly(Side.CLIENT)
public class RotatableBlockRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        GL11.glPushMatrix();
        Tessellator t = Tessellator.instance;
        GL11.glTranslated(-0.5, -0.5, -0.5);
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        renderer.renderFaceXNeg(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.WEST.ordinal(), 1));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        renderer.renderFaceXPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.EAST.ordinal(), 1));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(0, 0, -1);
        renderer.renderFaceZNeg(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.NORTH.ordinal(), 1));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(0, 0, 1);
        renderer.renderFaceZPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.SOUTH.ordinal(), 1));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        renderer.renderFaceYNeg(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.DOWN.ordinal(), 1));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        renderer.renderFaceYPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, ForgeDirection.UP.ordinal(), 1));
        t.draw();
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileEntityFL l = (TileEntityFL) world.getTileEntity(x, y, z);
        int i1 = l.getOrientation().ordinal();
        int c = l.getColor();
        switch (i1) {
            case 0:
                renderer.uvRotateEast = 3;
                renderer.uvRotateWest = 3;
                renderer.uvRotateSouth = 3;
                renderer.uvRotateNorth = 3;
                break;
            case 1:
                break;
            case 2:
                renderer.uvRotateSouth = 1;
                renderer.uvRotateNorth = 2;
                break;
            case 3:
                renderer.uvRotateSouth = 2;
                renderer.uvRotateNorth = 1;
                renderer.uvRotateTop = 3;
                renderer.uvRotateBottom = 3;
                break;
            case 4:
                renderer.uvRotateEast = 1;
                renderer.uvRotateWest = 2;
                renderer.uvRotateTop = 2;
                renderer.uvRotateBottom = 1;
                break;
            case 5:
                renderer.uvRotateEast = 2;
                renderer.uvRotateWest = 1;
                renderer.uvRotateTop = 1;
                renderer.uvRotateBottom = 2;
        }
        if (c == 16) {
            renderer.renderStandardBlock(block, x, y, z);
        } else {
            float cMult = 1.0F;
            if (block == ModBlocks.blockCarbonLight) {
                cMult = 1.41531F; // 1.0F + (float) Math.log10(255 / 98);
            } else if (block == ModBlocks.blockElectricLight) {
                cMult = 1.2512F; // 1.0F + (float) Math.log10(255 / 143);
            } else if (block == ModBlocks.blockUVLight) {
                cMult = 1.273F; // 1.0F + (float) Math.log10(255 / 137);
            }
            renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, RenderUtil.r[c] * cMult, RenderUtil.g[c] * cMult, RenderUtil.b[c] * cMult);
        }
        renderer.uvRotateEast = 0;
        renderer.uvRotateWest = 0;
        renderer.uvRotateSouth = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int i) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RenderIDs.ROTATABLE_BLOCK;
    }
}

