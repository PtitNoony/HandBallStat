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
import static fr.noony.handstats.team.hmi.Events.CANCEL_EVENT;
import static fr.noony.handstats.team.hmi.Events.OK_EVENT;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

//    private Stage playerEditor;
    private CustomPopup playerEditor;
    private PlayerEditorController playerEditorController;
    private ObservableList<Player> restingPlayers;
    private ObservableList<Player> activePlayers;

    private Team currentTeam;
    private Color homeColor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Logger.getLogger(TeamEditorController.class.getName()).log(Level.INFO, "Init Team editor panel");
        editPlayerB.setDisable(true);
//        playerEditor = createNewPlayerEditor();
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
            Logger.getLogger(TeamEditorController.class.getName()).log(Level.FINEST, "active player selection changed", new Object[]{observable, oldValue, newValue});
            processActiveListSelectionChanged();
        });
        reposListe.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Player> observable, Player oldValue, Player newValue) -> {
            Logger.getLogger(TeamEditorController.class.getName()).log(Level.FINEST, "resting player selection changed", new Object[]{observable, oldValue, newValue});
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
        Logger.getLogger(TeamEditorController.class.getName()).log(Level.INFO, "Editing new player {0}", new Object[]{event});
        newPlayerB.setDisable(true);
        playerEditorController.resetFields();
        playerEditor.show(getWindow());
        playerEditor.setSize(700, 500);
    }

    @FXML
    public void toRestingAction(ActionEvent event) {
        Logger.getLogger(TeamEditorController.class.getName()).log(Level.INFO, "To resting players action {0}", new Object[]{event});
        Player toRestPlayer = activeList.getSelectionModel().getSelectedItem();
        activePlayers.remove(toRestPlayer);
        restingPlayers.add(toRestPlayer);
        currentTeam.setPlayerActive(toRestPlayer, false);
    }

    @FXML
    public void toActiveAction(ActionEvent event) {
        Logger.getLogger(TeamEditorController.class.getName()).log(Level.INFO, "To active players action {0}", new Object[]{event});
        Player toActivePlayer = reposListe.getSelectionModel().getSelectedItem();
        restingPlayers.remove(toActivePlayer);
        activePlayers.add(toActivePlayer);
        currentTeam.setPlayerActive(toActivePlayer, true);
    }

    @FXML
    public void backAction(ActionEvent event) {
        Logger.getLogger(TeamEditorController.class.getName()).log(Level.INFO, "backAction {0}", new Object[]{event});
        firePropertyChange(Events.BACK_TO_TEAM_MAIN, null, currentTeam);
    }

//    private Stage createNewPlayerEditor() {
//        Stage stage = new Stage();
//        Platform.runLater(() -> {
//            try {
//                FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("PlayerEditorPanel.fxml"));
//                fXMLLoader.load();
//                Parent root = fXMLLoader.getRoot();
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                playerEditorController = (PlayerEditorController) fXMLLoader.getController();
//                playerEditorController.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(TeamEditorController.this);
//                stage.hide();
//                stage.setAlwaysOnTop(true);
//                stage.setResizable(false);
//                stage.setOnCloseRequest((WindowEvent event) -> {
//                    Logger.getLogger(TeamEditorController.class.getName()).log(Level.SEVERE, "tried to close the player editor window on event {0}", event);
//                    playerEditorController.annulerAction(new ActionEvent(stage, null));
//                });
//            } catch (IOException ex) {
//                Logger.getLogger(TeamEditorController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
//        return stage;
//    }
    private void createNewPlayerEditor() {
//        Stage stage = new Stage();
//        Platform.runLater(() -> {
//            try {
//                FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("PlayerEditorPanel.fxml"));
//                fXMLLoader.load();
//                Parent root = fXMLLoader.getRoot();
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
        playerEditorController = (PlayerEditorController) playerEditor.getController();
        playerEditorController.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(TeamEditorController.this);
        playerEditor.hide();
//                stage.setAlwaysOnTop(true);
//                stage.setResizable(false);
//                stage.setOnCloseRequest((WindowEvent event) -> {
//                    Logger.getLogger(TeamEditorController.class.getName()).log(Level.SEVERE, "tried to close the player editor window on event {0}", event);
//                    playerEditorController.annulerAction(new ActionEvent(stage, null));
//                });
//            } catch (IOException ex) {
//                Logger.getLogger(TeamEditorController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
//        return stage;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case OK_EVENT:
                //pas comme ca qu'il faut faire
                Player nouveauJoueur = (Player) evt.getNewValue();
                restingPlayers.add(nouveauJoueur);
                reposListe.setItems(restingPlayers);
                playerEditor.hide();
                newPlayerB.setDisable(false);
                currentTeam.addPlayer(nouveauJoueur, false);
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
        playerEditor.show(getWindow());
//        playerEditorController.loadParameters(params);
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
            Logger.getLogger(TeamEditorController.class.getName()).log(Level.SEVERE, "Not usable paramters : {0}", e);
        }
    }

    private void processActiveListSelectionChanged() {
        Platform.runLater(() -> {
            if (!activeList.getSelectionModel().getSelectedIndices().isEmpty()) {
                toRestingButton.setDisable(false);
                toActiveButton.setDisable(true);
                reposListe.getSelectionModel().clearSelection();
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
            } else {
//                toRestingButton.setDisable(true);
//                toActiveButton.setDisable(true);
//                editPlayerB.setDisable(true);
            }
        });
    }

}
