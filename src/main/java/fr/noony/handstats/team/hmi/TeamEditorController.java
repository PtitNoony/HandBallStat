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

import fr.noony.handstats.Poste;
import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.Team;
import static fr.noony.handstats.team.hmi.Events.CANCEL_EVENT;
import static fr.noony.handstats.team.hmi.Events.PLAYER_CREATION_OK_EVENT;
import static fr.noony.handstats.team.hmi.Events.PLAYER_EDITION_OK_EVENT;
import fr.noony.handstats.utils.log.MainLogger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.paint.Color;
import org.pmw.tinylog.Level;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class TeamEditorController extends FXController implements PropertyChangeListener {

    @FXML
    private Button newPlayerB;
    @FXML
    private Button editPlayerB;
    @FXML
    private Button toActiveButton;
    @FXML
    private Button toRestingButton;
    @FXML
    private ListView<Player> reposListe;
    @FXML
    private ListView<Player> activeList;
    @FXML
    private Label teamLabel;
    @FXML
    private ColorPicker colorPicker;
    //
    private CustomPopup playerEditor;
    private PlayerEditorController playerEditorController;
    private ObservableList<Player> restingPlayers;
    private ObservableList<Player> activePlayers;
    private Player selectedPlayer = null;

    private Team currentTeam;
    private Color homeColor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainLogger.log(Level.INFO, "Init Team editor panel");
        editPlayerB.setDisable(true);
        playerEditor = new CustomPopup("PlayerEditorPanel");
        createNewPlayerEditor();
        restingPlayers = FXCollections.observableArrayList();
        reposListe.setItems(restingPlayers);
        activePlayers = FXCollections.observableArrayList();
        activeList.setItems(activePlayers);
        //
        toActiveButton.setDisable(true);
        toRestingButton.setDisable(true);
        //
        activeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        reposListe.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        activeList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Player> observable, Player oldValue, Player newValue) -> {
            MainLogger.log(Level.INFO, "active player selection changed", new Object[]{observable, oldValue, newValue});
            processActiveListSelectionChanged();
        });
        reposListe.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Player> observable, Player oldValue, Player newValue) -> {
            MainLogger.log(Level.INFO, "resting player selection changed", new Object[]{observable, oldValue, newValue});
            processReposListSelectionChanged();
        });
        colorPicker.setOnAction((ActionEvent event) -> {
            //TODO: log event
            homeColor = colorPicker.getValue();
            currentTeam.setPreferedColor(homeColor);
        });
    }

    /**
     *
     * @param event action event
     */
    @FXML
    public void newPlayerAction(ActionEvent event) {
        MainLogger.log(Level.INFO, "Editing new player {0}", new Object[]{event});
        newPlayerB.setDisable(true);
        playerEditorController.loadParameters();
        playerEditor.show(getWindow());
        //TODO use constant
        playerEditor.setSize(700, 500);
    }

    @FXML
    public void toRestingAction(ActionEvent event) {
        MainLogger.log(Level.INFO, "To resting players action {0}", new Object[]{event});
        Player toRestPlayer = activeList.getSelectionModel().getSelectedItem();
        activePlayers.remove(toRestPlayer);
        restingPlayers.add(toRestPlayer);
        currentTeam.setPlayerActive(toRestPlayer, false);
    }

    @FXML
    public void toActiveAction(ActionEvent event) {
        MainLogger.log(Level.INFO, "To active players action {0}", new Object[]{event});
        Player toActivePlayer = reposListe.getSelectionModel().getSelectedItem();
        restingPlayers.remove(toActivePlayer);
        activePlayers.add(toActivePlayer);
        currentTeam.setPlayerActive(toActivePlayer, true);
    }

    @FXML
    public void backAction(ActionEvent event) {
        MainLogger.log(Level.INFO, "backAction {0}", new Object[]{event});
        firePropertyChange(Events.BACK_TO_TEAM_MAIN, null, currentTeam);
    }

    private void createNewPlayerEditor() {
        playerEditorController = (PlayerEditorController) playerEditor.getController();
        playerEditorController.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(TeamEditorController.this);
        playerEditor.hide();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case PLAYER_CREATION_OK_EVENT:
                //pas comme ca qu'il faut faire
                //"" + name + "%%" + lastName + "%%" + number + "%%" + poste
                String[] values = ((String) evt.getNewValue()).split("%%");
                Player newPlayer = currentTeam.createPlayer(values[0], values[1], Integer.parseInt(values[2]), Poste.valueOf(values[3]), false);
                restingPlayers.add(newPlayer);
                reposListe.setItems(restingPlayers);
                playerEditor.hide();
                newPlayerB.setDisable(false);
//                currentTeam.addPlayer(newPlayer, false);
                break;
            case PLAYER_EDITION_OK_EVENT:
                //TODO : enhance

                Player editedPlayer = (Player) evt.getNewValue();
                if (activeList.getSelectionModel().getSelectedItem() != null) {
                    activeList.getSelectionModel().getSelectedItem().setNumero(editedPlayer.getNumber());
                    activeList.getSelectionModel().getSelectedItem().setPositionActuelle(editedPlayer.getPositionActuelle());
                    activeList.getSelectionModel().getSelectedItem().setPositionPreferee(editedPlayer.getPositionPreferee());
                } else {
                    reposListe.getSelectionModel().getSelectedItem().setNumero(editedPlayer.getNumber());
                    reposListe.getSelectionModel().getSelectedItem().setPositionActuelle(editedPlayer.getPositionActuelle());
                    reposListe.getSelectionModel().getSelectedItem().setPositionPreferee(editedPlayer.getPositionPreferee());
                }
                restingPlayers = FXCollections.observableArrayList();
                activePlayers = FXCollections.observableArrayList();
                reposListe.getItems().clear();
                activeList.getItems().clear();
                //
                Platform.runLater(() -> {
                    restingPlayers.setAll(currentTeam.getRestingPlayers());
                    reposListe.setItems(restingPlayers);
                    activePlayers.setAll(currentTeam.getActivePlayers());
                    activeList.setItems(activePlayers);
                });
                //
                playerEditor.hide();
                break;
            case CANCEL_EVENT:
                playerEditor.hide();
                newPlayerB.setDisable(false);
                break;
        }
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    @FXML
    public void editPlayerAction(ActionEvent event) {
        playerEditorController.loadParameters(selectedPlayer);
        //TODO fix set size after show : window null
        playerEditor.show(getWindow());
        playerEditor.setSize(700, 500);
    }

    @Override
    public void loadParameters(Object... params) {
        try {
            currentTeam = (Team) params[0];
            teamLabel.setText(currentTeam.getName());
            //
            restingPlayers = FXCollections.observableArrayList();
            restingPlayers.addAll(currentTeam.getRestingPlayers());
            reposListe.getItems().clear();
            reposListe.setItems(restingPlayers);
            //
            activePlayers = FXCollections.observableArrayList();
            activePlayers.addAll(currentTeam.getActivePlayers());
            activeList.getItems().clear();
            activeList.setItems(activePlayers);
            //
            colorPicker.setValue(currentTeam.getPreferedColor());
            //
        } catch (Exception e) {
            MainLogger.log(Level.ERROR, "TEAM EDITOR CONTROLLER :: Not usable paramters : {0}", e);
        }
    }

    private void processActiveListSelectionChanged() {
        Platform.runLater(() -> {
            if (!activeList.getSelectionModel().getSelectedIndices().isEmpty()) {
                toRestingButton.setDisable(false);
                toActiveButton.setDisable(true);
                reposListe.getSelectionModel().clearSelection();
                selectedPlayer = activeList.getSelectionModel().getSelectedItem();
            }
        });
    }

    private void processReposListSelectionChanged() {
        Platform.runLater(() -> {
            if (!reposListe.getSelectionModel().getSelectedIndices().isEmpty()) {
                toRestingButton.setDisable(true);
                toActiveButton.setDisable(false);
                activeList.getSelectionModel().clearSelection();
                editPlayerB.setDisable(false);
                selectedPlayer = reposListe.getSelectionModel().getSelectedItem();
            } else {
//                toRestingButton.setDisable(true);
//                toActiveButton.setDisable(true);
//                editPlayerB.setDisable(true);
            }
        });
    }

}
