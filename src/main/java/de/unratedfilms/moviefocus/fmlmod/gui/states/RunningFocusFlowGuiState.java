
package de.unratedfilms.moviefocus.fmlmod.gui.states;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;

public class RunningFocusFlowGuiState extends GuiState {

    private static final Minecraft MC           = Minecraft.getMinecraft();

    private final EventHandler     eventHandler = new EventHandler();

    @Override
    protected ImmutableList<Object> getEventHandlers() {

        return ImmutableList.of(eventHandler);
    }

    protected class EventHandler {

        @SubscribeEvent
        public void onRenderGameOverlay(RenderGameOverlayEvent event) {

            if (!MC.gameSettings.hideGUI) {
                // Draw the focal depth indicator
                GuiHelper.drawFocalDepthIndicator();
            }
        }

    }

}
