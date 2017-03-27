
package de.unratedfilms.moviefocus.fmlmod.conf;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FocusFlowSmoother {

    private static final int SMOOTH_TRANSITION_DURATION = 1000; // ms

    private static long      lastSwitchTimestamp        = 0;
    private static float     previousFocalDepth;

    static {

        // Register the event handler that makes sure things stay consistent
        MinecraftForge.EVENT_BUS.register(new EventHandler());

    }

    public static float getSmoothedFocalDepth() {

        long millisSinceLastSwitch = System.currentTimeMillis() - lastSwitchTimestamp;
        float currentFocalDepth = FocusFlowRunner.getCurrentEntry().getFocusConfig().getFocalDepth();

        if (millisSinceLastSwitch > SMOOTH_TRANSITION_DURATION) {
            return currentFocalDepth;
        } else {
            float newEntryInfluence = smooth((float) millisSinceLastSwitch / SMOOTH_TRANSITION_DURATION);
            return previousFocalDepth * (1 - newEntryInfluence) + currentFocalDepth * newEntryInfluence;
        }
    }

    /**
     * Smoothness function.
     *
     * @param x Value from 0 to 1 that describes how far the smooth transition has progressed.
     * @return Value from 0 to 1 that describes how much of the new focal depth should determine the current smoothed focal depth.
     */
    private static float smooth(float x) {

        return (float) ( (Math.tanh( (x - 0.5) * 1.3 * Math.PI) + 1) / 2);
    }

    private FocusFlowSmoother() {}

    protected static class EventHandler {

        @SubscribeEvent
        public void onFocusFlowTransition(FocusFlowTransitionEvent event) {

            if (event.from != null && event.from.getFocusConfig().isAvailable()) {
                previousFocalDepth = event.from.getFocusConfig().getFocalDepth();
                lastSwitchTimestamp = System.currentTimeMillis();
            } else {
                // This means that there will be no smooth transition at all, since there's no focal depth to transition from
                previousFocalDepth = 0;
            }
        }

    }

}
