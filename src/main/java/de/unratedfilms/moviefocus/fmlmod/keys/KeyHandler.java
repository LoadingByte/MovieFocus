
package de.unratedfilms.moviefocus.fmlmod.keys;

import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import de.unratedfilms.moviefocus.fmlmod.shader.ShaderConfiguration;

public class KeyHandler {

    // We catch keyboard events AND mouse events in case the key has been set to a mouse button
    @SubscribeEvent
    public void onKeyInput(InputEvent event) {

        if (KeyBindings.toggle.isPressed()) {
            ShaderConfiguration.setEnabled(!ShaderConfiguration.isEnabled());
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {

        if (ShaderConfiguration.isEnabled()) {
            int rawMouseScroll = Mouse.getDWheel();
            if (rawMouseScroll != 0) {
                int mouseScroll = rawMouseScroll > 0 ? 1 : -1;

                float focalDepthChange = mouseScroll * 0.1f;
                ShaderConfiguration.addFocalDepth(focalDepthChange);
                Minecraft.getMinecraft().thePlayer.sendChatMessage("New focal depth: " + ShaderConfiguration.getFocalDepth());
            }
        }
    }

}
