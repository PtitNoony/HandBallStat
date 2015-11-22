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
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

/**
 *
 * @author Arnaud
 */
public class GameSummaryScreen extends FXScreen {

    private static final double SUMMARY_RATIO = 0.4;
    private static final double CONTROLS_RATIO = 0.08;
    private static final int CONTROLS_SPACING = 8;

    private enum SummaryMode {
        SUMMARY, HOME_TEAM, AWAY_TEAM
    }

    //debug
    private final Rectangle summaryBackground;
    //
    private final ToggleButton summaryTB;
    private final ToggleButton homeTeamTB;
    private final ToggleButton awayTeamTB;
    private final Line verticalSeparation;
    //
    private final GameSummaryGroup gameSummaryGroup;

    private double screenWidth;
    private double screenHeight;
    private double controlWidth;
    private double controlHeight;
    private double summaryY;
    private double summaryWidth;
    private double summaryHeight;
    private double verticalSeparationPosX;

    private SummaryMode summaryMode = SummaryMode.SUMMARY;

    public GameSummaryScreen() {
        super();
        summaryBackground = new Rectangle();
        summaryTB = new ToggleButton("Résumé");
        homeTeamTB = new ToggleButton("Mes Joueurs");
        awayTeamTB = new ToggleButton("Adversaires");
        gameSummaryGroup = new GameSummaryGroup();
        verticalSeparation = new Line();
        initGameSummaryScreen();
    }

    private void initGameSummaryScreen() {
        summaryBackground.setFill(Color.WHITESMOKE);
        summaryBackground.setStroke(Color.BLACK);
        //
        summaryTB.setOnAction(event -> {
            //TODO : log event
            setSummaryMode(SummaryMode.SUMMARY);
        });
        homeTeamTB.setOnAction(event -> {
            //TODO : log event
            setSummaryMode(SummaryMode.HOME_TEAM);
        });
        awayTeamTB.setOnAction(event -> {
            //TODO : log event
            setSummaryMode(SummaryMode.AWAY_TEAM);
        });
        //
        summaryTB.setWrapText(true);
        homeTeamTB.setWrapText(true);
        awayTeamTB.setWrapText(true);
        //
        verticalSeparation.setStroke(Color.BLACK);
        verticalSeparation.setStrokeLineCap(StrokeLineCap.ROUND);
        verticalSeparation.setStrokeWidth(4.0);
        //
        addNode(summaryBackground);
        addNode(gameSummaryGroup.getNode());
        addNode(summaryTB);
        addNode(homeTeamTB);
        addNode(awayTeamTB);
        addNode(verticalSeparation);

        setSummaryMode(SummaryMode.SUMMARY);

    }

    @Override
    public void updateSize(double newWidth, double newHeight) {
        screenWidth = newWidth;
        screenHeight = newHeight * (1 - FXScreenUtils.DOCK_VERTICAL_RATIO);
        controlWidth = (newWidth * SUMMARY_RATIO - 4.0 * CONTROLS_SPACING) / 3.0;
        controlHeight = (screenHeight - 3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING) * CONTROLS_RATIO;
        summaryY = 2.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + controlHeight;
        summaryWidth = 3.0 * controlWidth + 2.0 * CONTROLS_SPACING;
        summaryHeight = screenHeight - 3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING - controlHeight;
        verticalSeparationPosX = newWidth * SUMMARY_RATIO - CONTROLS_SPACING;
        //
        summaryBackground.setX(CONTROLS_SPACING);
        summaryBackground.setY(summaryY);
        summaryBackground.setWidth(summaryWidth);
        summaryBackground.setHeight(summaryHeight);
        //
        summaryTB.setTranslateX(CONTROLS_SPACING);
        homeTeamTB.setTranslateX(2.0 * CONTROLS_SPACING + controlWidth);
        awayTeamTB.setTranslateX(3.0 * CONTROLS_SPACING + 2.0 * controlWidth);
        summaryTB.setTranslateY(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        homeTeamTB.setTranslateY(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        awayTeamTB.setTranslateY(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        //
        summaryTB.setMinSize(controlWidth, controlHeight);
        summaryTB.setMaxSize(controlWidth, controlHeight);
        homeTeamTB.setMinSize(controlWidth, controlHeight);
        homeTeamTB.setMaxSize(controlWidth, controlHeight);
        awayTeamTB.setMinSize(controlWidth, controlHeight);
        awayTeamTB.setMaxSize(controlWidth, controlHeight);
        //
        String fontStyle = "-fx-font: " + FXScreenUtils.getButtonFontSize(controlHeight) + "px Tahoma;";
        summaryTB.setStyle(fontStyle);
        homeTeamTB.setStyle(fontStyle);
        awayTeamTB.setStyle(fontStyle);
        //
        verticalSeparation.setStartX((FXScreenUtils.DEFAULT_ELEMENT_SPACING * 1.5) + verticalSeparationPosX);
        verticalSeparation.setStartY((FXScreenUtils.DEFAULT_ELEMENT_SPACING * 0.5));
        verticalSeparation.setEndX((FXScreenUtils.DEFAULT_ELEMENT_SPACING * 1.5) + verticalSeparationPosX);
        verticalSeparation.setEndY(screenHeight - (FXScreenUtils.DEFAULT_ELEMENT_SPACING * 0.5));
        //

        gameSummaryGroup.setPosition(CONTROLS_SPACING, summaryY);
        gameSummaryGroup.setSize(summaryWidth, summaryHeight);

    }

    public void setGame(Game game) {

    }

    private void setSummaryMode(SummaryMode mode) {
        summaryMode = mode;
        switch (summaryMode) {
            case SUMMARY:
                setGameSummary();
                break;
            case HOME_TEAM:
                setHomeTeamSummary();
                break;
            case AWAY_TEAM:
                setAwayTeamSummary();
                break;
            default:
                throw new IllegalArgumentException("" + mode);
        }
    }

    private void setGameSummary() {
        summaryTB.setSelected(true);
        homeTeamTB.setSelected(false);
        awayTeamTB.setSelected(false);
        //
        gameSummaryGroup.setVisible(true);

        //??
        gameSummaryGroup.setPosition(CONTROLS_SPACING, summaryY);
        gameSummaryGroup.setSize(summaryWidth, summaryHeight);
    }

    private void setHomeTeamSummary() {
        summaryTB.setSelected(false);
        homeTeamTB.setSelected(true);
        awayTeamTB.setSelected(false);

    }

    private void setAwayTeamSummary() {
        summaryTB.setSelected(false);
        homeTeamTB.setSelected(false);
        awayTeamTB.setSelected(true);

    }

}
