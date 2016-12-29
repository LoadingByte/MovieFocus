
package de.unratedfilms.moviefocus.fmlmod.util;

import static net.minecraft.client.renderer.ActiveRenderInfo.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.Vec3;

public class GeometryUtils {

    /**
     * Calculates the normal vector of the current camera plane.
     * The returned vector will always face away from the camera.
     * Note that the returned vector has not been normalized.
     *
     * @return The camera plane's normal vector.
     */
    public static Vec3 getCamSightLine() {

        /*
         * This calculates the normal vector of the camera plane.
         * What you see is a cross product between the two vectors u and v on that plane.
         * In EntityFX.renderParticle(), the four corners of a quad that always lie on the camera plane are calculated:
         *
         * x1 = (+rX + rYZ | +rXZ | +rZ + rXY)
         * x2 = (+rX - rYZ | -rXZ | +rZ - rXY)
         * x3 = (-rX - rYZ | -rXZ | -rZ - rXY)
         * x4 = (-rX + rYZ | +rXZ | -rZ + rXY)
         *
         * I chose three of those corners -- which define the camera plane -- and used them to calculate u and v:
         *
         * u = x4 - x1 = (-2 * rX | 0 | -2 * rZ)
         * v = x2 - x1 = (-2 * rYZ | -2 * rXZ | -2 * rXY)
         *
         * I then simplified the two vectors u and v, changing their length but keeping their direction:
         *
         * u' = (rX | 0 | rZ)
         * v' = (rYZ | rXZ | rXY)
         *
         * Finally, I calculated the cross product u' x v', resulting in the code you see down below.
         */
        // @formatter:off
        return Vec3.createVectorHelper(
                        0 * rotationXY  -  rotationZ * rotationXZ,
                rotationZ * rotationYZ  -  rotationX * rotationXY,
                rotationX * rotationXZ  -          0 * rotationYZ);
        // @formatter:on
    }

    /**
     * Takes a point in the world and calculates the depth of that point relative to the camera, assuming the player is looking in its general direction.
     * In case the player is looking away from the supplied point, the negative depth is returned.<br>
     * <br>
     * Internally, this method determines the distance between the near camera plane and that very point.
     * Note that the distance is returned in regular units (1 unit = 1 block) and not mapped to {@code [0; 1]}.
     *
     * @param point The distance should be calculated between this point and the camera plane.
     * @return The distance between the given point and the camera plane.
     */
    public static double getDepth(Vec3 point) {

        // This is the exact location of the camera. Note that this point must lie on the camera plain.
        Vec3 camLoc = ActiveRenderInfo.projectViewFromEntity(Minecraft.getMinecraft().renderViewEntity, 0);

        // This is the normal vector of the camera plane.
        Vec3 camSightLine = getCamSightLine().normalize();

        /*
         * I can now describe the camera plane since we have a point on that plane (camLoc) and the plane's normalized normal vector (camSightLine).
         * Using that information, I calculate the distance between the supplied point and the camera plane.
         */
        return camSightLine.dotProduct(camLoc.subtract(point)); // N * (P - C)
    }

    private GeometryUtils() {}

}
