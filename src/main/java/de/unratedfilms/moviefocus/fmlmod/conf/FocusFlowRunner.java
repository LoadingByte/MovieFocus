
package de.unratedfilms.moviefocus.fmlmod.conf;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang3.Validate;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow.FocusFlowEntry;

public class FocusFlowRunner {

    private static Queue<FocusFlowEntry> remainingSequence;

    public static boolean isRunning() {

        return remainingSequence != null;
    }

    public static FocusFlowEntry getCurrentEntry() {

        Validate.validState(isRunning(), "Cannot get the current focus flow entry while the runner is not running");
        return remainingSequence.peek();
    }

    public static void loadAndStartRunning() {

        stopRunning();

        if (!FocusFlow.sequence.isEmpty()) {
            remainingSequence = new LinkedList<>(FocusFlow.sequence);
            getCurrentEntry().getFocusConfig().setActive(true);
        }
    }

    public static void advance() {

        if (!isRunning()) {
            return;
        }

        getCurrentEntry().getFocusConfig().setActive(false);
        remainingSequence.remove();

        if (remainingSequence.isEmpty()) {
            remainingSequence = null;
        } else {
            getCurrentEntry().getFocusConfig().setActive(true);
        }
    }

    public static void stopRunning() {

        if (!isRunning()) {
            return;
        }

        getCurrentEntry().getFocusConfig().setActive(false);
        remainingSequence = null;
    }

    public static boolean isFocusRendered() {

        return isRunning() && getCurrentEntry().getFocusConfig().isAvailable();
    }

    private FocusFlowRunner() {}

}
