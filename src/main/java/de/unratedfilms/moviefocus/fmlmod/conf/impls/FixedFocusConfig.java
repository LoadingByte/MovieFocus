
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigAdapter;

@FocusConfig.InternalName ("fixed")
public class FixedFocusConfig extends FocusConfigAdapter {

    private float fixedFocalDepth;

    @Override
    public boolean isAvailable() {

        return true;
    }

    @Override
    public float getFocalDepth() {

        return fixedFocalDepth;
    }

    public void setFocalDepth(float fixedFocalDepth) {

        this.fixedFocalDepth = fixedFocalDepth;
    }

}
