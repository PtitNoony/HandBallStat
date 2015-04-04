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
import fr.noony.handstats.core.Fault;
import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.Team;
import fr.noony.handstats.court.GoalCageDrawing;
import fr.noony.handstats.court.HalfCourtDrawing;
import fr.noony.handstats.court.InteractiveShootingArea;
import fr.noony.handstats.court.LeftCourtDrawing;
import fr.noony.handstats.court.RightCourtDrawing;
import fr.noony.handstats.stats.GameStat;
import fr.noony.handstats.team.hmi.drawing.ButtonDrawing;
import static fr.noony.handstats.team.hmi.drawing.ButtonDrawing.BUTTON_CLICKED;
import fr.noony.handstats.team.hmi.drawing.ClockDrawing;
import fr.noony.handstats.team.hmi.drawing.GoalKeeperDrawing;
import fr.noony.handstats.team.hmi.drawing.PlayerDrawing;
import fr.noony.handstats.team.hmi.drawing.ScoreDisplayer;
import fr.noony.handstats.team.hmi.drawing.TeamDrawing;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Arnaud Hamon-Keromen
 */
public class MatchScoreBoardController extends FXController implements PropertyChangeListener {

    public static final Dimension DEFAULT_SCOREBOARD_CONTROLLER = new Dimension(1200, 675);
    private static final double DEFAUL_MINIATURE_SCALE = 0.25;
    private static final double DEFAULT_INNER_MARGIN = 20.0;
    private static final Logger LOG = Logger.getLogger(InteractiveShootingArea.class.getName());
    private static final Level LOG_LEVEL = Level.FINEST;

    private enum ScoringState {

        IDLE, IN_PLAY, PLAYER_SELECTED, SHOOTING_ZONE_SELECTED, GOAL_AREA_SELECTED, FAULT_SELECTED, ZONE
    }

    @FXML
    private AnchorPane scoreBoardPane;

