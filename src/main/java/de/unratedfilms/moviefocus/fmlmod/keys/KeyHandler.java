
package de.unratedfilms.moviefocus.fmlmod.keys;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlowRunner;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiStateMachine;
import de.unratedfilms.moviefocus.fmlmod.gui.states.EditFocusFlowGuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.states.RunningFocusFlowGuiState;

public class KeyHandler {

    // We catch keyboard events AND mouse events in case the key has been set to a mouse button
    @SubscribeEvent
    public void onKeyInput(InputEvent event) {

        if (KeyBindings.FLOW.isPressed()) {
            GuiState currentState = GuiStateMachine.getCurrentState();

            if (currentState instanceof RunningFocusFlowGuiState) {
                FocusFlowRunner.advance();
            } else if (currentState == null) {
                GuiStateMachine.transitionToState(new EditFocusFlowGuiState());
            }
        }
    }

}
