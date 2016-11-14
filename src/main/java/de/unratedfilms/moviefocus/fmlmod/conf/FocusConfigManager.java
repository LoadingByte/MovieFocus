
package de.unratedfilms.moviefocus.fmlmod.conf;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.Validate;
import com.google.common.collect.ImmutableList;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.EntityFocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.FixedFocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.PointFocusConfig;

public class FocusConfigManager {

    public static final FocusConfigManager   INSTANCE  = new FocusConfigManager(Arrays.asList(
            new FixedFocusConfig(), new PointFocusConfig(), new EntityFocusConfig()));

    private final ImmutableList<FocusConfig> configurations;

    private FocusConfig                      selectedConfiguration;
    private boolean                          activated = false;

    public FocusConfigManager(List<FocusConfig> configurations) {

        Validate.notEmpty(configurations, "You cannot create a focus configuration manager without any focus configurations");

        this.configurations = ImmutableList.copyOf(configurations);

        // Select the first provided configuration by default, but don't activate it
        setSelected(configurations.get(0));
    }

    public ImmutableList<FocusConfig> getAll() {

        return configurations;
    }

    public FocusConfig getSelected() {

        return selectedConfiguration;
    }

    public void setSelected(FocusConfig selectedConfiguration) {

        Validate.notNull(selectedConfiguration, "Currently selected focus configuration cannot be null");

        if (this.selectedConfiguration != null) {
            this.selectedConfiguration.setStatus(false, false);
        }
        selectedConfiguration.setStatus(true, activated);

        this.selectedConfiguration = selectedConfiguration;
    }

    public boolean isActivated() {

        return activated;
    }

    public void setActivated(boolean activated) {

        this.activated = activated;
        selectedConfiguration.setStatus(true, activated);
    }

    public boolean isRendered() {

        return activated && selectedConfiguration.isAvailable();
    }

}
