
package de.unratedfilms.moviefocus.fmlmod.gui.states;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.moviefocus.fmlmod.events.FocalDepthRequestEvent;
import de.unratedfilms.moviefocus.fmlmod.flow.FocusFlow.FocusFlowEntry;
import de.unratedfilms.moviefocus.fmlmod.flow.FocusFlowRunner;
import de.unratedfilms.moviefocus.fmlmod.flow.FocusFlowSmoother;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiStateMachine;
import de.unratedfilms.moviefocus.fmlmod.keys.KeyBindings;

public class RunningFocusFlowGuiState extends GuiState {

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

    // Called by the KeyHandler
    public void onFlowKey() {

        FocusFlowRunner.advance();

        if (!FocusFlowRunner.isRunning()) {
            GuiStateMachine.transitionToState(null);
        }
    }

    protected class EventHandler {

        @SubscribeEvent
        public void onFocalDepthRequest(FocalDepthRequestEvent event) {

            if (FocusFlowRunner.getCurrentEntry().getFocusConfig().isAvailable()) {
                event.supplyFocalDepth(FocusFlowSmoother.getSmoothedFocalDepth());
            }
        }

        @SubscribeEvent
        public void onRenderGameOverlay(RenderGameOverlayEvent event) {

            if (!MC.gameSettings.hideGUI) {
                FocusFlowEntry currEntry = FocusFlowRunner.getCurrentEntry();

                // Draw the focal depth indicator
                GuiHelper.drawFocalDepthIndicator(currEntry.getFocusConfig().isAvailable(), FocusFlowSmoother::getSmoothedFocalDepth);

                // Draw the current flow entry name and title
                String name = I18n.format("gui." + MOD_ID + ".general.focusConfigName." + currEntry.getFocusConfig().getInternalName());
                MC.fontRendererObj.drawStringWithShadow(I18n.format("gui.moviefocus.runningFocusFlow.configName", name), 10, 35, 0xffffff);

                if (!currEntry.getTitle().isEmpty()) {
                    MC.fontRendererObj.drawStringWithShadow(I18n.format("gui.moviefocus.runningFocusFlow.entryTitle", currEntry.getTitle()), 10, 50, 0xffffff);
                }

                // Draw hints that tell the user how to run the flow
                String flowKey = GameSettings.getKeyDisplayString(KeyBindings.FLOW.getKeyCode());
                MC.fontRendererObj.drawStringWithShadow(I18n.format("gui.moviefocus.runningFocusFlow.flowKeyHint", flowKey), 10, 75, 0xffffff);
                MC.fontRendererObj.drawStringWithShadow(I18n.format("gui.moviefocus.runningFocusFlow.hideKeyHint", "F1"), 10, 90, 0xffffff);
            }
        }

    }

}
