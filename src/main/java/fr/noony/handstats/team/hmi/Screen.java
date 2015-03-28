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
package fr.noony.handstats.team.hmi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class Screen implements ControlledScreen, PropertyChangeListener {

    private final Node mainNode;
    private final FXMLLoader loader;
    private final FXController controller;
    private final String fxmlName;
    private MainScreensController myScreensController;

    public Screen(String fxmlFileName) {
        fxmlName = fxmlFileName;
        loader = new FXMLLoader(getClass().getResource(fxmlName + ".fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        mainNode = loader.getRoot();
        controller = (FXController) loader.getController();
        controller.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(Screen.this);

    }

    public Node getNode() {
        return mainNode;
    }

    public String getName() {
        return fxmlName;
    }

    @Override
    public void setScreenParent(MainScreensController screensController) {
        myScreensController = screensController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        myScreensController.processEvent(evt);
    }

    public void updateSize(double width, double height) {
        controller.updateSize(width, height);
    }

    public void loadParameters(Object... params) {
        controller.loadParameters(params);
    }

}
