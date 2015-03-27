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
import static fr.noony.handstats.court.DrawingUtilities.COURT_RATIO;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public abstract class HalfCourtDrawing {

    public static final double DEFAULT_HALF_COURT_WIDTH = COURT_RATIO * TERRAIN_LENGHT / 2.0;
    public static final double DEFAULT_HALF_COURT_HEIGHT = COURT_RATIO * TERRAIN_WIDTH;

    private final Group mainNode;
    private final Group scaleNode;
    private final List<InteractiveShootingArea> interactiveAreas;

    public HalfCourtDrawing() {
        mainNode = new Group();
        scaleNode = new Group();
        interactiveAreas = new LinkedList<>();
        mainNode.getChildren().add(scaleNode);
    }

    public final Node getNode() {
        return mainNode;
    }

    protected final void addNode(Node n) {
        scaleNode.getChildren().add(n);
    }

    public List<InteractiveShootingArea> getInteractiveAreas() {
        return interactiveAreas;
    }

    protected void addInteractiveArea(InteractiveShootingArea interactiveArea) {
        interactiveAreas.add(interactiveArea);
    }

    public final void setPosition(double xPos, double yPos) {
        mainNode.setTranslateX(xPos);
        mainNode.setTranslateY(yPos);
    }

    public final void setVisibilty(boolean visibility) {
        mainNode.setVisible(visibility);
    }

    protected static Node createHalfCourtClip() {
        return new Rectangle(COURT_RATIO * TERRAIN_LENGHT / 2.0, TERRAIN_WIDTH * COURT_RATIO);
    }

}
