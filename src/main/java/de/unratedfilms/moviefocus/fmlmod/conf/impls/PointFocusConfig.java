
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import net.minecraft.util.math.Vec3d;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigAdapter;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;

@FocusConfig.InternalName ("point")
public class PointFocusConfig extends FocusConfigAdapter {

    private Vec3d focusedPoint    = Vec3d.ZERO;
    private float envsphereRadius = 0.5f;

    public PointFocusConfig() {}

    public PointFocusConfig(Vec3d focusedPoint, float envsphereRadius) {

        this.focusedPoint = focusedPoint;
        this.envsphereRadius = envsphereRadius;
    }

    public Vec3d getFocusedPoint() {

        return focusedPoint;
    }

    public void setFocusedPoint(Vec3d focusedPoint) {

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
