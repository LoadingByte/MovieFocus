
package de.unratedfilms.moviefocus.fmlmod.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class RenderUtils {

    private static final Minecraft       MC                      = Minecraft.getMinecraft();

    private static final Sphere          SPHERE                  = new Sphere();

    private static final RenderSetting[] GIZMO_X_RENDER_SETTINGS = { new RenderSetting(1f, 0f, 0f, 1f, GL11.GL_ALWAYS) };
    private static final RenderSetting[] GIZMO_Y_RENDER_SETTINGS = { new RenderSetting(0f, 1f, 0f, 1f, GL11.GL_ALWAYS) };
    private static final RenderSetting[] GIZMO_Z_RENDER_SETTINGS = { new RenderSetting(0f, 0f, 1f, 1f, GL11.GL_ALWAYS) };

    public static void drawLine(Vec3 pos1, Vec3 pos2, RenderSetting[] settings) {

        drawLine(pos1.xCoord, pos1.yCoord, pos1.zCoord, pos2.xCoord, pos2.yCoord, pos2.zCoord, settings);
    }

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, RenderSetting[] settings) {

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int originalDepthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
        Tessellator tes = Tessellator.instance;

        for (RenderSetting setting : settings) {
            GL11.glColor4f(setting.r, setting.g, setting.b, setting.a);
            GL11.glDepthFunc(setting.depthFunc);

            tes.startDrawing(3);
            tes.addVertex(x1, y1, z1);
            tes.addVertex(x2, y2, z2);
            tes.draw();
        }

        GL11.glDepthFunc(originalDepthFunc);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawAABB(AxisAlignedBB aabb, RenderSetting[] settings) {

        drawAABB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, settings);
    }

    public static void drawAABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, RenderSetting[] settings) {

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int originalDepthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
        Tessellator tes = Tessellator.instance;

        for (RenderSetting setting : settings) {
            GL11.glColor4f(setting.r, setting.g, setting.b, setting.a);
            GL11.glDepthFunc(setting.depthFunc);

            tes.startDrawing(3);
            tes.addVertex(minX, minY, minZ);
            tes.addVertex(maxX, minY, minZ);
            tes.addVertex(maxX, minY, maxZ);
            tes.addVertex(minX, minY, maxZ);
            tes.addVertex(minX, minY, minZ);
            tes.draw();
            tes.startDrawing(3);
            tes.addVertex(minX, maxY, minZ);
            tes.addVertex(maxX, maxY, minZ);
            tes.addVertex(maxX, maxY, maxZ);
            tes.addVertex(minX, maxY, maxZ);
            tes.addVertex(minX, maxY, minZ);
            tes.draw();
            tes.startDrawing(1);
            tes.addVertex(minX, minY, minZ);
            tes.addVertex(minX, maxY, minZ);
            tes.addVertex(maxX, minY, minZ);
            tes.addVertex(maxX, maxY, minZ);
            tes.addVertex(maxX, minY, maxZ);
            tes.addVertex(maxX, maxY, maxZ);
            tes.addVertex(minX, minY, maxZ);
            tes.addVertex(minX, maxY, maxZ);
            tes.draw();
        }

        GL11.glDepthFunc(originalDepthFunc);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawSphere(Vec3 center, float radius, int resolution, boolean wireframe, RenderSetting[] settings) {

        drawSphere(center.xCoord, center.yCoord, center.zCoord, radius, resolution, wireframe, settings);
    }

    public static void drawSphere(double x, double y, double z, float radius, int resolution, boolean wireframe, RenderSetting[] settings) {

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int originalDepthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);

        // Translate to the sphere's center point, and then actually draw the sphere
        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);

            for (RenderSetting setting : settings) {
                GL11.glColor4f(setting.r, setting.g, setting.b, setting.a);
                GL11.glDepthFunc(setting.depthFunc);

                SPHERE.setDrawStyle(wireframe ? GLU.GLU_LINE : GLU.GLU_FILL);
                SPHERE.draw(radius, resolution, resolution);
            }
        }
        GL11.glPopMatrix();

        GL11.glDepthFunc(originalDepthFunc);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawGizmo(float lineLength, float lineWidth, float partialTicks) {

        ScaledResolution scaledResolution = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);

        GL11.glPushMatrix();
        {
            // This code is shamelessly stolen from the latest Minecraft version
            GL11.glTranslatef(scaledResolution.getScaledWidth() * 0.5f, scaledResolution.getScaledHeight() * 0.5f, 0);
            Entity entity = MC.renderViewEntity;
            GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1, 0, 0);
            GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0, 1, 0);
            GL11.glScalef(-1, -1, -1);

            GL11.glLineWidth(lineWidth);
            RenderUtils.drawLine(0, 0, 0, lineLength, 0, 0, GIZMO_X_RENDER_SETTINGS);
            RenderUtils.drawLine(0, 0, 0, 0, lineLength, 0, GIZMO_Y_RENDER_SETTINGS);
            RenderUtils.drawLine(0, 0, 0, 0, 0, lineLength, GIZMO_Z_RENDER_SETTINGS);
            GL11.glLineWidth(1);
        }
        GL11.glPopMatrix();
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
