
package de.unratedfilms.moviefocus.fmlmod.conf;

import org.apache.commons.lang3.Validate;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.FixedFocusConfiguration;

public class FocusConfigurationManager {

    private static FocusConfiguration currentConfiguration = new FixedFocusConfiguration();

    public static FocusConfiguration getCurrent() {

        return currentConfiguration;
    }

    public static void setCurrent(FocusConfiguration currentConfiguration) {

        Validate.notNull(currentConfiguration, "Current focus configuration cannot be null");
        FocusConfigurationManager.currentConfiguration = currentConfiguration;
    }

    private FocusConfigurationManager() {}

}
