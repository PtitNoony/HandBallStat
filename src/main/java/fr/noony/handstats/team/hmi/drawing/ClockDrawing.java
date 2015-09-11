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

import fr.noony.handstats.GameClock;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class ClockDrawing implements PropertyChangeListener {

    public static final Dimension DEFAULT_CLOCK_DIMENSION = new Dimension(300, 140);

    private static final Color CLOCK_BACKGROUND_COLOR = Color.BLACK;
    private static final Color CLOCK_FONT_COLOR = Color.WHITESMOKE;
    private static final Color CLOCK_DISABLE_COLOR = Color.LIGHTGRAY;
    private static final Color CLOCK_TIMER_BORDER = Color.CYAN;

    private final Group mainNode;
    private Rectangle background;
    private Label timeLabel;
    private Rectangle timeLabelBackground;
    private Label firstHalfLabel;
    private Label secondHalfLabel;
    private PlayButton playButton;
    private PauseButton pauseButton;
    //
    private GameClock gameClock;

    public ClockDrawing() {
        mainNode = new Group();
        initDrawing();
    }

    public void setGameClock(GameClock clock) {
        gameClock = clock;
        gameClock.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(ClockDrawing.this);
        playButton.setGameClock(clock);
        pauseButton.setGameClock(clock);

        Platform.runLater(() -> timeLabel.setText(gameClock.getTime()));
    }

    private void initDrawing() {
        background = new Rectangle(DEFAULT_CLOCK_DIMENSION.getWidth(), DEFAULT_CLOCK_DIMENSION.getHeight());
        background.setFill(CLOCK_BACKGROUND_COLOR);
        //
        createTimeLabel();
        createHalfLabels();
        createTimeControls();
        //
        mainNode.getChildren().add(background);
        mainNode.getChildren().add(timeLabelBackground);
        mainNode.getChildren().add(timeLabel);
        mainNode.getChildren().add(firstHalfLabel);
        mainNode.getChildren().add(secondHalfLabel);
        mainNode.getChildren().add(playButton.getNode());
        mainNode.getChildren().add(pauseButton.getNode());

    }

    private void createTimeLabel() {
        timeLabelBackground = new Rectangle(DEFAULT_CLOCK_DIMENSION.getWidth() / 2.0, DEFAULT_CLOCK_DIMENSION.getHeight() / 2.0);
        timeLabelBackground.setX(DEFAULT_CLOCK_DIMENSION.getWidth() / 4.0);
        timeLabelBackground.setY(DEFAULT_CLOCK_DIMENSION.getHeight() / 4.0);
        timeLabelBackground.setStroke(CLOCK_TIMER_BORDER);
        //
        timeLabel = new Label("00:00");
        timeLabel.setMinWidth(DEFAULT_CLOCK_DIMENSION.getWidth() / 2.0);
        timeLabel.setMaxWidth(DEFAULT_CLOCK_DIMENSION.getWidth() / 2.0);
        timeLabel.setMinHeight(DEFAULT_CLOCK_DIMENSION.getHeight());
        timeLabel.setMaxHeight(DEFAULT_CLOCK_DIMENSION.getHeight());
        timeLabel.setTranslateX(DEFAULT_CLOCK_DIMENSION.getWidth() / 4.0);
        timeLabel.setFont(new Font(42));
        timeLabel.setTextFill(CLOCK_FONT_COLOR);
        timeLabel.setAlignment(Pos.CENTER);
    }

    private void createHalfLabels() {
        firstHalfLabel = new Label("MT 1");
        firstHalfLabel.setMinWidth(DEFAULT_CLOCK_DIMENSION.getWidth() / 4.0);
        firstHalfLabel.setMaxWidth(DEFAULT_CLOCK_DIMENSION.getWidth() / 4.0);
        firstHalfLabel.setMinHeight(DEFAULT_CLOCK_DIMENSION.getHeight() / 4.0);
        firstHalfLabel.setMaxHeight(DEFAULT_CLOCK_DIMENSION.getHeight() / 4.0);
        firstHalfLabel.setTranslateY(DEFAULT_CLOCK_DIMENSION.getHeight() / 5.0);
        firstHalfLabel.setFont(new Font(22));
        firstHalfLabel.setTextFill(CLOCK_DISABLE_COLOR);
        firstHalfLabel.setAlignment(Pos.CENTER);
        //
        secondHalfLabel = new Label("MT 2");
        secondHalfLabel.setMinWidth(DEFAULT_CLOCK_DIMENSION.getWidth() / 4.0);
        secondHalfLabel.setMaxWidth(DEFAULT_CLOCK_DIMENSION.getWidth() / 4.0);
        secondHalfLabel.setMinHeight(DEFAULT_CLOCK_DIMENSION.getHeight() / 4.0);
        secondHalfLabel.setMaxHeight(DEFAULT_CLOCK_DIMENSION.getHeight() / 4.0);
        secondHalfLabel.setTranslateY(3.0 * DEFAULT_CLOCK_DIMENSION.getHeight() / 5.0);
        secondHalfLabel.setFont(new Font(22));
        secondHalfLabel.setTextFill(CLOCK_DISABLE_COLOR);
        secondHalfLabel.setAlignment(Pos.CENTER);
    }

    private void createTimeControls() {
        playButton = new PlayButton();
        double posX = DEFAULT_CLOCK_DIMENSION.getWidth() * (3.0 / 4.0) + (DEFAULT_CLOCK_DIMENSION.getWidth() / 4.0 - PlayButton.DEFAULT_SIZE) / 2.0;
        playButton.setPosition(posX, Button.DEFAULT_SIZE);
        pauseButton = new PauseButton();
        pauseButton.setPosition(posX, DEFAULT_CLOCK_DIMENSION.getHeight() - 2.0 * Button.DEFAULT_SIZE);
    }

    public final Node getNode() {
        return mainNode;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case GameClock.CLOCK_VALUE_CHANGED:
                Platform.runLater(() -> timeLabel.setText(evt.getNewValue().toString()));
                break;
            case GameClock.CLOCK_STATE_CHANGED:
                updateClockDrawingState(evt);
                break;
            case GameClock.CLOCK_START_FIRST_HALF:
                drawInFirstHalf();
                break;
            case GameClock.CLOCK_START_SECOND_HALF:
                drawInSecondHalf();
                break;
            case GameClock.CLOCK_END_FIST_HALF:
                setHalfBreak();
                break;
            case GameClock.CLOCK_END_SECOND_HALF:
                setEnded();
                break;
            default:
                //nothing
                break;
        }
    }

    private void updateClockDrawingState(PropertyChangeEvent evt) {
        GameClock.ClockState state = (GameClock.ClockState) evt.getNewValue();
        switch (state) {
            case PLAYING:
                setPlaying(true);
                break;
            case PAUSED:
                setPlaying(false);
                break;
            default:
                //nothing
                break;
        }
    }

    private void setPlaying(boolean playing) {
        if (playing) {
            playButton.setState(Button.ButtonState.DISABLED);
            pauseButton.setState(Button.ButtonState.ENABLED);
        } else {
            playButton.setState(Button.ButtonState.ENABLED);
            pauseButton.setState(Button.ButtonState.DISABLED);
        }
    }

    private void setEnded() {
        playButton.setState(Button.ButtonState.DISABLED);
        pauseButton.setState(Button.ButtonState.DISABLED);
        firstHalfLabel.setTextFill(Color.ORANGE);
        secondHalfLabel.setTextFill(Color.ORANGE);
    }

    private void drawInFirstHalf() {
        firstHalfLabel.setTextFill(Color.AQUA);
    }

    private void setHalfBreak() {
        firstHalfLabel.setTextFill(Color.ORANGE);
    }

    private void drawInSecondHalf() {
        secondHalfLabel.setTextFill(Color.AQUA);
    }

}
