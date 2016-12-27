
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.layouts.FitLayout;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.layouts.SqueezeLayout;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.model.TextField;
import de.unratedfilms.guilib.widgets.model.TextField.DecimalNumberFilter;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.TextFieldImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigAdapter;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;

@FocusConfig.InternalName ("point")
public class PointFocusConfig extends FocusConfigAdapter {

    private Vec3    focusedPoint    = Vec3.createVectorHelper(0, 0, 0);
    private float   envsphereRadius = 0.5f;

    private boolean waitingForUserSelection;

    public PointFocusConfig() {

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean isAvailable() {

        return true;
    }

    @Override
    public float getFocalDepth() {

        return (float) GeometryUtils.getDepth(focusedPoint) - envsphereRadius;
    }

    // ----- Events -----

    // Renders the envsphere which highlights the focused point if the DoF effect is not activated
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        if (!isActive()) {
            return;
        }

        if (!Minecraft.getMinecraft().gameSettings.hideGUI) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
            double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
            double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

            GL11.glPushMatrix();
            {
                GL11.glTranslated(-playerX, -playerY, -playerZ);
                EnvsphereHelper.renderEnvsphere(focusedPoint, envsphereRadius);
            }
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void onBlockClick(PlayerInteractEvent event) {

        if (waitingForUserSelection) {
            waitingForUserSelection = false;
            event.setCanceled(true);

            focusedPoint = Vec3.createVectorHelper(event.x + 0.5, event.y + 0.5, event.z + 0.5);
        }
    }

    // ----- GUI -----

    @Override
    public ContainerFlexible createSettingsContainer() {

        return new SettingsContainer();
    }

    public class SettingsContainer extends ContainerClippingImpl {

        private final Label       focusedPointLabel;
        private final TextField   focusedPointXCoordTextField;
        private final TextField   focusedPointYCoordTextField;
        private final TextField   focusedPointZCoordTextField;

        private final ButtonLabel selectionStartButton;
        private final Label       selectionStatusLabel;

        public SettingsContainer() {

            focusedPointLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".focusConfigSettings.point.focusedPoint"));

            focusedPointXCoordTextField = createCoordTextField(() -> focusedPoint.xCoord, (x) -> focusedPoint.xCoord = x);
            focusedPointYCoordTextField = createCoordTextField(() -> focusedPoint.yCoord, (y) -> focusedPoint.yCoord = y);
            focusedPointZCoordTextField = createCoordTextField(() -> focusedPoint.zCoord, (z) -> focusedPoint.zCoord = z);
            ContainerFlexible focusedPointCoordContainer = new ContainerClippingImpl(focusedPointXCoordTextField, focusedPointYCoordTextField, focusedPointZCoordTextField);

            selectionStartButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".focusConfigSettings.point.selectionStart"),
                    (button, mouseButton) -> {
                        waitingForUserSelection = true;
                        refreshSelectionStatusLabel();
                    });

            selectionStatusLabel = new LabelImpl("");
            refreshSelectionStatusLabel();

            addWidgets(focusedPointLabel, focusedPointCoordContainer, selectionStartButton, selectionStatusLabel);

            EnvsphereHelper.addGuiSettings(this, () -> envsphereRadius, (r) -> envsphereRadius = r);

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

        private TextField createCoordTextField(Supplier<Double> getter, Consumer<Double> setter) {

            TextField coordTextField = new TextFieldImpl(new DecimalNumberFilter());
            coordTextField.setMaxLength(10);
            coordTextField.setText(String.format(Locale.ENGLISH, "%.2f", getter.get()));
            coordTextField.setHandler((textField, typedChar, keyCode) -> {
                setter.accept(NumberUtils.toDouble(coordTextField.getText(), getter.get()));
            });
            return coordTextField;
        }

        private void refreshSelectionStatusLabel() {

            if (waitingForUserSelection) {
                selectionStatusLabel.setText(I18n.format("gui." + MOD_ID + ".focusConfigSettings.point.selectionStatus.waiting"));
            } else {
                selectionStatusLabel.setText("");
            }
        }

    }

}
