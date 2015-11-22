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
package fr.noony.handstats.team.newhmi.statviewer;

import fr.noony.handstats.core.Game;
import fr.noony.handstats.team.newhmi.FXScreen;
import fr.noony.handstats.team.newhmi.FXScreenUtils;
import fr.noony.handstats.team.newhmi.MainCore;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

/**
 *
 * @author Arnaud
 */
public class GameSelectionScreen extends FXScreen {

    public static final String DISPLAY_GAME_STAT = "displayGameStat";

    private final static double UPPER_LABEL_RATIO = 0.08;
    private final static double GAME_LIST_RATIO = 0.5;

    //
    private final Label summaryLabel;
    private final Line verticalSeparation;
    private final Label instructionLabel;
    private final ListView gameList;
    private final Button gameChoiceButton;
    //for debug
    private final Rectangle background;

    //for calculations
    double intructionLabelWidth;
    double intructionLabelHeight;
    double sceneHeight;

    public GameSelectionScreen() {
        super();
        background = new Rectangle();
        instructionLabel = new Label("Choix du match à analyser");
        summaryLabel = new Label("Résumé des matchs");
        verticalSeparation = new Line();
        gameList = new ListView();
        gameChoiceButton = new Button("Choisir le match");
        Platform.runLater(() -> initGameSelectionScreen());
    }

    private void initGameSelectionScreen() {
        //debug
        background.setFill(Color.BEIGE);
        //
        summaryLabel.setAlignment(Pos.CENTER);
        summaryLabel.setWrapText(true);
        summaryLabel.setFont(new Font(36));
        //
        verticalSeparation.setStroke(Color.BLACK);
        verticalSeparation.setStrokeLineCap(StrokeLineCap.ROUND);
        verticalSeparation.setStrokeWidth(4.0);
        //
        instructionLabel.setAlignment(Pos.CENTER);
        instructionLabel.setTextFill(Color.WHITESMOKE);
        instructionLabel.setWrapText(true);
        instructionLabel.setFont(new Font(36));
        //
        gameChoiceButton.setWrapText(true);
        gameChoiceButton.setDisable(true);
        gameChoiceButton.setOnAction(event -> handleGameChoice(event));
        //
        addNode(summaryLabel);
        addNode(verticalSeparation);
        addNode(instructionLabel);
        addNode(gameList);
        addNode(gameChoiceButton);
        //
        updateList();
    }

    @Override
    public void updateSize(double newWidth, double newHeight) {
        sceneHeight = newHeight * (1 - FXScreenUtils.DOCK_VERTICAL_RATIO);
        background.setWidth(newWidth);
        background.setHeight(newHeight);
        //
        intructionLabelWidth = (newWidth - 3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING) / 2.0;
        intructionLabelHeight = (sceneHeight - 3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING) * UPPER_LABEL_RATIO;
        instructionLabel.setMinSize(intructionLabelWidth, intructionLabelHeight);
        instructionLabel.setMaxSize(intructionLabelWidth, intructionLabelHeight);
        instructionLabel.setTranslateX(2.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + intructionLabelWidth);
        instructionLabel.setTranslateY(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        int fontSize = (int) (intructionLabelHeight * 0.8);
        String fontStyle = "-fx-font: " + fontSize + "px Tahoma;";
        instructionLabel.setStyle(fontStyle);
        //
        summaryLabel.setMinSize(intructionLabelWidth, intructionLabelHeight);
        summaryLabel.setMaxSize(intructionLabelWidth, intructionLabelHeight);
        summaryLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        summaryLabel.setTranslateY(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        summaryLabel.setStyle(fontStyle);
        //
        verticalSeparation.setStartX((FXScreenUtils.DEFAULT_ELEMENT_SPACING * 1.5) + intructionLabelWidth);
        verticalSeparation.setStartY((FXScreenUtils.DEFAULT_ELEMENT_SPACING * 0.5));
        verticalSeparation.setEndX((FXScreenUtils.DEFAULT_ELEMENT_SPACING * 1.5) + intructionLabelWidth);
        verticalSeparation.setEndY(sceneHeight - (FXScreenUtils.DEFAULT_ELEMENT_SPACING * 0.5));
        //
        double listHeight = sceneHeight - 4.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING - 2.0 * intructionLabelHeight;
        gameList.setMinSize(intructionLabelWidth, listHeight);
        gameList.setMaxSize(intructionLabelWidth, listHeight);
        gameList.setTranslateX(2.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + intructionLabelWidth);
        gameList.setTranslateY(2.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + intructionLabelHeight);
        int listFontSize = (int) (listHeight / FXScreenUtils.NB_ITEMS_PER_LIST);
        String listFontStyle = "-fx-font: " + listFontSize + "px Arial;";
        gameList.setStyle(listFontStyle);
        //
        double buttonWidth = intructionLabelWidth / 2.0;
        gameChoiceButton.setMinSize(buttonWidth, intructionLabelHeight);
        gameChoiceButton.setMaxSize(buttonWidth, intructionLabelHeight);
        gameChoiceButton.setTranslateX(newWidth - FXScreenUtils.DEFAULT_ELEMENT_SPACING - buttonWidth);
        gameChoiceButton.setTranslateY(sceneHeight - intructionLabelHeight - FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        gameChoiceButton.setFont(new Font(FXScreenUtils.getButtonFontSize(intructionLabelHeight)));
        //

    }

    private void updateList() {
        ObservableList<Game> games = FXCollections.observableArrayList(MainCore.getCurrentTeam().getGames());
        gameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gameList.getItems().setAll(games);
        gameList.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            //TODO: log
            updateGameSelection();
        });
    }

    private void updateGameSelection() {
        gameChoiceButton.setDisable(gameList.getSelectionModel().getSelectedItem() == null);
    }

    private void handleGameChoice(ActionEvent event) {
        //TODO : LOG EVENT
        if (gameList.getSelectionModel().getSelectedItem() != null) {
            firePropertyChangeEvent(DISPLAY_GAME_STAT, null, gameList.getSelectionModel().getSelectedItem());
        }
    }

}
