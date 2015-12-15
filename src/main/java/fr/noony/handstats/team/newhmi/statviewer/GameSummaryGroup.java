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

import fr.noony.handstats.stats.GameStat;
import fr.noony.handstats.team.newhmi.FXScreenUtils;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud
 */
public class GameSummaryGroup {

    private static final int NB_INFO_GROUPS = 6;

    private final Group mainNode;
    private final Label victoryLabel;
    private final Label scoreLabel;
    private final Label homeTeamLabel;
    private final Label awayTeamLabel;
    private final Label shotPercentageTitleLabel;
    private final Label shotPercentageHomeLabel;
    private final Label shotPercentageAwayLabel;
    private final Label shotBlockedPercentageTitleLabel;
    private final Label shotBlockedPercentageHomeLabel;
    private final Label shotBlockedPercentageAwayLabel;

    //debug
    private final Rectangle debugR;

    private double innerWidth;
    private double infoGroupHeight;

    public GameSummaryGroup() {
        mainNode = new Group();
        victoryLabel = new Label("XXX");
        scoreLabel = new Label();
        homeTeamLabel = new Label("Nous");
        awayTeamLabel = new Label("Eux");
        shotPercentageTitleLabel = new Label("% tirs : ");
        shotBlockedPercentageTitleLabel = new Label("% arrêts : ");
        shotPercentageHomeLabel = new Label("XXX");
        shotPercentageAwayLabel = new Label("XXX");
        shotBlockedPercentageHomeLabel = new Label("XXX");
        shotBlockedPercentageAwayLabel = new Label("XXX");
        //
        debugR = new Rectangle();
        //
        initGameSummaryGroup();
    }

    private void initGameSummaryGroup() {
        victoryLabel.setAlignment(Pos.CENTER);
        scoreLabel.setAlignment(Pos.CENTER);
        homeTeamLabel.setAlignment(Pos.CENTER);
        awayTeamLabel.setAlignment(Pos.CENTER);
        shotPercentageTitleLabel.setAlignment(Pos.CENTER);
        shotPercentageHomeLabel.setAlignment(Pos.CENTER);
        shotPercentageAwayLabel.setAlignment(Pos.CENTER);
        shotBlockedPercentageTitleLabel.setAlignment(Pos.CENTER);
        shotBlockedPercentageHomeLabel.setAlignment(Pos.CENTER);
        shotBlockedPercentageAwayLabel.setAlignment(Pos.CENTER);
        //
        debugR.setFill(Color.YELLOW);
//        mainNode.getChildren().add(debugR);
        //
        mainNode.getChildren().add(victoryLabel);
        mainNode.getChildren().add(scoreLabel);
        mainNode.getChildren().add(homeTeamLabel);
        mainNode.getChildren().add(awayTeamLabel);
        mainNode.getChildren().add(shotPercentageTitleLabel);
        mainNode.getChildren().add(shotPercentageHomeLabel);
        mainNode.getChildren().add(shotPercentageAwayLabel);
        mainNode.getChildren().add(shotBlockedPercentageTitleLabel);
        mainNode.getChildren().add(shotBlockedPercentageHomeLabel);
        mainNode.getChildren().add(shotBlockedPercentageAwayLabel);
    }

    public Node getNode() {
        return mainNode;
    }

    public void setGameStat(GameStat gameStat) {
        if (gameStat.isHomeVictor()) {
            victoryLabel.setText("VICTOIRE");
        } else if (gameStat.isDraw()) {
            victoryLabel.setText("MATCH NUL");
        } else {
            victoryLabel.setText("DEFAITE");
        }
        //
        scoreLabel.setText("" + gameStat.getHomeScore() + "-" + gameStat.getAwayScore());
        shotPercentageHomeLabel.setText(Double.toString(gameStat.getHomeAccuracy()));
        shotPercentageAwayLabel.setText(Double.toString(gameStat.getAwayAccuracy()));
        shotBlockedPercentageHomeLabel.setText(Double.toString(gameStat.getHomeShotBlockedPercentage()));
        shotBlockedPercentageAwayLabel.setText(Double.toString(gameStat.getAwayShotBlockedPercentage()));
    }

    protected void setPosition(double x, double y) {
        mainNode.setTranslateX(x);
        mainNode.setTranslateY(y);
    }

