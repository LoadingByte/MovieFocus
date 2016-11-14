
package de.unratedfilms.moviefocus.fmlmod.conf;

import org.apache.commons.lang3.Validate;
import com.google.common.collect.ImmutableList;

public class FocusConfigManager {

    private static ImmutableList<FocusConfig> configs;

    private static FocusConfig                selectedConfig;
    private static boolean                    activated = false;

    // This block initializes the manager automatically as soon as it is referenced for the first time
    static {

        configs = ImmutableList.copyOf(FocusConfigRegistry.createAll());
        Validate.validState(!configs.isEmpty(),
                "The focus config manager just tried to initialize itself, but no focus configs were registered yet; did you load the focus config manager class too early?");

        // Select the first provided configuration by default, but don't activate it
        setSelected(configs.get(0));

    }

    public static ImmutableList<FocusConfig> getAllConfigs() {

        return configs;
    }

    public static FocusConfig getSelected() {

        return selectedConfig;
    }

    public static void setSelected(FocusConfig selectedConfig) {

        Validate.notNull(selectedConfig, "Currently selected focus config cannot be null");

        if (FocusConfigManager.selectedConfig != null) {
            FocusConfigManager.selectedConfig.setStatus(false, false);
        }
        selectedConfig.setStatus(true, activated);

        FocusConfigManager.selectedConfig = selectedConfig;
    }

    public static boolean isActivated() {

        return activated;
    }

    public static void setActivated(boolean activated) {

        FocusConfigManager.activated = activated;
        selectedConfig.setStatus(true, activated);
    }

    public static boolean isRendered() {

        return activated && selectedConfig.isAvailable();
    }

}
