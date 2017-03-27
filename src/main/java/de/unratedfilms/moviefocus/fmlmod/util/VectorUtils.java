
package de.unratedfilms.moviefocus.fmlmod.util;

import net.minecraft.util.math.Vec3d;

public class VectorUtils {

    public static Vec3d withX(Vec3d vec, double x) {

        return new Vec3d(x, vec.yCoord, vec.zCoord);
    }

    public static Vec3d withY(Vec3d vec, double y) {

        return new Vec3d(vec.xCoord, y, vec.zCoord);
    }

    public static Vec3d withZ(Vec3d vec, double z) {

        return new Vec3d(vec.zCoord, vec.yCoord, z);
    }

    private VectorUtils() {}

}
