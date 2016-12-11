
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
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.layouts.AlignLayout;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.model.TextField;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.TextFieldImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigAdapter;

public class FixedFocusConfig extends FocusConfigAdapter {

    private float fixedFocalDepth;

    public FixedFocusConfig() {

        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getTitle() {

        return I18n.format("gui." + MOD_ID + ".focusConfigTitle.fixed");
    }

    @Override
    public boolean isAvailable() {

        return true;
    }

    @Override
    public float getFocalDepth() {

        return fixedFocalDepth;
    }

    // ----- Events -----

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {

        if (!isSelected() || !isActivated()) {
            return;
        }

        if (event.phase == Phase.START && Minecraft.getMinecraft().theWorld != null) {
            int rawMouseScroll = Mouse.getDWheel();
            if (rawMouseScroll != 0) {
                int mouseScroll = rawMouseScroll > 0 ? 1 : -1;

                float focalDepthChange = mouseScroll * 0.1f;
                fixedFocalDepth = Math.max(0, fixedFocalDepth + focalDepthChange);
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

            focalDepthLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".settings.fixed.focalDepth"));

            focalDepthTextField = new TextFieldImpl();
            focalDepthTextField.setFilter(c -> Character.isDigit(c) || c == '.'); // allow positive decimal numbers
            focalDepthTextField.setMaxLength(10);
            focalDepthTextField.setText(String.format(Locale.ENGLISH, "%.3f", fixedFocalDepth));
            focalDepthTextField.setHandler((textField, typedChar, keyCode) -> {
                fixedFocalDepth = NumberUtils.toFloat(focalDepthTextField.getText(), fixedFocalDepth);
            });

            addWidgets(focalDepthLabel, focalDepthTextField);

            // ----- Revalidation -----

            appendLayoutManager(c -> {
                focalDepthTextField.setWidth(getWidth());
            });
            appendLayoutManager(new AlignLayout(Axis.X, 0));
            appendLayoutManager(new FlowLayout(Axis.Y, 0, 5));
        }

    }

}
