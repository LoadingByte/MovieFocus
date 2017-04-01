
package de.unratedfilms.moviefocus.fmlmod.gui.states.editflowentry;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.widgets.model.Button.FilteredButtonHandler;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.EntityFocusConfig;
import de.unratedfilms.moviefocus.fmlmod.flow.FocusFlow.FocusFlowEntry;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiStateMachine;
import de.unratedfilms.moviefocus.fmlmod.gui.states.GuiHelper;
import de.unratedfilms.moviefocus.fmlmod.gui.states.SelectEntityGuiState;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils.RenderSetting;

class EntityFocusConfigGuiImpls {

    private final FocusFlowEntry    flowEntry;
    private final EntityFocusConfig config;

    public EntityFocusConfigGuiImpls(FocusFlowEntry flowEntry) {

        Validate.isInstanceOf(EntityFocusConfig.class, flowEntry.getFocusConfig());

        this.flowEntry = flowEntry;
        config = (EntityFocusConfig) flowEntry.getFocusConfig();
    }

    public class SettingsContainer extends ContainerClippingImpl {

        private final Label       focusedEntityLabel;

        private final ButtonLabel selectionStartButton;

        public SettingsContainer() {

            boolean empty = !config.isDescribingExistingFocusedEntity();
            focusedEntityLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.focusConfigSettings.entity.focusedEntity",
                    empty ? "--" : config.getFocusedEntity().getName(), empty ? "--" : config.getFocusedEntity().getEntityId()));

            selectionStartButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.focusConfigSettings.entity.startSelection"),
                    new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> {
                        GuiStateMachine.transitionToState(new SelectEntityGuiState(selEntity -> {
                            config.setFocusedEntityId(selEntity.getEntityId());
                            GuiStateMachine.transitionToState(new EditFocusFlowEntryGuiState(flowEntry));
                        }));
                    }));

            addWidgets(focusedEntityLabel, selectionStartButton);

            GuiHelper.addEnvsphereGuiSettings(this, () -> config.getEnvsphereRadius(), r -> config.setEnvsphereRadius(r));

            // ----- Revalidation -----

            appendLayoutManager(c -> {
                selectionStartButton.setWidth(getWidth());
            });
            appendLayoutManager(new FlowLayout(Axis.Y, 0, 5));
        }

    }

    public class EventHandler {

        private final RenderSetting[] aabbRenderSettings = {
                new RenderSetting(1, 0, 0, .3).depthFunc(GL11.GL_LEQUAL),
                new RenderSetting(1, 0, 0, .11).depthFunc(GL11.GL_GREATER)
        };

        @SubscribeEvent
        public void onRenderWorldLast(RenderWorldLastEvent event) {

            if (!Minecraft.getMinecraft().gameSettings.hideGUI && config.isDescribingExistingFocusedEntity()) {
                EntityPlayer player = Minecraft.getMinecraft().player;
                double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
                double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
                double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

                GlStateManager.pushMatrix();
                {
                    GlStateManager.translate(-playerX, -playerY, -playerZ);

                    // Render the bounding box around the focused entity
                    if (config.getFocusedEntity().getEntityBoundingBox() != null) {
                        RenderUtils.drawAABB(config.getFocusedEntity().getEntityBoundingBox(), aabbRenderSettings);
                    }

                    // Render the envsphere
                    GuiHelper.drawEnvsphere(config.getEnvsphereCenter(), config.getEnvsphereRadius());
                }
                GlStateManager.popMatrix();
            }
        }

    }

}
