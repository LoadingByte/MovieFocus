
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.extra.FlowLayoutManager;
import de.unratedfilms.guilib.extra.FlowLayoutManager.Axis;
import de.unratedfilms.guilib.widgets.model.Button;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils;
import de.unratedfilms.moviefocus.fmlmod.util.RenderUtils.RenderSetting;

public class PointFocusConfig extends PointFocusConfigWithoutGui {

    private static final RenderSetting[] OUTER_BOX_RENDER_SETTINGS  = new RenderSetting[] {
            new RenderSetting(1f, 0f, 0f, .7f, GL11.GL_LEQUAL),
            new RenderSetting(1f, 0f, 0f, .2f, GL11.GL_GREATER)
    };
    private static final RenderSetting[] MIDDLE_BOX_RENDER_SETTINGS = new RenderSetting[] {
            new RenderSetting(1f, .5f, 0f, .7f, GL11.GL_LEQUAL),
            new RenderSetting(1f, .5f, 0f, .2f, GL11.GL_GREATER)
    };
    private static final RenderSetting[] INNER_BOX_RENDER_SETTINGS  = new RenderSetting[] {
            new RenderSetting(1f, .75f, 0f, .7f, GL11.GL_LEQUAL),
            new RenderSetting(1f, .75f, 0f, .2f, GL11.GL_GREATER)
    };

    private boolean                      waitingForUserSelection;

    // For rendering
    private AxisAlignedBB                cachedOuterBox;
    private AxisAlignedBB                cachedMiddleBox;
    private AxisAlignedBB                cachedInnerBox;

    @Override
    public void setFocusedPoint(Vec3 focusedPoint) {

        super.setFocusedPoint(focusedPoint);

        cachedOuterBox = AxisAlignedBB.getBoundingBox(focusedPoint.xCoord - 0.5, focusedPoint.yCoord - 0.5, focusedPoint.zCoord - 0.5, focusedPoint.xCoord + 0.5, focusedPoint.yCoord + 0.5, focusedPoint.zCoord + 0.5);
        cachedMiddleBox = cachedOuterBox.expand(-0.25, -0.25, -0.25);
        cachedInnerBox = cachedMiddleBox.expand(-0.1, -0.1, -0.1);
    }

    // ----- Events -----

    @Override
    public void setStatus(boolean selected, boolean activated) {

        MinecraftForge.EVENT_BUS.unregister(this);
        if (selected) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    // Renders the box which highlights the focused point if the DoF effect is not activated
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {

        if (!Minecraft.getMinecraft().gameSettings.hideGUI && focusedPoint != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
            double playerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
            double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

            RenderUtils.drawAABB(event.context, cachedOuterBox.getOffsetBoundingBox(-playerX, -playerY, -playerZ), OUTER_BOX_RENDER_SETTINGS);
            RenderUtils.drawAABB(event.context, cachedMiddleBox.getOffsetBoundingBox(-playerX, -playerY, -playerZ), MIDDLE_BOX_RENDER_SETTINGS);
            RenderUtils.drawAABB(event.context, cachedInnerBox.getOffsetBoundingBox(-playerX, -playerY, -playerZ), INNER_BOX_RENDER_SETTINGS);
        }
    }

    @SubscribeEvent
    public void onBlockClick(PlayerInteractEvent event) {

        if (waitingForUserSelection) {
            waitingForUserSelection = false;
            event.setCanceled(true);

            setFocusedPoint(Vec3.createVectorHelper(event.x + 0.5, event.y + 0.5, event.z + 0.5));
        }
    }

    // ----- GUI -----

    @Override
    public ContainerFlexible createSettingsContainer() {

        return new SettingsContainer();
    }

    public class SettingsContainer extends ContainerClippingImpl {

        private final Label       focusedPointLabel;

        private final ButtonLabel selectionStartButton;
        private final Label       selectionStatusLabel;

        public SettingsContainer() {

            focusedPointLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".settings.point.focusedPoint",
                    focusedPoint == null ? "--" : String.format("%.2f,%.2f,%.2f", focusedPoint.xCoord, focusedPoint.yCoord, focusedPoint.zCoord)));
            addWidgets(focusedPointLabel);

            selectionStartButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".settings.point.selectionStart"),
                    (Button button, MouseButton mouseButton) -> {
                        waitingForUserSelection = true;
                        refreshSelectionStatusLabel();
                    });
            addWidgets(selectionStartButton);

            selectionStatusLabel = new LabelImpl("");
            refreshSelectionStatusLabel();
            addWidgets(selectionStatusLabel);

            // ----- Revalidation -----

            appendLayoutManager(() -> {
                selectionStartButton.setWidth(getWidth());
            });
            appendLayoutManager(new FlowLayoutManager(this, Axis.Y, 0, 0, 5));
        }

        private void refreshSelectionStatusLabel() {

            if (waitingForUserSelection) {
                selectionStatusLabel.setText(I18n.format("gui." + MOD_ID + ".settings.point.selectionStatus.waiting"));
            } else {
                selectionStatusLabel.setText(" ");
            }
        }

    }

}
