
package de.unratedfilms.moviefocus.fmlmod.util;

import static net.minecraft.client.renderer.ActiveRenderInfo.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.Vec3;

public class GeometryUtils {

    /**
     * Takes a point in the world and calculates the depth of that point relative to the camera, assuming the player is looking in its general direction.
     * In case the player is looking away from the supplied point, the negative depth is returned.<br>
     * <br>
     * Internally, this method determines the distance between the near camera plane and that very point.
     * Note that the distance is returned in regular units (1 unit = 1 block) and not mapped to {@code [0; 1]}.
     */
    public static double getDepth(Vec3 point) {

        Vec3 camLoc = ActiveRenderInfo.projectViewFromEntity(Minecraft.getMinecraft().renderViewEntity, 0);

        /*
         * This is the normal vector of the camera plane.
         * What you see is a cross product between the two vectors U and V on that plane.
         * In EntityFX.renderParticle(), the four corners of a quad that always lie on the camera plane are calculated.
         * I have chosen three of those corners -- which define the camera plane -- and used them to calculate U and V:
         *
         * U = (rX | rYZ | rZ)
         * V = (rYZ | rXZ | rXY)
         *
         * I have then calculated the cross product U x V, resulting in the code you see down below.
         */
        Vec3 camSightLine = Vec3.createVectorHelper(
                rotationXZ * rotationXY - rotationZ * rotationXZ,
                rotationZ * rotationYZ - rotationX * rotationXY,
                rotationX * rotationXZ - rotationXZ * rotationYZ).normalize();

        /*
         * I can now describe the camera plane since we have a point on that plane (camLoc) and the plane's normal vector (camSightLine).
         * Using that information, I calculate the distance between the supplied point and the camera plane.
         */
        return camSightLine.dotProduct(camLoc.subtract(point)); // N * (P - C)
    }

    private GeometryUtils() {}

}
