
package de.unratedfilms.moviefocus.fmlmod.gui.states.editflowentry;

import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow.FocusFlowEntry;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.EntityFocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.FixedFocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.PointFocusConfig;

class FocusConfigGuiImplsFactory {

    public static ContainerClippingImpl createSettingsContainer(FocusFlowEntry flowEntry) {

        FocusConfig config = flowEntry.getFocusConfig();

        if (config instanceof FixedFocusConfig) {
            return new FixedFocusConfigGuiImpls(flowEntry).new SettingsContainer();
        } else if (config instanceof PointFocusConfig) {
            return new PointFocusConfigGuiImpls(flowEntry).new SettingsContainer();
        } else if (config instanceof EntityFocusConfig) {
            return new EntityFocusConfigGuiImpls(flowEntry).new SettingsContainer();
        } else {
            throw new IllegalArgumentException("Unknown focus config type: '" + config.getClass() + "'");
        }
    }

    public static Object createEventHandler(FocusFlowEntry flowEntry) {

        FocusConfig config = flowEntry.getFocusConfig();

        if (config instanceof FixedFocusConfig) {
            return new FixedFocusConfigGuiImpls(flowEntry).new EventHandler();
        } else if (config instanceof PointFocusConfig) {
            return new PointFocusConfigGuiImpls(flowEntry).new EventHandler();
        } else if (config instanceof EntityFocusConfig) {
            return new EntityFocusConfigGuiImpls(flowEntry).new EventHandler();
        } else {
            throw new IllegalArgumentException("Unknown focus config type: '" + config.getClass() + "'");
        }
    }

    private FocusConfigGuiImplsFactory() {}

}
