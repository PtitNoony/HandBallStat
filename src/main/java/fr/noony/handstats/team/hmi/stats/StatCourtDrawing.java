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
package fr.noony.handstats.team.hmi.stats;

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
import fr.noony.handstats.court.HalfCourtDrawing;
import fr.noony.handstats.court.InteractiveShootingArea;
import static fr.noony.handstats.team.hmi.stats.ColorFactory.getColorForValue;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class StatCourtDrawing extends HalfCourtDrawing {

    public static final Color SUCCESS_COLOR = Color.GREEN;
    public static final Color FAIL_COLOR = Color.RED;
    //
    private static final double DEFAULT_SATURATION = 0.3;
    private static final double DEFAULT_COMPLEMENTARY_SATURATION = (1 - DEFAULT_SATURATION) * 2.0;
    private static final double DEFAULT_BRIGHTNESS = 0.2;
    private static final double DEFAULT_COMPLEMENTARY_BRIGHTNESS = (1 - DEFAULT_BRIGHTNESS) * 2.0;

    private Rectangle background;
    private Shape z9M;
    private InteractiveShootingArea zone;
    private InteractiveShootingArea close1;
    private InteractiveShootingArea close2;
    private InteractiveShootingArea close3;
    private InteractiveShootingArea close4;
    private InteractiveShootingArea close5;
    private InteractiveShootingArea far1;
    private InteractiveShootingArea far2;
    private InteractiveShootingArea far3;
    private InteractiveShootingArea penalty;
    //
    double[] madeNbShots = null;
    double[] missedNbShots = null;
    double[] madePercShots = null;
    double[] missedPercShots = null;

    public StatCourtDrawing() {
        initDrawing();
    }

    private void initDrawing() {
        createBackground();
        create9MZone();
        createInteractiveZones();
        createZone();
        createPenalty();
    }

    private void createBackground() {
        background = new Rectangle(TERRAIN_LENGHT / 2.0 * COURT_RATIO, TERRAIN_WIDTH * COURT_RATIO);
        background.setFill(TERRAIN_FILL);
        background.setStrokeWidth(LINE_RATIO * LINE_WIDTH);
        background.setStroke(LINE_COLOR);
        addNode(background);
    }

    private void createZone() {
        Shape zoneShape = createZoneShape();
        zone = new InteractiveShootingArea(CourtArea.RIGHT_ZONE, zoneShape, Color.GOLD, false, TerrainAreas.ZONE_AREA.getName());
        addInteractiveArea(zone);
        addNode(zone.getNode());
    }

    private void createPenalty() {
        Circle circle = new Circle((TERRAIN_LENGHT / 2.0 - 1.5 * GOAL_WIDTH) * COURT_RATIO, TERRAIN_WIDTH * COURT_RATIO / 2.0, COURT_RATIO * GOAL_WIDTH * 0.75);
        penalty = new InteractiveShootingArea(CourtArea.RIGHT_ZONE, circle, Color.GOLD, false, TerrainAreas.PENALTY_AREA.getName());
        addInteractiveArea(penalty);
        addNode(penalty.getNode());
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
        close1 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_EXT_LEFT, aileD, ZONE_9M_COLOR, true, TerrainAreas.CLOSE_AREA_1.getName());
        addInteractiveArea(close1);
        addNode(close1.getNode());
    }

    private void createShootZone2() {
        Arc aileD_small = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, ZONE_RADIUS * COURT_RATIO, ZONE_RADIUS * COURT_RATIO, 135, 45.0);
        aileD_small.setType(ArcType.ROUND);
        Arc aileD_large = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0, ZONE_9_RADIUS * COURT_RATIO, ZONE_9_RADIUS * COURT_RATIO, 135, 45.0);
        aileD_large.setType(ArcType.ROUND);
        Shape aileD = Shape.intersect(Shape.subtract(aileD_large, aileD_small), (Shape) createHalfCourtClip());
        close2 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_INT_LEFT, aileD, ZONE_9M_COLOR, true, TerrainAreas.CLOSE_AREA_2.getName());
        addInteractiveArea(close2);
        addNode(close2.getNode());
    }

    private void createShootZone3() {
        Rectangle zoneC = new Rectangle(COURT_RATIO * (TERRAIN_LENGHT / 2.0 - ZONE_9_RADIUS), COURT_RATIO * (TERRAIN_WIDTH - GOAL_WIDTH) / 2.0,
                COURT_RATIO * ZONE_RADIUS, COURT_RATIO * GOAL_WIDTH);
        Shape shotZone3 = Shape.intersect(zoneC, create9MZoneDShape());
        close3 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_CENTER, shotZone3, ZONE_9M_COLOR, true, TerrainAreas.CLOSE_AREA_3.getName());
        addInteractiveArea(close3);
        addNode(close3.getNode());
    }

    private void createShootZone4() {
        Arc aileD_small = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_RADIUS * COURT_RATIO, ZONE_RADIUS * COURT_RATIO, 180, 45.0);
        aileD_small.setType(ArcType.ROUND);
        Arc aileD_large = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_9_RADIUS * COURT_RATIO, ZONE_9_RADIUS * COURT_RATIO, 180, 45.0);
        aileD_large.setType(ArcType.ROUND);
        Shape aileD = Shape.intersect(Shape.subtract(aileD_large, aileD_small), (Shape) createHalfCourtClip());
        close4 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_INT_RIGHT, aileD, ZONE_9M_COLOR, true, TerrainAreas.CLOSE_AREA_4.getName());
        addInteractiveArea(close4);
        addNode(close4.getNode());
    }

    private void createShootZone5() {
        Arc aileD_small = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_RADIUS * COURT_RATIO, ZONE_RADIUS * COURT_RATIO, 225, 45.0);
        aileD_small.setType(ArcType.ROUND);
        Arc aileD_large = new Arc(COURT_RATIO * TERRAIN_LENGHT / 2.0, COURT_RATIO * (TERRAIN_WIDTH + GOAL_WIDTH) / 2.0, ZONE_9_RADIUS * COURT_RATIO, ZONE_9_RADIUS * COURT_RATIO, 225, 45.0);
        aileD_large.setType(ArcType.ROUND);
        Shape aileD = Shape.intersect(Shape.subtract(aileD_large, aileD_small), (Shape) createHalfCourtClip());
        close5 = new InteractiveShootingArea(CourtArea.RIGHT_SHOOT_EXT_RIGHT, aileD, ZONE_9M_COLOR, true, TerrainAreas.CLOSE_AREA_5.getName());
        addInteractiveArea(close5);
        addNode(close5.getNode());
    }

    private void createFullCourtZone1() {
        Rectangle r1 = new Rectangle(COURT_RATIO * TERRAIN_LENGHT / 2.0, 3.0 * COURT_RATIO * TERRAIN_WIDTH / 10.0);
        Shape z1 = Shape.subtract(r1, create9MFullShape());
        far1 = new InteractiveShootingArea(CourtArea.RIGHT_FAR_LEFT, z1, TERRAIN_FILL, true, TerrainAreas.FAR_AREA_1.getName());
        addInteractiveArea(far1);
        addNode(far1.getNode());
    }

    private void createFullCourtZone2() {
        Rectangle r2 = new Rectangle(COURT_RATIO * TERRAIN_LENGHT / 2.0, 2.0 * COURT_RATIO * TERRAIN_WIDTH / 5.0);
        r2.setY(3.0 * COURT_RATIO * TERRAIN_WIDTH / 10.0);
        Shape z2 = Shape.subtract(r2, create9MFullShape());
        far2 = new InteractiveShootingArea(CourtArea.RIGHT_FAR_CENTER, z2, TERRAIN_FILL, true, TerrainAreas.FAR_AREA_2.getName());
        addInteractiveArea(far2);
        addNode(far2.getNode());
    }

    private void createFullCourtZone3() {
        Rectangle r3 = new Rectangle(COURT_RATIO * TERRAIN_LENGHT / 2.0, 3.0 * COURT_RATIO * TERRAIN_WIDTH / 10.0);
        r3.setY(7.0 * COURT_RATIO * TERRAIN_WIDTH / 10.0);
        Shape z3 = Shape.subtract(r3, create9MFullShape());
        far3 = new InteractiveShootingArea(CourtArea.RIGHT_FAR_RIGHT, z3, TERRAIN_FILL, true, TerrainAreas.FAR_AREA_3.getName());
        addInteractiveArea(far3);
        addNode(far3.getNode());
    }

    public void setNbShotMade(double[] nbShotMadeByTerrainArea) {
        madeNbShots = nbShotMadeByTerrainArea.clone();
    }

    public void setNbShotMissed(double[] nbShotMissedByTerrainArea) {
        missedNbShots = nbShotMissedByTerrainArea.clone();
    }

    public void setPercShotMade(double[] percShotMadeByTerrainArea) {
        madePercShots = percShotMadeByTerrainArea.clone();
    }

    public void setPercShotMissed(double[] percShotMissedByTerrainArea) {
        missedPercShots = percShotMissedByTerrainArea.clone();
    }

    public void displayAsNeutal() {
        background.setFill(Color.WHITESMOKE);
        getInteractiveAreas().stream().forEach(interactiveShootingArea
                -> interactiveShootingArea.setFillColor(Color.LIGHTGREY));
    }

    public void displayMadeNbShots() {
        if (madeNbShots != null) {
            fillZones(madeNbShots, SUCCESS_COLOR);
        }
    }

    public void displayMissedNbShots() {
        if (missedNbShots != null) {
            fillZones(missedNbShots, FAIL_COLOR);
        }
    }

    public void displayMadePercShots() {
        if (madePercShots != null) {
            fillZones(madePercShots, SUCCESS_COLOR);
        }
    }

    public void displayMissedPercShots() {
        if (missedPercShots != null) {
            fillZones(missedPercShots, FAIL_COLOR);
        }
    }

    private void fillZones(double[] values, Color color) {
        zone.setFillColor(getColorForValue(color, values[0]));
        close1.setFillColor(getColorForValue(color, values[1]));
        close2.setFillColor(getColorForValue(color, values[2]));
        close3.setFillColor(getColorForValue(color, values[3]));
        close4.setFillColor(getColorForValue(color, values[4]));
        close5.setFillColor(getColorForValue(color, values[5]));
        far1.setFillColor(getColorForValue(color, values[6]));
        far2.setFillColor(getColorForValue(color, values[7]));
        far3.setFillColor(getColorForValue(color, values[8]));
        penalty.setFillColor(getColorForValue(color, values[9]));
    }

}
