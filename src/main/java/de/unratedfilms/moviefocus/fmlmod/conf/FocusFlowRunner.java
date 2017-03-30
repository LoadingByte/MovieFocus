
package de.unratedfilms.moviefocus.fmlmod.conf;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang3.Validate;
import net.minecraftforge.common.MinecraftForge;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow.FocusFlowEntry;

public class FocusFlowRunner {

    private static Queue<FocusFlowEntry> remainingSequence = new LinkedList<>();

    public static boolean isRunning() {

        return !remainingSequence.isEmpty();
    }

    public static FocusFlowEntry getCurrentEntry() {

        Validate.validState(isRunning(), "Cannot get the current focus flow entry while the runner is not running");
        return remainingSequence.peek();
    }

    public static void loadAndStartRunning() {

        stopRunning();

        if (!FocusFlow.sequence.isEmpty()) {
            remainingSequence.addAll(FocusFlow.sequence);
            MinecraftForge.EVENT_BUS.post(new FocusFlowTransitionEvent(null, getCurrentEntry()));
        }
    }

    public static void advance() {

        if (!isRunning()) {
            return;
        }

        // Save the old entry for later
        FocusFlowEntry fromEntry = getCurrentEntry();

        // Remove the old entry from the queue, so that the next entry shows up
        remainingSequence.poll();

        // Inform everyone about the transition
        MinecraftForge.EVENT_BUS.post(new FocusFlowTransitionEvent(fromEntry, isRunning() ? getCurrentEntry() : null));
    }

    public static void stopRunning() {

        if (isRunning()) {
            remainingSequence.clear();
        }
    }

    public static boolean isFocusAvailable() {

        return isRunning() && getCurrentEntry().getFocusConfig().isAvailable();
    }

    private FocusFlowRunner() {}

}
