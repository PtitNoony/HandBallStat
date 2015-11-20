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
package fr.noony.handstats.team.newhmi.basiccomponents;

import fr.noony.handstats.team.newhmi.FXScreenUtils;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * @author Arnaud
 */
public class SimpleButton extends SimpleComponent {

    private static final Dimension DEFAUL_DIMENSION = new Dimension(100, 50);
    private static final Color DEFAULT_FILL_COLOR = Color.GRAY;
    private static final Color DEFAULT_STROKE_COLOR = Color.BLACK;
    private static final Color DEFAULT_LABEL_COLOR = Color.BLACK;

    //
    private final Rectangle buttonBackground;
    private final Label buttonLabel;
    // needed or do we use the label ?
    private final Rectangle buttonForeGround;
    //
    private String buttonText;
    private double width;
    private double height;

    public SimpleButton(String text, double x, double y, double buttonWidth, double buttonHeight) {
        super();
        super.setPosition(x, y);
        //
        buttonText = text;
        width = buttonWidth;
        height = buttonHeight;
        // instanciate graphical primitives
        buttonBackground = new Rectangle();
        buttonLabel = new Label(buttonText);
        buttonForeGround = new Rectangle();
        initButton();
    }

    public SimpleButton(String text) {
        this(text, 0, 0, DEFAUL_DIMENSION.getWidth(), DEFAUL_DIMENSION.getHeight());
    }

    private void initButton() {
        buttonBackground.setFill(DEFAULT_FILL_COLOR);
        buttonBackground.setStroke(DEFAULT_STROKE_COLOR);
        //
        buttonLabel.setAlignment(Pos.CENTER);
        buttonLabel.setTextFill(DEFAULT_STROKE_COLOR);
        //
        buttonForeGround.setOpacity(0.0);
        //
        addNode(buttonBackground);
        addNode(buttonLabel);
        addNode(buttonForeGround);
        //
        updateSize();
    }

    public void setButtonText(String text) {
        buttonText = text;
        updateText();
    }

    private void updateText() {
        Platform.runLater(() -> buttonLabel.setText(buttonText));
    }

    @Override
    public void setSize(double newWidth, double newHeight) {
        width = newWidth;
        height = newHeight;
        updateSize();
    }

    private void updateSize() {
        buttonBackground.setWidth(width);
        buttonBackground.setHeight(height);
        //
        buttonLabel.setMinSize(width, height);
        buttonLabel.setMaxSize(width, height);
        buttonLabel.setFont(new Font(FXScreenUtils.getFontSize(height)));
        //
        buttonForeGround.setWidth(width);
        buttonForeGround.setHeight(height);
    }

}
