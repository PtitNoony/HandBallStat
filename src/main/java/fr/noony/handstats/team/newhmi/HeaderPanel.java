/*
 * Copyright (C) 2015 HAMON-KEROMEN A.
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
package fr.noony.handstats.team.newhmi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * @author Arnaud
 */
public class HeaderPanel implements PropertyChangeListener {

    public static final String SWITCH_FULL_SCREEN_MODE = "switchFullScreenMode";

    private static final double MAX_CONTROL_WIDTH_RATIO = 3;

    private static HeaderPanel instance;

    private final PropertyChangeSupport propertyChangeSupport;
    //
    private final Group mainNode;
    private final Rectangle background;
    private final Group screenControls;
    private final Button fullScreenButton;

    private double width = 0.0;
    private double controlSize = 1;
    private final List<Control> controls = new LinkedList<>();

    public static HeaderPanel getInstance() {
        if (instance == null) {
            instance = new HeaderPanel();
        }
        return instance;
    }

    private HeaderPanel() {
        propertyChangeSupport = new PropertyChangeSupport(HeaderPanel.this);
        mainNode = new Group();
        background = new Rectangle();
        fullScreenButton = new Button("F");
        screenControls = new Group();
        initHeaderPanel();
    }

    private void initHeaderPanel() {
        background.setHeight(FXScreenUtils.UPPER_BORDER_HEIGHT);
        //
        fullScreenButton.setPrefSize(FXScreenUtils.UPPER_BORDER_HEIGHT, FXScreenUtils.UPPER_BORDER_HEIGHT);
        fullScreenButton.setFont(new Font(FXScreenUtils.getButtonFontSize(FXScreenUtils.UPPER_BORDER_HEIGHT)));
        fullScreenButton.setOnAction(event -> {
            //TODO: log event
            propertyChangeSupport.firePropertyChange(SWITCH_FULL_SCREEN_MODE, null, null);
        });
        mainNode.getChildren().add(background);
        mainNode.getChildren().add(fullScreenButton);
        mainNode.getChildren().add(screenControls);
        updateHeaderPanelWidth();
    }

    public Node getNode() {
        return mainNode;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (FXScreenUtils.STAGE_DIMENSION_CHANGED.equals(evt.getPropertyName())) {
            setWidth((double) evt.getOldValue());
            updateHeaderPanelWidth();
        }
    }

    private void updateHeaderPanelWidth() {
        background.setWidth(width);
        fullScreenButton.setTranslateX(width - FXScreenUtils.UPPER_BORDER_HEIGHT);
        controlSize = width / Math.max(MAX_CONTROL_WIDTH_RATIO, controls.size());
        controls.stream().forEach(control -> {
            control.setMinSize(controlSize, FXScreenUtils.UPPER_BORDER_HEIGHT);
            control.setMaxSize(controlSize, FXScreenUtils.UPPER_BORDER_HEIGHT);
            control.setTranslateX(controlSize * controls.indexOf(control));
        });
    }

    public boolean cleanControls() {
        controls.clear();
        return removeControls();
    }

    public void addControl(Control control) {
        if (!controls.contains(control)) {
            controls.add(control);
            String fontStyle = "-fx-font: " + FXScreenUtils.getButtonFontSize(FXScreenUtils.UPPER_BORDER_HEIGHT) + "px Courrier;";
            control.setStyle(fontStyle);
            screenControls.getChildren().add(control);
            updateHeaderPanelWidth();
        }
    }

    public void removeControl(Control control) {
        if (controls.contains(control)) {
            controls.remove(control);
            screenControls.getChildren().remove(control);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private boolean removeControls() {
        List children = screenControls.getChildren();
        screenControls.getChildren().removeAll(children);
        return true;
    }

    private void setWidth(double newWidth) {
        width = newWidth;
    }

}
