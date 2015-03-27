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
import static fr.noony.handstats.Court.TERRAIN_WIDTH;
import static fr.noony.handstats.Court.ZONE_9_RADIUS;
import static fr.noony.handstats.Court.ZONE_RADIUS;
import fr.noony.handstats.areas.CourtArea;
import static fr.noony.handstats.court.CourtDrawing.LINE_COLOR;
import static fr.noony.handstats.court.CourtDrawing.LINE_WIDTH;
import static fr.noony.handstats.court.CourtDrawing.TERRAIN_FILL;
import static fr.noony.handstats.court.CourtDrawing.ZONE_9M_COLOR;
import static fr.noony.handstats.court.DrawingUtilities.COURT_RATIO;
import static fr.noony.handstats.court.DrawingUtilities.LINE_RATIO;
import static fr.noony.handstats.court.GoalCageDrawing.GOAL_WIDTH;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class RightCourtDrawing extends HalfCourtDrawing {

    private Rectangle background;
    private Shape zone;
    private Shape z9M;

    public RightCourtDrawing() {
        initDrawing();
    }

    private void initDrawing() {
        createBackground();
        create9MZone();
        createInteractiveZones();
        createZone();
    }

    private void createBackground() {
        background = new Rectangle(TERRAIN_LENGHT / 2.0 * COURT_RATIO, TERRAIN_WIDTH * COURT_RATIO);
        background.setFill(TERRAIN_FILL);
        background.setStrokeWidth(LINE_RATIO * LINE_WIDTH);
        background.setStroke(LINE_COLOR);
        addNode(background);
    }

    private void createZone() {
        zone = createZoneShape();
        InteractiveShootingArea iZone = new InteractiveShootingArea(CourtArea.RIGHT_ZONE, zone, Color.GOLD, false, "RIGHT_ZONE");
        addInteractiveArea(iZone);
        addNode(iZone.getNode());
    }

    private void create9MZone() {
        z9M = create9MZoneDShape();
        z9M.setFill(Color.GOLDENROD);
        addNode(z9M);
    }

    private Shape createZoneShape() {
        Arc zoneD = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, ZONE_RADIUS * COURT_RATIO, ZONE_RADIUS * COURT_RATIO, 90.0, 90.0);
        zoneD.setType(ArcType.ROUND);
        Arc zoneG = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_RADIUS * COURT_RATIO, ZONE_RADIUS * COURT_RATIO, 180.0, 90.0);
        zoneG.setType(ArcType.ROUND);
        Rectangle zoneC = new Rectangle(COURT_RATIO * (TERRAIN_LENGHT / 2.0 - ZONE_RADIUS), COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, COURT_RATIO * ZONE_RADIUS, COURT_RATIO * GOAL_WIDTH);
        Shape sZone = Shape.union(zoneC, Shape.union(zoneD, zoneG));
        return sZone;
    }

    private Shape create9MFullShape() {
        Arc zone9DFull = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, ZONE_9_RADIUS * COURT_RATIO, ZONE_9_RADIUS * COURT_RATIO, 90.0, 90.0);
        zone9DFull.setType(ArcType.ROUND);
        Arc zone9GFull = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_9_RADIUS * COURT_RATIO, ZONE_9_RADIUS * COURT_RATIO, 180.0, 90.0);
        zone9GFull.setType(ArcType.ROUND);
        Rectangle zone9CFull = new Rectangle(COURT_RATIO * (TERRAIN_LENGHT / 2.0 - ZONE_9_RADIUS), COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, COURT_RATIO * ZONE_9_RADIUS, COURT_RATIO * GOAL_WIDTH);
        Shape sZone9Full = Shape.union(zone9CFull, Shape.union(zone9DFull, zone9GFull));
        return sZone9Full;
    }

    private Shape create9MZoneDShape() {
        Shape z9MShape = Shape.subtract(create9MFullShape(), createZoneShape());
        z9MShape.setClip(createHalfCourtClip());
        return z9MShape;
    }

    private void createInteractiveZones() {
        createShootingZones();
        createFullCourtZones();
    }

    private void createShootingZones() {
        createShootZone1();
        createShootZone2();
        createShootZone3();
        createShootZone4();
        createShootZone5();
    }

    private void createFullCourtZones() {
        createFullCourtZone1();
        createFullCourtZone2();
        createFullCourtZone3();
    }

    private void createShootZone1() {
        Arc aileD_small = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, ZONE_RADIUS * COURT_RATIO, ZONE_RADIUS * COURT_RATIO, 90, 45.0);
        aileD_small.setType(ArcType.ROUND);
        Arc aileD_large = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, ZONE_9_RADIUS * COURT_RATIO, ZONE_9_RADIUS * COURT_RATIO, 90, 45.0);
        aileD_large.setType(ArcType.ROUND);
        Shape aileD = Shape.intersect(Shape.subtract(aileD_large, aileD_small), (Shape) createHalfCourtClip());
        InteractiveShootingArea shotZone1 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_EXT_LEFT, aileD, ZONE_9M_COLOR, true, "RIGHT_CLOSE_1");
        addInteractiveArea(shotZone1);
        addNode(shotZone1.getNode());
    }

    private void createShootZone2() {
        Arc aileD_small = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, ZONE_RADIUS * COURT_RATIO, ZONE_RADIUS * COURT_RATIO, 135, 45.0);
        aileD_small.setType(ArcType.ROUND);
        Arc aileD_large = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, ZONE_9_RADIUS * COURT_RATIO, ZONE_9_RADIUS * COURT_RATIO, 135, 45.0);
        aileD_large.setType(ArcType.ROUND);
        Shape aileD = Shape.intersect(Shape.subtract(aileD_large, aileD_small), (Shape) createHalfCourtClip());
        InteractiveShootingArea shotZone2 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_INT_LEFT, aileD, ZONE_9M_COLOR, true, "RIGHT_CLOSE_2");
        addInteractiveArea(shotZone2);
        addNode(shotZone2.getNode());
    }

    private void createShootZone3() {
        Rectangle zoneC = new Rectangle(COURT_RATIO * (TERRAIN_LENGHT / 2.0 - ZONE_9_RADIUS), COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0,
                COURT_RATIO * ZONE_RADIUS, COURT_RATIO * GOAL_WIDTH);
        Shape shotZone3 = Shape.intersect(zoneC, create9MZoneDShape());
        InteractiveShootingArea iShotZone3 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_CENTER, shotZone3, ZONE_9M_COLOR, true, "RIGHT_CLOSE_3");
        addInteractiveArea(iShotZone3);
        addNode(iShotZone3.getNode());
    }

    private void createShootZone4() {
        Arc aileD_small = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_RADIUS * COURT_RATIO, ZONE_RADIUS * COURT_RATIO, 180, 45.0);
        aileD_small.setType(ArcType.ROUND);
        Arc aileD_large = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_9_RADIUS * COURT_RATIO, ZONE_9_RADIUS * COURT_RATIO, 180, 45.0);
        aileD_large.setType(ArcType.ROUND);
        Shape aileD = Shape.intersect(Shape.subtract(aileD_large, aileD_small), (Shape) createHalfCourtClip());
        InteractiveShootingArea shotZone4 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_INT_RIGHT, aileD, ZONE_9M_COLOR, true, "RIGHT_CLOSE_4");
        addInteractiveArea(shotZone4);
        addNode(shotZone4.getNode());
    }

    private void createShootZone5() {
        Arc aileD_small = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_RADIUS * COURT_RATIO, ZONE_RADIUS * COURT_RATIO, 225, 45.0);
        aileD_small.setType(ArcType.ROUND);
        Arc aileD_large = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_9_RADIUS * COURT_RATIO, ZONE_9_RADIUS * COURT_RATIO, 225, 45.0);
        aileD_large.setType(ArcType.ROUND);
        Shape aileD = Shape.intersect(Shape.subtract(aileD_large, aileD_small), (Shape) createHalfCourtClip());
        InteractiveShootingArea shotZone5 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_EXT_RIGHT, aileD, ZONE_9M_COLOR, true, "RIGHT_CLOSE_5");
        addInteractiveArea(shotZone5);
        addNode(shotZone5.getNode());
    }

    private void createFullCourtZone1() {
        Rectangle r1 = new Rectangle(COURT_RATIO * TERRAIN_LENGHT / 2.0, 3.0 * COURT_RATIO * TERRAIN_WIDTH / 10.0);
        Shape z1 = Shape.subtract(r1, create9MFullShape());
        InteractiveShootingArea iZ1 = new InteractiveShootingArea(CourtArea.RIGHT_FAR_LEFT, z1, TERRAIN_FILL, true, "RIGHT_COURT_1");
        addInteractiveArea(iZ1);
        addNode(iZ1.getNode());
    }

    private void createFullCourtZone2() {
        Rectangle r2 = new Rectangle(COURT_RATIO * TERRAIN_LENGHT / 2.0, 2.0 * COURT_RATIO * TERRAIN_WIDTH / 5.0);
        r2.setY(3.0 * COURT_RATIO * TERRAIN_WIDTH / 10.0);
        Shape z2 = Shape.subtract(r2, create9MFullShape());
        InteractiveShootingArea iZ2 = new InteractiveShootingArea(CourtArea.RIGHT_FAR_CENTER, z2, TERRAIN_FILL, true, "RIGHT_COURT_2");
        addInteractiveArea(iZ2);
        addNode(iZ2.getNode());
    }

    private void createFullCourtZone3() {
        Rectangle r3 = new Rectangle(COURT_RATIO * TERRAIN_LENGHT / 2.0, 3.0 * COURT_RATIO * TERRAIN_WIDTH / 10.0);
        r3.setY(7.0 * COURT_RATIO * TERRAIN_WIDTH / 10.0);
        Shape z3 = Shape.subtract(r3, create9MFullShape());
        InteractiveShootingArea iZ3 = new InteractiveShootingArea(CourtArea.RIGHT_FAR_RIGHT, z3, TERRAIN_FILL, true, "RIGHT_COURT_3");
        addInteractiveArea(iZ3);
        addNode(iZ3.getNode());
    }

}
