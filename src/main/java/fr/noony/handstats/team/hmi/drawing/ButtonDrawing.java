/*
 * Copyright (C) 2015 Arnaud
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.noony.handstats.team.hmi.drawing;

import java.awt.Dimension;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Arnaud
 */
public class ButtonDrawing {

    public static final double DEFAULT_WRAPPING_WIDTH = 0.9;
    public static final Dimension DEFAULT_BUTTON_SIZE = new Dimension(120, 75);
    public static final String BUTTON_CLICKED = "BUTTON_CLICKED";

    public enum ButtonInnerState {

        SELECTED, PRESSED_IN, PRESSED_OUT, DISABLED, ENABLED, HIGHLIGHTED, HIDDEN, DISABLED_SELECTED
    }
    //
    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);

    private final Group mainNode;
    private final Text buttonText;
    //
    private static final Logger LOG = Logger.getLogger(ButtonDrawing.class.getName());
    private static final Level LOG_LEVEL = Level.FINEST;

    //
    private double sizeX, sizeY;
    private ButtonInnerState myState;
    //graphical components
    private Rectangle buttonBackground, textClip, foreground;
    private Polygon buttonLowerBorder;
    private Polygon buttonUpperBorder;
    //borders
    private double borderSize;
    //
    private final String eventNameOnClicked;

    public ButtonDrawing(String text, String eventName) {
        propertyChangeSupport = new PropertyChangeSupport(ButtonDrawing.this);
        lookupContents.add(propertyChangeSupport);
        eventNameOnClicked = eventName;
        //
        mainNode = new Group();
        buttonText = new Text(text);
        buttonText.setFont(new Font(24));
        buttonText.setFontSmoothingType(FontSmoothingType.LCD);
        initDrawing();
    }

    public ButtonDrawing(String text) {
        this(text, BUTTON_CLICKED);
    }

    private void initDrawing() {
        buttonBackground = new Rectangle();
        textClip = new Rectangle();
        foreground = new Rectangle();
        buttonLowerBorder = new Polygon();
        buttonUpperBorder = new Polygon();
        //
        mainNode.getChildren().add(buttonBackground);
        mainNode.getChildren().add(buttonUpperBorder);
        mainNode.getChildren().add(buttonLowerBorder);
        mainNode.getChildren().add(buttonText);
        mainNode.getChildren().add(foreground);
        borderSize = 4.0;
        foreground.setOpacity(0.0);
        createInteractivity();
        setSize(DEFAULT_BUTTON_SIZE.getWidth(), DEFAULT_BUTTON_SIZE.getHeight());
        adjustLabelPosition();
        calculatePolygones();
        setInnerState(ButtonInnerState.ENABLED);
    }

    private void adjustLabelPosition() {
        textClip.setWidth(sizeX);
        textClip.setHeight(sizeY);
        textClip.setVisible(false);
        textClip.setLayoutX(0.0);
        textClip.setLayoutY(-sizeY / 2.0);
        //
        buttonText.setTextAlignment(TextAlignment.CENTER);
        buttonText.setTextOrigin(VPos.CENTER);
        buttonText.setClip(textClip);
        buttonText.setWrappingWidth(sizeX * DEFAULT_WRAPPING_WIDTH);
        buttonText.setLayoutX(sizeX / 2.0 - buttonText.getLayoutBounds().getWidth() / 2.0);
        buttonText.setLayoutY(sizeY / 2.0);
    }

    private void calculatePolygones() {
        Double[] uB = {
            0.0, 0.0,
            sizeX - 1.0, 0.0,
            sizeX - borderSize, borderSize,
            borderSize, borderSize,
            borderSize, sizeY - borderSize,
            0.0, sizeY,
            0.0, 0.0
        };
        //
        buttonUpperBorder.getPoints().addAll(uB);
        Double[] lB = {
            sizeX, sizeY,
            0.0, sizeY,
            borderSize, sizeY - borderSize,
            sizeX - borderSize, sizeY - borderSize,
            sizeX - borderSize, borderSize,
            sizeX, 0.0,
            sizeX, sizeY
        };
        buttonLowerBorder.getPoints().setAll(lB);
        //
    }

    private void createInteractivity() {
        foreground.setOnMousePressed((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "mouse event {0} on object {1}", new Object[]{event, this});
            processMousePressed();
        });
        foreground.setOnMouseReleased((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "mouse event {0} on object {1}", new Object[]{event, this});
            processMouseReleased();
        });
        foreground.setOnMouseEntered((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "mouse event {0} on object {1}", new Object[]{event, this});
            processMouseEntered();
        });
        foreground.setOnMouseExited((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "mouse event {0} on object {1}", new Object[]{event, this});
            processMouseExited();
        });
    }

    private void processMousePressed() {
        switch (myState) {
            case HIGHLIGHTED:
                setInnerState(ButtonInnerState.PRESSED_IN);
                break;
            default:
                //nothing
                break;
        }
    }

    private void processMouseReleased() {
        switch (myState) {
            case PRESSED_IN:
                setInnerState(ButtonInnerState.ENABLED);
                propertyChangeSupport.firePropertyChange(eventNameOnClicked, null, this);
                break;
            case PRESSED_OUT:
                setInnerState(ButtonInnerState.ENABLED);
                break;
            default:
                //nothing
                break;
        }
    }

    private void processMouseEntered() {
        switch (myState) {
            case ENABLED:
                setInnerState(ButtonInnerState.HIGHLIGHTED);
                break;
            case PRESSED_OUT:
                setInnerState(ButtonInnerState.PRESSED_IN);
                break;
            default:
                //nothing
                break;
        }
    }

    private void processMouseExited() {
        switch (myState) {
            case HIGHLIGHTED:
                setInnerState(ButtonInnerState.ENABLED);
                break;
            case PRESSED_IN:
                setInnerState(ButtonInnerState.PRESSED_OUT);
                break;
            default:
                //nothing
                break;
        }
    }

    public final Node getNode() {
        return mainNode;
    }

    public Lookup getLookup() {
        return alookup;
    }

    public final void setText(String text) {
        buttonText.setText(text);
        adjustLabelPosition();
    }

    public final void setInnerState(ButtonInnerState state) {
        myState = state;
        switch (myState) {
            case DISABLED:
                buttonBackground.setFill(JFXDrawingConstants.CDS_DARK_GREY);
                buttonLowerBorder.setFill(JFXDrawingConstants.CDS_GREY);
                buttonUpperBorder.setFill(JFXDrawingConstants.CDS_GREY);
                buttonText.setFill(JFXDrawingConstants.CDS_GREY);
                break;
            case ENABLED:
                buttonBackground.setFill(JFXDrawingConstants.CDS_DARK_GREY);
                buttonLowerBorder.setFill(JFXDrawingConstants.CDS_GREY);
                buttonUpperBorder.setFill(JFXDrawingConstants.CDS_VERY_LIGHT_GREY);
                buttonText.setFill(JFXDrawingConstants.CDS_WHITE);
                break;
            case PRESSED_IN:
                buttonBackground.setFill(JFXDrawingConstants.CDS_VERY_DARK_GREY);
                buttonLowerBorder.setFill(JFXDrawingConstants.CDS_VERY_LIGHT_GREY);
                buttonUpperBorder.setFill(JFXDrawingConstants.CDS_GREY);
                buttonText.setFill(JFXDrawingConstants.CDS_LIGHT_BLUE);
                break;
            case PRESSED_OUT:
                buttonBackground.setFill(JFXDrawingConstants.CDS_DARK_GREY);
                buttonLowerBorder.setFill(JFXDrawingConstants.CDS_AMBER);
                buttonUpperBorder.setFill(JFXDrawingConstants.CDS_AMBER);
                buttonText.setFill(JFXDrawingConstants.CDS_AMBER);
                break;
            case SELECTED:
                buttonBackground.setFill(JFXDrawingConstants.CDS_LIGHT_GREY);
                buttonLowerBorder.setFill(JFXDrawingConstants.CDS_GREY);
                buttonUpperBorder.setFill(JFXDrawingConstants.CDS_VERY_LIGHT_GREY);
                buttonText.setFill(JFXDrawingConstants.CDS_LIGHT_GREEN);
                break;
            case HIGHLIGHTED:
                buttonBackground.setFill(JFXDrawingConstants.CDS_DARK_GREY);
                buttonLowerBorder.setFill(JFXDrawingConstants.CDS_GREY);
                buttonUpperBorder.setFill(JFXDrawingConstants.CDS_VERY_LIGHT_GREY);
                buttonText.setFill(JFXDrawingConstants.CDS_LIGHT_BLUE);
                break;
            default:
                buttonBackground.setFill(JFXDrawingConstants.CDS_MAGENTA);
                buttonLowerBorder.setFill(JFXDrawingConstants.CDS_MAGENTA);
                buttonUpperBorder.setFill(JFXDrawingConstants.CDS_MAGENTA);
                buttonText.setFill(JFXDrawingConstants.CDS_MAGENTA);
                break;
        }
    }

    public final void setPosition(double xPos, double yPos) {
        mainNode.setTranslateX(xPos);
        mainNode.setTranslateY(yPos);
    }

    public final void setSize(double newWidth, double newHeight) {
        sizeX = newWidth;
        sizeY = newHeight;
        buttonBackground.setWidth(sizeX);
        buttonBackground.setHeight(sizeY);
        foreground.setWidth(sizeX);
        foreground.setHeight(sizeY);
        calculatePolygones();
        adjustLabelPosition();
    }

    public final void setVisible(boolean visibility) {
        mainNode.setVisible(visibility);
    }

}
