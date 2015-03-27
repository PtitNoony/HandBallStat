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
import fr.noony.handstats.core.Player;
import static fr.noony.handstats.team.hmi.Events.CANCEL_EVENT;
import static fr.noony.handstats.team.hmi.Events.OK_EVENT;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Arnaud Hamon-Keromen
 */
public class PlayerEditorController extends FXController implements PropertyChangeListener {

    @FXML
    private Button okB;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField numeroField;
    @FXML
    private Label prenomL;
    @FXML
    private Label nomL;
    @FXML
    private Label numeroL;
    @FXML
    private Label posteL;
    @FXML
    private ChoiceBox posteSelecteur;

    private boolean validity = false;
    private int number;
    private String name;
    private String lastName;
    private Poste poste = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        okB.setDisable(true);
        prenomL.setDisable(true);
        nomL.setDisable(true);
        numeroL.setDisable(true);
        posteL.setDisable(true);
        posteSelecteur.setItems(FXCollections.observableList(Arrays.asList(Poste.values())));
        posteSelecteur.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            Logger.getLogger(PlayerEditorController.class.getName()).log(Level.FINEST, "{0} {1} {2}", new Object[]{observable, oldValue, newValue});
            checkPlayerValidity();
        });
        prenomField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Logger.getLogger(PlayerEditorController.class.getName()).log(Level.FINEST, "{0} {1} {2}", new Object[]{observable, oldValue, newValue});
            checkPlayerValidity();
        });
        nomField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Logger.getLogger(PlayerEditorController.class.getName()).log(Level.FINEST, "{0} {1} {2}", new Object[]{observable, oldValue, newValue});
            checkPlayerValidity();
        });
        numeroField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Logger.getLogger(PlayerEditorController.class.getName()).log(Level.FINEST, "{0} {1} {2}", new Object[]{observable, oldValue, newValue});
            checkPlayerValidity();
        });
    }

    private boolean checkPlayerValidity() {
        boolean numberOK = checkNumber();
        boolean prenomOK = checkName();
        boolean nomOK = checkLastName();
        boolean posteOK = checkPoste();
        prenomL.setDisable(!prenomOK);
        nomL.setDisable(!nomOK);
        numeroL.setDisable(!numberOK);
        posteL.setDisable(!posteOK);
        validity = numberOK && prenomOK && nomOK && posteOK;
        okB.setDisable(!validity);
        return validity;
    }

    private boolean checkNumber() {
        String numeroS = numeroField.getText();
        try {
            number = Integer.parseInt(numeroS);
            return true;
        } catch (Exception e) {
            Logger.getLogger(PlayerEditorController.class.getName()).log(Level.SEVERE, "While cheking player number :: {0}", new Object[]{e});
            return false;
        }
    }

    private boolean checkName() {
        String nameS = prenomField.getText();
        if (!"".equals(nameS)) {
            name = nameS;
            return true;
        }
        return false;
    }

    private boolean checkLastName() {
        String lastNameS = nomField.getText();
        if (!"".equals(lastNameS)) {
            lastName = lastNameS;
            return true;
        }
        return false;
    }

    private boolean checkPoste() {
        try {
            String posteS = posteSelecteur.getSelectionModel().getSelectedItem().toString();
            poste = Poste.valueOf(posteS);
            return true;
        } catch (Exception e) {
            Logger.getLogger(PlayerEditorController.class.getName()).log(Level.SEVERE, "While cheking player poste :: {0}", new Object[]{e});
            return false;
        }
    }

    @FXML
    public void annulerAction(ActionEvent event) {
        firePropertyChange(CANCEL_EVENT, null, null);
    }

    @FXML
    public void okAction(ActionEvent event) {
        firePropertyChange(OK_EVENT, null, new Player(name, lastName, number, poste));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    @Override
    public void loadParameters(Object... params) {
        //TODO
    }

    public void resetFields() {
        okB.setDisable(true);
        prenomL.setDisable(true);
        nomL.setDisable(true);
        numeroL.setDisable(true);
        posteL.setDisable(true);
        prenomField.setText("");
        nomField.setText("");
        numeroField.setText("");
        posteSelecteur.getSelectionModel().clearSelection();
    }

}
