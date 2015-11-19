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

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud
 */
public class TouchableIcon {

    private final Group mainNode;
    private final Rectangle background;
    private final ImageView view;
    private final Rectangle foreground;

    public TouchableIcon(String name) {
        mainNode = new Group();
        background = new Rectangle();
        background.setArcWidth(10.0);
        background.setArcHeight(10.0);
        background.setFill(Color.WHITESMOKE);
        background.setStroke(Color.BLACK);
        background.setEffect(new DropShadow(10, 5, 5, Color.GOLD));
        //
        Image image = new Image(TouchableIcon.class.getResourceAsStream(name + ".png"));
        view = new ImageView(image);
        //
        foreground = new Rectangle();
        foreground.setOpacity(0.0);
        //
        mainNode.getChildren().add(background);
        mainNode.getChildren().add(view);
        mainNode.getChildren().add(foreground);
    }

    public Node getNode() {
        return mainNode;
    }

    public void setSize(double width, double height) {
        background.setWidth(width);
        background.setHeight(height);
        view.setFitWidth(width);
        view.setFitHeight(height);
        foreground.setWidth(width);
        foreground.setHeight(height);
    }

    public void setPosition(double xPos, double yPos) {
        mainNode.setTranslateX(xPos);
        mainNode.setTranslateY(yPos);
    }

    public void setOnAction(EventHandler<? super MouseEvent> handler) {
        foreground.setOnMouseClicked(handler);
    }

    public void setSelected(boolean selected) {
        if (selected) {
            background.setFill(Color.GREEN);
        } else {
            background.setFill(Color.WHITESMOKE);
        }
    }
}
