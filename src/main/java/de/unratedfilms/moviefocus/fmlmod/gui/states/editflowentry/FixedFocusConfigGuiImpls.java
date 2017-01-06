
package de.unratedfilms.moviefocus.fmlmod.gui.states.editflowentry;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.Locale;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.model.TextField;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.TextFieldImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow.FocusFlowEntry;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.FixedFocusConfig;

class FixedFocusConfigGuiImpls {

    private final FixedFocusConfig config;

    public FixedFocusConfigGuiImpls(FocusFlowEntry flowEntry) {

        Validate.isInstanceOf(FixedFocusConfig.class, flowEntry.getFocusConfig());
        config = (FixedFocusConfig) flowEntry.getFocusConfig();
    }

    public class SettingsContainer extends ContainerClippingImpl {

        private final Label     focalDepthLabel;
        private final TextField focalDepthTextField;

        public SettingsContainer() {

            focalDepthLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.focusConfigSettings.fixed.focalDepth"));

            focalDepthTextField = new TextFieldImpl();
            focalDepthTextField.setFilter(c -> Character.isDigit(c) || c == '.'); // allow positive decimal numbers
            focalDepthTextField.setMaxLength(10);
            focalDepthTextField.setText(String.format(Locale.ENGLISH, "%.3f", config.getFocalDepth()));
            focalDepthTextField.setHandler((textField, typedChar, keyCode) -> {
                config.setFocalDepth(NumberUtils.toFloat(focalDepthTextField.getText(), config.getFocalDepth()));
            });

            addWidgets(focalDepthLabel, focalDepthTextField);

            // ----- Revalidation -----

            appendLayoutManager(c -> {
                focalDepthTextField.setWidth(getWidth());
            });
            appendLayoutManager(new FlowLayout(Axis.Y, 0, 5));
        }

    }

    public class EventHandler {

        @SubscribeEvent
        public void onTick(ClientTickEvent event) {

            if (event.phase == Phase.START && Minecraft.getMinecraft().theWorld != null) {
                int rawMouseScroll = Mouse.getDWheel();
                if (rawMouseScroll != 0) {
                    int mouseScroll = rawMouseScroll > 0 ? 1 : -1;

                    float focalDepthChange = mouseScroll * 0.1f;
                    config.setFocalDepth(Math.max(0, config.getFocalDepth() + focalDepthChange));
                }
            }
        }

    }

}
