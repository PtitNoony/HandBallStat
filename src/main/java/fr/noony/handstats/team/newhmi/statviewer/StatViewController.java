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
package fr.noony.handstats.team.newhmi.statviewer;

import fr.noony.handstats.team.newhmi.FXScreen;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;

/**
 *
 * @author Arnaud
 */
public class StatViewController extends FXScreen {

    //local copy used to update children's size
    private double width = 0;
    private double height = 0;

    private enum StatsScreens {
        GAME_SELECTION,
        GAME_SUMMARY
    }

    private FXScreen currentScreen = null;
    private final Map<StatsScreens, FXScreen> screens;

    public StatViewController() {
        super();
        screens = new HashMap<>();
        Platform.runLater(() -> initStatScreens());
    }

    private void initStatScreens() {
        FXScreen gameSelectionScreen = new GameSelectionScreen();
        gameSelectionScreen.addPropertyChangeListener(this);
        screens.put(StatsScreens.GAME_SELECTION, gameSelectionScreen);
        currentScreen = gameSelectionScreen;
        currentScreen.updateSize(width, height);
        addNode(currentScreen.getNode());
    }

    @Override
    public void updateSize(double newWidth, double newHeight) {
        width = newWidth;
        height = newHeight;
        if (currentScreen != null) {
            currentScreen.updateSize(width, height);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);

    }

}
