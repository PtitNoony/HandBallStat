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
package fr.noony.handstats.utils;

import fr.noony.handstats.Game;
import fr.noony.handstats.core.GameAction;
import static fr.noony.handstats.utils.XMLSaver.GAME_AWAY_TEAM_SCORE_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAME_AWAY_TEAM_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAME_DATE_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAME_FINISHED;
import static fr.noony.handstats.utils.XMLSaver.GAME_HOME_TEAM_SCORE_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAME_HOME_TEAM_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAME_TAG;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Arnaud HAMON
 */
public class SaveVisitor {

    private SaveVisitor() {
        //private constructor
    }

    public static final Element visitGame(Document doc, Game game) {
        Element gameElement = doc.createElement(GAME_TAG);
        gameElement.setAttribute(GAME_DATE_TAG, game.getDate().toString());
        gameElement.setAttribute(GAME_HOME_TEAM_TAG, game.getHomeTeam().getName());
        gameElement.setAttribute(GAME_AWAY_TEAM_TAG, game.getAwayTeam().getName());
        gameElement.setAttribute(GAME_HOME_TEAM_SCORE_TAG, "" + game.getHomeScore());
        gameElement.setAttribute(GAME_AWAY_TEAM_SCORE_TAG, "" + game.getAwayScore());
        gameElement.setAttribute(GAME_FINISHED, "" + game.isFinished());
        for (GameAction gameAction : game.getActions()) {
            gameElement.appendChild(visitGameAction(doc, gameAction));
        }
        if (!game.isFinished()) {
            Element gamesuspendedTimeElement = doc.createElement(XMLSaver.GAME_SUSPENED_TAG);
            gamesuspendedTimeElement.setAttribute("time", "" + game.getGameClock().getTime());
            gameElement.appendChild(gamesuspendedTimeElement);
        }
        return gameElement;
    }

    private static Element visitGameAction(Document doc, GameAction gameAction) {
        return gameAction.visit(doc);
    }
}
