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
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_DEFENSE_BLOCKEDSHOT;
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
public class DefenseBlockedShot extends GameAction {

    private final Player player;
    private final Team playerTeam;
    private final String shootingZone;

    public DefenseBlockedShot(Player shooter, Team shooterTeam, String sZone, String time) {
        super(time);
        player = shooter;
        playerTeam = shooterTeam;
        shootingZone = sZone;
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

    @Override
    public Element visit(Document doc) {
        Element gameActionElement = doc.createElement(GAMEACTION_TAG);
        gameActionElement.setAttribute(GAMEACTION_TYPE, GAMEACTION_DEFENSE_BLOCKEDSHOT);
        gameActionElement.setAttribute(GAMEACTION_TEAM, playerTeam.getName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_LASTNAME, player.getLastName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_FIRSTNAME, player.getFirstName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_NUMBER, "" + player.getNumber());
        gameActionElement.setAttribute(XMLSaver.PLAYER_ID_TAG, "" + player.getUniqueID());
        gameActionElement.setAttribute(GAMEACTION_TIME, getActionTime());
        gameActionElement.setAttribute(GAMEACTION_SHOTZONE, shootingZone);
        return gameActionElement;
    }

}
