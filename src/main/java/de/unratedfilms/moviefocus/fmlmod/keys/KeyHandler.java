
package de.unratedfilms.moviefocus.fmlmod.keys;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
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
                // Sadly, even this state-specific key event has to be handled here since key handlers must be registered at initialization time
                ((RunningFocusFlowGuiState) currentState).onFlowKey();
            } else if (currentState == null) {
                GuiStateMachine.transitionToState(new EditFocusFlowGuiState());
            }
        }
    }

}
