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

import fr.noony.handstats.areas.AbstractArea;
import static fr.noony.handstats.court.CourtDrawing.MIN_WIDTH;
import static fr.noony.handstats.court.CourtDrawing.SHOOTING_ZONE_FILL;
import static fr.noony.handstats.court.CourtDrawing.SHOOTING_ZONE_STROKE;
import fr.noony.handstats.team.hmi.drawing.PlayerDrawing;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class InteractiveShootingArea {

    public enum SelectedState {

        IDLE, SELECTED
    }

    public static final String INTERACTIVE_ZONE_CLICKED = "INTERACTIVE_ZONE_CLICKED";

    private final AbstractArea courtArea;
    private final Shape mainNode;
    private Color fillColor;
    private static final Logger LOG = Logger.getLogger(InteractiveShootingArea.class.getName());
    private static final Level LOG_LEVEL = Level.FINEST;
    //
    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);
    private PlayerDrawing.SelectedState selectedState;
    private final boolean allowScore;
    private final String name;

    public InteractiveShootingArea(AbstractArea area, Shape shape, Color color, boolean canScore, String zoneName) {
        propertyChangeSupport = new PropertyChangeSupport(InteractiveShootingArea.this);
        lookupContents.add(propertyChangeSupport);
        allowScore = canScore;
        name = zoneName;
        //
        courtArea = area;
        mainNode = shape;
        fillColor = color;
        mainNode.setStroke(SHOOTING_ZONE_STROKE);
        mainNode.setStrokeWidth(MIN_WIDTH);
        createInteractivity();
        setSelectedState(PlayerDrawing.SelectedState.IDLE);
    }

    private void createInteractivity() {
        mainNode.setOnMouseEntered((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "Event {0} in Zone {1}", new Object[]{event, courtArea});
            mainNode.setFill(SHOOTING_ZONE_FILL);
        });
        mainNode.setOnMouseExited((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "Event {0} in Zone {1}", new Object[]{event, courtArea});
            updateDrawing();
        });
        mainNode.setOnMouseClicked((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "Event {0} in Zone {1}", new Object[]{event, courtArea});
            propertyChangeSupport.firePropertyChange(INTERACTIVE_ZONE_CLICKED, allowScore, this);
        });
    }

    private void updateDrawing() {
        switch (selectedState) {
            case IDLE:
                mainNode.setFill(fillColor);
                break;
            case SELECTED:
                mainNode.setFill(Color.MAGENTA);
                break;
        }
    }

    public Lookup getLookup() {
        return alookup;
    }

    public Node getNode() {
        return mainNode;
    }

    public String getName() {
        return name;
    }

    public final void setFillColor(Color c) {
        fillColor = c;
        updateDrawing();
    }

    public final void setSelectedState(PlayerDrawing.SelectedState state) {
        selectedState = state;
        updateDrawing();
    }

    public boolean canScore() {
        return allowScore;
    }

}
