
package de.unratedfilms.moviefocus.fmlmod.gui;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import de.unratedfilms.guilib.extra.CloseScreenButtonHandler;
import de.unratedfilms.guilib.extra.FlowLayoutManager;
import de.unratedfilms.guilib.extra.FlowLayoutManager.Axis;
import de.unratedfilms.guilib.integration.BasicScreen;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.model.Checkbox;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Dropdown;
import de.unratedfilms.guilib.widgets.model.Dropdown.GenericUserObjectOption;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.CheckboxImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.DropdownLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigManager;
import de.unratedfilms.moviefocus.fmlmod.keys.KeyBindings;

public class SettingsScreen extends BasicScreen {

    private static final int                                       MARGIN  = 5;
    private static final int                                       PADDING = 5;
    private static final float                                     H_RATIO = 1f / 4f;

    private ContainerFlexible                                      mainContainer;

    private Label                                                  titleLabel;
    private ButtonLabel                                            closeButton;
    private ContainerFlexible                                      contentContainer;

    private Checkbox                                               activatedCheckbox;
    private Label                                                  configDropdownLabel;
    private Dropdown<GenericUserObjectOption<FocusConfig, String>> configDropdown;
    private ContainerFlexible                                      settingsContainer;  // This field will be set to the currently selected focus config's settings container

    public SettingsScreen(GuiScreen parent) {

        super(parent);
    }

    @Override
    protected void createGui() {

        ContainerFlexible rootContainer = new ContainerClippingImpl();
        setRootWidget(rootContainer);

        mainContainer = new ContainerClippingImpl();
        rootContainer.addWidgets(mainContainer);

        titleLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".settings.title"));
        closeButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".settings.close"), new CloseScreenButtonHandler(this));
        mainContainer.addWidgets(titleLabel, closeButton);

        contentContainer = new ContainerClippingImpl();
        mainContainer.addWidgets(contentContainer);

        String toggleActivityKey = GameSettings.getKeyDisplayString(KeyBindings.toggleActivity.getKeyCode());
        activatedCheckbox = new CheckboxImpl(I18n.format("gui." + MOD_ID + ".settings.activated", toggleActivityKey), FocusConfigManager.INSTANCE.isActivated());
        activatedCheckbox.setHandler((Checkbox checkbox, boolean checked) -> {
            FocusConfigManager.INSTANCE.setActivated(checked);
        });
        contentContainer.addWidgets(activatedCheckbox);

        configDropdownLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".settings.config"));
        List<GenericUserObjectOption<FocusConfig, String>> configOptions = new ArrayList<>();
        for (FocusConfig config : FocusConfigManager.INSTANCE.getAll()) {
            configOptions.add(new GenericUserObjectOption<>(config, config.getTitle()));
        }
        configDropdown = new DropdownLabelImpl<>(configOptions);
        configDropdown.setHandler((Dropdown<GenericUserObjectOption<FocusConfig, String>> dropdown, GenericUserObjectOption<FocusConfig, String> option) -> {
            FocusConfigManager.INSTANCE.setSelected(option.getUserObject());
            updateSettingsContainer(); // Switch the settings container if another focus config has been selected by the player
        });
        contentContainer.addWidgets(configDropdownLabel, configDropdown);

        // Initially add the currently selected focus config's settings container
        updateSettingsContainer();

        // ----- Revalidation -----

        rootContainer
                .appendLayoutManager(() -> {
                    mainContainer.setBounds((int) (rootContainer.getWidth() * (1 - H_RATIO) - MARGIN), MARGIN,
                            (int) (rootContainer.getWidth() * H_RATIO), rootContainer.getHeight() - 2 * MARGIN);
                });

        mainContainer
                .appendLayoutManager(() -> {
                    // Title label at the top, close button at the bottom
                    titleLabel.setPosition( (mainContainer.getWidth() - titleLabel.getWidth()) / 2, PADDING);
                    closeButton.setBounds(PADDING, mainContainer.getHeight() - PADDING - 20, mainContainer.getWidth() - 2 * PADDING, 20);

                    // The scrollable container should use up all the available space between the title label and the close button, with a gap of 10 pixels at the top and bottom
                    int titleLabelBottom = titleLabel.getY() + titleLabel.getHeight();
                    contentContainer.setBounds(PADDING, titleLabelBottom + 10, mainContainer.getWidth() - 2 * PADDING, closeButton.getY() - 10 - (titleLabelBottom + 10));
                });

        contentContainer
                .appendLayoutManager(() -> {
                    configDropdown.setWidth(contentContainer.getWidth());
                })
                .appendLayoutManager(new FlowLayoutManager(contentContainer, Axis.Y, 0, 0, 5))
                .appendLayoutManager(() -> {
                    // The settings container should use up all the available space below the config dropdown (below which it has flowed)
                    settingsContainer.setSize(contentContainer.getWidth(), contentContainer.getHeight() - settingsContainer.getY());
                });
    }

    private void updateSettingsContainer() {

        if (settingsContainer != null) {
            contentContainer.removeWidgets(settingsContainer);
        }

        settingsContainer = FocusConfigManager.INSTANCE.getSelected().createSettingsContainer();
        contentContainer.addWidgets(settingsContainer);
    }

    @Override
    public void drawBackground() {

        // We can simply query these coordinates because the main container is a direct child of the root container, meaning that the coordinates are absolute
        drawRect(mainContainer.getX(), mainContainer.getY(), mainContainer.getX() + mainContainer.getWidth(), mainContainer.getY() + mainContainer.getHeight(), 0x80101010);
    }

}
