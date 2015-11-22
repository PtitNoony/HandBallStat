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
package fr.noony.handstats.core;

import fr.noony.handstats.utils.XMLSaver;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_GOALZONE;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_SHOTZONE;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_SHOT_STOP;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TEAM;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TIME;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TYPE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class ShotStop extends GameAction {

    private final Player goalK;
    private final Player player;
    private final Team playerTeam;
    private final String shootingZone;
    private final String goalZone;

    public ShotStop(Player goalKeeper, Player shooter, Team shooterTeam, String sZone, String gZone, String time) {
        super(time);
        goalK = goalKeeper;
        player = shooter;
        playerTeam = shooterTeam;
        shootingZone = sZone;
        goalZone = gZone;
    }

    public Player getGoalKeeper() {
        return goalK;
    }

    public Player getShooter() {
        return player;
    }

    public Team getShooterTeam() {
        return playerTeam;
    }

    public String getShootingZone() {
        return shootingZone;
    }

    public String getGoalZone() {
        return goalZone;
    }

    @Override
    public Element visit(Document doc) {
        Element gameActionElement = doc.createElement(GAMEACTION_TAG);
        gameActionElement.setAttribute(GAMEACTION_TYPE, GAMEACTION_SHOT_STOP);
        gameActionElement.setAttribute(GAMEACTION_TEAM, playerTeam.getName());
        //
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_LASTNAME, player.getLastName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_FIRSTNAME, player.getFirstName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_NUMBER, "" + player.getNumber());
        gameActionElement.setAttribute(XMLSaver.PLAYER_ID_TAG, "" + player.getUniqueID());
        //
//        gameActionElement.setAttribute(GAMEACTION_GOALKEEPER_NAME, goalK.getLastName());
//        gameActionElement.setAttribute(GAMEACTION_GOALKEEPER_FIRSTNAME, goalK.getFirstName());
//        gameActionElement.setAttribute(GAMEACTION_GOALKEEPER_NUMBER, "" + goalK.getNumber());
        gameActionElement.setAttribute(XMLSaver.GAMEACTION_GOALKEEPER_ID, "" + goalK.getUniqueID());
        //
        gameActionElement.setAttribute(GAMEACTION_TIME, getActionTime());
        gameActionElement.setAttribute(GAMEACTION_SHOTZONE, shootingZone);
        gameActionElement.setAttribute(GAMEACTION_GOALZONE, goalZone);
        return gameActionElement;
    }

    @Override
    public String toString() {
        //UGLY
        return "" + GAMEACTION_SHOT_STOP + " " + playerTeam + " " + player + " " + goalK + " " + getActionTime();
    }

}
