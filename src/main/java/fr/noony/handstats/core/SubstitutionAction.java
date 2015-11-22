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
package fr.noony.handstats.core;

import fr.noony.handstats.utils.XMLSaver;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_GOALKEEPER_SUBS;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TEAM;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TIME;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TYPE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Arnaud
 */
public class SubstitutionAction extends GameAction {

    private final Player player;
    private final Team playerTeam;

    public SubstitutionAction(Player goalKeeper, Team goalTeam, String time) {
        super(time);
        player = goalKeeper;
        playerTeam = goalTeam;
    }

    @Override
    public Element visit(Document doc) {
        Element gameActionElement = doc.createElement(GAMEACTION_TAG);
        gameActionElement.setAttribute(GAMEACTION_TYPE, GAMEACTION_GOALKEEPER_SUBS);
        gameActionElement.setAttribute(GAMEACTION_TEAM, playerTeam.getName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_LASTNAME, player.getLastName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_FIRSTNAME, player.getFirstName());
//        gameActionElement.setAttribute(GAMEACTION_PLAYER_NUMBER, "" + player.getNumber());
        gameActionElement.setAttribute(XMLSaver.PLAYER_ID_TAG, "" + player.getUniqueID());
        gameActionElement.setAttribute(GAMEACTION_TIME, getActionTime());
        return gameActionElement;
    }

    public Player getGoalKeeper() {
        return player;
    }

    public Team getPlayerTeam() {
        return playerTeam;
    }

}
