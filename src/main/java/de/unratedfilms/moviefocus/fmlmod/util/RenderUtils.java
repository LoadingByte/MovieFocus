
package de.unratedfilms.moviefocus.fmlmod.util;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;

public class RenderUtils {

    public static void drawAABB(RenderGlobal r, AxisAlignedBB aabb, RenderSetting[] settings) {

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int originalDepthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
        Tessellator tes = Tessellator.instance;

        for (RenderSetting setting : settings) {
            GL11.glColor4f(setting.r, setting.g, setting.b, setting.a);
            GL11.glDepthFunc(setting.depthFunc);

            tes.startDrawing(3);
            tes.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            tes.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            tes.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            tes.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            tes.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            tes.draw();
            tes.startDrawing(3);
            tes.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            tes.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            tes.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            tes.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            tes.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            tes.draw();
            tes.startDrawing(1);
            tes.addVertex(aabb.minX, aabb.minY, aabb.minZ);
            tes.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
            tes.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
            tes.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
            tes.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
            tes.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
            tes.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
            tes.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
            tes.draw();
        }

        GL11.glDepthFunc(originalDepthFunc);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private RenderUtils() {}

    public static class RenderSetting {

        public final float r, g, b, a;
        public final int   depthFunc;

        public RenderSetting(float r, float g, float b, float a, int depthFunc) {

            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.depthFunc = depthFunc;
        }

    }

}
