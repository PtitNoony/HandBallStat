/*
 * Copyright (C) 2015 HAMON-KEROMEN A.
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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Popup;
import javax.swing.SwingUtilities;

/**
 * FXML Controller class
 *
 * @author Arnaud
 */
public class ExitController extends FXController {

    private Popup popup;
    private MainScreensController screensController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void quitSaveAction(ActionEvent event) {
        boolean saved = screensController.saveTeam();
        if (saved) {
            SwingUtilities.invokeLater(() -> Platform.runLater(() -> Platform.exit()));
        }
    }

    @FXML
    private void quitNoSaveAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        popup.hide();
    }

    @Override
    public void updateSize(double width, double height) {
        //
    }

    @Override
    public void loadParameters(Object... params) {
        //TODO: CHECK
        popup = (Popup) params[0];
        screensController = (MainScreensController) params[1];
    }

}
