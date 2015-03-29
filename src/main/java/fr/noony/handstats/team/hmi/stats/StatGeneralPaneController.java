/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.noony.handstats.team.hmi.stats;

import fr.noony.handstats.stats.GameStat;
import fr.noony.handstats.team.hmi.FXController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class StatGeneralPaneController extends FXController implements PropertyChangeListener {

    @FXML
    public Accordion accordion;
    @FXML
    public TitledPane summaryPane;

    @FXML
    public Label isVictoryLabel;
    @FXML
    public Label dateLabel;
    @FXML
    public Label opponentLabel;
    @FXML
    public Label scoreLabel;
    @FXML
    public Label homeShotsLabel;
    @FXML
    public Label awayShotsLabel;
    @FXML
    public Label awayStopsLabel;
    @FXML
    public Label homeStopsLabel;

    //
    private GameStat gameStat;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accordion.setExpandedPane(summaryPane);
    }

    @Override
    public void updateSize(double width, double height) {
    }

    @Override
    public void loadParameters(Object... params) {
        //TODO: checks
        gameStat = (GameStat) params[0];
        opponentLabel.setText(gameStat.getAwayTeam().getName());
        if (gameStat.isHomeVictor()) {
            isVictoryLabel.setText("VICTOIRE");
        } else if (gameStat.isDraw()) {
            isVictoryLabel.setText("EGALITE");
        } else {
            isVictoryLabel.setText("DEFAITE");
        }
        dateLabel.setText(gameStat.getGameDate().toString());
        //oula
        String score = "" + gameStat.getHomeScore() + " - " + gameStat.getAwayScore();
        scoreLabel.setText(score);
        homeShotsLabel.setText("" + gameStat.getHomeAccuracy());
        homeStopsLabel.setText("" + gameStat.getHomeShotBlockedPercentage());
        awayShotsLabel.setText("" + gameStat.getAwayAccuracy());
        awayStopsLabel.setText("" + gameStat.getAwayShotBlockedPercentage());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

}
