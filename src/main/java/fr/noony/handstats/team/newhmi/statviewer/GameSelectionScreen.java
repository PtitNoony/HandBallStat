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
package fr.noony.handstats.team.newhmi.statviewer;

import fr.noony.handstats.team.newhmi.FXScreen;
import fr.noony.handstats.team.newhmi.FXScreenUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Arnaud
 */
public class GameSelectionScreen extends FXScreen {

    private final static double UPPER_LABEL_RATIO = 0.08;
    private final static double GAME_LIST_RATIO = 0.5;

    private final Label intructionLabel;
    private final Line verticalSeparation;
    //for debug
    private final Rectangle background;

    //for calculations
    double intructionLabelWidth;
    double intructionLabelHeight;

    public GameSelectionScreen() {
        super();
        background = new Rectangle();
        intructionLabel = new Label("Choix du match à analyser");
        verticalSeparation = new Line();
        Platform.runLater(() -> initGameSelectionScreen());
    }

    private void initGameSelectionScreen() {
        //debug
        background.setFill(Color.BEIGE);
        //
        intructionLabel.setTextAlignment(TextAlignment.RIGHT);
        intructionLabel.setWrapText(true);
        intructionLabel.setFont(new Font(36));
        //
        verticalSeparation.setStroke(Color.BLACK);
        verticalSeparation.setStrokeLineCap(StrokeLineCap.ROUND);
//        verticalSeparation.setStrokeType(StrokeType.INSIDE);
        verticalSeparation.setStrokeWidth(8.0);
        //
        addNode(background);
        addNode(intructionLabel);
        addNode(verticalSeparation);

    }

    @Override
    public void updateSize(double newWidth, double newHeight) {
        background.setWidth(newWidth);
        background.setHeight(newHeight);
        //
        intructionLabelWidth = (newWidth - 3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING) / 2.0;
        intructionLabelHeight = (newHeight - 3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING) * UPPER_LABEL_RATIO;
        intructionLabel.setMinSize(intructionLabelWidth, intructionLabelHeight);
        intructionLabel.setMaxSize(intructionLabelWidth, intructionLabelHeight);
        intructionLabel.setTranslateX(2.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + intructionLabelWidth);
        intructionLabel.setTranslateY(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        int fontSize = (int) (intructionLabelHeight * 0.8);
        String fontStyle = "-fx-font: " + fontSize + "px Tahoma;";
        intructionLabel.setStyle(fontStyle);
        //
        verticalSeparation.setStartX((FXScreenUtils.DEFAULT_ELEMENT_SPACING * 1.5) + intructionLabelWidth);
        verticalSeparation.setStartY((FXScreenUtils.DEFAULT_ELEMENT_SPACING * 0.5));
        verticalSeparation.setEndX((FXScreenUtils.DEFAULT_ELEMENT_SPACING * 1.5) + intructionLabelWidth);
        verticalSeparation.setEndY(newHeight - (FXScreenUtils.DEFAULT_ELEMENT_SPACING * 0.5));
    }

}
