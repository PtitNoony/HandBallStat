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
package fr.noony.handstats.court;

import static fr.noony.handstats.Court.TERRAIN_LENGHT;
import static fr.noony.handstats.court.DrawingUtilities.COURT_RATIO;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class CourtDrawing {

    public static final Color TERRAIN_FILL = Color.MEDIUMBLUE;
    public static final int PENALTY_DISTANCE = 700;
    public static final int LINE_WIDTH = 5;
    public static final int MIN_WIDTH = 1;
    public static final int TERRAIN_BORDER = 100;
    public static final Color ZONE_COLOR = Color.GOLD;
    public static final Color ZONE_9M_COLOR = Color.GOLDENROD;
    public static final Color LINE_COLOR = ZONE_COLOR;
    public static final Color GOAL_COLOR = Color.RED;
    public static final Color PENALTY_COLOR = Color.BLACK;
    public static final Color SHOOTING_ZONE_FILL = Color.GREEN;
    public static final double SHOOTING_ZONE_OPACITY = 0.0;
    public static final Color SHOOTING_ZONE_STROKE = Color.BLACK;

    private final Group mainNode;
    private RightCourtDrawing rightCourtDrawing;
    private LeftCourtDrawing leftCourtDrawing;

    public CourtDrawing() {
        mainNode = new Group();
        initDrawing();
    }

    private void initDrawing() {
        rightCourtDrawing = new RightCourtDrawing();
        leftCourtDrawing = new LeftCourtDrawing();
        //
        rightCourtDrawing.setPosition(TERRAIN_LENGHT / 2.0 * COURT_RATIO, 0.0);
        //
        mainNode.getChildren().add(leftCourtDrawing.getNode());
        mainNode.getChildren().add(rightCourtDrawing.getNode());
        //TODO:: rajouter les bancs et la table de marque pour visualiser le terrain correctement
    }

    public final Node getNode() {
        return mainNode;
    }

    public final void setPosition(double xPos, double yPos) {
        mainNode.setTranslateX(xPos);
        mainNode.setTranslateY(yPos);
    }

}
