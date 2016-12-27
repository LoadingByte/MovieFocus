
package de.unratedfilms.moviefocus.fmlmod.gui;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import net.minecraft.client.resources.I18n;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.integration.BasicScreen;
import de.unratedfilms.guilib.layouts.AlignLayout;
import de.unratedfilms.guilib.layouts.SqueezeLayout;
import de.unratedfilms.guilib.widgets.model.Button.FilteredButtonHandler;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.Checkbox;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.CheckboxImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow.FocusFlowEntry;
import de.unratedfilms.moviefocus.fmlmod.conf.RenderedFocusConfigProvider;

public class FocusFlowEntryScreen extends BasicScreen {

    private static final int     MARGIN  = 5;
    private static final int     PADDING = 5;
    private static final float   H_RATIO = 1f / 4f;

    private final FocusFlowEntry editedFlowEntry;

    private ContainerFlexible    mainContainer;

    private Label                titleLabel;
    private Label                configNameLabel;
    private Label                flowEntryTitleLabel;
    private Checkbox             tryoutCheckbox;
    private ContainerFlexible    configSettingsContainer; // This field will be set to the currently selected focus config's settings container
    private ButtonLabel          closeButton;

    public FocusFlowEntryScreen(FocusFlowEntry editedFlowEntry) {

        super(null);

        this.editedFlowEntry = editedFlowEntry;
    }

    @Override
    protected void createGui() {

        ContainerFlexible rootContainer = new ContainerClippingImpl();
        setRootWidget(rootContainer);

        mainContainer = new ContainerClippingImpl();
        rootContainer.addWidgets(mainContainer);

        titleLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".focusFlowEntry.title"));
        configNameLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".focusConfigName." + editedFlowEntry.getFocusConfig().getInternalName()));
        flowEntryTitleLabel = new LabelImpl(editedFlowEntry.getTitle().isEmpty() ? I18n.format("gui." + MOD_ID + ".focusFlowEntry.noEntryTitle") : '"' + editedFlowEntry.getTitle() + '"');

        tryoutCheckbox = new CheckboxImpl(I18n.format("gui." + MOD_ID + ".focusFlowEntry.tryout"), true);
        tryoutCheckbox.setHandler((cb, checked) -> setRenderedFocusConfigOverrideActivity(checked));
        setRenderedFocusConfigOverrideActivity(tryoutCheckbox.isChecked()); // The checkbox is checked in the beginning

        configSettingsContainer = editedFlowEntry.getFocusConfig().createSettingsContainer();
        closeButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".focusFlowEntry.close"), new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> close()));

        mainContainer.addWidgets(titleLabel, configNameLabel, flowEntryTitleLabel, tryoutCheckbox, configSettingsContainer, closeButton);

        // ----- Revalidation -----

        rootContainer
                .appendLayoutManager(c -> {
                    mainContainer.setBounds((int) (rootContainer.getWidth() * (1 - H_RATIO) - MARGIN), MARGIN,
                            (int) (rootContainer.getWidth() * H_RATIO), rootContainer.getHeight() - 2 * MARGIN);
                });

        mainContainer
                .appendLayoutManager(new AlignLayout(Axis.X, PADDING))
                .appendLayoutManager(new SqueezeLayout(Axis.Y, PADDING, 4)
                        .keep(titleLabel, configNameLabel, flowEntryTitleLabel, tryoutCheckbox, closeButton)
                        .weight(1, configSettingsContainer))
                .appendLayoutManager(c -> {
                    // Center the title label
                    titleLabel.setX( (mainContainer.getWidth() - titleLabel.getWidth()) / 2);

                    // The settings container and the close button should span the whole main container
                    configSettingsContainer.setWidth(mainContainer.getWidth() - 2 * PADDING);
                    closeButton.setWidth(mainContainer.getWidth() - 2 * PADDING);
                });
    }

    private void setRenderedFocusConfigOverrideActivity(boolean active) {

        RenderedFocusConfigProvider.setRenderedFocusConfigOverride(active ? editedFlowEntry.getFocusConfig() : null);
    }

    @Override
    public void drawBackground() {

        // We can simply query these coordinates because the main container is a direct child of the root container, meaning that the coordinates are absolute
        drawRect(mainContainer.getX(), mainContainer.getY(), mainContainer.getX() + mainContainer.getWidth(), mainContainer.getY() + mainContainer.getHeight(), 0x80101010);
    }

    @Override
    public void close() {

        // Always remember to clear the focus config override that's been used by the tryout checkbox
        setRenderedFocusConfigOverrideActivity(false);

        // When someone exits this GUI, he needs to see the focus flow screen again instead of no GUI, which is what super.close() would do
        mc.displayGuiScreen(new FocusFlowScreen());
    }

}
