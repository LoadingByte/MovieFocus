
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import net.minecraft.util.Vec3;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigAdapter;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;

@FocusConfig.InternalName ("point")
public class PointFocusConfig extends FocusConfigAdapter {

    private Vec3  focusedPoint    = Vec3.createVectorHelper(0, 0, 0);
    private float envsphereRadius = 0.5f;

    public Vec3 getFocusedPoint() {

        return focusedPoint;
    }

    public void setFocusedPoint(Vec3 focusedPoint) {

        this.focusedPoint = focusedPoint;
    }

    public float getEnvsphereRadius() {

        return envsphereRadius;
    }

    public void setEnvsphereRadius(float envsphereRadius) {

        this.envsphereRadius = envsphereRadius;
    }

    @Override
    public boolean isAvailable() {

        return true;
    }

    @Override
    public float getFocalDepth() {

        return (float) GeometryUtils.getDepth(focusedPoint) - envsphereRadius;
    }

}
