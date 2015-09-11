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
 * @author Arnaud
 */
public class ExitPopUp extends Popup {

    private static final Dimension2D DEFAULT_POP_UP_DIMENSION = new Dimension2D(670, 680);

    private final Screen popupScreen;
    private final Rectangle popupTranslucidBackground;
    private final Rectangle popupBackground;

    private double width = DEFAULT_POP_UP_DIMENSION.getWidth();
    private double height = DEFAULT_POP_UP_DIMENSION.getHeight();
    private final MainScreensController screensController;

    public ExitPopUp(Window parent, MainScreensController mainScreensController) {
        super();
        popupTranslucidBackground = new Rectangle(1200, 800);
        popupTranslucidBackground.setFill(Color.GREY);
        popupTranslucidBackground.setOpacity(0.7);
        popupBackground = new Rectangle(DEFAULT_POP_UP_DIMENSION.getWidth(), DEFAULT_POP_UP_DIMENSION.getHeight());
        popupBackground.setFill(Color.GREY);
        popupScreen = new Screen("exit");
        getContent().add(popupTranslucidBackground);
        getContent().add(popupBackground);
        getContent().add(popupScreen.getNode());
        screensController = mainScreensController;
        popupScreen.loadParameters(ExitPopUp.this, screensController);
    }

    private void update() {
        double frameWidth = getOwnerWindow().getWidth();
        double frameHeight = getOwnerWindow().getHeight();
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

    @Override
    protected void show() {
        super.show();
        update();
    }

    @Override
    public void show(Window owner) {
        super.show(owner);
        update();
    }

}
