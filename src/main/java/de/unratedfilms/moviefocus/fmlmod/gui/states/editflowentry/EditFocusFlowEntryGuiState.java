
package de.unratedfilms.moviefocus.fmlmod.gui.states.editflowentry;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiScreen;
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
import de.unratedfilms.moviefocus.fmlmod.gui.GuiState;
import de.unratedfilms.moviefocus.fmlmod.gui.GuiStateMachine;
import de.unratedfilms.moviefocus.fmlmod.gui.states.EditFocusFlowGuiState;

public class EditFocusFlowEntryGuiState extends GuiState {

    private final FocusFlowEntry editedFlowEntry;

    private final Screen         screen = new Screen();
    private final Object         configEventHandler;

    public EditFocusFlowEntryGuiState(FocusFlowEntry editedFlowEntry) {

        this.editedFlowEntry = editedFlowEntry;

        configEventHandler = FocusConfigGuiImplsFactory.createEventHandler(editedFlowEntry);
    }

    @Override
    protected GuiScreen getScreen() {

        return screen;
    }

    @Override
    protected ImmutableList<Object> getEventHandlers() {

        return ImmutableList.of(configEventHandler);
    }

    public FocusFlowEntry getEditedFlowEntry() {

        return editedFlowEntry;
    }

    public boolean isTryoutEnabled() {

        return screen.tryoutCheckbox.isChecked();
    }

    private class Screen extends BasicScreen {

        private static final int   MARGIN  = 5;
        private static final int   PADDING = 5;
        private static final float H_RATIO = 1f / 4f;

        private ContainerFlexible  mainContainer;

        private Label              titleLabel;
        private Label              configNameLabel;
        private Label              flowEntryTitleLabel;
        Checkbox                   tryoutCheckbox;
        private ContainerFlexible  configSettingsContainer; // This field will be set to the currently selected focus config's settings container
        private ButtonLabel        backButton;
        private ButtonLabel        closeButton;

        public Screen() {

            super(null);
        }

        @Override
        protected void createGui() {

            ContainerFlexible rootContainer = new ContainerClippingImpl();
            setRootWidget(rootContainer);

            mainContainer = new ContainerClippingImpl();
            rootContainer.addWidgets(mainContainer);

            titleLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.title"));
            configNameLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".focusConfigName." + editedFlowEntry.getFocusConfig().getInternalName()));
            flowEntryTitleLabel = new LabelImpl(editedFlowEntry.getTitle().isEmpty() ? I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.noEntryTitle") : '"' + editedFlowEntry.getTitle() + '"');
            tryoutCheckbox = new CheckboxImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.tryout"), true);
            configSettingsContainer = FocusConfigGuiImplsFactory.createSettingsContainer(editedFlowEntry);

            backButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.back"), new FilteredButtonHandler(MouseButton.LEFT,
                    // When someone exits this GUI via the back button, he needs to see the focus flow screen again
                    (b, mb) -> GuiStateMachine.transitionToState(new EditFocusFlowGuiState())));
            closeButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".editFocusFlowEntry.close"), (b, mb) -> close());

            mainContainer.addWidgets(titleLabel, configNameLabel, flowEntryTitleLabel, tryoutCheckbox, configSettingsContainer, backButton, closeButton);

            // ----- Revalidation -----

            rootContainer
                    .appendLayoutManager(c -> {
                        mainContainer.setBounds((int) (rootContainer.getWidth() * (1 - H_RATIO) - MARGIN), MARGIN,
                                (int) (rootContainer.getWidth() * H_RATIO), rootContainer.getHeight() - 2 * MARGIN);
                    });

            mainContainer
                    .appendLayoutManager(new AlignLayout(Axis.X, PADDING))
                    .appendLayoutManager(new SqueezeLayout(Axis.Y, PADDING, 4)
                            .keep(titleLabel, configNameLabel, flowEntryTitleLabel, tryoutCheckbox, backButton, closeButton)
                            .weight(1, configSettingsContainer))
                    .appendLayoutManager(c -> {
                        // Center the title label
                        titleLabel.setX( (mainContainer.getWidth() - titleLabel.getWidth()) / 2);

                        // The settings container and the close button should span the whole main container
                        configSettingsContainer.setWidth(mainContainer.getWidth() - 2 * PADDING);
                        backButton.setWidth(mainContainer.getWidth() - 2 * PADDING);
                        closeButton.setWidth(mainContainer.getWidth() - 2 * PADDING);
                    });
        }

        @Override
        public void drawBackground() {

            // We can simply query these coordinates because the main container is a direct child of the root container, meaning that the coordinates are absolute
            drawRect(mainContainer.getX(), mainContainer.getY(), mainContainer.getX() + mainContainer.getWidth(), mainContainer.getY() + mainContainer.getHeight(), 0x80101010);
        }

    }

}
