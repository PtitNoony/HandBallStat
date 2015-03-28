/*
 * Copyright (C) 2015 Arnaud
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
package fr.noony.handstats.team.hmi.drawing;

import fr.noony.handstats.Poste;
import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.Team;
import java.awt.Dimension;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class TeamDrawing {

    public enum TeamSide {

        LEFT, RIGHT
    }

    public static final Dimension DEFAULT_TEAM_GROUP_SIZE = new Dimension(560, 460);
    public static final double DEFAULT_ELEMENT_SEPARATION = 12;
    private double width = DEFAULT_TEAM_GROUP_SIZE.getWidth();
    private double height = DEFAULT_TEAM_GROUP_SIZE.getHeight();

    private final Group mainNode;
    private Team myTeam;
    //
    private final Rectangle background;
    private final List<GoalKeeperDrawing> goalKeeperDrawings;
    private final List<FieldPlayerDrawing> fieldPlayerDrawings;
    private TeamSide currentSide;

    public TeamDrawing(TeamSide side) {
        mainNode = new Group();
        currentSide = side;
        background = new Rectangle(width, height);
        goalKeeperDrawings = new LinkedList<>();
        fieldPlayerDrawings = new LinkedList<>();
        //
        init();
    }

    private void init() {
        //TODO: link with team color
        background.setFill(Color.CHARTREUSE);
        mainNode.getChildren().add(background);

    }

    public Node getNode() {
        return mainNode;
    }

    public void setPosition(double x, double y) {
        mainNode.setTranslateX(x);
        mainNode.setTranslateY(y);
    }

    public void setSize(double newWidth, double newHeight) {
        width = newWidth;
        height = newHeight;
        updateDrawing();
    }

    public void setTeam(Team currentTeam) {
        //factoriser dans une seule methode ?
        myTeam = currentTeam;
        createGoalKeepersDrawings();
        createFieldPlayersDrawings();
        reOrderPlayers();
        updateDrawing();
    }

    private void createGoalKeepersDrawings() {
        for (Player player : myTeam.getActivePlayers()) {
            if (player.getPositionActuelle().equals(Poste.GARDIEN)) {
                GoalKeeperDrawing goalKeeperDrawing = new GoalKeeperDrawing(player);
                goalKeeperDrawings.add(goalKeeperDrawing);
                mainNode.getChildren().add(goalKeeperDrawing.getNode());
            }
        }
    }

    private void createFieldPlayersDrawings() {
        for (Player player : myTeam.getActivePlayers()) {
            if (!player.getPositionActuelle().equals(Poste.GARDIEN)) {
                FieldPlayerDrawing fieldPlayerDrawing = new FieldPlayerDrawing(player);
                fieldPlayerDrawings.add(fieldPlayerDrawing);
                mainNode.getChildren().add(fieldPlayerDrawing.getNode());
            }
        }
    }

    private void reOrderPlayers() {
        Collections.sort(fieldPlayerDrawings, (FieldPlayerDrawing p1, FieldPlayerDrawing p2) -> Integer.compare(p1.getPlayer().getNumero(), p2.getPlayer().getNumero()));
        Collections.sort(goalKeeperDrawings, (GoalKeeperDrawing p1, GoalKeeperDrawing p2) -> Integer.compare(p1.getPlayer().getNumero(), p2.getPlayer().getNumero()));
    }

    private void updateDrawing() {
        background.setWidth(width);
        background.setHeight(height);
        //TODO: change player drawing size and update
        if (currentSide.equals(TeamSide.LEFT)) {
            drawAsLeft();
        } else if (currentSide.equals(TeamSide.RIGHT)) {
            drawAsRight();
        }
    }

    private void drawAsLeft() {
        for (GoalKeeperDrawing gkd : goalKeeperDrawings) {
            gkd.setPosition(DEFAULT_ELEMENT_SEPARATION, DEFAULT_ELEMENT_SEPARATION
                    + goalKeeperDrawings.indexOf(gkd) * (DEFAULT_ELEMENT_SEPARATION + PlayerDrawing.STANDARD_PLAYER_WIDTH));
        }
        double xPos;
        double yPos;
        int index;
        int row;
        for (FieldPlayerDrawing fpd : fieldPlayerDrawings) {
            index = fieldPlayerDrawings.indexOf(fpd);
            row = index / 2;
            if (index % 2 == 0) {
                xPos = DEFAULT_ELEMENT_SEPARATION * 2.0 + PlayerDrawing.STANDARD_PLAYER_HEIGHT;
            } else {
                xPos = DEFAULT_ELEMENT_SEPARATION * 3.0 + PlayerDrawing.STANDARD_PLAYER_HEIGHT + PlayerDrawing.STANDARD_PLAYER_WIDTH;
            }
            yPos = DEFAULT_ELEMENT_SEPARATION + row * (DEFAULT_ELEMENT_SEPARATION + PlayerDrawing.STANDARD_PLAYER_HEIGHT);
            fpd.setPosition(xPos, yPos);
        }
    }

    private void drawAsRight() {
        for (GoalKeeperDrawing gkd : goalKeeperDrawings) {
            gkd.setPosition(DEFAULT_ELEMENT_SEPARATION * 3.0 + 2.0 * PlayerDrawing.STANDARD_PLAYER_WIDTH, DEFAULT_ELEMENT_SEPARATION
                    + goalKeeperDrawings.indexOf(gkd) * (DEFAULT_ELEMENT_SEPARATION + PlayerDrawing.STANDARD_PLAYER_WIDTH));
        }
        double xPos;
        double yPos;
        int index;
        int row;
        for (FieldPlayerDrawing fpd : fieldPlayerDrawings) {
            index = fieldPlayerDrawings.indexOf(fpd);
            row = index / 2;
            if (index % 2 == 0) {
                xPos = DEFAULT_ELEMENT_SEPARATION;
            } else {
                xPos = DEFAULT_ELEMENT_SEPARATION * 2.0 + PlayerDrawing.STANDARD_PLAYER_WIDTH;
            }
            yPos = DEFAULT_ELEMENT_SEPARATION + row * (DEFAULT_ELEMENT_SEPARATION + PlayerDrawing.STANDARD_PLAYER_HEIGHT);
            fpd.setPosition(xPos, yPos);
        }
    }

    public void setSide(TeamSide side) {
        currentSide = side;
        updateDrawing();
    }

    public void setVisible(boolean visibilty) {
        mainNode.setVisible(visibilty);
    }

    public List<PlayerDrawing> getPlayerDrawings() {
        //un peu moche !!
        List<PlayerDrawing> result = new LinkedList<>();
        result.addAll(fieldPlayerDrawings);
        result.addAll(goalKeeperDrawings);
        return result;
    }

    public void setFill(Color fill) {
        background.setFill(fill);
    }
}
