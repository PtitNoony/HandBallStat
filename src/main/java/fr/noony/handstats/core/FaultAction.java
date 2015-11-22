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
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_FAULT;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_FAULT_DESCRIPTION;
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
public class FaultAction extends GameAction {

    private final Fault myFault;
    private final Team playerTeam;
    private final Player player;

    public FaultAction(Player p, Team shooterTeam, Fault fault, String time) {
        super(time);
        myFault = fault;
        player = p;
        playerTeam = shooterTeam;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getShooterTeam() {
        return playerTeam;
    }

    public Fault getFault() {
        return myFault;
    }

    @Override
    public Element visit(Document doc) {
        Element gameActionElement = doc.createElement(GAMEACTION_TAG);
        gameActionElement.setAttribute(GAMEACTION_TYPE, GAMEACTION_FAULT);
        gameActionElement.setAttribute(GAMEACTION_TEAM, playerTeam.getName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_LASTNAME, player.getLastName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_FIRSTNAME, player.getFirstName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_NUMBER, "" + player.getNumber());
        gameActionElement.setAttribute(XMLSaver.PLAYER_ID_TAG, "" + player.getUniqueID());
        gameActionElement.setAttribute(GAMEACTION_TIME, getActionTime());
        gameActionElement.setAttribute(GAMEACTION_FAULT_DESCRIPTION, myFault.name());
        return gameActionElement;
    }

}
