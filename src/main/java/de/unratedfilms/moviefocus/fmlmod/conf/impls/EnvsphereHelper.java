
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import static de.unratedfilms.moviefocus.fmlmod.util.VectorUtils.add;
import static de.unratedfilms.moviefocus.fmlmod.util.VectorUtils.multiply;
import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Vec3;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.model.TextField;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.TextFieldImpl;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils.RenderSetting;

class EnvsphereHelper {

    private static final RenderSetting[] ENVSPHERE_WIREFRAME_RENDER_SETTINGS = new RenderSetting[] {
            new RenderSetting(1f, .2f, 0f, .7f, GL11.GL_LEQUAL),
            new RenderSetting(1f, .2f, 0f, .2f, GL11.GL_GREATER)
    };
    private static final RenderSetting[] ENVSPHERE_CENTER_RENDER_SETTINGS    = new RenderSetting[] {
            new RenderSetting(1f, .5f, 0f, .7f, GL11.GL_LEQUAL),
            new RenderSetting(1f, .5f, 0f, .2f, GL11.GL_GREATER)
    };

    private static final RenderSetting[] FOCUS_SPOT_RENDER_SETTINGS          = new RenderSetting[] {
            new RenderSetting(1f, .75f, 0f, .7f, GL11.GL_LEQUAL),
            new RenderSetting(1f, .75f, 0f, .2f, GL11.GL_GREATER)
    };

    public static void addGuiSettings(Container container, Supplier<Float> radiusGetter, Consumer<Float> radiusSetter) {

        Label radiusLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".focusConfigSettings.general.envsphereRadius"));

        TextField radiusTextField = new TextFieldImpl();
        radiusTextField.setFilter(c -> Character.isDigit(c) || c == '.'); // allow positive decimal numbers
        radiusTextField.setMaxLength(10);
        radiusTextField.setText(String.format(Locale.ENGLISH, "%.2f", radiusGetter.get()));
        radiusTextField.setHandler((textField, typedChar, keyCode) -> {
            radiusSetter.accept(NumberUtils.toFloat(radiusTextField.getText(), radiusGetter.get()));
        });

        container.addWidgets(radiusLabel, radiusTextField);

        // ----- Revalidation -----

        container.appendLayoutManager(c -> {
            radiusTextField.setWidth(container.getWidth());
        });
    }

    public static void renderEnvsphere(Vec3 center, float radius) {

        RenderUtils.drawSphere(center, radius, (int) ( (Math.log(radius) + 5) * 3), true, ENVSPHERE_WIREFRAME_RENDER_SETTINGS);
        RenderUtils.drawSphere(center, 0.03f, 16, false, ENVSPHERE_CENTER_RENDER_SETTINGS);

        Vec3 focusSpot = add(center, multiply(GeometryUtils.getCamSightLine().normalize(), -radius));
        RenderUtils.drawSphere(focusSpot, 0.03f, 16, false, FOCUS_SPOT_RENDER_SETTINGS);
        RenderUtils.drawLine(center, focusSpot, FOCUS_SPOT_RENDER_SETTINGS);
    }

    private EnvsphereHelper() {}

}
