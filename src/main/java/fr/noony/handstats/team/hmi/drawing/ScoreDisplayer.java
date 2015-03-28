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

import fr.noony.handstats.Game;
import fr.noony.handstats.core.Team;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class ScoreDisplayer implements PropertyChangeListener {

    public static final Dimension DEFAULT_SCORE_DISPLAYER_DIMENSION = new Dimension(200, 100);
    private final double innerBorder = 6.0;
    //
    private final Game myGame;
    private final Team homeTeam;
    private final Team awayTeam;
    //
    private final Group mainNode;
    private Rectangle background;
    private Rectangle homeBackground;
    private Rectangle awayBackground;
    private Line separationLine;
    private Label homeLabel;
    private Label homeScoreLabel;
    private Label awayLabel;
    private Label awayScoreLabel;

    public ScoreDisplayer(Game game) {
        myGame = game;
        homeTeam = myGame.getHomeTeam();
        awayTeam = myGame.getAwayTeam();
        //
        game.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        mainNode = new Group();
        initDrawing();
    }

    private void initDrawing() {
        background = new Rectangle(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth(), DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight());
        background.setFill(Color.DARKCYAN);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(2.5);
        //
        homeBackground = new Rectangle(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0, DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight());
        homeBackground.setFill(Color.DARKCYAN);
        homeBackground.setStroke(null);
        //
        awayBackground = new Rectangle(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0, DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight());
        awayBackground.setFill(Color.DARKCYAN);
        awayBackground.setStroke(null);
        awayBackground.setX(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        //
        separationLine = new Line(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0, 0.0,
                DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0, DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight());
        separationLine.setStroke(Color.BLACK);
        separationLine.setStrokeWidth(2.5);
        //
        createHome();
        createAway();
        //
        mainNode.getChildren().add(background);
        mainNode.getChildren().add(homeBackground);
        mainNode.getChildren().add(awayBackground);
        mainNode.getChildren().add(separationLine);
        mainNode.getChildren().add(homeLabel);
        mainNode.getChildren().add(homeScoreLabel);
        mainNode.getChildren().add(awayLabel);
        mainNode.getChildren().add(awayScoreLabel);
    }

    private void createHome() {
        homeLabel = new Label("HOME");
        homeLabel.setMinWidth(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        homeLabel.setMaxWidth(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        homeLabel.setMaxHeight(DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        homeLabel.setMinHeight(DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        homeLabel.setFont(new Font(24));
        homeLabel.setAlignment(Pos.CENTER);
        //
        homeScoreLabel = new Label("0");
        homeScoreLabel.setMinWidth(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        homeScoreLabel.setMaxWidth(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        homeScoreLabel.setMaxHeight(2.0 * DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        homeScoreLabel.setMinHeight(2.0 * DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        homeScoreLabel.setTranslateY(DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        homeScoreLabel.setFont(new Font(48));
        homeScoreLabel.setAlignment(Pos.CENTER);
    }

    private void createAway() {
        awayLabel = new Label("AWAY");
        awayLabel.setMinWidth(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        awayLabel.setMaxWidth(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        awayLabel.setMaxHeight(DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        awayLabel.setMinHeight(DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        awayLabel.setTranslateX(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        awayLabel.setFont(new Font(24));
        awayLabel.setAlignment(Pos.CENTER);
        //
        awayScoreLabel = new Label("0");
        awayScoreLabel.setMinWidth(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        awayScoreLabel.setMaxWidth(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        awayScoreLabel.setMaxHeight(2.0 * DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        awayScoreLabel.setMinHeight(2.0 * DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        awayScoreLabel.setTranslateX(DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0);
        awayScoreLabel.setTranslateY(DEFAULT_SCORE_DISPLAYER_DIMENSION.getHeight() / 3.0);
        awayScoreLabel.setFont(new Font(48));
        awayScoreLabel.setAlignment(Pos.CENTER);
    }

    public void setPosition(double x, double y) {
        mainNode.setTranslateX(x);
        mainNode.setTranslateY(y);
    }

    public Node getNode() {
        return mainNode;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int score;
        switch (evt.getPropertyName()) {
            case "HOME_SCORED":
                score = (int) evt.getNewValue();
                homeScoreLabel.setText("" + score);
                break;
            case "AWAY_SCORED":
                score = (int) evt.getNewValue();
                awayScoreLabel.setText("" + score);
                break;
            default:
                //nothing
                break;
        }
    }

    public void setColors(Color homeColor, Color awayColor) {
        homeBackground.setFill(homeColor);
        awayBackground.setFill(awayColor);
    }

}
