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
package fr.noony.handstats.team.hmi.drawing;

import fr.noony.handstats.core.GameAction;
import fr.noony.handstats.core.GameActionComparator;
import fr.noony.handstats.core.GameHalf;
import fr.noony.handstats.core.GoodShot;
import fr.noony.handstats.core.Team;
import fr.noony.handstats.stats.GameStat;
import fr.noony.handstats.utils.TimeCalculator;
import java.awt.Dimension;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class HalfTimeLine {

    public static final Dimension DEFAULT_SIZE = new Dimension(640, 200);
    public static final int INNER_MARGIN = 10;
    private static final String HALF_TIME = "30:00";
    private static final int NB_MINUTES_PER_TICK = 5;
    private static final double VERTICAL_TICK_SIZE = 6;
    //
    private final int nbStartMinutes;

    //
    private final GameHalf myHalf;
    //
    private final Group mainNode;
    private final Rectangle background;
    private final List<Line> tickLines;
    private Line timeLine;
    private List<Circle> goals;
    private GameStat myGameStat;

    public HalfTimeLine(GameHalf half) {
        myHalf = half;
        switch (myHalf) {
            case FIRST_HALF:
                nbStartMinutes = 0;
                break;
            case SECOND_HALF:
                nbStartMinutes = 30;
                break;
            default:
                throw new IllegalArgumentException();
        }
        mainNode = new Group();
        tickLines = new LinkedList<>();
        background = new Rectangle(DEFAULT_SIZE.getWidth(), DEFAULT_SIZE.getHeight());
        initDrawing();
    }

    private void initDrawing() {
        background.setFill(Color.BLACK);
        timeLine = new Line(INNER_MARGIN, DEFAULT_SIZE.getHeight() / 2.0, DEFAULT_SIZE.getWidth() - INNER_MARGIN, DEFAULT_SIZE.getHeight() / 2.0);
        timeLine.setStroke(Color.WHITESMOKE);
        timeLine.setStrokeWidth(2.5);
        //
        Line tick0 = new Line(INNER_MARGIN, (DEFAULT_SIZE.getHeight() - VERTICAL_TICK_SIZE) / 2.0, INNER_MARGIN, (DEFAULT_SIZE.getHeight() + VERTICAL_TICK_SIZE) / 2.0);
        tick0.setStroke(Color.WHITESMOKE);
        tick0.setStrokeWidth(2.5);
        tickLines.add(tick0);
        double tickXSpace = (DEFAULT_SIZE.getWidth() - 2.0 * INNER_MARGIN) / (30.0 / NB_MINUTES_PER_TICK);
        for (int i = 1; i < 30 / NB_MINUTES_PER_TICK; i++) {
            Line tick = new Line(INNER_MARGIN + tickXSpace * i, (DEFAULT_SIZE.getHeight() - VERTICAL_TICK_SIZE) / 2.0, INNER_MARGIN + tickXSpace * i, (DEFAULT_SIZE.getHeight() + VERTICAL_TICK_SIZE) / 2.0);
            tick.setStroke(Color.WHITESMOKE);
            tick.setStrokeWidth(2.5);
            tickLines.add(tick);
        }
        Line tickN = new Line(DEFAULT_SIZE.getWidth() - INNER_MARGIN, (DEFAULT_SIZE.getHeight() - VERTICAL_TICK_SIZE) / 2.0, DEFAULT_SIZE.getWidth() - INNER_MARGIN, (DEFAULT_SIZE.getHeight() + VERTICAL_TICK_SIZE) / 2.0);
        tickN.setStroke(Color.WHITESMOKE);
        tickLines.add(tickN);
        tickN.setStrokeWidth(2.5);
        //
        mainNode.getChildren().add(background);
        mainNode.getChildren().add(timeLine);
        tickLines.stream().forEach(tickLine -> mainNode.getChildren().add(tickLine));

    }

    public final Node getNode() {
        return mainNode;
    }

    public final void setPosition(double newX, double newY) {
        mainNode.setTranslateX(newX);
        mainNode.setTranslateY(newY);
    }

    public void initStat(GameStat stat) {
        myGameStat = stat;
        List<GoodShot> gameActions = new LinkedList<>();
        for (GameAction action : stat.getGoalChronology()) {
            switch (myHalf) {
                case FIRST_HALF:
                    if (action.getActionTime().compareTo(HALF_TIME) <= 0) {
                        gameActions.add((GoodShot) action);
                    }
                    break;
                case SECOND_HALF:
                    if (action.getActionTime().compareTo(HALF_TIME) >= 0) {
                        gameActions.add((GoodShot) action);
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        //should not be necessary
        Collections.sort(gameActions, new GameActionComparator());
        displayGoals(gameActions);
    }

    private void displayGoals(List<GoodShot> actions) {
        double minutesSpace = (DEFAULT_SIZE.getWidth() - 2.0 * INNER_MARGIN) / 30.0;
        double deltaSpace = (DEFAULT_SIZE.getHeight() / 2.0) / myGameStat.getMaxDelta();
        int deltaForHome = 0;
        int nbAction = actions.size();
        goals = new LinkedList<>();
        Team homeTeam = myGameStat.getHomeTeam();
        Team awayTeam = myGameStat.getAwayTeam();
        Color homeColor = homeTeam.getPreferedColor();
        Color awayColor = awayTeam.getPreferedColor();
        double[] homeVertices = new double[nbAction * 2 + 4];
        double[] awayVertices = new double[nbAction * 2 + 4];
        homeVertices[0] = INNER_MARGIN;
        homeVertices[1] = DEFAULT_SIZE.getHeight() / 2.0;
        awayVertices[0] = INNER_MARGIN;
        awayVertices[1] = DEFAULT_SIZE.getHeight() / 2.0;
        //to optimize
        GoodShot currentAction;
        int currentMinutes;
        int currentSeconds;
        double currentTime;
        for (int i = 0; i < actions.size(); i++) {
            currentAction = actions.get(i);
            currentMinutes = TimeCalculator.timeStringToMinutes(currentAction.getActionTime());
            currentSeconds = TimeCalculator.timeStringToSeconds(currentAction.getActionTime());
            currentTime = currentMinutes + currentSeconds / 60.0 - nbStartMinutes;
            if (currentAction.getShooterTeam().equals(homeTeam)) {
                deltaForHome++;
                final Circle homeGoal = new Circle(6.0);
                homeGoal.setCenterX(INNER_MARGIN + minutesSpace * currentTime);
                homeGoal.setCenterY(DEFAULT_SIZE.getHeight() / 2.0 - deltaForHome * deltaSpace);
                homeGoal.setFill(homeColor);
                homeGoal.setStroke(Color.WHITESMOKE);
                homeGoal.setStrokeWidth(2.0);
                goals.add(homeGoal);
            } else {
                deltaForHome--;
                final Circle awayGoal = new Circle(6.0);
                awayGoal.setCenterX(INNER_MARGIN + minutesSpace * currentTime);
                awayGoal.setCenterY(DEFAULT_SIZE.getHeight() / 2.0 - deltaForHome * deltaSpace);
                awayGoal.setFill(awayColor);
                awayGoal.setStroke(Color.WHITESMOKE);
                awayGoal.setStrokeWidth(2.0);
                goals.add(awayGoal);
            }
            //adding homeTimeline points
            homeVertices[1 + 2 * i + 1] = INNER_MARGIN + minutesSpace * currentTime;
            if (deltaForHome >= 0) {
                homeVertices[1 + 2 * i + 2] = DEFAULT_SIZE.getHeight() / 2.0 - deltaForHome * deltaSpace;
            } else {
                homeVertices[1 + 2 * i + 2] = DEFAULT_SIZE.getHeight() / 2.0;
            }
            //adding away Timeline points
            awayVertices[1 + 2 * i + 1] = INNER_MARGIN + minutesSpace * currentTime;
            if (deltaForHome >= 0) {
                awayVertices[1 + 2 * i + 2] = DEFAULT_SIZE.getHeight() / 2.0;
            } else {
                awayVertices[1 + 2 * i + 2] = DEFAULT_SIZE.getHeight() / 2.0 - deltaForHome * deltaSpace;
            }
        }
        homeVertices[2 * nbAction + 2] = homeVertices[2 * nbAction];
        homeVertices[2 * nbAction + 3] = DEFAULT_SIZE.getHeight() / 2.0;
        awayVertices[2 * nbAction + 2] = awayVertices[2 * nbAction];
        awayVertices[2 * nbAction + 3] = DEFAULT_SIZE.getHeight() / 2.0;
        Polygon awayTimeline = new Polygon(awayVertices);
        awayTimeline.setFill(awayColor);
        mainNode.getChildren().add(awayTimeline);
        Polygon homeTimeline = new Polygon(homeVertices);
        homeTimeline.setFill(homeColor);
        mainNode.getChildren().add(homeTimeline);
        for (Circle c : goals) {
            mainNode.getChildren().add(c);
        }
        timeLine.toFront();
        tickLines.stream().forEach(tickLine -> tickLine.toFront());
    }
}
