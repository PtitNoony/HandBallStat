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
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 * FXML Controller class
 *
 * @author Arnaud Hamon-Keromen
 */
public class LoadingPageController extends FXController {

    @FXML
    private Label loadingLabel;
    @FXML
    private ProgressIndicator loadingIndicator;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadingLabel.setText("Chargement de l'application");
    }

    @Override
    public void updateSize(double width, double height) {
        //nothing to do
    }

    @Override
    public void loadParameters(Object... params) {
        //nothing to do
        double percentage = (double) params[0];
        loadingIndicator.setProgress(percentage);
    }

}
