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
package fr.noony.handstats.team.hmi;

import javafx.geometry.Dimension2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Window;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class CustomPopup extends Popup {

    private static final Dimension2D DEFAULT_POP_UP_DIMENSION = new Dimension2D(670, 630);

    private final Screen popupScreen;
    private final Rectangle popupTranslucidBackground;
    private final Rectangle popupBackground;

    private double width = DEFAULT_POP_UP_DIMENSION.getWidth();
    private double height = DEFAULT_POP_UP_DIMENSION.getHeight();

    /**
     * Creates a pop up containg the team stat page
     *
     * @param fxmlName
     */
    public CustomPopup(String fxmlName) {
        super();
        popupTranslucidBackground = new Rectangle(800, 600);
        popupTranslucidBackground.setFill(Color.GREY);
        popupTranslucidBackground.setOpacity(0.7);
        popupBackground = new Rectangle(DEFAULT_POP_UP_DIMENSION.getWidth(), DEFAULT_POP_UP_DIMENSION.getHeight());
        popupBackground.setFill(Color.GREY);
        popupScreen = new Screen(fxmlName);
        getContent().add(popupTranslucidBackground);
        getContent().add(popupBackground);
        getContent().add(popupScreen.getNode());
    }

    @Override
    public void show(Window ownerWindow, double anchorX, double anchorY) {
        super.show(ownerWindow, anchorX, anchorY);
        update();
    }

    private void update() {
        double frameWidth = getOwnerWindow().getWidth();
        double frameHeight = getOwnerWindow().getHeight();
        System.err.println(" my window is " + getOwnerWindow());
        System.err.println(" WINDOW SIZE :: " + frameWidth + "x" + frameHeight);
        System.err.println("position ::" + getOwnerWindow().getX() + "x" + getOwnerWindow().getY());
        popupTranslucidBackground.setWidth(frameWidth);
        popupTranslucidBackground.setHeight(frameHeight);
        popupTranslucidBackground.setX(0);
        popupTranslucidBackground.setY(0);
        double innerX = (frameWidth - width) / 2.0;
        double innerY = (frameHeight - height) / 2.0;
        popupBackground.setX(innerX);
        popupBackground.setY(innerY);
        popupBackground.setWidth(width);
        popupBackground.setHeight(height);
        popupScreen.getNode().setTranslateX(innerX);
        popupScreen.getNode().setTranslateY(innerY);
        setX(0.0);
        setY(0.0);
    }

    public Screen getPopupScreen() {
        return popupScreen;
    }

    public void loadParameters(Object... params) {
        popupScreen.loadParameters(params);
    }

    public FXController getController() {
        return popupScreen.getController();
    }

    public final void setSize(double newWidth, double newHeight) {
        width = newWidth;
        height = newHeight;
        update();
    }

}
