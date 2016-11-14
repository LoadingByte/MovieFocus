
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.Locale;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import de.unratedfilms.guilib.extra.FlowLayoutManager;
import de.unratedfilms.guilib.extra.FlowLayoutManager.Axis;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.model.TextField;
import de.unratedfilms.guilib.widgets.model.TextField.DecimalNumberFilter;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.TextFieldImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;

public class FixedFocusConfig implements FocusConfig {

    private float   fixedFocalDepth;

    private boolean activated;

    // ----- Properties -----

    @Override
    public String getTitle() {

        return I18n.format("gui." + MOD_ID + ".focusConfigTitle.fixed");
    }

    public void setFocalDepth(float fixedFocalDepth) {

        // The focal depth mustn't be below 0
        if (fixedFocalDepth < 0) {
            fixedFocalDepth = 0;
        }

        this.fixedFocalDepth = fixedFocalDepth;
    }

    // ----- Queries -----

    @Override
    public boolean isAvailable() {

        return true;
    }

    @Override
    public float getFocalDepth() {

        return fixedFocalDepth;
    }

    // ----- Events -----

    @Override
    public void setStatus(boolean selected, boolean activated) {

        this.activated = activated;

        FMLCommonHandler.instance().bus().unregister(this);
        if (selected) {
            FMLCommonHandler.instance().bus().register(this);
        }
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {

        if (activated && event.phase == Phase.START && Minecraft.getMinecraft().theWorld != null) {
            int rawMouseScroll = Mouse.getDWheel();
            if (rawMouseScroll != 0) {
                int mouseScroll = rawMouseScroll > 0 ? 1 : -1;

                float focalDepthChange = mouseScroll * 0.1f;
                setFocalDepth(fixedFocalDepth + focalDepthChange);
            }
        }
    }

    // ----- GUI -----

    @Override
    public ContainerFlexible createSettingsContainer() {

        return new SettingsContainer();
    }

    public class SettingsContainer extends ContainerClippingImpl {

        private final Label     focalDepthLabel;
        private final TextField focalDepthTextField;

        public SettingsContainer() {

            focalDepthLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".settings.fixed.focalDepthLabel"));
            addWidgets(focalDepthLabel);

            focalDepthTextField = new TextFieldImpl(new DecimalNumberFilter());
            focalDepthTextField.setMaxLength(15);
            focalDepthTextField.setText(String.format(Locale.ENGLISH, "%.3f", fixedFocalDepth));
            focalDepthTextField.setHandler((TextField textField, char typedChar, int keyCode) -> {
                updateFocalDepth();
            });
            addWidgets(focalDepthTextField);

            // ----- Revalidation -----

            appendLayoutManager(() -> {
                focalDepthTextField.setWidth(getWidth());
            });
            appendLayoutManager(new FlowLayoutManager(this, Axis.Y, 0, 0, 5));
        }

        private void updateFocalDepth() {

            fixedFocalDepth = NumberUtils.toFloat(focalDepthTextField.getText(), fixedFocalDepth);
        }

    }

}
