package fr.noony.handstats.team.hmi.stats;

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
import fr.noony.handstats.core.Team;
import fr.noony.handstats.stats.GameStat;
import fr.noony.handstats.team.hmi.Events;
import fr.noony.handstats.team.hmi.FXController;
import fr.noony.handstats.team.hmi.Screen;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON
 */
public class StatPopupPageController extends FXController implements PropertyChangeListener {

    @FXML
    public Pane statsPane;
    @FXML
    public ToggleButton homeTeamToggleB;
    @FXML
    public ToggleButton awayTeamToggleB;

    private Screen teamStatPage;
    private GameStat gameStat;
    private Team homeTeam;
    private Team awayTeam;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        teamStatPage = new Screen("StatTeamPane");
        statsPane.getChildren().add(teamStatPage.getNode());
        //
        homeTeamToggleB.setSelected(true);
        awayTeamToggleB.setSelected(false);
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    @Override
    public void loadParameters(Object... params) {
        gameStat = (GameStat) params[0];
        homeTeam = gameStat.getHomeTeam();
        awayTeam = gameStat.getAwayTeam();
        updateStatPage();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //TODO
    }

    private void updateStatPage() {
        if (homeTeamToggleB.isSelected()) {
            teamStatPage.loadParameters(gameStat, homeTeam);
        } else {
            teamStatPage.loadParameters(gameStat, awayTeam);
        }
    }

    @FXML
    public void homeTeamToggleAction(ActionEvent event) {
        //TODO log
        homeTeamToggleB.setSelected(true);
        awayTeamToggleB.setSelected(false);
        updateStatPage();
    }

    @FXML
    public void awayTeamToggleAction(ActionEvent event) {
        //TODO log
        homeTeamToggleB.setSelected(false);
        awayTeamToggleB.setSelected(true);
        updateStatPage();
    }

    @FXML
    public void backGameAction(ActionEvent event) {
        //TODO log
        firePropertyChange(Events.BACK_TO_GAME, null, null);
    }

}
