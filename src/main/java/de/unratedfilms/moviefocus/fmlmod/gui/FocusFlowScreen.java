
package de.unratedfilms.moviefocus.fmlmod.gui;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import net.minecraft.client.resources.I18n;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.integration.BasicScreen;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.layouts.SqueezeLayout;
import de.unratedfilms.guilib.widgets.model.Button.FilteredButtonHandler;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.model.Scrollbar;
import de.unratedfilms.guilib.widgets.model.TextField;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerAdjustingImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerScrollableImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ScrollbarImpl;
import de.unratedfilms.guilib.widgets.view.impl.TextFieldImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigRegistry;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlow.FocusFlowEntry;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlowRunner;

public class FocusFlowScreen extends BasicScreen {

    private static final int             MARGIN  = 5;
    private static final int             PADDING = 5;

    private ContainerFlexible            mainContainer;

    private Label                        titleLabel;
    private ButtonLabel                  closeButton;
    private ButtonLabel                  startRunningButton;
    private Container                    addFlowEntryButtonsContainer;

    private ContainerFlexible            flowListContainer;
    private EditableList<FocusFlowEntry> flowList;

    public FocusFlowScreen() {

        super(null);
    }

    @Override
    protected void createGui() {

        ContainerFlexible rootContainer = new ContainerClippingImpl();
        setRootWidget(rootContainer);

        mainContainer = new ContainerClippingImpl();
        rootContainer.addWidgets(mainContainer);

        titleLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".focusFlow.title"));
        startRunningButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".focusFlow.startRunning"), new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> {
            FocusFlowRunner.loadAndStartRunning();
            close();
        }));
        closeButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".focusFlow.close"), new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> close()));
        mainContainer.addWidgets(titleLabel, startRunningButton, closeButton);

        addFlowEntryButtonsContainer = new ContainerAdjustingImpl();
        for (Class<? extends FocusConfig> fct : FocusConfigRegistry.getAllTypes()) {
            addFlowEntryButtonsContainer.addWidgets(new ButtonLabelImpl("+ " + I18n.format("gui." + MOD_ID + ".focusConfigName." + FocusConfig.getInternalName(fct)),
                    new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> flowList.addElement(new FocusFlowEntry(FocusConfigRegistry.newInstance(fct))))));
        }
        mainContainer.addWidgets(addFlowEntryButtonsContainer);

        Scrollbar flowListScrollbar = new ScrollbarImpl(2 * PADDING);
        flowListContainer = new ContainerScrollableImpl(flowListScrollbar, 10);

        flowList = new EditableList<>(4, FocusFlow.sequence, this::createWidgetFromFlowEntry);
        flowListContainer.addWidgets(flowList);
        mainContainer.addWidgets(flowListContainer);

        // ----- Revalidation -----

        rootContainer
                .appendLayoutManager(c -> {
                    mainContainer.setBounds(MARGIN, MARGIN, rootContainer.getWidth() - 2 * MARGIN, rootContainer.getHeight() - 2 * MARGIN);
                });

        mainContainer
                .appendLayoutManager(c -> {
                    // Title label at the top, main buttons at the bottom
                    titleLabel.setPosition( (mainContainer.getWidth() - titleLabel.getWidth()) / 2, PADDING);
                    startRunningButton.setBounds(mainContainer.getWidth() / 2 - 2 - 120, mainContainer.getHeight() - PADDING - 20, 120, 20);
                    closeButton.setBounds(mainContainer.getWidth() / 2 + 2, mainContainer.getHeight() - PADDING - 20, 120, 20);

                    // Add flow entry buttons container above the main buttons, with a gap of 10 pixels
                    addFlowEntryButtonsContainer.setPosition(PADDING, closeButton.getY() - 10 - addFlowEntryButtonsContainer.getHeight());

                    // The scrollable flow list container should use up all the remaining space between the title label and the button container, with a gap of 10 pixels at the top and bottom
                    flowListContainer.setBounds(PADDING, titleLabel.getBottom() + 10, mainContainer.getWidth() - 2 * PADDING, addFlowEntryButtonsContainer.getY() - 10 - (titleLabel.getBottom() + 10));
                });

        addFlowEntryButtonsContainer
                .appendLayoutManager(c -> addFlowEntryButtonsContainer.getWidgets().stream().map(w -> (ButtonLabel) w)
                        .forEach(button -> button.setWidth(mc.fontRenderer.getStringWidth(button.getLabel()) + 10)))
                .appendLayoutManager(new FlowLayout(Axis.X, 0, 4));

        flowListContainer
                .appendLayoutManager(c -> {
                    flowListScrollbar.setPosition(flowListContainer.getWidth() - flowListScrollbar.getWidth(), 0);

                    flowList.setWidth(flowListScrollbar.getX() - 5);
                });
    }

    private WidgetFlexible createWidgetFromFlowEntry(FocusFlowEntry flowEntry) {

        ButtonLabel editButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".focusFlow.editEntry"), (b, m) -> {});
        editButton.setHandler(new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> mc.displayGuiScreen(new FocusFlowEntryScreen(flowEntry))));

        TextField titleTextField = new TextFieldImpl().setText(flowEntry.getTitle());
        titleTextField.setHandler((textField, typedChar, keyCode) -> flowEntry.setTitle(titleTextField.getText()));

        Label nameLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".focusConfigName." + flowEntry.getFocusConfig().getInternalName()));

        ContainerFlexible container = new ContainerClippingImpl(editButton, titleTextField, nameLabel);
        container.appendLayoutManager(c -> {
            editButton.setWidth(mc.fontRenderer.getStringWidth(editButton.getLabel()) + 10);
        });
        container.appendLayoutManager(new SqueezeLayout(Axis.X, 0, 8)
                .keep(editButton, nameLabel)
                .weight(1, titleTextField));

        return container;
    }

    @Override
    public void drawBackground() {

        // We can simply query these coordinates because the main container is a direct child of the root container, meaning that the coordinates are absolute
        drawRect(mainContainer.getX(), mainContainer.getY(), mainContainer.getX() + mainContainer.getWidth(), mainContainer.getY() + mainContainer.getHeight(), 0x80101010);
    }

}
