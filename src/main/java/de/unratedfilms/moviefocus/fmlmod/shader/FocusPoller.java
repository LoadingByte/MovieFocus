
package de.unratedfilms.moviefocus.fmlmod.shader;

import org.apache.commons.lang3.Validate;
import de.unratedfilms.moviefocus.fmlmod.gui.FocusingGuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiStateMachine;

/**
 * This class collects information from the current GUI state and decides whether the depth of field effect should be rendered and if so,
 * which focal depth should be applied.
 */
class FocusPoller {

    public static boolean pollIsFocusRendered() {

        GuiState guiState = GuiStateMachine.getCurrentState();

        if (guiState instanceof FocusingGuiState) {
            return ((FocusingGuiState) guiState).isFocusRendered();
        } else {
            return false;
        }
    }

    public static float pollFocalDepth() {

        Validate.validState(pollIsFocusRendered(), "Cannot poll focal depth if the focus should not be rendered");

        GuiState guiState = GuiStateMachine.getCurrentState();

        if (guiState instanceof FocusingGuiState) {
            return ((FocusingGuiState) guiState).getFocalDepth();
        } else {
            // This code should never run
            throw new IllegalStateException("I don't know how we've got here");
        }
    }

    private FocusPoller() {}

}
