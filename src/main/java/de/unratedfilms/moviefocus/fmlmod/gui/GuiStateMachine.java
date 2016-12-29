
package de.unratedfilms.moviefocus.fmlmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiStateMachine {

    private static GuiState currentState;

    static {

        // Register the event handler that makes sure things stay consistent
        MinecraftForge.EVENT_BUS.register(new EventHandler());

    }

    public static GuiState getCurrentState() {

        return currentState;
    }

    public static void transitionToState(GuiState newState) {

        if (currentState != null) {
            for (Object eventHandler : currentState.getEventHandlers()) {
                FMLCommonHandler.instance().bus().unregister(eventHandler);
                MinecraftForge.EVENT_BUS.unregister(eventHandler);
            }
        }

        currentState = newState;

        if (newState != null) {
            for (Object eventHandler : newState.getEventHandlers()) {
                // We don't know what kind of events the handler wants to handle, so we just register it to both buses
                FMLCommonHandler.instance().bus().register(eventHandler);
                MinecraftForge.EVENT_BUS.register(eventHandler);
            }
        }

        if (newState != null && newState.getScreen() != null) {
            Minecraft.getMinecraft().displayGuiScreen(newState.getScreen());
        } else {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    private GuiStateMachine() {}

    protected static class EventHandler {

        @SubscribeEvent
        public void onGuiOpen(GuiOpenEvent event) {

            // If a previous GUI state screen has been replaced with a new screen without the transitionToState() method being used
            if (currentState != null && event.gui != currentState.getScreen()) {
                // Set the GUI state to null
                transitionToState(null);
            }
        }

    }

}
