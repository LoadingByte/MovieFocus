
package de.unratedfilms.moviefocus.fmlmod.gui.states.editflowentry;

import static de.unratedfilms.moviefocus.fmlmod.util.VectorUtils.*;
import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.layouts.FitLayout;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.layouts.SqueezeLayout;
import de.unratedfilms.guilib.widgets.model.Button.FilteredButtonHandler;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.model.TextField;
import de.unratedfilms.guilib.widgets.model.TextField.DecimalNumberFilter;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.TextFieldImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.PointFocusConfig;
import de.unratedfilms.moviefocus.fmlmod.flow.FocusFlow.FocusFlowEntry;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiStateMachine;
import de.unratedfilms.moviefocus.fmlmod.gui.states.GuiHelper;
import de.unratedfilms.moviefocus.fmlmod.gui.states.SelectBlockGuiState;

class PointFocusConfigGuiImpls {

    private final FocusFlowEntry   flowEntry;
    private final PointFocusConfig config;

    public PointFocusConfigGuiImpls(FocusFlowEntry flowEntry) {

        Validate.isInstanceOf(PointFocusConfig.class, flowEntry.getFocusConfig());

        this.flowEntry = flowEntry;
        config = (PointFocusConfig) flowEntry.getFocusConfig();
    }

    public class SettingsContainer extends ContainerClippingImpl {

        private final Label       focusedPointLabel;
        private final TextField   focusedPointXCoordTextField;
        private final TextField   focusedPointYCoordTextField;
        private final TextField   focusedPointZCoordTextField;

        private final ButtonLabel selectionStartButton;

        public SettingsContainer() {

            focusedPointLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.focusConfigSettings.point.focusedPoint"));

            focusedPointXCoordTextField = createCoordTextField(0xffff0000, () -> config.getFocusedPoint().xCoord, x -> config.setFocusedPoint(withX(config.getFocusedPoint(), x)));
            focusedPointYCoordTextField = createCoordTextField(0xff00ff00, () -> config.getFocusedPoint().yCoord, y -> config.setFocusedPoint(withY(config.getFocusedPoint(), y)));
            focusedPointZCoordTextField = createCoordTextField(0xff0000ff, () -> config.getFocusedPoint().zCoord, z -> config.setFocusedPoint(withZ(config.getFocusedPoint(), z)));
            ContainerFlexible focusedPointCoordContainer = new ContainerClippingImpl(focusedPointXCoordTextField, focusedPointYCoordTextField, focusedPointZCoordTextField);

            selectionStartButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.focusConfigSettings.point.startSelection"),
                    new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> {
                        GuiStateMachine.transitionToState(new SelectBlockGuiState(selPoint -> {
                            config.setFocusedPoint(selPoint);
                            GuiStateMachine.transitionToState(new EditFocusFlowEntryGuiState(flowEntry));
                        }));
                    }));

            addWidgets(focusedPointLabel, focusedPointCoordContainer, selectionStartButton);

            GuiHelper.addEnvsphereGuiSettings(this, () -> config.getEnvsphereRadius(), r -> config.setEnvsphereRadius(r));

            // ----- Revalidation -----

            appendLayoutManager(c -> {
                focusedPointCoordContainer.setSize(getWidth(), 20);
                selectionStartButton.setWidth(getWidth());
            });
            appendLayoutManager(new FlowLayout(Axis.Y, 0, 5));

            focusedPointCoordContainer
                    .appendLayoutManager(new FitLayout(Axis.Y))
                    .appendLayoutManager(new SqueezeLayout(Axis.X, 0, 0)
                            .weight(1, focusedPointXCoordTextField, focusedPointYCoordTextField, focusedPointZCoordTextField));
        }

        private TextField createCoordTextField(int color, Supplier<Double> getter, Consumer<Double> setter) {

            TextFieldImpl coordTextField = new TextFieldImpl(new DecimalNumberFilter());
            coordTextField.setOuterColor(color);
            coordTextField.setMaxLength(10);
            coordTextField.setText(String.format(Locale.ENGLISH, "%.2f", getter.get()));
            coordTextField.setHandler((textField, typedChar, keyCode) -> {
                setter.accept(NumberUtils.toDouble(coordTextField.getText(), getter.get()));
            });
            return coordTextField;
        }

    }

    public class EventHandler {

        @SubscribeEvent
        public void onRenderWorldLast(RenderWorldLastEvent event) {

            if (!Minecraft.getMinecraft().gameSettings.hideGUI) {
                EntityPlayer player = Minecraft.getMinecraft().player;
                double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
                double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
                double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

                GlStateManager.pushMatrix();
                {
                    GlStateManager.translate(-playerX, -playerY, -playerZ);

                    // Render the envsphere which highlights the focused point
                    GuiHelper.drawEnvsphere(config.getFocusedPoint(), config.getEnvsphereRadius());
                }
                GlStateManager.popMatrix();
            }
        }

        @SubscribeEvent
        public void onRenderGameOverlay(RenderGameOverlayEvent event) {

            if (!Minecraft.getMinecraft().gameSettings.hideGUI) {
                // Render the gizmo
                GuiHelper.drawGizmo(20, event.getPartialTicks());
            }
        }

    }

}
