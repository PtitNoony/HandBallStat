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
package fr.noony.handstats.team.newhmi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author Arnaud
 */
public abstract class FXScreen implements PropertyChangeListener {

    private final PropertyChangeSupport propertyChangeSupport;
    private final Group mainNode;

    public FXScreen() {
        mainNode = new Group();
        propertyChangeSupport = new PropertyChangeSupport(FXScreen.this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (FXScreenUtils.STAGE_DIMENSION_CHANGED.equals(evt.getPropertyName())) {
            final double width = (double) evt.getOldValue();
            final double height = (double) evt.getNewValue();
            updateSize(width, height);
        }
    }

    public abstract void updateSize(final double width, final double height);

    public Node getNode() {
        return mainNode;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Must be called from withing javaFX thread
     *
     * @param node
     */
    protected void addNode(Node node) {
        mainNode.getChildren().add(node);
    }

    /**
     * Must be called from withing javaFX thread
     *
     * @param node
     */
    protected void removeNode(Node node) {
        mainNode.getChildren().remove(node);
    }

    protected void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(name, oldValue, newValue);
    }

}
