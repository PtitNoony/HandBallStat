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
package fr.noony.handstats.team.hmi.stats;

import fr.noony.handstats.team.hmi.Screen;
import javafx.geometry.Dimension2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Window;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class StatPopup extends Popup {

    private static final Dimension2D DEFAULT_POP_UP_DIMENSION = new Dimension2D(670, 630);

    private final Screen popupScreen;
    private final Rectangle popupTranslucidBackground;
    private final Rectangle popupBackground;

    public StatPopup() {
        super();
        popupTranslucidBackground = new Rectangle(800, 600);
        popupTranslucidBackground.setFill(Color.GREY);
        popupTranslucidBackground.setOpacity(0.7);
        popupBackground = new Rectangle(DEFAULT_POP_UP_DIMENSION.getWidth(), DEFAULT_POP_UP_DIMENSION.getHeight());
        popupBackground.setFill(Color.GREY);
        popupScreen = new Screen("StatPopupPage");
        getContent().add(popupTranslucidBackground);
        getContent().add(popupBackground);
        getContent().add(popupScreen.getNode());
    }

    @Override
    public void show(Window ownerWindow, double anchorX, double anchorY) {
        super.show(ownerWindow, anchorX, anchorY);
        double width = getOwnerWindow().getWidth();
        double height = getOwnerWindow().getHeight();
        System.err.println(" width :: " + width);
        System.err.println(" height :: " + height);
        popupTranslucidBackground.setWidth(width);
        popupTranslucidBackground.setHeight(height);
        double innerX = (width - DEFAULT_POP_UP_DIMENSION.getWidth()) / 2.0;
        double innerY = (height - DEFAULT_POP_UP_DIMENSION.getHeight()) / 2.0;
        popupBackground.setX(innerX);
        popupBackground.setY(innerY);
        popupScreen.getNode().setTranslateX(innerX);
        popupScreen.getNode().setTranslateY(innerY);
    }

    public Screen getPopupScreen() {
        return popupScreen;
    }

    public void loadParameters(Object... params) {
        popupScreen.loadParameters(params);
    }

}
