
package de.unratedfilms.moviefocus.fmlmod.util;

import net.minecraft.util.Vec3;

public class VectorUtils {

    public static Vec3 add(Vec3 vec1, Vec3 vec2) {

        return Vec3.createVectorHelper(vec1.xCoord + vec2.xCoord, vec1.yCoord + vec2.yCoord, vec1.zCoord + vec2.zCoord);
    }

    public static Vec3 multiply(Vec3 vec, double scalar) {

        return Vec3.createVectorHelper(vec.xCoord * scalar, vec.yCoord * scalar, vec.zCoord * scalar);
    }

    private VectorUtils() {}

}
