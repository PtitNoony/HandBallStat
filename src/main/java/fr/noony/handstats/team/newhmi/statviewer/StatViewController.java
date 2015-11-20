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

import fr.noony.handstats.core.Game;
import fr.noony.handstats.team.newhmi.FXScreen;
import fr.noony.handstats.team.newhmi.HeaderPanel;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 *
 * @author Arnaud
 */
public class StatViewController extends FXScreen {

    //local copy used to update children's size
    private double width = 0;
    private double height = 0;
    //
    private final Button goToGameSelectionButton;
    private final Button goToGameStatButton;
    private StatsScreens currentStatsScreenType;

    private enum StatsScreens {
        GAME_SELECTION,
        GAME_SUMMARY
    }

    private FXScreen currentScreen = null;
    private final Map<StatsScreens, FXScreen> screens;

    public StatViewController() {
        super();
        screens = new HashMap<>();
        goToGameSelectionButton = new Button("Sélection du match");
        goToGameStatButton = new Button("Statistiques match");
        currentStatsScreenType = StatsScreens.GAME_SELECTION;
        Platform.runLater(() -> initStatScreens());
    }

    private void initStatScreens() {
        FXScreen gameSelectionScreen = new GameSelectionScreen();
        gameSelectionScreen.addPropertyChangeListener(this);
        screens.put(StatsScreens.GAME_SELECTION, gameSelectionScreen);
        currentScreen = gameSelectionScreen;
        currentScreen.updateSize(width, height);
        goToGameSelectionButton.setWrapText(true);
        goToGameSelectionButton.setOnAction(event -> setGameChoiceScreen(event));
        HeaderPanel.getInstance().addControl(goToGameSelectionButton);
        goToGameStatButton.setWrapText(true);
        goToGameStatButton.setDisable(true);
        addNode(currentScreen.getNode());
    }

    private void setGameChoiceScreen(ActionEvent event) {
        currentStatsScreenType = StatsScreens.GAME_SELECTION;
        if (currentScreen != null) {
            removeNode(currentScreen.getNode());
        }
        HeaderPanel.getInstance().removeControl(goToGameStatButton);
        currentScreen = screens.get(StatsScreens.GAME_SELECTION);
        addNode(currentScreen.getNode());
    }

    private void setGameSummaryScreen(Game game) {
        currentStatsScreenType = StatsScreens.GAME_SUMMARY;
        goToGameSelectionButton.setDisable(false);
        HeaderPanel.getInstance().addControl(goToGameStatButton);
        goToGameStatButton.setDisable(true);
        if (currentScreen != null) {
            removeNode(currentScreen.getNode());
        }
        if (!screens.containsKey(StatsScreens.GAME_SUMMARY)) {
            GameSummaryScreen gameSummaryScreen = new GameSummaryScreen();
            screens.put(StatsScreens.GAME_SUMMARY, gameSummaryScreen);
        }
        currentScreen = screens.get(StatsScreens.GAME_SUMMARY);
        addNode(currentScreen.getNode());
        ((GameSummaryScreen) currentScreen).setGame(game);
    }

    public void repopulateHeaderPanel() {
        switch (currentStatsScreenType) {
            case GAME_SELECTION:
                HeaderPanel.getInstance().addControl(goToGameSelectionButton);
                goToGameSelectionButton.setDisable(true);
                break;
            case GAME_SUMMARY:
                HeaderPanel.getInstance().addControl(goToGameSelectionButton);
                goToGameSelectionButton.setDisable(false);

                break;
            default:
            //nothing to do
        }
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
        switch (evt.getPropertyName()) {
            case GameSelectionScreen.DISPLAY_GAME_STAT:
                Game game = (Game) evt.getNewValue();
                setGameSummaryScreen(game);
                break;
            default:
            //nothing
        }
    }

}
