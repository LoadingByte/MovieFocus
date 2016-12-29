
package de.unratedfilms.moviefocus.fmlmod.gui.states;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.core.Widget;
import de.unratedfilms.guilib.core.WidgetFlexible;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.layouts.SqueezeLayout;
import de.unratedfilms.guilib.widgets.model.Button.FilteredButtonHandler;
import de.unratedfilms.guilib.widgets.model.ButtonLabel;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.moviefocus.fmlmod.util.ListUtils;
import de.unratedfilms.moviefocus.fmlmod.util.ListUtils.Direction;

/**
 * Note that this widget is half-rigid and changes its own height depending on how many elements it contains.
 */
public class EditableList<E> extends ContainerClippingImpl {

    // Note that the model should never change while this widget is in use!
    private final List<E>                     model;
    private final Function<E, WidgetFlexible> elemToWidget;

    public EditableList(int gap, List<E> model, Function<E, WidgetFlexible> elemToWidget) {

        this.model = model;
        this.elemToWidget = elemToWidget;

        for (E element : model) {
            addWidgets(new ElementContainer(element));
        }

        // Propagate the width to the individual element containers, and set their heights to 20
        appendLayoutManager(c -> {
            for (Widget widget : getWidgets()) {
                @SuppressWarnings ("unchecked")
                ElementContainer elemContainer = (ElementContainer) widget;
                elemContainer.setSize(getWidth(), 20);
            }
        });

        // Flow the element containers vertically
        appendLayoutManager(new FlowLayout(Axis.Y, 0, gap));

        // Adjust this container's height
        appendLayoutManager(c -> setHeight(getWidgets().stream().mapToInt(Widget::getBottom).max().orElse(0)));
    }

    public void addElement(E element) {

        model.add(element);
        addWidgets(new ElementContainer(element));
    }

    @SuppressWarnings ("unchecked")
    public void removeElement(E element) {

        model.remove(element);
        getWidgets().stream().filter(c -> ((ElementContainer) c).element == element).findFirst().ifPresent(this::removeWidgets);
    }

    private void moveElementContainer(ElementContainer toMove, Direction direction) {

        int toMoveIdx = getWidgets().indexOf(toMove);

        // Model
        ListUtils.moveElementIfPossible(model, toMoveIdx, direction);

        // Widget list
        List<Widget> widgets = new ArrayList<>(getWidgets());
        clearWidgets();
        ListUtils.moveElementIfPossible(widgets, toMoveIdx, direction);
        addWidgets(widgets.toArray(new Widget[0]));
    }

    private class ElementContainer extends ContainerClippingImpl {

        private final E              element;

        private final ButtonLabel    removeButton, moveUpButton, moveDownButton;
        private final WidgetFlexible customWidget;

        private ElementContainer(E element) {

            this.element = element;

            removeButton = new ButtonLabelImpl("x", new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> removeElement(element)));
            moveUpButton = new ButtonLabelImpl("^", new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> moveElementContainer(this, Direction.UP)));
            moveDownButton = new ButtonLabelImpl("v", new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> moveElementContainer(this, Direction.DOWN)));

            customWidget = elemToWidget.apply(element);

            addWidgets(removeButton, moveUpButton, moveDownButton, customWidget);

            // ----- Revalidation -----

            appendLayoutManager(c -> {
                removeButton.setSize(20, 20);
                moveUpButton.setSize(20, 20);
                moveDownButton.setSize(20, 20);
                customWidget.setHeight(20);
            });
            appendLayoutManager(new SqueezeLayout(Axis.X, 0, 2)
                    // Give the remaining width to the custom widget
                    .keep(removeButton, moveUpButton, moveDownButton)
                    .weight(1, customWidget));
        }

    }

}
