
package de.unratedfilms.moviefocus.fmlmod.gui.states;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.function.Consumer;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;

public class SelectBlockGuiState extends GuiState {

    private final Consumer<Vec3> selectionCallback;

    private final EventHandler   eventHandler = new EventHandler();

    public SelectBlockGuiState(Consumer<Vec3> selectionCallback) {

        this.selectionCallback = selectionCallback;
    }

    @Override
    protected ImmutableList<Object> getEventHandlers() {

        return ImmutableList.of(eventHandler);
    }

    protected class EventHandler {

        @SubscribeEvent
        public void onRenderGameOverlay(RenderGameOverlayEvent event) {

            if (!Minecraft.getMinecraft().gameSettings.hideGUI) {
                String info = I18n.format("gui." + MOD_ID + ".selectBlock.info");
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(info, 10, 10, 0xffffff);
            }
        }

        @SubscribeEvent
        public void onBlockClick(PlayerInteractEvent event) {

            event.setCanceled(true);

            Vec3 selectedPoint = Vec3.createVectorHelper(event.x + 0.5, event.y + 0.5, event.z + 0.5);
            selectionCallback.accept(selectedPoint);
        }

    }

}
