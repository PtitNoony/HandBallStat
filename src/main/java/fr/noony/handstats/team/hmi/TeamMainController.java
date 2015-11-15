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

import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.Team;
import static fr.noony.handstats.team.hmi.Events.LOAD_GAME_TO_RESUME;
import fr.noony.handstats.utils.XMLSaver;
import fr.noony.handstats.utils.log.MainLogger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.pmw.tinylog.Level;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class TeamMainController extends FXController implements PropertyChangeListener {

    private Stage playerEditor;
    private PlayerEditorController playerEditorController;
    private ObservableList<Player> restingPlayers;
    private ObservableList<Player> activePlayers;

    @FXML
    private Label teamLabel;

    private Team currentTeam;
    @FXML
    private Button resumeGameButton;
    @FXML
    private Button configureGameButton;
    @FXML
    private Button terminateGameButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainLogger.log(Level.INFO, "Init Team editor panel");
//        editPlayerB.setDisable(true);
//        playerEditor = createNewPlayerEditor();
//        restingPlayers = FXCollections.observableArrayList();
//        reposListe.setItems(restingPlayers);
//        activePlayers = FXCollections.observableArrayList();
//        activeList.setItems(activePlayers);
//        //
//        toActiveButton.setDisable(true);
//        toRestingButton.setDisable(true);
//        //
//        activeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        reposListe.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        activeList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Player> observable, Player oldValue, Player newValue) -> {
//            Logger.getLogger(TeamMainController.class.getName()).log(Level.FINEST, "active player selection changed", new Object[]{observable, oldValue, newValue});
//            processActiveListSelectionChanged();
//        });
//        reposListe.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Player> observable, Player oldValue, Player newValue) -> {
//            Logger.getLogger(TeamMainController.class.getName()).log(Level.FINEST, "resting player selection changed", new Object[]{observable, oldValue, newValue});
//            processReposListSelectionChanged();
//        });
    }

    @FXML
    public void retourAction(ActionEvent event) {
        MainLogger.log(Level.INFO, "retourAction {0}", new Object[]{event});
        XMLSaver.saveTeam(currentTeam);
        firePropertyChange(Events.BACK_TEAM_SELECTION, null, null);
    }

    @FXML
    public void editTeamAction(ActionEvent event) {
        MainLogger.log(Level.INFO, "editTeamAction {0}", new Object[]{event});
        firePropertyChange(Events.EDIT_CURRENT_TEAM, null, currentTeam);
    }

    @FXML
    public void configureMatchAction(ActionEvent event) {
        MainLogger.log(Level.INFO, "playMatchAction {0}", new Object[]{event});
        firePropertyChange(Events.CONFIGURE_MATCH, null, currentTeam);
    }

    @FXML
    public void seeStatsAction(ActionEvent event) {
        MainLogger.log(Level.INFO, "seeStatsAction {0}", new Object[]{event});
        firePropertyChange(Events.DISPLAY_STATS_FROM_MAIN, null, currentTeam);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //??
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    @Override
    public void loadParameters(Object... params) {
        try {
            currentTeam = (Team) params[0];
            Platform.runLater(() -> teamLabel.setText(currentTeam.getName()));
            if (currentTeam.getGameInProgress() != null) {
                resumeGameButton.setDisable(false);
                terminateGameButton.setDisable(false);
                configureGameButton.setDisable(true);
            } else {
                Platform.runLater(() -> {
                    resumeGameButton.setDisable(true);
                    terminateGameButton.setDisable(true);
                    configureGameButton.setDisable(false);
                });
            }
        } catch (Exception e) {
            MainLogger.log(Level.INFO, "Not usable paramters : {0}", e);
        }
    }

    @FXML
    private void resumeGameAction(ActionEvent event) {
        firePropertyChange(LOAD_GAME_TO_RESUME, null, currentTeam.getGameInProgress());
    }

    @FXML
    private void closeGameInProgress(ActionEvent event) {
        currentTeam.getGameInProgress().close();
        resumeGameButton.setDisable(true);
        terminateGameButton.setDisable(true);
        configureGameButton.setDisable(false);
    }

}
