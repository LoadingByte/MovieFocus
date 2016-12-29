
package de.unratedfilms.moviefocus.fmlmod.poller;

import org.apache.commons.lang3.Validate;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlowRunner;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiStateMachine;
import de.unratedfilms.moviefocus.fmlmod.gui.states.RunningFocusFlowGuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.states.editflowentry.EditFocusFlowEntryGuiState;

/**
 * This class collects information from various sources (GuiStates etc.) and decides whether the depth of field effect should be rendered and if so,
 * which focal depth should be applied.
 */
public class FocusPoller {

    public static boolean pollIsFocusRendered() {

        GuiState guiState = GuiStateMachine.getCurrentState();

        if (guiState instanceof RunningFocusFlowGuiState) {
            return FocusFlowRunner.isFocusRendered();
        } else if (guiState instanceof EditFocusFlowEntryGuiState) {
            EditFocusFlowEntryGuiState eGuiState = (EditFocusFlowEntryGuiState) guiState;
            return eGuiState.isTryoutEnabled() && eGuiState.getEditedFlowEntry().getFocusConfig().isAvailable();
        } else {
            return false;
        }
    }

    public static float pollFocus() {

        Validate.validState(pollIsFocusRendered(), "Cannot poll focus if the focus should not be rendered");

        GuiState guiState = GuiStateMachine.getCurrentState();

        if (guiState instanceof RunningFocusFlowGuiState) {
            return FocusFlowRunner.getCurrentEntry().getFocusConfig().getFocalDepth();
        } else if (guiState instanceof EditFocusFlowEntryGuiState) {
            return ((EditFocusFlowEntryGuiState) guiState).getEditedFlowEntry().getFocusConfig().getFocalDepth();
        } else {
            // This code should never run
            throw new IllegalStateException("I don't know how we've got here");
        }
    }

    private FocusPoller() {}

}
