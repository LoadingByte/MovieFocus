
package de.unratedfilms.moviefocus.fmlmod.util;

import java.util.function.Consumer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class RenderUtils {

    private static final Sphere SPHERE = new Sphere();

    public static void drawWithRenderSettings(Consumer<RenderSetting> drawer, RenderSetting... settings) {

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

        float originalLineWidth = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
        int originalDepthFunc = GlStateManager.glGetInteger(GL11.GL_DEPTH_FUNC);

        for (RenderSetting setting : settings) {
            GlStateManager.color(setting.r, setting.g, setting.b, setting.a);
            GlStateManager.glLineWidth(setting.lineWidth);
            GlStateManager.depthFunc(setting.depthFunc);

            drawer.accept(setting);
        }

        GlStateManager.glLineWidth(originalLineWidth);
        GlStateManager.depthFunc(originalDepthFunc);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void drawLine(Vec3d pos1, Vec3d pos2, RenderSetting... settings) {

        drawLine(pos1.xCoord, pos1.yCoord, pos1.zCoord, pos2.xCoord, pos2.yCoord, pos2.zCoord, settings);
    }

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, RenderSetting... settings) {

        drawWithRenderSettings(setting -> {
            Tessellator tes = Tessellator.getInstance();
            VertexBuffer vb = tes.getBuffer();

            vb.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            vb.pos(x1, y1, z1).endVertex();
            vb.pos(x2, y2, z2).endVertex();
            tes.draw();
        }, settings);
    }

    public static void drawAABB(AxisAlignedBB aabb, RenderSetting... settings) {

        drawAABB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, settings);
    }

    public static void drawAABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, RenderSetting... settings) {

        drawWithRenderSettings(setting -> {
            RenderGlobal.drawBoundingBox(minX, minY, minZ, maxX, maxY, maxZ, setting.r, setting.g, setting.b, setting.a);
        }, settings);
    }

    public static void drawAABB2D(double minX, double minY, double maxX, double maxY, RenderSetting... settings) {

        drawWithRenderSettings(setting -> {
            Tessellator tes = Tessellator.getInstance();
            VertexBuffer vb = tes.getBuffer();

            vb.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
            vb.pos(minX, minY, 0).endVertex();
            vb.pos(maxX, minY, 0).endVertex();
            vb.pos(maxX, maxY, 0).endVertex();
            vb.pos(minX, maxY, 0).endVertex();
            vb.pos(minX, minY, 0).endVertex();
            tes.draw();
        }, settings);
    }

    public static void drawSphere(Vec3d center, float radius, int resolution, boolean wireframe, RenderSetting... settings) {

        drawSphere(center.xCoord, center.yCoord, center.zCoord, radius, resolution, wireframe, settings);
    }

    public static void drawSphere(double x, double y, double z, float radius, int resolution, boolean wireframe, RenderSetting... settings) {

        // Translate to the sphere's center point, and then actually draw the sphere
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);

            drawWithRenderSettings(setting -> {
                SPHERE.setDrawStyle(wireframe ? GLU.GLU_LINE : GLU.GLU_FILL);
                SPHERE.draw(radius, resolution, resolution);
            }, settings);
        }
        GlStateManager.popMatrix();
    }

    private RenderUtils() {}

    public static class RenderSetting {

        public final float r, g, b, a;
        public float       lineWidth = 1;
        public int         depthFunc = GL11.GL_ALWAYS;

        public RenderSetting(double r, double g, double b, double a) {

            this.r = (float) r;
            this.g = (float) g;
            this.b = (float) b;
            this.a = (float) a;
        }

        public RenderSetting lineWidth(float lineWidth) {

            this.lineWidth = lineWidth;
            return this;
        }

        public RenderSetting depthFunc(int depthFunc) {

            this.depthFunc = depthFunc;
            return this;
        }

    }

}
