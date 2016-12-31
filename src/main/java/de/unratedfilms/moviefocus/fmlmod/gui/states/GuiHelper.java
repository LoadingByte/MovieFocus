
package de.unratedfilms.moviefocus.fmlmod.gui.states;

import static de.unratedfilms.moviefocus.fmlmod.util.VectorUtils.add;
import static de.unratedfilms.moviefocus.fmlmod.util.VectorUtils.multiply;
import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.model.TextField;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.TextFieldImpl;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils.RenderSetting;

public class GuiHelper {

    private static final Minecraft       MC                                   = Minecraft.getMinecraft();

    private static final RenderSetting   GIZMO_X_RENDER_SETTING               = new RenderSetting(1, 0, 0, 1).lineWidth(4);
    private static final RenderSetting   GIZMO_Y_RENDER_SETTING               = new RenderSetting(0, 1, 0, 1).lineWidth(4);
    private static final RenderSetting   GIZMO_Z_RENDER_SETTING               = new RenderSetting(0, 0, 1, 1).lineWidth(4);

    private static final RenderSetting[] ENVSPHERE_WIREFRAME_RENDER_SETTINGS  = {
            new RenderSetting(1, .2, 0, .7).lineWidth(2).depthFunc(GL11.GL_LEQUAL),
            new RenderSetting(1, .2, 0, .2).lineWidth(2).depthFunc(GL11.GL_GREATER)
    };
    private static final RenderSetting[] ENVSPHERE_CENTER_RENDER_SETTINGS     = {
            new RenderSetting(1, .5, 0, .7).depthFunc(GL11.GL_LEQUAL),
            new RenderSetting(1, .5, 0, .2).depthFunc(GL11.GL_GREATER)
    };
    private static final RenderSetting[] ENVSPHERE_FOCUS_SPOT_RENDER_SETTINGS = {
            new RenderSetting(1, .75, 0, .7).depthFunc(GL11.GL_LEQUAL),
            new RenderSetting(1, .75, 0, .2).depthFunc(GL11.GL_GREATER)
    };

    public static void drawGizmo(float lineLength, float partialTicks) {

        ScaledResolution scaledResolution = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);

        GL11.glPushMatrix();
        {
            // This code is shamelessly stolen from the latest Minecraft version
            GL11.glTranslatef(scaledResolution.getScaledWidth() * 0.5f, scaledResolution.getScaledHeight() * 0.5f, 0);
            Entity entity = MC.renderViewEntity;
            GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1, 0, 0);
            GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0, 1, 0);
            GL11.glScalef(-1, -1, -1);

            RenderUtils.drawLine(0, 0, 0, lineLength, 0, 0, GIZMO_X_RENDER_SETTING);
            RenderUtils.drawLine(0, 0, 0, 0, lineLength, 0, GIZMO_Y_RENDER_SETTING);
            RenderUtils.drawLine(0, 0, 0, 0, 0, lineLength, GIZMO_Z_RENDER_SETTING);
        }
        GL11.glPopMatrix();
    }

    public static void addEnvsphereGuiSettings(Container container, Supplier<Float> radiusGetter, Consumer<Float> radiusSetter) {

        Label radiusLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.focusConfigSettings.general.envsphereRadius"));

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

    public static void drawEnvsphere(Vec3 center, float radius) {

        RenderUtils.drawSphere(center, radius, (int) ( (Math.log(radius) + 5) * 3), true, ENVSPHERE_WIREFRAME_RENDER_SETTINGS);
        RenderUtils.drawSphere(center, 0.03f, 16, false, ENVSPHERE_CENTER_RENDER_SETTINGS);

        Vec3 focusSpot = add(center, multiply(GeometryUtils.getCamSightLine(), -radius));
        RenderUtils.drawSphere(focusSpot, 0.03f, 16, false, ENVSPHERE_FOCUS_SPOT_RENDER_SETTINGS);
        RenderUtils.drawLine(center, focusSpot, ENVSPHERE_FOCUS_SPOT_RENDER_SETTINGS);
    }

    private GuiHelper() {}

}
