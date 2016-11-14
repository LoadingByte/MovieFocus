
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.extra.FlowLayoutManager;
import de.unratedfilms.guilib.extra.FlowLayoutManager.Axis;
import de.unratedfilms.guilib.widgets.model.Button;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils.RenderSetting;

public class EntityFocusConfig extends EntityFocusConfigWithoutGui {

    private static final RenderSetting[] OUTLINE_RENDER_SETTINGS = new RenderSetting[] {
            new RenderSetting(1f, 0f, 0f, .7f, GL11.GL_LEQUAL),
            new RenderSetting(1f, 0f, 0f, .2f, GL11.GL_GREATER)
    };

    private boolean                      waitingForUserSelection;

    // ----- Events -----

    @Override
    public void setStatus(boolean selected, boolean activated) {

        MinecraftForge.EVENT_BUS.unregister(this);
        if (selected) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    // Renders the bounding box around the focused entity if the DoF effect is not activated
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        if (!Minecraft.getMinecraft().gameSettings.hideGUI && hasFocusedEntity() && focusedEntity.get().boundingBox != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
            double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
            double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;
            RenderUtils.drawAABB(event.context, focusedEntity.get().boundingBox.getOffsetBoundingBox(-playerX, -playerY, -playerZ), OUTLINE_RENDER_SETTINGS);
        }
    }

    @SubscribeEvent
    public void onEntityClick(EntityInteractEvent event) {

        if (waitingForUserSelection) {
            waitingForUserSelection = false;
            event.setCanceled(true);

            setFocusedEntity(event.target);
        }
    }

    // ----- GUI -----

    @Override
    public ContainerFlexible createSettingsContainer() {

        return new SettingsContainer();
    }

    public class SettingsContainer extends ContainerClippingImpl {

        private final Label       focusedEntityLabel;

        private final ButtonLabel selectionStartButton;
        private final Label       selectionStatusLabel;

        public SettingsContainer() {

            focusedEntityLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".settings.entity.focusedEntity",
                    !hasFocusedEntity() ? "--" : focusedEntity.get().getCommandSenderName()));
            addWidgets(focusedEntityLabel);

            selectionStartButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".settings.entity.selectionStart"),
                    (Button button, MouseButton mouseButton) -> {
                        waitingForUserSelection = true;
                        refreshSelectionStatusLabel();
                    });
            addWidgets(selectionStartButton);

            selectionStatusLabel = new LabelImpl("");
            refreshSelectionStatusLabel();
            addWidgets(selectionStatusLabel);

            // ----- Revalidation -----

            appendLayoutManager(() -> {
                selectionStartButton.setWidth(getWidth());
            });
            appendLayoutManager(new FlowLayoutManager(this, Axis.Y, 0, 0, 5));
        }

        private void refreshSelectionStatusLabel() {

            if (waitingForUserSelection) {
                selectionStatusLabel.setText(I18n.format("gui." + MOD_ID + ".settings.entity.selectionStatus.waiting"));
            } else {
                selectionStatusLabel.setText(" ");
            }
        }

    }

}
