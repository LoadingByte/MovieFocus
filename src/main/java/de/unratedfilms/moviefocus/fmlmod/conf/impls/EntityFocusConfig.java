
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.lang.ref.WeakReference;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.layouts.AlignLayout;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigAdapter;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils.RenderSetting;

public class EntityFocusConfig extends FocusConfigAdapter {

    private static final RenderSetting[] AABB_RENDER_SETTINGS = new RenderSetting[] {
            new RenderSetting(1f, 0f, 0f, .3f, GL11.GL_LEQUAL),
            new RenderSetting(1f, 0f, 0f, .11f, GL11.GL_GREATER)
    };

    private WeakReference<Entity>        focusedEntity;
    private float                        envsphereRadius      = 0.5f;

    private boolean                      waitingForUserSelection;

    public EntityFocusConfig() {

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getTitle() {

        return I18n.format("gui." + MOD_ID + ".focusConfigTitle.entity");
    }

    private boolean hasFocusedEntity() {

        return focusedEntity != null && focusedEntity.get() != null && focusedEntity.get().isEntityAlive();
    }

    private Vec3 getEnvsphereCenter() {

        Entity focusedEntity = this.focusedEntity.get();
        return Vec3.createVectorHelper(focusedEntity.posX, focusedEntity.posY + focusedEntity.getEyeHeight(), focusedEntity.posZ);
    }

    @Override
    public boolean isAvailable() {

        return hasFocusedEntity();
    }

    @Override
    public float getFocalDepth() {

        return (float) GeometryUtils.getDepth(getEnvsphereCenter()) - envsphereRadius;
    }

    // ----- Events -----

    // Renders the bounding box around the focused entity if the DoF effect is not activated
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        if (!isSelected()) {
            return;
        }

        if (!Minecraft.getMinecraft().gameSettings.hideGUI && hasFocusedEntity()) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
            double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
            double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

            GL11.glPushMatrix();
            {
                GL11.glTranslated(-playerX, -playerY, -playerZ);

                if (focusedEntity.get().boundingBox != null) {
                    RenderUtils.drawAABB(focusedEntity.get().boundingBox, AABB_RENDER_SETTINGS);
                }
                EnvsphereHelper.renderEnvsphere(getEnvsphereCenter(), envsphereRadius);
            }
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void onEntityClick(EntityInteractEvent event) {

        if (!isSelected()) {
            return;
        }

        if (waitingForUserSelection) {
            waitingForUserSelection = false;
            event.setCanceled(true);

            focusedEntity = new WeakReference<>(event.target);
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

            selectionStartButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".settings.entity.selectionStart"),
                    (button, mouseButton) -> {
                        waitingForUserSelection = true;
                        refreshSelectionStatusLabel();
                    });

            selectionStatusLabel = new LabelImpl("");
            refreshSelectionStatusLabel();

            addWidgets(focusedEntityLabel, selectionStartButton, selectionStatusLabel);

            EnvsphereHelper.addGuiSettings(this, () -> envsphereRadius, (r) -> envsphereRadius = r);

            // ----- Revalidation -----

            appendLayoutManager(c -> {
                selectionStartButton.setWidth(getWidth());
            });
            appendLayoutManager(new AlignLayout(Axis.X, 0));
            appendLayoutManager(new FlowLayout(Axis.Y, 0, 5));
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
