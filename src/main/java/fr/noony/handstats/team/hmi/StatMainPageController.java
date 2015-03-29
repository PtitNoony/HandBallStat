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
import fr.noony.handstats.Game;
import fr.noony.handstats.Poste;
import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.Team;
import fr.noony.handstats.stats.GameProcessor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON
 */
public class StatMainPageController extends FXController implements PropertyChangeListener {

    @FXML
    public Pane mainPane;
    @FXML
    public Pane statPane;
    @FXML
    public HBox gameTeamSelectorBox;
    @FXML
    public ChoiceBox<Player> homePlayerChoiceBox;
    @FXML
    public ChoiceBox<Player> awayPlayerChoiceBox;
    @FXML
    public ToggleButton homeToggleB;
    @FXML
    public ToggleButton generalToggleB;
    @FXML
    public ToggleButton awayToggleB;
    @FXML
    public ListView<Game> gameListView;

    private enum StatPageState {

        ALL_GAMES, ONE_MATCH, ONE_MATCH_HOME, ONE_MATCH_AWAY
    }
    //
    private StatScreensManager statScreensManager;
    private static final String STAT_OVERVIEW_PANE = "StatOverviewPane";
    private static final String STAT_GENERAL_PANE = "StatGeneralPane";
    private final Screen generalScreen = new Screen(STAT_GENERAL_PANE);
    private final Screen overviewScreen = new Screen(STAT_OVERVIEW_PANE);
    //
    private StatPageState currentState = StatPageState.ALL_GAMES;
    private StatPageState previousState = StatPageState.ALL_GAMES;
    //
    private Team homeTeam;
    private final Player homeP = new Player("", "TOUS ", 0, Poste.UNDEFINED);
    private final Player awayP = new Player("", "TOUS Adversaires", 0, Poste.UNDEFINED);
    private ObservableList<Game> games;
    private ObservableList<Player> homePlayers;
    private ObservableList<Player> awayPlayers;
    //
    private Game selectedGame = null;
    //
    private GameProcessor gameProcessor;
    //

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        games = FXCollections.observableArrayList();
        homePlayers = FXCollections.observableArrayList();
        awayPlayers = FXCollections.observableArrayList();
        //
        statPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        // game list set up
        gameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gameListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Game> observable, Game oldValue, Game newValue) -> {
            updateMatchSelection();
        });
        //
        statScreensManager = new StatScreensManager();
        statScreensManager.addScreen(generalScreen);
        statScreensManager.addScreen(overviewScreen);
        mainPane.getChildren().add(statScreensManager);
        statScreensManager.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        statScreensManager.setPrefSize(640, 500);
        statScreensManager.setLayoutX(270);
        statScreensManager.setLayoutY(140);
        //
        setStatPageState(StatPageState.ALL_GAMES);
    }

    private void setStatPageState(StatPageState state) {
        previousState = currentState;
        currentState = state;
        switch (currentState) {
            case ALL_GAMES:
                displayAllMatchs();
                break;
            case ONE_MATCH:
                displayOneMatch(previousState.equals(StatPageState.ALL_GAMES));
                break;
            case ONE_MATCH_HOME:
                displayOneMatchHome();
                break;
            case ONE_MATCH_AWAY:
                displayOneMatchAway();
                break;
        }
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    @Override
    public void loadParameters(Object... params) {
        homeTeam = (Team) params[0];
        homePlayers.add(homeP);
        homePlayers.addAll(homeTeam.getAllPlayers());
        homePlayerChoiceBox.setItems(homePlayers);
        homePlayerChoiceBox.getSelectionModel().select(homeP);
        //
        gameProcessor = new GameProcessor(homeTeam);
        //
        overviewScreen.loadParameters(gameProcessor);
        //
        games.setAll(homeTeam.getGames());
        gameListView.setItems(games);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //TODO
    }

    @FXML
    public void allMatchsAction(ActionEvent event) {
        //TODO log event
        setStatPageState(StatPageState.ALL_GAMES);
    }

    @FXML
    public void generalToggleBAction(ActionEvent event) {
        //TODO log event
        setStatPageState(StatPageState.ONE_MATCH);
    }

    @FXML
    public void homeToggleBAction(ActionEvent event) {
        //TODO log event
        setStatPageState(StatPageState.ONE_MATCH_HOME);
    }

    @FXML
    public void awayToggleBAction(ActionEvent event) {
        //TODO log event
        setStatPageState(StatPageState.ONE_MATCH_AWAY);
    }

    private void updateMatchSelection() {
        selectedGame = gameListView.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            //
            awayPlayers.clear();
            awayPlayers.add(awayP);
            awayPlayers.addAll(selectedGame.getAwayTeam().getAllPlayers());
            awayPlayerChoiceBox.setItems(awayPlayers);
            awayPlayerChoiceBox.getSelectionModel().select(awayP);
            //
            setStatPageState(StatPageState.ONE_MATCH);
        }
    }

    private void displayAllMatchs() {
        gameTeamSelectorBox.setVisible(false);
        gameListView.getSelectionModel().clearSelection();
        //TODO: optimize flow and send calculated parameters ?
        statScreensManager.setScreen(STAT_OVERVIEW_PANE, gameProcessor);
    }

    private void displayOneMatch(boolean fromAllMatchs) {
        if (fromAllMatchs && selectedGame != null) {
            statScreensManager.setScreen(STAT_GENERAL_PANE, gameProcessor.getGameStats(selectedGame));
        } else {
            generalScreen.loadParameters(gameProcessor.getGameStats(selectedGame));
        }
        gameTeamSelectorBox.setVisible(true);
        generalToggleB.setSelected(true);
        generalToggleB.setDisable(true);
        homeToggleB.setDisable(false);
        homeToggleB.setSelected(false);
        homePlayerChoiceBox.setDisable(true);
        homePlayerChoiceBox.getSelectionModel().select(homeP);
        awayToggleB.setDisable(false);
        awayToggleB.setSelected(false);
        awayPlayerChoiceBox.setDisable(true);
    }

    private void displayOneMatchHome() {
        gameTeamSelectorBox.setVisible(true);
        generalToggleB.setDisable(false);
        generalToggleB.setSelected(false);
        homeToggleB.setDisable(true);
        homeToggleB.setSelected(true);
        homePlayerChoiceBox.setDisable(false);
        homePlayerChoiceBox.getSelectionModel().select(homeP);
        awayToggleB.setDisable(false);
        awayToggleB.setSelected(false);
        awayPlayerChoiceBox.setDisable(true);
    }

    private void displayOneMatchAway() {
        gameTeamSelectorBox.setVisible(true);
        generalToggleB.setDisable(false);
        generalToggleB.setSelected(false);
        homeToggleB.setDisable(false);
        homeToggleB.setSelected(false);
        homePlayerChoiceBox.setDisable(true);
        awayToggleB.setDisable(true);
        awayToggleB.setSelected(true);
        awayPlayerChoiceBox.setDisable(false);
        awayPlayerChoiceBox.getSelectionModel().select(awayP);
    }
}
