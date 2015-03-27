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

import java.beans.PropertyChangeSupport;
import javafx.fxml.Initializable;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public abstract class FXController implements Initializable {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(FXController.this);
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);

    public FXController() {
        lookupContents.add(propertyChangeSupport);
    }

    public Lookup getLookup() {
        return alookup;
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        getLookup().lookup(PropertyChangeSupport.class).firePropertyChange(propertyName, oldValue, newValue);
    }

    public abstract void updateSize(double width, double height);

    public abstract void loadParameters(Object... params);

}
