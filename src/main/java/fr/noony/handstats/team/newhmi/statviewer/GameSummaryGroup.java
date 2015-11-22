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

    private static final int NB_INFO_GROUPS = 4;

    private final Group mainNode;
    private final Label victoryLabel;
    private final Label scoreTitleLabel;
    private final Label scoreValueLabel;
    private final Label homeTeamLabel;
    private final Label awayTeamLabel;
    private final Label shotPercentageTitleLabel;
    private final Label shotBlockedPercentageTitleLabel;

    //debug
    private final Rectangle debugR;

    private double innerWidth;
    private double infoGroupHeight;

    public GameSummaryGroup() {
        mainNode = new Group();
        victoryLabel = new Label();
        scoreTitleLabel = new Label("Score : ");
        scoreValueLabel = new Label();
        homeTeamLabel = new Label("Nous");
        awayTeamLabel = new Label("Eux");
        shotPercentageTitleLabel = new Label("% tirs : ");
        shotBlockedPercentageTitleLabel = new Label("% arrêts : ");
        //
        debugR = new Rectangle();
        //
        initGameSummaryGroup();
    }

    private void initGameSummaryGroup() {
        victoryLabel.setAlignment(Pos.CENTER);
        scoreTitleLabel.setAlignment(Pos.CENTER);
        scoreValueLabel.setAlignment(Pos.CENTER);
        homeTeamLabel.setAlignment(Pos.CENTER);
        awayTeamLabel.setAlignment(Pos.CENTER);
        shotPercentageTitleLabel.setAlignment(Pos.CENTER);
        shotBlockedPercentageTitleLabel.setAlignment(Pos.CENTER);
        //
        debugR.setFill(Color.YELLOW);
        mainNode.getChildren().add(debugR);
        //
        mainNode.getChildren().add(victoryLabel);
//        mainNode.getChildren().add(scoreTitleLabel);
//        mainNode.getChildren().add(scoreValueLabel);
//        mainNode.getChildren().add(homeTeamLabel);
//        mainNode.getChildren().add(awayTeamLabel);
//        mainNode.getChildren().add(shotPercentageTitleLabel);
//        mainNode.getChildren().add(shotBlockedPercentageTitleLabel);
    }

    public Node getNode() {
        return mainNode;
    }

    public void setGame(Game game) {
        if (game.getHomeScore() > game.getAwayScore()) {
            victoryLabel.setText("VICTOIRE");
        } else if (game.getHomeScore() == game.getAwayScore()) {
            victoryLabel.setText("MATCH NUL");
        } else {
            victoryLabel.setText("DEFAITE");
        }

    }

    protected void setPosition(double x, double y) {
        mainNode.setTranslateX(x);
        mainNode.setTranslateY(y);
    }

    protected void setSize(double newWidth, double newHeight) {
        debugR.setWidth(newWidth);
        debugR.setHeight(newHeight);
        victoryLabel.setTranslateX(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        victoryLabel.setTranslateY(FXScreenUtils.DEFAULT_ELEMENT_SPACING);
        innerWidth = newWidth - 2.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING;
        infoGroupHeight = (newHeight - 2.0 * FXScreenUtils.DEFAULT_ELEMENT_SPACING) / NB_INFO_GROUPS;
        victoryLabel.setMinSize(innerWidth, infoGroupHeight);
        victoryLabel.setMaxSize(innerWidth, infoGroupHeight);
        String fontStyle = "-fx-font: " + FXScreenUtils.getButtonFontSize(infoGroupHeight) + "px Tahoma;";
        victoryLabel.setStyle(fontStyle);
    }

    protected void setVisible(boolean visibility) {
        mainNode.setVisible(visibility);
    }

}
