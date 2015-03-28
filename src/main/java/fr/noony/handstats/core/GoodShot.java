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

import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_GOALZONE;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_GOOD_SHOT;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_PLAYER_FIRSTNAME;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_PLAYER_LASTNAME;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_PLAYER_NUMBER;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_SHOTZONE;
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
public class GoodShot implements GameAction {

    private final Player player;
    private final Team playerTeam;
    private final String shootingZone;
    private final String goalZone;
    private final String shotTime;

    public GoodShot(Player shooter, Team shooterTeam, String sZone, String gZone, String time) {
        player = shooter;
        playerTeam = shooterTeam;
        shootingZone = sZone;
        goalZone = gZone;
        shotTime = time;
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

    public String getShotTime() {
        return shotTime;
    }

    @Override
    public Element visit(Document doc) {
        Element gameActionElement = doc.createElement(GAMEACTION_TAG);
        gameActionElement.setAttribute(GAMEACTION_TYPE, GAMEACTION_GOOD_SHOT);
        gameActionElement.setAttribute(GAMEACTION_TEAM, playerTeam.getName());
        gameActionElement.setAttribute(GAMEACTION_PLAYER_LASTNAME, player.getLastName());
        gameActionElement.setAttribute(GAMEACTION_PLAYER_FIRSTNAME, player.getFirstName());
        gameActionElement.setAttribute(GAMEACTION_PLAYER_NUMBER, "" + player.getNumero());
        gameActionElement.setAttribute(GAMEACTION_TIME, shotTime);
        gameActionElement.setAttribute(GAMEACTION_SHOTZONE, shootingZone);
        gameActionElement.setAttribute(GAMEACTION_GOALZONE, goalZone);
        return gameActionElement;
    }
}
