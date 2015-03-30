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

import fr.noony.handstats.core.Team;
import fr.noony.handstats.court.InteractiveShootingArea;
import fr.noony.handstats.stats.GameStat;
import fr.noony.handstats.team.hmi.FXController;
import fr.noony.handstats.team.hmi.drawing.PlayerDrawing;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class StatTeamPaneController extends FXController implements PropertyChangeListener {

    @FXML
    public Accordion accordion;
    //summary
    @FXML
    public TitledPane summaryPane;
    @FXML
    public Label isVictoryLabel;
    @FXML
    public Label dateLabel;
    @FXML
    public Label opponentLabel;
    @FXML
    public Label scoreLabel;
    @FXML
    public Label madeShotsLabel;
    @FXML
    public Label stoppedShotsLabel;
    // terrain stats
    @FXML
    public Pane terrainPane;
    @FXML
    public RadioButton madeFromTerrainRB;
    @FXML
    public RadioButton missedFromTerrainRB;

    //
    private GameStat gameStat;
    private Team displayedTeam;
    private boolean isHomeTeam;
    private StatCourtDrawing courtDrawing;
    private InteractiveShootingArea selectedTerrainArea = null;
    private InteractiveShootingArea selectedGoalArea = null;
    private InteractiveShootingArea tmpSelected = null;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accordion.setExpandedPane(summaryPane);
        //create terrain drawing
        courtDrawing = new StatCourtDrawing();
        courtDrawing.getInteractiveAreas().stream().forEach(area
                -> area.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(event -> handleTerrainZoneClicked(event)));
        terrainPane.getChildren().add(courtDrawing.getNode());
        madeFromTerrainRB.setSelected(true);
        madeFromTerrainRB.setDisable(true);
        madeFromTerrainRB.setOnAction(event -> handleMadeTerrainShotRBAction(event));
        missedFromTerrainRB.setOnAction(event -> handleMissedTerrainShotRBAction(event));
        //
        courtDrawing.displayAsNeutal();
    }

    @Override
    public void updateSize(double width, double height) {
    }

    @Override
    public void loadParameters(Object... params) {
        //TODO: checks
        gameStat = (GameStat) params[0];
        displayedTeam = (Team) params[1];
        //determine if is home team
        isHomeTeam = displayedTeam.getName().equals(gameStat.getHomeTeam().getName());
        //
        opponentLabel.setText(gameStat.getAwayTeam().getName());
        if (gameStat.isHomeVictor()) {
            if (isHomeTeam) {
                isVictoryLabel.setText("VICTOIRE");
            } else {
                isVictoryLabel.setText("DEFAITE");
            }
        } else if (gameStat.isDraw()) {
            isVictoryLabel.setText("EGALITE");
        } else {
            if (isHomeTeam) {
                isVictoryLabel.setText("DEFAITE");
            } else {
                isVictoryLabel.setText("VICTOIRE");
            }
        }
        dateLabel.setText(gameStat.getGameDate().toString());
        //oula
        String score = "" + gameStat.getHomeScore() + " - " + gameStat.getAwayScore();
        scoreLabel.setText(score);
        //
        if (isHomeTeam) {
            madeShotsLabel.setText("" + gameStat.getHomeAccuracy());
            stoppedShotsLabel.setText("" + gameStat.getHomeShotBlockedPercentage());
            courtDrawing.setShotMade(gameStat.getHomeShotMadeByTerrainArea());
            courtDrawing.setShotMissed(gameStat.getHomeShotMissedByTerrainArea());
        } else {
            madeShotsLabel.setText("" + gameStat.getAwayAccuracy());
            stoppedShotsLabel.setText("" + gameStat.getAwayShotBlockedPercentage());
            courtDrawing.setShotMade(gameStat.getAwayShotMadeByTerrainAreaRatio());
            courtDrawing.setShotMissed(gameStat.getAwayShotMissedByTerrainAreaRatio());
        }
        //TODO make it clean
        handleMadeTerrainShotRBAction(new ActionEvent(this, null));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.err.println(" Evt ::" + evt);

    }

    private void updateTerrainStats() {

    }

    private void handleTerrainZoneClicked(PropertyChangeEvent event) {
        tmpSelected = (InteractiveShootingArea) event.getNewValue();
        if (selectedTerrainArea != null) {
            selectedTerrainArea.setSelectedState(PlayerDrawing.SelectedState.IDLE);
        }
        if (tmpSelected != null && tmpSelected != selectedTerrainArea) {
            selectedTerrainArea = tmpSelected;
            selectedTerrainArea.setSelectedState(PlayerDrawing.SelectedState.SELECTED);
        } else {
            selectedTerrainArea = null;
        }

        updateTerrainStats();
    }

    private void handleMadeTerrainShotRBAction(ActionEvent event) {
        //TODO: log event
        madeFromTerrainRB.setSelected(true);
        madeFromTerrainRB.setDisable(true);
        missedFromTerrainRB.setSelected(false);
        missedFromTerrainRB.setDisable(false);
        courtDrawing.displayMadeShots();
    }

    private void handleMissedTerrainShotRBAction(ActionEvent event) {
        //TODO: log event
        madeFromTerrainRB.setSelected(false);
        madeFromTerrainRB.setDisable(false);
        missedFromTerrainRB.setSelected(true);
        missedFromTerrainRB.setDisable(true);
        courtDrawing.displayMissedShots();
    }

}
