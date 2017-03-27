
package de.unratedfilms.moviefocus.fmlmod.conf;

import net.minecraftforge.fml.common.eventhandler.Event;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow.FocusFlowEntry;

public class FocusFlowTransitionEvent extends Event {

    public final FocusFlowEntry from;
    public final FocusFlowEntry to;

    public FocusFlowTransitionEvent(FocusFlowEntry from, FocusFlowEntry to) {

        this.from = from;
        this.to = to;
    }

}
