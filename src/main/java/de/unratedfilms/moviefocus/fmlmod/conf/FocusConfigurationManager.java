
package de.unratedfilms.moviefocus.fmlmod.conf;

import org.apache.commons.lang3.Validate;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.FixedFocusConfiguration;

public class FocusConfigurationManager {

    private static FocusConfiguration currentConfiguration = new FixedFocusConfiguration();

    public static FocusConfiguration getCurrentConfiguration() {

        return currentConfiguration;
    }

    public static void setCurrentConfiguration(FocusConfiguration currentConfiguration) {

        Validate.notNull(currentConfiguration, "Current focus configuration cannot be null");
        FocusConfigurationManager.currentConfiguration = currentConfiguration;
    }

    private FocusConfigurationManager() {}

}
