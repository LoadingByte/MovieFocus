
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Vec3;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;

abstract class PointFocusConfigWithoutGui implements FocusConfig {

    protected Vec3 focusedPoint;

    // ----- Properties -----

    @Override
    public String getTitle() {

        return I18n.format("gui." + MOD_ID + ".focusConfigTitle.point");
    }

    public Vec3 getFocusedPoint() {

        return focusedPoint;
    }

    public void setFocusedPoint(Vec3 focusedPoint) {

        this.focusedPoint = focusedPoint;
    }

    // ----- Queries -----

    @Override
    public boolean isAvailable() {

        return focusedPoint != null;
    }

    @Override
    public float getFocalDepth() {

        return (float) GeometryUtils.getDepth(focusedPoint);
    }

}
