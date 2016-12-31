
package de.unratedfilms.moviefocus.fmlmod.gui.states.editflowentry;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow.FocusFlowEntry;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.EntityFocusConfig;
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

            focusedEntityLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.focusConfigSettings.entity.focusedEntity",
                    !config.hasFocusedEntity() ? "--" : config.getFocusedEntity().getCommandSenderName()));

            selectionStartButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.focusConfigSettings.entity.startSelection"),
                    (b, mb) -> {
                        GuiStateMachine.transitionToState(new SelectEntityGuiState(selEntity -> {
                            config.setFocusedEntity(selEntity);
                            GuiStateMachine.transitionToState(new EditFocusFlowEntryGuiState(flowEntry));
                        }));
                    });

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

            if (!Minecraft.getMinecraft().gameSettings.hideGUI && config.hasFocusedEntity()) {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
                double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
                double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

                GL11.glPushMatrix();
                {
                    GL11.glTranslated(-playerX, -playerY, -playerZ);

                    // Render the bounding box around the focused entity
                    if (config.getFocusedEntity().boundingBox != null) {
                        RenderUtils.drawAABB(config.getFocusedEntity().boundingBox, aabbRenderSettings);
                    }

                    // Render the envsphere
                    GuiHelper.drawEnvsphere(config.getEnvsphereCenter(), config.getEnvsphereRadius());
                }
                GL11.glPopMatrix();
            }
        }

    }

}
