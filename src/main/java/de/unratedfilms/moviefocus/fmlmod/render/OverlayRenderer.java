
package de.unratedfilms.moviefocus.fmlmod.render;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.moviefocus.fmlmod.conf.RenderedFocusConfigProvider;

public class OverlayRenderer {

    private static final Minecraft MC = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {

        if (!MC.gameSettings.hideGUI && RenderedFocusConfigProvider.isFocusRendered()) {
            String shortFocalDepth = String.format("%.2f", RenderedFocusConfigProvider.getRenderedFocusConfig().getFocalDepth());
            String focalDepthInfo = I18n.format("gui." + MOD_ID + ".overlay.focalDepth", shortFocalDepth);
            MC.fontRenderer.drawStringWithShadow(focalDepthInfo, 10, 10, 0xffffff);
        }
    }

}
