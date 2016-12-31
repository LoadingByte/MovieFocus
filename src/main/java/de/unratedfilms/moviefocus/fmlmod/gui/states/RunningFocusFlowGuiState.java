
package de.unratedfilms.moviefocus.fmlmod.gui.states;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlowRunner;
import de.unratedfilms.moviefocus.fmlmod.gui.FocusingGuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiStateMachine;

public class RunningFocusFlowGuiState extends GuiState implements FocusingGuiState {

    private static final Minecraft MC           = Minecraft.getMinecraft();

    private final EventHandler     eventHandler = new EventHandler();

    @Override
    protected ImmutableList<Object> getEventHandlers() {

        return ImmutableList.of(eventHandler);
    }

    @Override
    protected void enter() {

        FocusFlowRunner.loadAndStartRunning();
    }

    @Override
    protected void exit() {

        FocusFlowRunner.stopRunning();
    }

    @Override
    public boolean isFocusRendered() {

        return FocusFlowRunner.isFocusAvailable();
    }

    @Override
    public float getFocalDepth() {

        return FocusFlowRunner.getCurrentEntry().getFocusConfig().getFocalDepth();
    }

    // Called by the KeyHandler
    public void onFlowKey() {

        FocusFlowRunner.advance();

        if (!FocusFlowRunner.isRunning()) {
            GuiStateMachine.transitionToState(null);
        }
    }

    protected class EventHandler {

        @SubscribeEvent
        public void onRenderGameOverlay(RenderGameOverlayEvent event) {

            if (!MC.gameSettings.hideGUI) {
                // Draw the focal depth indicator
                if (isFocusRendered()) {
                    GuiHelper.drawFocalDepthIndicator(getFocalDepth());
                }
            }
        }

    }

}