    private Rectangle background;
    private Line verticalSeparator;
    private Line horizontalSeparator;
    //
    private TeamDrawing homeTeamDrawing;
    private TeamDrawing awayTeamDrawing;
    private ClockDrawing clockDrawing;
    private ScoreDisplayer scoreDisplayer;
    //
    private LeftCourtDrawing leftCourtDrawing;
    private RightCourtDrawing rightCourtDrawing;
    private GoalCageDrawing leftGoalAreaDrawing;
    private GoalCageDrawing rightGoalAreaDrawing;
    //
    private Team homeTeam;
    private Team awayTeam;
    private final List<Player> homeGoalKeepers = new LinkedList<>();
    private final List<Player> awayGoalKeepers = new LinkedList<>();
    private final List<GoalKeeperDrawing> homeGoalKeeperDrawings = new LinkedList<>();
    private final List<GoalKeeperDrawing> awayGoalKeeperDrawings = new LinkedList<>();
    //
    private Game game;
    private ScoringState state;
    private Player selectedPlayer;
    private PlayerDrawing selectedPlayerDrawing;
    private boolean selectedPlayerIsHome;
    //moche : fiare un truc un jour
    private boolean isValidFieldArea = false;
    private boolean isInsideGoalArea = false;
    private InteractiveShootingArea shootingZoneSelected;
    private InteractiveShootingArea goalAreaSelected;
    private ButtonDrawing leftValidateButton;
    private ButtonDrawing rightValidateButton;
    private ButtonDrawing leftYellowCardButton;
    private ButtonDrawing rightYellowCardButton;
    private ButtonDrawing left2MinButton;
    private ButtonDrawing right2MinButton;
    private ButtonDrawing leftRedCardButton;
    private ButtonDrawing rightRedCardButton;
    private ButtonDrawing leftCounteredButton;
    private ButtonDrawing rightCounteredButton;
    private ButtonDrawing left7MetersButton;
    private ButtonDrawing right7MetersButton;
    private ButtonDrawing cancelButton;
    private ButtonDrawing matchMenu;
    private ButtonDrawing statButton;
    private ImageView rightCourtImage;
    private ImageView leftCourtImage;
    private CustomPopup teamStatsPopUp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        background = new Rectangle(DEFAULT_SCOREBOARD_CONTROLLER.getWidth(), DEFAULT_SCOREBOARD_CONTROLLER.getHeight());
        background.setFill(Color.WHITESMOKE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(4.0);
        scoreBoardPane.getChildren().add(background);
        //
        createImages();
        createButtons();
        createCourt();
        createGoals();
        //
        homeTeamDrawing = new TeamDrawing(TeamDrawing.TeamSide.LEFT);
        scoreBoardPane.getChildren().add(homeTeamDrawing.getNode());
        homeTeamDrawing.setPosition(0, ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight());
        homeTeamDrawing.setSize(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0, DEFAULT_SCOREBOARD_CONTROLLER.getHeight() - ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight());
        //
        awayTeamDrawing = new TeamDrawing(TeamDrawing.TeamSide.RIGHT);
        scoreBoardPane.getChildren().add(awayTeamDrawing.getNode());
        awayTeamDrawing.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0, ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight());
        awayTeamDrawing.setSize(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0, DEFAULT_SCOREBOARD_CONTROLLER.getHeight() - ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight());
        //
        createSeparators();
        //
        //TODO string -> var
        teamStatsPopUp = new CustomPopup("StatPopupPage");
        teamStatsPopUp.getPopupScreen().getController().getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        setState(ScoringState.IN_PLAY);
    }

    @Override
    public void setWindow(Window window) {
        super.setWindow(window);
    }

    private void createButtons() {
        leftValidateButton = new ButtonDrawing(" ");
        rightValidateButton = new ButtonDrawing(" ");
        scoreBoardPane.getChildren().add(leftValidateButton.getNode());
        scoreBoardPane.getChildren().add(rightValidateButton.getNode());
        leftValidateButton.setPosition(430, 550);
        rightValidateButton.setPosition(430 + DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0, 550);
        leftValidateButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        rightValidateButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        matchMenu = new ButtonDrawing("MENU MATCH", Events.BACK_MACTH_MENU);
        scoreBoardPane.getChildren().add(matchMenu.getNode());
        matchMenu.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() - ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth(), DEFAULT_INNER_MARGIN);
        matchMenu.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        matchMenu.setInnerState(ButtonDrawing.ButtonInnerState.ENABLED);
        //
        cancelButton = new ButtonDrawing("ANNULER", Events.CANCEL_EVENT);
        scoreBoardPane.getChildren().add(cancelButton.getNode());
        cancelButton.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() - DEFAULT_INNER_MARGIN - ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth() * 2.0, DEFAULT_INNER_MARGIN);
        cancelButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        statButton = new ButtonDrawing("STATS", Events.TEAM_STATS);
        scoreBoardPane.getChildren().add(statButton.getNode());
        statButton.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() - 2.0 * DEFAULT_INNER_MARGIN - ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth() * 3.0, DEFAULT_INNER_MARGIN);
        statButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        statButton.setInnerState(ButtonDrawing.ButtonInnerState.ENABLED);
        //
        leftYellowCardButton = new ButtonDrawing("Carton Jaune", Events.YELLOW_CARD);
        scoreBoardPane.getChildren().add(leftYellowCardButton.getNode());
        leftYellowCardButton.setPosition(DEFAULT_INNER_MARGIN, ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT + 2.0 * DEFAULT_INNER_MARGIN);
        leftYellowCardButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        left2MinButton = new ButtonDrawing("2 Minutes", Events.TWO_MINUTES);
        scoreBoardPane.getChildren().add(left2MinButton.getNode());
        left2MinButton.setPosition(2.0 * DEFAULT_INNER_MARGIN + ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth(), ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT + 2.0 * DEFAULT_INNER_MARGIN);
        left2MinButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        leftRedCardButton = new ButtonDrawing("Carton Rouge", Events.RED_CARD);
        scoreBoardPane.getChildren().add(leftRedCardButton.getNode());
        leftRedCardButton.setPosition(3.0 * DEFAULT_INNER_MARGIN + 2.0 * ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth(), ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT + 2.0 * DEFAULT_INNER_MARGIN);
        leftRedCardButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        rightYellowCardButton = new ButtonDrawing("Carton Jaune", Events.YELLOW_CARD);
        scoreBoardPane.getChildren().add(rightYellowCardButton.getNode());
        rightYellowCardButton.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 + DEFAULT_INNER_MARGIN, ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT + 2.0 * DEFAULT_INNER_MARGIN);
        rightYellowCardButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        right2MinButton = new ButtonDrawing("2 Minutes", Events.TWO_MINUTES);
        scoreBoardPane.getChildren().add(right2MinButton.getNode());
        right2MinButton.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 + 2.0 * DEFAULT_INNER_MARGIN + ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth(), ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT + 2.0 * DEFAULT_INNER_MARGIN);
        right2MinButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        rightRedCardButton = new ButtonDrawing("Carton Rouge", Events.RED_CARD);
        scoreBoardPane.getChildren().add(rightRedCardButton.getNode());
        rightRedCardButton.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 + 3.0 * DEFAULT_INNER_MARGIN + 2.0 * ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth(), ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT + 2.0 * DEFAULT_INNER_MARGIN);
        rightRedCardButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        leftCounteredButton = new ButtonDrawing("Tir contré", Events.GOAL_COUNTERED);
        scoreBoardPane.getChildren().add(leftCounteredButton.getNode());
        leftCounteredButton.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 - ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth(), ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT);
        leftCounteredButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        rightCounteredButton = new ButtonDrawing("Tir contré", Events.GOAL_COUNTERED);
        scoreBoardPane.getChildren().add(rightCounteredButton.getNode());
        rightCounteredButton.setPosition(2.0 * DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 3.0 - ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth(), ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT);
        rightCounteredButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        left7MetersButton = new ButtonDrawing("Jet de 7 M", Events.SEVEN_METERS_GOAL);
        scoreBoardPane.getChildren().add(left7MetersButton.getNode());
        left7MetersButton.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 - DEFAULT_INNER_MARGIN - ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth(), ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + (HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT - ButtonDrawing.DEFAULT_BUTTON_SIZE.getHeight()) / 2.0);
        left7MetersButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //
        right7MetersButton = new ButtonDrawing("Jet de 7 M", Events.SEVEN_METERS_GOAL);
        scoreBoardPane.getChildren().add(right7MetersButton.getNode());
        right7MetersButton.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() - DEFAULT_INNER_MARGIN - ButtonDrawing.DEFAULT_BUTTON_SIZE.getWidth(), ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight() + (HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT - ButtonDrawing.DEFAULT_BUTTON_SIZE.getHeight()) / 2.0);
        right7MetersButton.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
        //

    }

    private void createImages() {
        leftCourtImage = new ImageView();
        scoreBoardPane.getChildren().add(leftCourtImage);
        leftCourtImage.setFitWidth(HalfCourtDrawing.DEFAULT_HALF_COURT_WIDTH * DEFAUL_MINIATURE_SCALE);
        leftCourtImage.setFitHeight(HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT * DEFAUL_MINIATURE_SCALE);
        leftCourtImage.setX(DEFAULT_INNER_MARGIN);
        leftCourtImage.setY(160);
        createImageInteractivity(leftCourtImage);
        rightCourtImage = new ImageView();
        scoreBoardPane.getChildren().add(rightCourtImage);
        rightCourtImage.setFitWidth(HalfCourtDrawing.DEFAULT_HALF_COURT_WIDTH * DEFAUL_MINIATURE_SCALE);
        rightCourtImage.setFitHeight(HalfCourtDrawing.DEFAULT_HALF_COURT_HEIGHT * DEFAUL_MINIATURE_SCALE);
        rightCourtImage.setX(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 + DEFAULT_INNER_MARGIN);
        rightCourtImage.setY(160);
        createImageInteractivity(rightCourtImage);
    }

    private void createCourt() {
        leftCourtDrawing = new LeftCourtDrawing();
        rightCourtDrawing = new RightCourtDrawing();
        leftCourtDrawing.setPosition(20, 160);
        rightCourtDrawing.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 + DEFAULT_INNER_MARGIN, 160);
        scoreBoardPane.getChildren().add(leftCourtDrawing.getNode());
        scoreBoardPane.getChildren().add(rightCourtDrawing.getNode());
        rightCourtDrawing.getInteractiveAreas().stream().forEach(interactiveArea
                -> interactiveArea.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this));
        leftCourtDrawing.getInteractiveAreas().stream().forEach(interactiveArea
                -> interactiveArea.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this));

    }

    private void createGoals() {
        leftGoalAreaDrawing = new GoalCageDrawing();
        rightGoalAreaDrawing = new GoalCageDrawing();
        scoreBoardPane.getChildren().add(leftGoalAreaDrawing.getNode());
        scoreBoardPane.getChildren().add(rightGoalAreaDrawing.getNode());
        leftGoalAreaDrawing.setPosition(2.0 * DEFAULT_INNER_MARGIN + HalfCourtDrawing.DEFAULT_HALF_COURT_WIDTH * DEFAUL_MINIATURE_SCALE, 160);
        rightGoalAreaDrawing.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 + 2.0 * DEFAULT_INNER_MARGIN + HalfCourtDrawing.DEFAULT_HALF_COURT_WIDTH * DEFAUL_MINIATURE_SCALE, 160);
        leftGoalAreaDrawing.getInteractiveAreas().stream().forEach(interactiveArea
                -> interactiveArea.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this));
        rightGoalAreaDrawing.getInteractiveAreas().stream().forEach(interactiveArea
                -> interactiveArea.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this));
    }

    private void createGoalKeepersButtons() {
        createHomeGoalKeepersButtons();
        createAwayGoalKeepersButtons();
    }

    private void createHomeGoalKeepersButtons() {
        homeTeam.getActivePlayers().stream().filter(p -> p.getPositionActuelle().equals(Poste.GARDIEN))
                .forEach(p -> homeGoalKeepers.add(p));
        for (Player goal : homeGoalKeepers) {
            GoalKeeperDrawing goalKeeperDrawing = new GoalKeeperDrawing(goal, GoalKeeperDrawing.GOAL_SAVE_ACTION_EVENT);
            goalKeeperDrawing.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
            homeGoalKeeperDrawings.add(goalKeeperDrawing);
            int index = homeGoalKeepers.indexOf(goal);
            //TODO: set proper goal cage position and use it here
            double x = 2.0 * DEFAULT_INNER_MARGIN + HalfCourtDrawing.DEFAULT_HALF_COURT_WIDTH * DEFAUL_MINIATURE_SCALE
                    + index * (DEFAULT_INNER_MARGIN + GoalKeeperDrawing.STANDARD_PLAYER_HEIGHT);
            double y = 160 + DEFAULT_INNER_MARGIN + GoalCageDrawing.DEFAULT_GOAL_CAGE_HEIGHT;
            goalKeeperDrawing.setPosition(x, y);
            //TODO: fix setSize
            scoreBoardPane.getChildren().add(goalKeeperDrawing.getNode());
        }
    }

    private void createAwayGoalKeepersButtons() {
        awayTeam.getActivePlayers().stream().filter(p -> p.getPositionActuelle().equals(Poste.GARDIEN))
                .forEach(p -> awayGoalKeepers.add(p));
        for (Player goal : awayGoalKeepers) {
            GoalKeeperDrawing goalKeeperDrawing = new GoalKeeperDrawing(goal, GoalKeeperDrawing.GOAL_SAVE_ACTION_EVENT);
            goalKeeperDrawing.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
            awayGoalKeeperDrawings.add(goalKeeperDrawing);
            int index = awayGoalKeepers.indexOf(goal);
            double x = DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 + 2.0 * DEFAULT_INNER_MARGIN + HalfCourtDrawing.DEFAULT_HALF_COURT_WIDTH * DEFAUL_MINIATURE_SCALE
                    + index * (DEFAULT_INNER_MARGIN + GoalKeeperDrawing.STANDARD_PLAYER_HEIGHT);
            //TODO: set proper goal cage position and use it here
            double y = 160 + DEFAULT_INNER_MARGIN + GoalCageDrawing.DEFAULT_GOAL_CAGE_HEIGHT;
            goalKeeperDrawing.setPosition(x, y);
            //TODO: fix setSize
            scoreBoardPane.getChildren().add(goalKeeperDrawing.getNode());
        }
    }

    private void createImageInteractivity(ImageView image) {
        image.setOnMouseEntered((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "Event {0} on MatchScoreBoardController {1}", new Object[]{event, this});
            ColorAdjust colorAdjust = new ColorAdjust(0.5, 1, 0.5, 0.3);
            image.setEffect(colorAdjust);
        });
        image.setOnMouseExited((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "Event {0} on MatchScoreBoardController {1}", new Object[]{event, this});
            image.setEffect(null);
        });
        image.setOnMouseClicked((MouseEvent event) -> {
            LOG.log(LOG_LEVEL, "Event {0} on MatchScoreBoardController {1}", new Object[]{event, this});
            processImageClicked(image);
        });
    }

    private void createSeparators() {
        clockDrawing = new ClockDrawing();
        scoreBoardPane.getChildren().add(clockDrawing.getNode());
        //
        verticalSeparator = new Line(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0, 0,
                DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0, DEFAULT_SCOREBOARD_CONTROLLER.getHeight());
        verticalSeparator.setStrokeWidth(4.0);
        horizontalSeparator = new Line(0.0, ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight(),
                DEFAULT_SCOREBOARD_CONTROLLER.getWidth(), ClockDrawing.DEFAULT_CLOCK_DIMENSION.getHeight());
        horizontalSeparator.setStrokeWidth(4.0);
        scoreBoardPane.getChildren().add(verticalSeparator);
        scoreBoardPane.getChildren().add(horizontalSeparator);
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    private void processImageClicked(ImageView image) {
        LOG.log(LOG_LEVEL, "Image clicked : {0}", new Object[]{image});
        setState(ScoringState.PLAYER_SELECTED);
    }

    private void setState(ScoringState scoringState) {
        state = scoringState;
        switch (scoringState) {
            case IN_PLAY:
                displayAsIdle();
                break;
            case PLAYER_SELECTED:
                displayAsPlayerSelected();
                break;
            case SHOOTING_ZONE_SELECTED:
                displayAsShootingSelected();
                break;
            case ZONE:
                displayAsZone();
                break;
            case GOAL_AREA_SELECTED:
                displayAsGoalAreaSelected();
                break;
            default:
                throw new IllegalStateException("scoringState ::" + scoringState);
        }
    }

    private void displayAsIdle() {
        //TODO: masquer les bouttons jets de 7 m
        homeTeamDrawing.setVisible(true);
        awayTeamDrawing.setVisible(true);
        rightCourtDrawing.setVisibilty(false);
        leftCourtDrawing.setVisibilty(false);
        rightGoalAreaDrawing.setVisibility(false);
        leftGoalAreaDrawing.setVisibility(false);
        leftValidateButton.setVisible(false);
        rightValidateButton.setVisible(false);
        setHomeGoalKeepersVisible(false);
        setAwayGoalKeepersVisible(false);
        if (selectedPlayerDrawing != null) {
            selectedPlayerDrawing.setSelectedState(PlayerDrawing.SelectedState.IDLE);
            selectedPlayerDrawing = null;
        }
        cancelButton.setInnerState(ButtonDrawing.ButtonInnerState.DISABLED);
        setLeftFaultButtonsVisible(false);
        setRightFaultButtonsVisible(false);
        leftCounteredButton.setVisible(false);
        rightCounteredButton.setVisible(false);
    }

    private void displayAsPlayerSelected() {
        //TODO:: tenir compte du coté duquel ils jouent
        if (selectedPlayerIsHome) {
            homeTeamDrawing.setVisible(true);
            awayTeamDrawing.setVisible(false);
            rightCourtDrawing.setVisibilty(true);
            leftCourtDrawing.setVisibilty(false);
            setRightFaultButtonsVisible(true);
            setLeftFaultButtonsVisible(false);
        } else {
            homeTeamDrawing.setVisible(false);
            awayTeamDrawing.setVisible(true);
            rightCourtDrawing.setVisibilty(false);
            leftCourtDrawing.setVisibilty(true);
            setRightFaultButtonsVisible(false);
            setLeftFaultButtonsVisible(true);
        }
        rightGoalAreaDrawing.setVisibility(false);
        leftGoalAreaDrawing.setVisibility(false);
        leftValidateButton.setVisible(false);
        rightValidateButton.setVisible(false);
        leftCounteredButton.setVisible(false);
        rightCounteredButton.setVisible(false);
        setHomeGoalKeepersVisible(false);
        setAwayGoalKeepersVisible(false);
        selectedPlayerDrawing.setSelectedState(PlayerDrawing.SelectedState.SELECTED);
        cancelButton.setInnerState(ButtonDrawing.ButtonInnerState.ENABLED);
    }

    private void displayAsShootingSelected() {
        if (selectedPlayerIsHome) {
            awayTeamDrawing.setVisible(false);
            leftGoalAreaDrawing.setVisibility(false);
            leftValidateButton.setVisible(false);
            leftCourtImage.setVisible(true);
            //
            homeTeamDrawing.setVisible(true);
            rightValidateButton.setVisible(false);
            rightGoalAreaDrawing.setVisibility(true);
            WritableImage snapshot = rightCourtDrawing.getNode().snapshot(new SnapshotParameters(), null);
            rightCourtImage.setImage(snapshot);
            rightCourtImage.setVisible(true);
            leftCounteredButton.setVisible(false);
            rightCounteredButton.setVisible(true);
        } else {
            homeTeamDrawing.setVisible(false);
            awayTeamDrawing.setVisible(true);
            rightGoalAreaDrawing.setVisibility(false);
            leftGoalAreaDrawing.setVisibility(true);
            leftValidateButton.setVisible(false);
            rightValidateButton.setVisible(false);
            WritableImage snapshot = leftCourtDrawing.getNode().snapshot(new SnapshotParameters(), null);
            leftCourtImage.setImage(snapshot);
            leftCourtImage.setVisible(true);
            rightCourtImage.setVisible(false);
            leftCounteredButton.setVisible(true);
            rightCounteredButton.setVisible(false);
        }
        rightCourtDrawing.setVisibilty(false);
        leftCourtDrawing.setVisibilty(false);
        setHomeGoalKeepersVisible(false);
        setAwayGoalKeepersVisible(false);
        setRightFaultButtonsVisible(false);
        setLeftFaultButtonsVisible(false);
        cancelButton.setInnerState(ButtonDrawing.ButtonInnerState.ENABLED);
    }

    private void displayAsZone() {
        if (selectedPlayerIsHome) {
            awayTeamDrawing.setVisible(false);
            leftGoalAreaDrawing.setVisibility(false);
            leftValidateButton.setVisible(false);
            leftCourtImage.setVisible(false);
            //
            homeTeamDrawing.setVisible(true);
            rightValidateButton.setVisible(true);
            rightValidateButton.setText("VALIDER");
            rightGoalAreaDrawing.setVisibility(false);
            rightCourtImage.setVisible(false);
            rightCourtDrawing.setVisibilty(true);
        } else {
            awayTeamDrawing.setVisible(true);
            leftCourtDrawing.setVisibilty(true);
            leftGoalAreaDrawing.setVisibility(false);
            leftValidateButton.setVisible(true);
            leftValidateButton.setText("VALIDER");
            leftCourtImage.setVisible(false);
            //
            homeTeamDrawing.setVisible(false);
            rightValidateButton.setVisible(false);
            rightGoalAreaDrawing.setVisibility(false);
            rightCourtDrawing.setVisibilty(false);
            rightCourtImage.setVisible(false);
        }
        cancelButton.setInnerState(ButtonDrawing.ButtonInnerState.ENABLED);
        setHomeGoalKeepersVisible(false);
        setAwayGoalKeepersVisible(false);
        setRightFaultButtonsVisible(false);
        setLeftFaultButtonsVisible(false);
        leftCounteredButton.setVisible(false);
        rightCounteredButton.setVisible(false);
    }

    private void displayAsGoalAreaSelected() {
        if (selectedPlayerIsHome) {
            awayTeamDrawing.setVisible(false);
            leftGoalAreaDrawing.setVisibility(false);
            leftValidateButton.setVisible(false);
            leftCourtImage.setVisible(true);
            homeTeamDrawing.setVisible(true);
            rightValidateButton.setVisible(true);
            if (isValidFieldArea && isInsideGoalArea) {
                rightValidateButton.setText("BUT !!");
            } else {
                rightValidateButton.setText("RATE...");
            }
            rightGoalAreaDrawing.setVisibility(true);
            setHomeGoalKeepersVisible(false);
            setAwayGoalKeepersVisible(true);
            rightCourtImage.setVisible(true);
        } else {
            homeTeamDrawing.setVisible(false);
            setHomeGoalKeepersVisible(true);
            awayTeamDrawing.setVisible(true);
            rightGoalAreaDrawing.setVisibility(false);
            leftGoalAreaDrawing.setVisibility(true);
            setAwayGoalKeepersVisible(false);
            leftValidateButton.setVisible(true);
            rightValidateButton.setVisible(false);
            leftCourtImage.setVisible(true);
            rightCourtImage.setVisible(false);
            if (isValidFieldArea && isInsideGoalArea) {
                leftValidateButton.setText("BUT !!");
            } else {
                leftValidateButton.setText("RATE...");
            }
        }
        rightCourtDrawing.setVisibilty(false);
        leftCourtDrawing.setVisibilty(false);
        cancelButton.setInnerState(ButtonDrawing.ButtonInnerState.ENABLED);
        setRightFaultButtonsVisible(false);
        setLeftFaultButtonsVisible(false);
        leftCounteredButton.setVisible(false);
        rightCounteredButton.setVisible(false);
    }

    private void setHomeGoalKeepersVisible(boolean visiblity) {
        homeGoalKeeperDrawings.stream().forEach(goalKeeperDrawing
                -> goalKeeperDrawing.setVisible(visiblity));
    }

    private void setAwayGoalKeepersVisible(boolean visiblity) {
        awayGoalKeeperDrawings.stream().forEach(goalKeeperDrawing
                -> goalKeeperDrawing.setVisible(visiblity));
    }

    private void setLeftFaultButtonsVisible(boolean visibility) {
        left2MinButton.setVisible(visibility);
        leftYellowCardButton.setVisible(visibility);
        leftRedCardButton.setVisible(visibility);
    }

    private void setRightFaultButtonsVisible(boolean visibility) {
        right2MinButton.setVisible(visibility);
        rightYellowCardButton.setVisible(visibility);
        rightRedCardButton.setVisible(visibility);
    }

    @Override
    public void loadParameters(Object... params) {
        //TODO
        if (params.length > 1) {
            homeTeam = (Team) params[0];
            if (params.length > 1) {
                awayTeam = (Team) params[1];
            } else {
                awayTeam = (Team) params[0];
            }
            game = new Game();
            game.setUpGame(homeTeam, awayTeam, LocalDate.now());
        } else if (params.length == 1) {
            game = (Game) params[0];
            homeTeam = game.getHomeTeam();
            awayTeam = game.getAwayTeam();
        }
        homeTeamDrawing.setTeam(homeTeam);
        awayTeamDrawing.setTeam(awayTeam);
        //
        homeTeamDrawing.getPlayerDrawings().stream().forEach(playerDrawing
                -> playerDrawing.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this));
        awayTeamDrawing.getPlayerDrawings().stream().forEach(playerDrawing
                -> playerDrawing.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this));
        //
        createGoalKeepersButtons();
        //TODO::
        clockDrawing.setGameClock(game.getGameClock());
        scoreDisplayer = new ScoreDisplayer(game);
        scoreBoardPane.getChildren().add(scoreDisplayer.getNode());
        scoreDisplayer.setPosition(DEFAULT_SCOREBOARD_CONTROLLER.getWidth() / 2.0 - ScoreDisplayer.DEFAULT_SCORE_DISPLAYER_DIMENSION.getWidth() / 2.0, 0.0);
        //TODO: set IDle
        setState(ScoringState.IN_PLAY);
        homeTeamDrawing.setFill(homeTeam.getPreferedColor());
        awayTeamDrawing.setFill(awayTeam.getPreferedColor());
        scoreDisplayer.setColors(homeTeam.getPreferedColor(), awayTeam.getPreferedColor());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case InteractiveShootingArea.INTERACTIVE_ZONE_CLICKED:
                processShootingZoneClicked((InteractiveShootingArea) evt.getNewValue(), (boolean) evt.getOldValue());
                break;
            case PlayerDrawing.PLAYER_ZONE_CLICKED:
                processPlayerClicked((PlayerDrawing) evt.getOldValue(), (Player) evt.getNewValue());
                break;
            case BUTTON_CLICKED:
                processValidation();
                break;
            case Events.CANCEL_EVENT:
                processCancel();
                break;
            case GoalKeeperDrawing.GOAL_SAVE_ACTION_EVENT:
                processGoalSave((Player) evt.getNewValue());
                break;
            case Events.YELLOW_CARD:
                processYellowCard();
                break;
            case Events.RED_CARD:
                processRedCard();
                break;
            case Events.TWO_MINUTES:
                process2Minutes();
                break;
            case Events.SEVEN_METERS_GOAL:
                processSevenMeterAttempt();
                break;
            case Events.GOAL_COUNTERED:
                processGoalCountered();
                break;
            case Events.TEAM_STATS:
                System.err.println(" trigger pop up");
