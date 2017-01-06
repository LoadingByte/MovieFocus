
package de.unratedfilms.moviefocus.fmlmod.conf;

import cpw.mods.fml.common.eventhandler.Event;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow.FocusFlowEntry;

public class FocusFlowTransitionEvent extends Event {

    public final FocusFlowEntry from;
    public final FocusFlowEntry to;

    public FocusFlowTransitionEvent(FocusFlowEntry from, FocusFlowEntry to) {

        this.from = from;
        this.to = to;
    }

}
