
package de.unratedfilms.moviefocus.fmlmod.gui.states;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.function.Consumer;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;

public class SelectEntityGuiState extends GuiState {

    private final Consumer<Entity> selectionCallback;

    private final EventHandler     eventHandler = new EventHandler();

    public SelectEntityGuiState(Consumer<Entity> selectionCallback) {

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
                String info = I18n.format("gui." + MOD_ID + ".selectEntity.info");
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(info, 10, 10, 0xffffff);
            }
        }

        @SubscribeEvent
        public void onEntityClick(EntityInteractEvent event) {

            event.setCanceled(true);

            selectionCallback.accept(event.target);
        }

    }

}
