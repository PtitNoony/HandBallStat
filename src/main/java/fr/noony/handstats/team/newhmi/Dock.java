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
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud
 */
public class Dock implements PropertyChangeListener {

    private final double innerMargin = 12;

    private final Color dockColor = Color.GRAY;
    private final Color dockStrokeColor = Color.BLACK;

    private final PropertyChangeSupport propertyChangeSupport;

    private final Rectangle dockBackground;
    private final Group mainNode;
    //
    private TouchableIcon teamImageView;
    private TouchableIcon gameImageView;
    private TouchableIcon statsImageView;
    private TouchableIcon parametersImageView;

    public Dock() {
        propertyChangeSupport = new PropertyChangeSupport(Dock.this);
        //
        mainNode = new Group();
        dockBackground = new Rectangle();
        Platform.runLater(() -> initDock());
    }

    public Node getNode() {
        return mainNode;
    }

    private void initDock() {
        dockBackground.setFill(dockColor);
        dockBackground.setStroke(dockStrokeColor);
        //
        teamImageView = new TouchableIcon("Team");
        gameImageView = new TouchableIcon("Game");
        statsImageView = new TouchableIcon("Stats");
        parametersImageView = new TouchableIcon("Parameters");
        //
        teamImageView.setOnAction(event -> {
            propertyChangeSupport.firePropertyChange(FXScreenUtils.REQUEST_TEAM_SCREEN, null, null);
            setTeamSelected();
        });
        gameImageView.setOnAction(event -> {
            propertyChangeSupport.firePropertyChange(FXScreenUtils.REQUEST_GAME_SCREEN, null, null);
            setGameSelected();
        });
        statsImageView.setOnAction(event -> {
            propertyChangeSupport.firePropertyChange(FXScreenUtils.REQUEST_STATS_SCREEN, null, null);
            setStatsSelected();
        });
        parametersImageView.setOnAction(event -> {
            propertyChangeSupport.firePropertyChange(FXScreenUtils.REQUEST_PARAMETERS_SCREEN, null, null);
            setParamtersSelected();
        });
        //
        mainNode.getChildren().add(dockBackground);
        mainNode.getChildren().add(teamImageView.getNode());
        mainNode.getChildren().add(gameImageView.getNode());
        mainNode.getChildren().add(statsImageView.getNode());
        mainNode.getChildren().add(parametersImageView.getNode());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (FXScreenUtils.STAGE_DIMENSION_CHANGED.equals(evt.getPropertyName())) {
            final double width = (double) evt.getOldValue();
            final double height = (double) evt.getNewValue();
            updateDockSize(width, height);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    private void updateDockSize(final double width, final double height) {
        double newHeight = height * FXScreenUtils.DOCK_VERTICAL_RATIO;
        dockBackground.setWidth(width);
        dockBackground.setHeight(newHeight);
        mainNode.setTranslateY(height * (1 - FXScreenUtils.DOCK_VERTICAL_RATIO));
        double iconSize = newHeight - 2.0 * innerMargin;
        teamImageView.setSize(iconSize, iconSize);
        gameImageView.setSize(iconSize, iconSize);
        statsImageView.setSize(iconSize, iconSize);
        parametersImageView.setSize(iconSize, iconSize);
        //
        double iconHorizontalSpace = (width - 4.0 * iconSize) / 5.0;
        teamImageView.setPosition(iconHorizontalSpace, innerMargin);
        gameImageView.setPosition(2.0 * iconHorizontalSpace + iconSize, innerMargin);
        statsImageView.setPosition(3.0 * iconHorizontalSpace + 2.0 * iconSize, innerMargin);
        parametersImageView.setPosition(4.0 * iconHorizontalSpace + 3.0 * iconSize, innerMargin);
    }

    private void setTeamSelected() {
        teamImageView.setSelected(true);
        gameImageView.setSelected(false);
        statsImageView.setSelected(false);
        parametersImageView.setSelected(false);
    }

    private void setGameSelected() {
        teamImageView.setSelected(false);
        gameImageView.setSelected(true);
        statsImageView.setSelected(false);
        parametersImageView.setSelected(false);
    }

    private void setStatsSelected() {
        teamImageView.setSelected(false);
        gameImageView.setSelected(false);
        statsImageView.setSelected(true);
        parametersImageView.setSelected(false);
    }

    private void setParamtersSelected() {
        teamImageView.setSelected(false);
        gameImageView.setSelected(false);
        statsImageView.setSelected(false);
        parametersImageView.setSelected(true);
    }
}
