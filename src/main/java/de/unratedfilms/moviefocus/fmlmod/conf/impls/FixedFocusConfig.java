
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;

@FocusConfig.InternalName ("fixed")
public class FixedFocusConfig implements FocusConfig {

    private float fixedFocalDepth;

    public FixedFocusConfig() {}

    public FixedFocusConfig(float fixedFocalDepth) {

        this.fixedFocalDepth = fixedFocalDepth;
    }

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
