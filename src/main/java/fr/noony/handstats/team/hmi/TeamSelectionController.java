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

import fr.noony.handstats.core.Team;
import fr.noony.handstats.utils.EnvLoader;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class TeamSelectionController extends FXController implements PropertyChangeListener {

    @FXML
    private ListView<Team> possibleTeamsList;
    @FXML
    private Button choiceTeamButton;
    @FXML
    private Button deleteTeamButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceTeamButton.setDisable(true);
        deleteTeamButton.setDisable(true);
        possibleTeamsList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Team> observable, Team oldValue, Team newValue) -> {
            Logger.getLogger(TeamSelectionController.class.getName()).log(Level.FINEST, "new team selected", new Object[]{observable, oldValue, newValue});
            checkTeamSelection();
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @FXML
    public void cancelTeamSelectionAction(ActionEvent event) {
        Logger.getLogger(TeamSelectionController.class.getName()).log(Level.INFO, "cancelTeamSelectionAction {0}", new Object[]{event});
        firePropertyChange(Events.CANCEL_TEAM_SELECTED_EVENT, null, null);
    }

    @FXML
    public void teamSelectionAction(ActionEvent event) {
        Logger.getLogger(TeamSelectionController.class.getName()).log(Level.INFO, "teamSelectionAction {0}", new Object[]{event});
        Team selectedTeam = possibleTeamsList.getSelectionModel().getSelectedItem();
        EnvLoader.setPreferedTeam(selectedTeam);
        firePropertyChange(Events.NEW_TEAM_SELECTED_EVENT, null, selectedTeam);
    }

    @FXML
    public void deleteTeamAction(ActionEvent event) {
        Logger.getLogger(TeamSelectionController.class.getName()).log(Level.INFO, "deleteTeamAction {0}", new Object[]{event});
        deleteTeam();
    }

    private void checkTeamSelection() {
        boolean disable = possibleTeamsList.getSelectionModel().getSelectedItem() == null;
        choiceTeamButton.setDisable(disable);
        //TODO:: delete team
//        deleteTeamButton.setDisable(disable);
    }

    private void deleteTeam() {
        //TODO:: delete team file
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    @Override
    public void loadParameters(Object... params) {
//        if (params.length > 0) {
//            //un peu moche !!
//            List<Team> teams = (List<Team>) params[0];
//            ObservableList<Team> teamsItems = FXCollections.observableList(teams);
//            possibleTeamsList.getItems().clear();
//            possibleTeamsList.getItems().setAll(teamsItems);
//        }
        ObservableList<Team> teamsItems = FXCollections.observableList(EnvLoader.getTeams());
        possibleTeamsList.getItems().clear();
        possibleTeamsList.getItems().setAll(teamsItems);

    }

}
