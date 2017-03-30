
package de.unratedfilms.moviefocus.fmlmod.flow;

import net.minecraftforge.fml.common.eventhandler.Event;
import de.unratedfilms.moviefocus.fmlmod.flow.FocusFlow.FocusFlowEntry;

public class FocusFlowTransitionEvent extends Event {

    public final FocusFlowEntry from;
    public final FocusFlowEntry to;

    public FocusFlowTransitionEvent(FocusFlowEntry from, FocusFlowEntry to) {

        this.from = from;
        this.to = to;
    }

}
