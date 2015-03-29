/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.noony.handstats.team.hmi.stats;

import fr.noony.handstats.stats.GameProcessor;
import fr.noony.handstats.team.hmi.FXController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Arnaud
 */
public class StatOverviewPaneController extends FXController implements PropertyChangeListener {

    @FXML
    public Label nbPlayedLabel;
    @FXML
    public Label nbWonLabel;
    @FXML
    public Label percentWonLabel;
    @FXML
    public Label percentShotsLabel;
    @FXML
    public Label percentShotBlockedLabel;

    //
    private GameProcessor gameProcessor = null;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nbPlayedLabel.setText("");
        nbWonLabel.setText("");
        percentWonLabel.setText("");
        percentShotsLabel.setText("");
        percentShotBlockedLabel.setText("");
    }

    @Override
    public void updateSize(double width, double height) {
    }

    @Override
    public void loadParameters(Object... params) {
        //TODO: checks
        gameProcessor = (GameProcessor) params[0];
        if (gameProcessor != null) {
            updateInfos();
        }
    }

    private void updateInfos() {
        nbPlayedLabel.setText("" + gameProcessor.getNbGamesPlayed());
        nbWonLabel.setText("" + gameProcessor.getNbVictories());
        percentWonLabel.setText("" + gameProcessor.getPercentageVictories() + "%");
        percentShotsLabel.setText("" + gameProcessor.getPercentageShotMade() + "%");
        percentShotBlockedLabel.setText("" + gameProcessor.getPercentageStops() + "%");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

}
