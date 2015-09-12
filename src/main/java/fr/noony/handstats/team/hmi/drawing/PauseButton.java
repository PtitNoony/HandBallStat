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

import fr.noony.handstats.core.GameClock;
import static fr.noony.handstats.team.hmi.drawing.Button.BUTTON_STROKE_COLOR;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class PauseButton implements Button {

    public static final Color PAUSE_BUTTON_ENABLED_COLOR = Color.ORANGERED;

    private ButtonState state;
    //
    private final Group mainNode;
    private Rectangle background;
    private Rectangle shape1;
    private Rectangle shape2;
    //
    private GameClock gameClock;

    public PauseButton() {
        mainNode = new Group();
        initDrawing();
        createInteractivity();
        setState(ButtonState.DISABLED);
    }

    public void setGameClock(GameClock clock) {
        gameClock = clock;
    }

    private void initDrawing() {
        background = new Rectangle(DEFAULT_SIZE, DEFAULT_SIZE);
        background.setOpacity(0.0);
        shape1 = new Rectangle(DEFAULT_SIZE / 3.0, DEFAULT_SIZE);
        shape1.setStrokeWidth(DEFAULT_BUTTON_STROKE_WIDTH);
        shape2 = new Rectangle(DEFAULT_SIZE / 3.0, DEFAULT_SIZE);
        shape2.setStrokeWidth(DEFAULT_BUTTON_STROKE_WIDTH);
        shape2.setX(2.0 * DEFAULT_SIZE / 3.0);
        //
        mainNode.getChildren().add(shape1);
        mainNode.getChildren().add(shape2);
        mainNode.getChildren().add(background);
    }

    private void createInteractivity() {
        background.setOnMouseEntered((MouseEvent event) -> {
            Logger.getLogger(PlayButton.class.getName()).log(Level.OFF, "entering play button on event {0}", event);
            switch (state) {
                case ENABLED:
                    shape1.setStroke(BUTTON_STROKE_COLOR);
                    shape2.setStroke(BUTTON_STROKE_COLOR);
                    break;
                default:
                    //nothing
                    break;
            }
        });
        background.setOnMouseExited((MouseEvent event) -> {
            Logger.getLogger(PlayButton.class.getName()).log(Level.OFF, "exiting play button on event {0}", event);
            shape1.setStroke(null);
            shape2.setStroke(null);
        });
        background.setOnMouseClicked((MouseEvent event) -> {
            Logger.getLogger(PlayButton.class.getName()).log(Level.OFF, "clicking play button on event {0}", event);
            switch (state) {
                case ENABLED:
                    gameClock.pauseTime();
                    break;
                default:
                    //nothing
                    break;
            }
        });
    }

    public final Node getNode() {
        return mainNode;
    }

    public void setPosition(double x, double y) {
        mainNode.setTranslateX(x);
        mainNode.setTranslateY(y);
    }

    @Override
    public final void setState(ButtonState buttonState) {
        state = buttonState;
        switch (state) {
            case ENABLED:
                shape1.setFill(PAUSE_BUTTON_ENABLED_COLOR);
                shape2.setFill(PAUSE_BUTTON_ENABLED_COLOR);
                break;
            case DISABLED:
                shape1.setFill(BUTTON_DISABLED_COLOR);
                shape2.setFill(BUTTON_DISABLED_COLOR);
                break;
        }
    }

}
