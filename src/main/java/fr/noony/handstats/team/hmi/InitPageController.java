package fr.noony.handstats.team.hmi;

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
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Arnaud Hamon-Keromen
 */
public class InitPageController extends FXController {

    private Team selectedTeam;
    private final List<Team> teamsInFolder = new LinkedList<>();

    @FXML
    private Pane mainNode;
    @FXML
    private Label selectedTeamLabel;
    @FXML
    private Button createTeamButton;
    @FXML
    private Button changeTeamButton;
    @FXML
    private Button manageTeamButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //proper initialization is done when params are loaded
    }

    private void initPage() {
        createTeamButton.setDisable(false);
        if (selectedTeam != null) {
            selectedTeamLabel.setText(selectedTeam.getName());
            manageTeamButton.setDisable(false);
        } else {
            selectedTeamLabel.setText("");
            manageTeamButton.setDisable(true);
        }
        //
        changeTeamButton.setDisable(teamsInFolder.isEmpty());
    }

    @FXML
    public void newTeamAction(ActionEvent event) {
        firePropertyChange(Events.ACTION_CREATE_TEAM, null, event);
    }

    @FXML
    public void changeTeamAction(ActionEvent event) {
        firePropertyChange(Events.ACTION_CHANGE_TEAM, null, teamsInFolder);
    }

    @FXML
    public void manageTeamAction(ActionEvent event) {
        firePropertyChange(Events.BACK_TO_TEAM_MAIN, null, selectedTeam);
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    @Override
    public void loadParameters(Object... params) {
        if (params.length == 2) {
            teamsInFolder.addAll((List<Team>) params[0]);
            selectedTeam = (Team) params[1];
            initPage();
        }
    }

}
