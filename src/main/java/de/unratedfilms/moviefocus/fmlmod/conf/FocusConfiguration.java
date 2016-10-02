
package de.unratedfilms.moviefocus.fmlmod.conf;

public interface FocusConfiguration {

    public boolean isActive();

    public void toggleActivity();

    /**
     * Returns the current <b>linear</b> depth value which should be in focus.
     * "Linear" means that the distance is returned in regular units (1 unit = 1 block) and not mapped to {@code [0; 1]}.
     *
     * @return The <b>linear</b> focused depth.
     */
    public float getFocalDepth();

}
