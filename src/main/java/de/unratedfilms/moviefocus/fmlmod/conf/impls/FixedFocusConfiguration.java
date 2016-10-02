
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfiguration;

public class FixedFocusConfiguration implements FocusConfiguration {

    private boolean enabled;
    private float   fixedFocalDepth;

    @Override
    public boolean isActive() {

        return enabled;
    }

    @Override
    public void toggleActivity() {

        enabled = !enabled;
    }

    @Override
    public float getFocalDepth() {

        return fixedFocalDepth;
    }

    public void setFocalDepth(float fixedFocalDepth) {

        // The focal depth mustn't be below 0
        if (fixedFocalDepth < 0) {
            fixedFocalDepth = 0;
        }

        this.fixedFocalDepth = fixedFocalDepth;
    }

}