//                teamStatsPopUp.setOpacity(1.0);
//                teamStatsPopUp.setWidth(670);
//                teamStatsPopUp.setHeight(630);
                teamStatsPopUp.show(getWindow(), 0.0, 0.0);
                final GameStat gameStat = new GameStat(game);
                teamStatsPopUp.loadParameters(gameStat);
                break;
            case Events.BACK_TO_GAME:
                teamStatsPopUp.hide();
                break;
            default:
                throw new UnsupportedOperationException("ppty change event " + evt.getPropertyName());
        }
    }

    private void processPlayerClicked(PlayerDrawing playerDrawing, Player player) {
        switch (state) {
            case IN_PLAY:
                setFirstTimePlayerSelected(playerDrawing, player);
                break;
            case PLAYER_SELECTED:
                updatePlayerSelected(playerDrawing, player);
                break;
            case SHOOTING_ZONE_SELECTED:
                updatePlayerSelected(playerDrawing, player);
                break;
            case GOAL_AREA_SELECTED:
                updatePlayerSelected(playerDrawing, player);
                break;
            default:
                throw new UnsupportedOperationException("Fail to process player clicked while in state " + state);
        }
    }

    private void setFirstTimePlayerSelected(PlayerDrawing playerDrawing, Player player) {
        selectedPlayerDrawing = playerDrawing;
        selectedPlayer = player;
        selectedPlayerIsHome = homeTeam.containsActivePlayer(selectedPlayer);
        setState(ScoringState.PLAYER_SELECTED);
    }

    private void updatePlayerSelected(PlayerDrawing playerDrawing, Player player) {
        selectedPlayerDrawing.setSelectedState(PlayerDrawing.SelectedState.IDLE);
        selectedPlayerDrawing = playerDrawing;
        selectedPlayer = player;
        selectedPlayerDrawing.setSelectedState(PlayerDrawing.SelectedState.SELECTED);
    }

    private void processYellowCard() {
        game.addFault(selectedPlayer, selectedPlayerIsHome, Fault.YELLOW_CARD);
        clearAllFields();
    }

    private void processRedCard() {
        game.addFault(selectedPlayer, selectedPlayerIsHome, Fault.RED_CARD);
        clearAllFields();
    }

    private void process2Minutes() {
        game.addFault(selectedPlayer, selectedPlayerIsHome, Fault.TWO_MINUTES);
        clearAllFields();
    }

    private void processShootingZoneClicked(InteractiveShootingArea interactiveArea, boolean canScore) {
        switch (state) {
            case PLAYER_SELECTED:
                processFieldZoneClicked(interactiveArea, canScore);
                break;
            case SHOOTING_ZONE_SELECTED:
                isInsideGoalArea = canScore;
                processGoalAreaClicked(interactiveArea);
                break;
            case GOAL_AREA_SELECTED:
                isInsideGoalArea = canScore;
                processGoalAreaClicked(interactiveArea);
                break;
            case ZONE:
                processFieldZoneClicked(interactiveArea, canScore);
                break;
            default:
                throw new UnsupportedOperationException("process shooting zone clicked in state:" + state + " " + interactiveArea);
        }
    }

    private void processSevenMeterAttempt() {
        if (shootingZoneSelected != null) {
            shootingZoneSelected.setSelectedState(PlayerDrawing.SelectedState.IDLE);
        }
        if (selectedPlayerIsHome) {
            shootingZoneSelected = createRightPenalty();
        } else {
            shootingZoneSelected = createLeftPenalty();
        }
        isValidFieldArea = true;
        setState(ScoringState.SHOOTING_ZONE_SELECTED);
    }

    private void processGoalCountered() {
        game.addGoalCountered(selectedPlayer, selectedPlayerIsHome, shootingZoneSelected);
        clearAllFields();
    }

    private void processFieldZoneClicked(InteractiveShootingArea interactiveArea, boolean canScore) {
        if (shootingZoneSelected != null) {
            shootingZoneSelected.setSelectedState(PlayerDrawing.SelectedState.IDLE);
        }
        shootingZoneSelected = interactiveArea;
        shootingZoneSelected.setSelectedState(PlayerDrawing.SelectedState.SELECTED);
        isValidFieldArea = canScore;
        if (canScore) {
            setState(ScoringState.SHOOTING_ZONE_SELECTED);
        } else {
            setState(ScoringState.ZONE);
        }
    }

    private void processGoalAreaClicked(InteractiveShootingArea interactiveArea) {
        if (goalAreaSelected != null) {
            //shall not happen
            goalAreaSelected.setSelectedState(PlayerDrawing.SelectedState.IDLE);
        }
        goalAreaSelected = interactiveArea;
        goalAreaSelected.setSelectedState(PlayerDrawing.SelectedState.SELECTED);
        setState(ScoringState.GOAL_AREA_SELECTED);
    }

    private void processGoalSave(Player goal) {
        game.addGoalStop(goal, selectedPlayer, selectedPlayerIsHome, shootingZoneSelected, goalAreaSelected);
        clearAllFields();
    }

    private void processValidation() {
        if (isInsideGoalArea && isValidFieldArea) {
            game.score(selectedPlayer, selectedPlayerIsHome, shootingZoneSelected, goalAreaSelected);
        }
        //TODO: tester si marqué ou non
        //TODO: mettre à jour le modele
        //clean the interactive areas
        clearAllFields();
    }

    private void processCancel() {
        clearAllFields();
        cancelButton.setInnerState(ButtonDrawing.ButtonInnerState.DISABLED);
    }

    private void clearAllFields() {
        if (shootingZoneSelected != null) {
            shootingZoneSelected.setSelectedState(PlayerDrawing.SelectedState.IDLE);
            shootingZoneSelected = null;
        }
        if (goalAreaSelected != null) {
            goalAreaSelected.setSelectedState(PlayerDrawing.SelectedState.IDLE);
            goalAreaSelected = null;
        }
        if (selectedPlayer != null) {
            selectedPlayer = null;
        }
        if (selectedPlayerDrawing != null) {
            selectedPlayerDrawing.setSelectedState(PlayerDrawing.SelectedState.IDLE);
            selectedPlayerDrawing = null;
        }
        isInsideGoalArea = false;
        isValidFieldArea = false;
        setState(ScoringState.IN_PLAY);
    }

    private InteractiveShootingArea createLeftPenalty() {
        return new InteractiveShootingArea(null, new Circle(), Color.AQUA, true, "LEFT_PENALTY");
    }

    private InteractiveShootingArea createRightPenalty() {
        return new InteractiveShootingArea(null, new Circle(), Color.AQUA, true, "RIGHT_PENALTY");
    }
}
