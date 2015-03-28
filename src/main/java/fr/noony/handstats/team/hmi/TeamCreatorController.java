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
package fr.noony.handstats.team.hmi;

import fr.noony.handstats.Championship;
import fr.noony.handstats.core.Team;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class TeamCreatorController extends FXController implements PropertyChangeListener {

    private static final int MIN_TEAM_NAME_LENGH = 3;

    @FXML
    private TextField teamField;
    @FXML
    private Button createTeamButton;
    @FXML
    private ChoiceBox<Championship> championshipChoiceB;

    private Championship selectedChampionship = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: reinitializer les champs : faire une methode que le controller de screen appelle
        createTeamButton.setDisable(true);
        //
        championshipChoiceB.getItems().setAll(Arrays.asList(Championship.values()));
        championshipChoiceB.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Championship> observable, Championship oldValue, Championship newValue) -> {
            Logger.getLogger(TeamCreatorController.class.getName()).log(Level.FINEST, "new championship for team creation", new Object[]{observable, oldValue, newValue});
            checkTeamValidity();
        });
        //
        Logger.getLogger(TeamCreatorController.class.getName()).log(Level.INFO, "Init Team creator panel");
        teamField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Logger.getLogger(TeamCreatorController.class.getName()).log(Level.FINEST, "new name for team creation", new Object[]{observable, oldValue, newValue});
            checkTeamValidity();
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //nothing just yet
    }

    private void checkTeamValidity() {
        selectedChampionship = championshipChoiceB.getSelectionModel().getSelectedItem();
        if (teamField.getText().length() > MIN_TEAM_NAME_LENGH && selectedChampionship != null) {
            createTeamButton.setDisable(false);
        }
    }

    public void cancelTeamCreationAction(ActionEvent event) {
        Logger.getLogger(TeamCreatorController.class.getName()).log(Level.INFO, "cancelTeamCreationAction {0}", new Object[]{event});
        firePropertyChange(Events.CANCEL_TEAM_CREATION_EVENT, null, null);

    }

    @FXML
    public void createTeamAction(ActionEvent event) {
        Logger.getLogger(TeamCreatorController.class.getName()).log(Level.INFO, "createTeamAction {0}", new Object[]{event});
        Team team = new Team(teamField.getText(), selectedChampionship);
        firePropertyChange(Events.CREATE_TEAM_EVENT, null, team);
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    @Override
    public void loadParameters(Object... params) {
        //no parameters needed for this screen
    }
}
