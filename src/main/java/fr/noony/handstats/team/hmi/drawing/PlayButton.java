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
import fr.noony.handstats.utils.log.MainLogger;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.pmw.tinylog.Level;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class PlayButton implements Button {

    public static final Color PLAY_BUTTON_COLOR = Color.GREEN;

    private ButtonState state;
    //
    private final Group mainNode;
    private Polygon mainShape;
    //
    private GameClock gameClock;

    public PlayButton() {
        mainNode = new Group();
        initDrawing();
        createInteractivity();
        setState(ButtonState.ENABLED);
    }

    public void setGameClock(GameClock clock) {
        gameClock = clock;
    }

    private void initDrawing() {
        Double[] points = new Double[]{0.0, 0.0,
            DEFAULT_SIZE, DEFAULT_SIZE / 2.0,
            0.0, DEFAULT_SIZE};
        mainShape = new Polygon();
        mainShape.getPoints().addAll(points);
        mainShape.setStrokeWidth(DEFAULT_BUTTON_STROKE_WIDTH);
        mainNode.getChildren().add(mainShape);
    }

    private void createInteractivity() {
        mainShape.setOnMouseEntered((MouseEvent event) -> {
            MainLogger.log(Level.OFF, "Entering play button on event", event);
            switch (state) {
                case ENABLED:
                    mainShape.setStroke(BUTTON_STROKE_COLOR);
                    break;
                default:
                    //nothing
                    break;
            }
        });
        mainShape.setOnMouseExited((MouseEvent event) -> {
            MainLogger.log(Level.OFF, "Exiting play button on event", event);
            mainShape.setStroke(null);
        });
        mainShape.setOnMouseClicked((MouseEvent event) -> {
            MainLogger.log(Level.INFO, "Clicking play button on event", event);
            switch (state) {
                case ENABLED:
                    gameClock.startTime();
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
                mainShape.setFill(PLAY_BUTTON_COLOR);
                break;
            case DISABLED:
                mainShape.setFill(BUTTON_DISABLED_COLOR);
                break;
        }
    }

}
