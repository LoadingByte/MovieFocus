
package de.unratedfilms.moviefocus.fmlmod.conf;

import org.apache.commons.lang3.Validate;

public class RenderedFocusConfigProvider {

    private static FocusConfig renderedFocusConfigOverride;

    public static boolean isFocusRendered() {

        return renderedFocusConfigOverride != null && renderedFocusConfigOverride.isAvailable() || FocusFlowRunner.isFocusRendered();
    }

    public static FocusConfig getRenderedFocusConfig() {

        Validate.validState(isFocusRendered(), "Canno retrieve the currently rendered focus config if no such config exists");

        if (renderedFocusConfigOverride != null) {
            return renderedFocusConfigOverride;
        } else {
            return FocusFlowRunner.getCurrentEntry().getFocusConfig();
        }
    }

    public static void setRenderedFocusConfigOverride(FocusConfig renderedFocusConfigOverride) {

        if (RenderedFocusConfigProvider.renderedFocusConfigOverride != null) {
            RenderedFocusConfigProvider.renderedFocusConfigOverride.setActive(false);
        }

        RenderedFocusConfigProvider.renderedFocusConfigOverride = renderedFocusConfigOverride;

        if (renderedFocusConfigOverride != null) {
            renderedFocusConfigOverride.setActive(true);
        }
    }

}