    protected void setSize(double newWidth, double newHeight) {
        //update size related attributes
        innerWidth = newWidth - 2.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING;
        infoGroupHeight = (newHeight - (NB_INFO_GROUPS + 1) * FXScreenUtils.DEFAULT_ELEMENT_SPACING) / NB_INFO_GROUPS;
        //
//        debugR.setWidth(newWidth);
//        debugR.setHeight(newHeight);
        debugR.setX(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        debugR.setY(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        debugR.setWidth(innerWidth);
        debugR.setHeight(infoGroupHeight);
        //
        victoryLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        victoryLabel.setTranslateY(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        victoryLabel.setMinSize(innerWidth, infoGroupHeight);
        victoryLabel.setMaxSize(innerWidth, infoGroupHeight);
        String fontStyle = "-fx-font: " + FXScreenUtils.getButtonFontSize(infoGroupHeight) + "px Tahoma;";
        victoryLabel.setStyle(fontStyle);
        //
        scoreLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        scoreLabel.setTranslateY(2.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + infoGroupHeight);
        scoreLabel.setMinSize(innerWidth, infoGroupHeight);
        scoreLabel.setMaxSize(innerWidth, infoGroupHeight);
        scoreLabel.setStyle(fontStyle);
        //
        double percentageWidth = innerWidth / 3.0;
        double percentageHeight = infoGroupHeight / 1.5;
        homeTeamLabel.setMinSize(percentageWidth, percentageHeight);
        homeTeamLabel.setMaxSize(percentageWidth, percentageHeight);
        awayTeamLabel.setMinSize(percentageWidth, percentageHeight);
        awayTeamLabel.setMaxSize(percentageWidth, percentageHeight);
        homeTeamLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING + percentageWidth);
        homeTeamLabel.setTranslateY(3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * infoGroupHeight);
        awayTeamLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * percentageWidth);
        awayTeamLabel.setTranslateY(3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * infoGroupHeight);
        String percentageFontStyle = "-fx-font: " + FXScreenUtils.getButtonFontSize(percentageHeight) + "px Tahoma;";
        homeTeamLabel.setStyle(percentageFontStyle);
        awayTeamLabel.setStyle(percentageFontStyle);
        //
        shotPercentageTitleLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        shotPercentageTitleLabel.setTranslateY(3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * infoGroupHeight + percentageHeight);
        shotPercentageHomeLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING + percentageWidth);
        shotPercentageHomeLabel.setTranslateY(3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * infoGroupHeight + percentageHeight);
        shotPercentageAwayLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * percentageWidth);
        shotPercentageAwayLabel.setTranslateY(3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * infoGroupHeight + percentageHeight);
        shotPercentageTitleLabel.setMinSize(percentageWidth, percentageHeight);
        shotPercentageTitleLabel.setMaxSize(percentageWidth, percentageHeight);
        shotPercentageHomeLabel.setMinSize(percentageWidth, percentageHeight);
        shotPercentageHomeLabel.setMaxSize(percentageWidth, percentageHeight);
        shotPercentageAwayLabel.setMinSize(percentageWidth, percentageHeight);
        shotPercentageAwayLabel.setMaxSize(percentageWidth, percentageHeight);
        shotPercentageTitleLabel.setStyle(percentageFontStyle);
        shotPercentageHomeLabel.setStyle(percentageFontStyle);
        shotPercentageAwayLabel.setStyle(percentageFontStyle);
        //
        shotBlockedPercentageTitleLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        shotBlockedPercentageTitleLabel.setTranslateY(3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * infoGroupHeight + 2.0 * percentageHeight);
        shotBlockedPercentageHomeLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING + percentageWidth);
        shotBlockedPercentageHomeLabel.setTranslateY(3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * infoGroupHeight + 2.0 * percentageHeight);
        shotBlockedPercentageAwayLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * percentageWidth);
        shotBlockedPercentageAwayLabel.setTranslateY(3.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING + 2.0 * infoGroupHeight + 2.0 * percentageHeight);
        shotBlockedPercentageTitleLabel.setMinSize(percentageWidth, percentageHeight);
        shotBlockedPercentageTitleLabel.setMaxSize(percentageWidth, percentageHeight);
        shotBlockedPercentageHomeLabel.setMinSize(percentageWidth, percentageHeight);
        shotBlockedPercentageHomeLabel.setMaxSize(percentageWidth, percentageHeight);
        shotBlockedPercentageAwayLabel.setMinSize(percentageWidth, percentageHeight);
        shotBlockedPercentageAwayLabel.setMaxSize(percentageWidth, percentageHeight);
        shotBlockedPercentageTitleLabel.setStyle(percentageFontStyle);
        shotBlockedPercentageHomeLabel.setStyle(percentageFontStyle);
        shotBlockedPercentageAwayLabel.setStyle(percentageFontStyle);

    }

    protected void setVisible(boolean visibility) {
        mainNode.setVisible(visibility);
    }

}
