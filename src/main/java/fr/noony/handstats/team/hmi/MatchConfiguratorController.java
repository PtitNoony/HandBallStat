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
import fr.noony.handstats.Poste;
import fr.noony.handstats.core.Game;
import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.Team;
import fr.noony.handstats.utils.log.MainLogger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.pmw.tinylog.Level;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class MatchConfiguratorController extends FXController implements PropertyChangeListener {

    private enum GameConfigurationMode {
        CREATION, EDITION
    }

    @FXML
    private ListView<Player> homePlayersList;
    @FXML
    private ListView<String> awayPlayersList;
    @FXML
    private Button previousMenuButton;
    @FXML
    private Button startButton;
    @FXML
    private Button addPlayerButton;
    @FXML
    private Button supprPlayerButton;
    @FXML
    private CheckBox alreadyPlayedCheckBox;
    @FXML
    private CheckBox isGoalKCheckBox;
    @FXML
    private ChoiceBox<Team> oppTeamChoiceBox;
    @FXML
    private TextField oppTeamNameField;
    @FXML
    private TextField playerNumberField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ColorPicker colorPicker;
    //
    private ObservableList<Player> homePlayers;
    private ObservableList<String> awayPlayers;

    private Team homeTeam = null;
    private Team awayTeam = null;
    private ObservableList<Team> existingOpponents;
    //
    private int playerNumber = -1;
    private int nbGoals = 0;
    private int nbPlayers = 0;
    private boolean oppTeamNameValidity = false;
    private boolean oppPlayerNameValidity = false;
    private boolean oppTeamCompositionValidity = false;
    private int selectedAwayPlayerIndex = -1;
    private Color awayColor = Color.ORANGE;
    private LocalDate date;
    private GameConfigurationMode configurationMode = GameConfigurationMode.CREATION;
    private Game game = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        homePlayers = FXCollections.observableArrayList();
        awayPlayers = FXCollections.observableArrayList();
        existingOpponents = FXCollections.observableArrayList();
        // home player list set up
        homePlayersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //TODO send selected player to be edited
//        homePlayersList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Player> observable, Player oldValue, Player newValue) -> {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        });
        // away player list set up
        awayPlayersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        awayPlayersList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            updateAwayPlayerSelection();
        });
        //listening opp team name
        oppTeamNameField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            MainLogger.log(Level.INFO, "{0} {1} {2}", new Object[]{observable, oldValue, newValue});
            checkOppTeamName();
        });
        //listening opp player number
        playerNumberField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            MainLogger.log(Level.INFO, "{0} {1} {2}", new Object[]{observable, oldValue, newValue});
            checkOppPlayerNumber();
        });
        //listening existing opp team choice
        oppTeamChoiceBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Team> observable, Team oldValue, Team newValue) -> {
            MainLogger.log(Level.INFO, "{0} {1} {2}", new Object[]{observable, oldValue, newValue});
            checkOppTeamChoice();
        });
        //
        colorPicker.setValue(awayColor);
        colorPicker.setOnAction((ActionEvent event) -> {
            //TODO: log event
            awayColor = colorPicker.getValue();
            if (awayTeam != null) {
                awayTeam.setPreferedColor(awayColor);
            }
        });
        //
        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(event -> {
            //TODO: log event
            date = datePicker.getValue();
            System.out.println("Selected date: " + date);
        });
        //
        homePlayersList.setEditable(false);

    }

    @Override
    public void updateSize(double width, double height) {

    }

    @Override
    public void loadParameters(Object... params) {
        if (params.length == 1) {
            setEditionMode(GameConfigurationMode.CREATION);
            homeTeam = (Team) params[0];
            homePlayers.setAll(homeTeam.getActivePlayers());
            homePlayersList.setItems(homePlayers);
            setUpOpposingTeams();
            updateControls();
            game = null;
        } else if (params.length == 2) {
            setEditionMode(GameConfigurationMode.EDITION);
            homeTeam = (Team) params[0];
            homePlayers.setAll(homeTeam.getActivePlayers());
            homePlayersList.setItems(homePlayers);
            game = (Game) params[1];
            awayTeam = game.getAwayTeam();
//            awayPlayers.setAll(awayTeam.getActivePlayers());
            //
            alreadyPlayedCheckBox.setDisable(true);
            alreadyPlayedCheckBox.setSelected(true);
            existingOpponents.clear();
            existingOpponents.addAll(homeTeam.getOpponentTeams());
            oppTeamChoiceBox.setItems(existingOpponents);
            oppTeamChoiceBox.getSelectionModel().select(game.getAwayTeam());
            //
            checkOppPlayerNumber();
            checkOppTeamChoice();
            checkOppTeamComposition();
            checkOppTeamName();
            updateControls();
        }
    }

    private void setUpOpposingTeams() {
        existingOpponents.clear();
        existingOpponents.addAll(homeTeam.getOpponentTeams());
        oppTeamChoiceBox.setItems(existingOpponents);
        if (existingOpponents.isEmpty()) {
            alreadyPlayedCheckBox.setDisable(true);
            oppTeamChoiceBox.setDisable(true);
        } else {
            alreadyPlayedCheckBox.setDisable(false);
            oppTeamChoiceBox.setDisable(true);
        }
    }

    private void checkOppTeamName() {
        oppTeamNameValidity = oppTeamNameField.getText().length() > 3;
        updateControls();
    }

    private void checkOppPlayerNumber() {
        String sNumber = playerNumberField.getText();
        try {
            playerNumber = Integer.parseInt(sNumber);
            oppPlayerNameValidity = !processOppPlayers(playerNumber);
        } catch (Exception e) {
            oppPlayerNameValidity = false;
        }
        updateControls();
    }

    private void checkOppTeamComposition() {
        processOppPlayers(-1);
        oppTeamCompositionValidity = (nbGoals > 0) && (nbPlayers > 5);
        updateControls();
    }

    private void updateAwayPlayerSelection() {
        selectedAwayPlayerIndex = awayPlayersList.getSelectionModel().getSelectedIndex();
        checkOppTeamComposition();
        updateControls();
    }

    private void updateControls() {
        addPlayerButton.setDisable(!oppPlayerNameValidity);
        supprPlayerButton.setDisable(selectedAwayPlayerIndex < 0);
        //
        boolean matchConfigured = oppTeamNameValidity && oppTeamCompositionValidity;
        startButton.setDisable(!matchConfigured);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    @FXML
    public void alreadyPlayedToggeld(ActionEvent event) {
        //TODO : log action event
        oppTeamChoiceBox.setDisable(!alreadyPlayedCheckBox.isSelected());
    }

    @FXML
    public void editHomeTeamAction(ActionEvent event) {
        firePropertyChange(Events.EDIT_CURRENT_TEAM, null, homeTeam);
    }

    @FXML
    public void addPlayerAction(ActionEvent action) {
        //TODO : log action event
        String player = playerNumberField.getText();
        if (isGoalKCheckBox.isSelected()) {
            player += " G";
        }
        playerNumberField.setText("");
        isGoalKCheckBox.setSelected(false);
        oppPlayerNameValidity = false;
        awayPlayers.add(player);
        Collections.sort(awayPlayers, new OppPlayerComparator());
        awayPlayersList.setItems(awayPlayers);
        checkOppTeamComposition();
    }

    @FXML
    public void supprPlayerAction(ActionEvent event) {
        String playerString = awayPlayersList.getSelectionModel().getSelectedItem();
        if (game != null) {
            String[] reg;
            int playerNum;
            reg = playerString.split(" ");
            playerNum = Integer.parseInt(reg[0]);
            awayTeam.removePlayer(playerNum);
        }
        awayPlayers.remove(playerString);
        awayPlayersList.getSelectionModel().clearSelection();
        awayPlayersList.setItems(awayPlayers);
        updateControls();
    }

    @FXML
    public void startAction(ActionEvent event) {
        switch (configurationMode) {
            case CREATION:
                createNewGame();
                break;
            case EDITION:
                resumeGame();
                break;
        }

    }

    private void setEditionMode(GameConfigurationMode mode) {
        configurationMode = mode;
        switch (configurationMode) {
            case CREATION:
                Platform.runLater(() -> {
                    startButton.setText("Commencer le match");
                });
                break;
            case EDITION:
                Platform.runLater(() -> {
                    startButton.setText("Reprendre le match");
                });
                break;
        }
    }

    private void createNewGame() {
        if (awayTeam != null) {
            //do some checking
//            homeTeam.addOpponentTeam(awayTeam);
            game = new Game();
            game.setUpGame(homeTeam, awayTeam, datePicker.getValue());
            //TODO fix and create GAME
            firePropertyChange(Events.START_GAME, null, game);
        } else {
            Team oppTeam = new Team(oppTeamNameField.getText(), homeTeam.getChampionship());
            String[] reg;
            int playerNum;
            for (String playerString : awayPlayers) {
                Player p;
                reg = playerString.split(" ");
                playerNum = Integer.parseInt(reg[0]);
                if (reg.length == 2) {
                    oppTeam.createPlayer("", "", playerNum, Poste.GARDIEN, true);
                } else {
                    oppTeam.createPlayer("", "", playerNum, Poste.UNDEFINED, true);
                }
            }
            oppTeam.setPreferedColor(awayColor);
            homeTeam.addOpponentTeam(oppTeam);
            game = new Game();
            game.setUpGame(homeTeam, oppTeam, datePicker.getValue());
            //TODO fix and create GAME
            firePropertyChange(Events.START_GAME, null, game);
        }
    }

    private void resumeGame() {
        firePropertyChange(Events.START_GAME, null, game);
    }

    private boolean processOppPlayers(int newPlayer) {
        boolean result = false;
        nbGoals = 0;
        nbPlayers = 0;
        int currentPlayerNb;
        for (String player : awayPlayers) {
            String[] reg = player.split(" ");
            if (reg.length == 1) {
                nbPlayers++;
            } else {
                nbGoals++;
            }
            currentPlayerNb = Integer.parseInt(reg[0]);
            if (currentPlayerNb == newPlayer) {
                result = true;
            }
        }
        return result;
    }

    private void checkOppTeamChoice() {
        Platform.runLater(() -> {
            awayTeam = oppTeamChoiceBox.getValue();
            oppTeamNameField.setText(awayTeam.getName());
            awayColor = awayTeam.getPreferedColor();
            colorPicker.setValue(awayColor);
            awayPlayers.clear();
            //on ne doit pas pouvoir suppr une joueur ???
            for (Player p : awayTeam.getActivePlayers()) {
                String pDisplay = "" + p.getNumber();
                if (p.getPositionPreferee().equals(Poste.GARDIEN)) {
                    pDisplay += " G";
                }
                awayPlayers.add(pDisplay);
            }
            awayPlayersList.getItems().clear();
            awayPlayersList.getItems().setAll(awayPlayers);
            startButton.setDisable(false);
        });
    }

    @FXML
    protected void previousMenuAction(ActionEvent event) {
        firePropertyChange(Events.CANCEL_MATCH_CONFIGURATION, null, homeTeam);
    }

    private class OppPlayerComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            int nbPlayer1;
            int nbPlayer2;
            String[] reg1 = o1.split(" ");
            String[] reg2 = o2.split(" ");
            nbPlayer1 = Integer.parseInt(reg1[0]);
            nbPlayer2 = Integer.parseInt(reg2[0]);
            return Integer.compare(nbPlayer1, nbPlayer2);
        }
    }

}
