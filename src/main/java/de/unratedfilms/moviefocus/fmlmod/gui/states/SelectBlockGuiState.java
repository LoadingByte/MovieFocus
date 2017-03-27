
package de.unratedfilms.moviefocus.fmlmod.gui.states;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.function.Consumer;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils.RenderSetting;

public class SelectBlockGuiState extends GuiState {

    private static final Minecraft     MC                    = Minecraft.getMinecraft();
    private static final RenderSetting BORDER_RENDER_SETTING = new RenderSetting(0, 0, 0, 1).lineWidth(10);

    private final Consumer<Vec3d>      selectionCallback;

    private final EventHandler         eventHandler          = new EventHandler();

    public SelectBlockGuiState(Consumer<Vec3d> selectionCallback) {

        this.selectionCallback = selectionCallback;
    }

    @Override
    protected ImmutableList<Object> getEventHandlers() {

        return ImmutableList.of(eventHandler);
    }

    protected class EventHandler {

        @SubscribeEvent
        public void onRenderGameOverlay(RenderGameOverlayEvent event) {

            if (!MC.gameSettings.hideGUI) {
                // Draw the border
                ScaledResolution scaledResolution = new ScaledResolution(MC);
                RenderUtils.drawAABB2D(15, 15, scaledResolution.getScaledWidth() - 15, scaledResolution.getScaledHeight() - 15, BORDER_RENDER_SETTING);

                // Draw the text
                String info = I18n.format("gui." + MOD_ID + ".selectBlock.info");
                MC.fontRendererObj.drawStringWithShadow(info, 25, 25, 0xffffff);
            }
        }

        @SubscribeEvent
        public void onBlockClick(PlayerInteractEvent event) {

            if (event instanceof PlayerInteractEvent.RightClickBlock || event instanceof PlayerInteractEvent.LeftClickBlock) {
                event.setCanceled(true);

                Vec3d selectedPoint = new Vec3d(event.getPos()).addVector(0.5, 0.5, 0.5);
                selectionCallback.accept(selectedPoint);
            }
        }

    }

}
