/*
 * Copyright (C) 2014 Arnaud
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
package fr.noony.handstats.court;

import fr.noony.handstats.areas.GoalArea;
import static fr.noony.handstats.court.DrawingUtilities.GOAL_RATIO;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class GoalCageDrawing {

    public static final int GOAL_WIDTH = 300;
    public static final int GOAL_HEIGHT = 200;
    public static final int GOAL_PYLON_WIDTH = 20;
    public static final int MARGIN = 50;
    public static final int DEFAULT_GOAL_CAGE_WIDTH = GOAL_WIDTH + 2 * (MARGIN + GOAL_PYLON_WIDTH);
    public static final int DEFAULT_GOAL_CAGE_HEIGHT = GOAL_HEIGHT + MARGIN + GOAL_PYLON_WIDTH;
    public static final Color BACKGROUND_FILL = Color.RED;
    public static final Color GOAL_FILL = Color.LIGHTGREY;
    public static final Color PYLON_FILL = Color.DARKGREY;
    public static final int NB_GOAL_HORIZONTAL_ZONES = 3;
    public static final int NB_GOAL_VERTICAL_ZONES = 3;

    private final Group mainNode;
    private final List<InteractiveShootingArea> interactiveAreas;

    public GoalCageDrawing() {
        mainNode = new Group();
        interactiveAreas = new LinkedList<>();
        init();
    }

    private void init() {
        mainNode.getChildren().add(createBackground());
        //
        mainNode.getChildren().add(createUpLeftArea().getNode());
        mainNode.getChildren().add(createUpRightArea().getNode());
        mainNode.getChildren().add(createUpArea().getNode());
        mainNode.getChildren().add(createLeftArea().getNode());
        mainNode.getChildren().add(createRightArea().getNode());
        //
        mainNode.getChildren().add(createGoal());
        mainNode.getChildren().add(createLeftPylon().getNode());
        mainNode.getChildren().add(createRightPylon().getNode());
        mainNode.getChildren().add(createCenterPylon().getNode());
        //
        addGoalInteractiveAreas();
    }

    private Shape createBackground() {
        Rectangle background = new Rectangle(0.0, 0.0, GOAL_RATIO * DEFAULT_GOAL_CAGE_WIDTH, GOAL_RATIO * DEFAULT_GOAL_CAGE_HEIGHT);
        background.setFill(BACKGROUND_FILL);
        return background;
    }

    private Shape createGoal() {
        Rectangle goal = new Rectangle(GOAL_RATIO * (MARGIN + GOAL_PYLON_WIDTH), GOAL_RATIO * (MARGIN + GOAL_PYLON_WIDTH), GOAL_WIDTH * GOAL_RATIO, GOAL_HEIGHT * GOAL_RATIO);
        goal.setFill(GOAL_FILL);
        return goal;
    }

    private InteractiveShootingArea createLeftPylon() {
        Rectangle goal = new Rectangle(GOAL_RATIO * MARGIN, GOAL_RATIO * MARGIN, GOAL_RATIO * GOAL_PYLON_WIDTH, GOAL_RATIO * (GOAL_HEIGHT + GOAL_PYLON_WIDTH));
        InteractiveShootingArea leftPylon = new InteractiveShootingArea(GoalArea.LEFT_POST, goal, PYLON_FILL, false, "LEFT_PYLON");
        interactiveAreas.add(leftPylon);
        return leftPylon;
    }

    private InteractiveShootingArea createCenterPylon() {
        Rectangle goal = new Rectangle(GOAL_RATIO * (MARGIN + GOAL_PYLON_WIDTH), GOAL_RATIO * MARGIN, GOAL_RATIO * (GOAL_WIDTH), GOAL_RATIO * GOAL_PYLON_WIDTH);
        InteractiveShootingArea centerPylon = new InteractiveShootingArea(GoalArea.UPPER_POST, goal, PYLON_FILL, false, "CENTER_PYLON");
        interactiveAreas.add(centerPylon);
        return centerPylon;
    }

    private InteractiveShootingArea createRightPylon() {
        Rectangle goal = new Rectangle(GOAL_RATIO * (MARGIN + GOAL_WIDTH + GOAL_PYLON_WIDTH), GOAL_RATIO * MARGIN, GOAL_RATIO * GOAL_PYLON_WIDTH, GOAL_RATIO * (GOAL_HEIGHT + GOAL_PYLON_WIDTH));
        InteractiveShootingArea rightPylon = new InteractiveShootingArea(GoalArea.RIGHT_POST, goal, PYLON_FILL, false, "RIGHT_PYLON");
        interactiveAreas.add(rightPylon);
        return rightPylon;
    }

    private InteractiveShootingArea createUpLeftArea() {
        Rectangle area = new Rectangle(0.0, 0.0, GOAL_RATIO * MARGIN, GOAL_RATIO * MARGIN);
        InteractiveShootingArea upLeftArea = new InteractiveShootingArea(GoalArea.UPPER_LEFT, area, BACKGROUND_FILL, false, "UP_LEFT");
        interactiveAreas.add(upLeftArea);
        return upLeftArea;
    }

    private InteractiveShootingArea createLeftArea() {
        Rectangle area = new Rectangle(0.0, GOAL_RATIO * MARGIN, GOAL_RATIO * MARGIN, GOAL_RATIO * (GOAL_HEIGHT + GOAL_PYLON_WIDTH));
        InteractiveShootingArea leftArea = new InteractiveShootingArea(GoalArea.LEFT, area, BACKGROUND_FILL, false, "LEFT");
        interactiveAreas.add(leftArea);
        return leftArea;
    }

    private InteractiveShootingArea createUpArea() {
        Rectangle area = new Rectangle(GOAL_RATIO * MARGIN, 0.0, GOAL_RATIO * (2.0 * GOAL_PYLON_WIDTH + GOAL_WIDTH), GOAL_RATIO * MARGIN);
        InteractiveShootingArea upArea = new InteractiveShootingArea(GoalArea.UPPER_CENTER, area, BACKGROUND_FILL, false, "UP");
        interactiveAreas.add(upArea);
        return upArea;
    }

    private InteractiveShootingArea createUpRightArea() {
        Rectangle area = new Rectangle(GOAL_RATIO * (MARGIN + GOAL_WIDTH + 2.0 * GOAL_PYLON_WIDTH), 0.0, GOAL_RATIO * MARGIN, GOAL_RATIO * MARGIN);
        InteractiveShootingArea upRightArea = new InteractiveShootingArea(GoalArea.UPPER_RIGHT, area, BACKGROUND_FILL, false, "UP_RIGHT");
        interactiveAreas.add(upRightArea);
        return upRightArea;
    }

    private InteractiveShootingArea createRightArea() {
        Rectangle area = new Rectangle(GOAL_RATIO * (MARGIN + GOAL_WIDTH + 2.0 * GOAL_PYLON_WIDTH), GOAL_RATIO * MARGIN, GOAL_RATIO * MARGIN, GOAL_RATIO * (GOAL_HEIGHT + GOAL_PYLON_WIDTH));
        InteractiveShootingArea rightArea = new InteractiveShootingArea(GoalArea.RIGHT, area, BACKGROUND_FILL, false, "RIGHT");
        interactiveAreas.add(rightArea);
        return rightArea;
    }

    private void addGoalInteractiveAreas() {
        for (int i = 0; i < NB_GOAL_HORIZONTAL_ZONES; i++) {
            for (int j = 0; j < NB_GOAL_VERTICAL_ZONES; j++) {
                mainNode.getChildren().add(createGoalInterativeArea(i, j).getNode());
            }
        }
    }

    private InteractiveShootingArea createGoalInterativeArea(int i, int j) {
        Rectangle goalZone = new Rectangle(
                GOAL_RATIO * (MARGIN + GOAL_PYLON_WIDTH + i * GOAL_WIDTH / NB_GOAL_HORIZONTAL_ZONES),
                GOAL_RATIO * (MARGIN + GOAL_PYLON_WIDTH + j * GOAL_HEIGHT / NB_GOAL_VERTICAL_ZONES),
                GOAL_RATIO * GOAL_WIDTH / NB_GOAL_HORIZONTAL_ZONES,
                GOAL_RATIO * GOAL_HEIGHT / NB_GOAL_VERTICAL_ZONES);
        InteractiveShootingArea insideArea = new InteractiveShootingArea(new GoalArea(i, j), goalZone, GOAL_FILL, true, "IN_" + i + "_" + j);
        interactiveAreas.add(insideArea);
        return insideArea;
    }

    public Node getNode() {
        return mainNode;
    }

    public void setPosition(double newX, double newY) {
        mainNode.setTranslateX(newX);
        mainNode.setTranslateY(newY);
    }

    public void setVisibility(boolean visibility) {
        mainNode.setVisible(visibility);
    }

    public List<InteractiveShootingArea> getInteractiveAreas() {
        return new LinkedList<>(interactiveAreas);
    }

}
