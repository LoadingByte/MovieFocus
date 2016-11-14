
package de.unratedfilms.moviefocus.fmlmod.conf;

import de.unratedfilms.guilib.widgets.model.ContainerFlexible;

public interface FocusConfig {

    // ----- Properties -----

    public String getTitle();

    /**
     * Changes the status of the focus configuration.
     *
     * @param selected Whether this configuration has been selected from the configuration dropdown and can therefore be viewed as the current configuration.
     * @param activated Whether this configuration has been selected <b>and</b> then explicitly activated. Without this activation, no DoF is rendered.
     */
    public void setStatus(boolean selected, boolean activated);

    // ----- Queries -----

    /**
     * Returns whether {@link #getFocalDepth()} provides a sensible focal depth value that should be rendered.
     * If this is {@code false}, no DoF effect will be rendered at all.
     * For example, that might be the case if an entity the focus depth calculation has been bound to has died.
     *
     * @return Whether a DoF effect should be rendered with the return value from {@link #getFocalDepth()}.
     */
    public boolean isAvailable();

    /**
     * Returns the current <b>linear</b> depth value which should be in focus.
     * "Linear" means that the distance is returned in regular units (1 unit = 1 block) and not mapped to {@code [0; 1]}.
     *
     * @return The <b>linear</b> focused depth.
     */
    public float getFocalDepth();

    // ----- GUI -----

    public ContainerFlexible createSettingsContainer();

}
