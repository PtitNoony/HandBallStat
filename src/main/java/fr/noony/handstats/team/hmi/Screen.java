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
import javafx.stage.Window;
import org.openide.util.Exceptions;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class Screen implements ControlledScreen, PropertyChangeListener {

//    private final PropertyChangeSupport propertyChangeSupport;
//    private final InstanceContent lookupContents = new InstanceContent();
//    private final AbstractLookup alookup = new AbstractLookup(lookupContents);
    private final Node mainNode;
    private final FXMLLoader loader;
    private final FXController controller;
    private final String fxmlName;
    private AbstractScreensController myScreensController;

    public Screen(String fxmlFileName) {
//        propertyChangeSupport = new PropertyChangeSupport(Screen.this);
//        lookupContents.add(propertyChangeSupport);
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

//    public Lookup getLookup() {
//        return alookup;
//    }
    public Node getNode() {
        return mainNode;
    }

    public String getName() {
        return fxmlName;
    }

    public FXController getController() {
        return controller;
    }

    @Override
    public void setScreenParent(AbstractScreensController screensController) {
        myScreensController = screensController;
    }

    @Override
    public void setWindow(Window window) {
        controller.setWindow(window);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (myScreensController != null) {
            myScreensController.processEvent(evt);
        }
    }

    public void updateSize(double width, double height) {
        controller.updateSize(width, height);
    }

    public void loadParameters(Object... params) {
        controller.loadParameters(params);
    }

}
