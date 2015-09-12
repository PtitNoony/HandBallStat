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

import fr.noony.handstats.core.Game;
import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.Team;
import static fr.noony.handstats.utils.EnvLoader.TEAM_RELATIVE_DIRECTORY;
import static fr.noony.handstats.utils.TeamFileProcessor.TEAM_FILE_EXTENSION;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.openide.util.Exceptions;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class XMLSaver {

    public static final String HANDSTAT_TAG = "handstat";

    public static final String TEAM_TAG = "team";

    public static final String TEAM_NAME_TAG = "teamName";

    public static final String TEAM_PREFERED_COLOR = "teamColor";

    public static final String TEAM_CHAMPIONSHIP_TAG = "teamChampionship";

    public static final String PLAYER_ACTIVE_TAG = "activePlayers";

    public static final String PLAYER_RESTING_TAG = "restingPlayers";

    public static final String OPPOSING_TEAMS = "opposingTeams";

    public static final String OPPOSING_TEAM = "opposingTeam";

    public static final String PLAYER_TAG = "player";

    public static final String PLAYER_FIRST_NAME_TAG = "playerFirstName";

    public static final String PLAYER_LAST_NAME_TAG = "playerLastName";

    public static final String PLAYER_NUMBER_TAG = "playerNumber";

    public static final String PLAYER_CURRENT_POSITION_TAG = "playerCurrentPosition";

    public static final String PLAYER_PREFERRED_POSITION_TAG = "playerPreferredPosition";

    public static final String GAMES_TAG = "games";

    public static final String GAME_TAG = "game";

    public static final String GAME_DATE_TAG = "gameDate";

    public static final String GAME_HOME_TEAM_TAG = "gameHomeTeam";

    public static final String GAME_AWAY_TEAM_TAG = "gameAwayTeam";

    public static final String GAME_HOME_TEAM_SCORE_TAG = "gameHomeScore";

    public static final String GAME_AWAY_TEAM_SCORE_TAG = "gameAwayScore";

    public static final String GAMEACTION_TAG = "gameAction";

    public static final String GAME_SUSPENED_TAG = "gameSuspended";

    public static final String GAMEACTION_TYPE = "gameActionType";

    public static final String GAMEACTION_SHOT_STOP = "gameActionShotStop";

    public static final String GAMEACTION_GOOD_SHOT = "gameActionGoodShot";

    public static final String GAMEACTION_FAULT = "gameActionFault";

    public static final String GAMEACTION_TEAM = "gameActionTeam";

    public static final String GAMEACTION_GOALKEEPER_SUBS = "goalKeeperSubstitution";

    public static final String GAMEACTION_FAULT_DESCRIPTION = "gameActionFaultDescription";

    public static final String GAMEACTION_DEFENSE_BLOCKEDSHOT = "gameActionDefenseBlockedshot";

    public static final String GAMEACTION_PLAYER_LASTNAME = "gameActionPlayerLastName";

    public static final String GAMEACTION_PLAYER_FIRSTNAME = "gameActionPlayerFirstName";

    public static final String GAMEACTION_PLAYER_NUMBER = "gameActionPlayerNumber";

    public static final String GAMEACTION_GOALKEEPER_NAME = "gameActionGoalKeeperLastName";

    public static final String GAMEACTION_GOALKEEPER_FIRSTNAME = "gameActionGoalKeeperFirstName";

    public static final String GAMEACTION_GOALKEEPER_NUMBER = "gameActionGoalKeeperNumber";

    public static final String GAMEACTION_TIME = "gameActionTime";

    public static final String GAMEACTION_SHOTZONE = "gameActionShotZone";

    public static final String GAMEACTION_GOALZONE = "gameActionGoalZone";

    public static final String GAME_FINISHED = "finished";

    private XMLSaver() {
        //empty constructor
    }

    public static boolean saveTeam(Team team) {
        System.err.println("SAVING TEAM " + team);
        //il faut connaitre le chemin

        //tester si le fichier existe deja
        //
        Document document;
        Node rootElement;

        document = XMLUtil.createDocument(HANDSTAT_TAG, null, null, null);
        rootElement = document.getFirstChild();
        rootElement.appendChild(getMainTeamXMLElement(document, team));

        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // send DOM to file
            String destFilePath = EnvLoader.getCurrentPath() + TEAM_RELATIVE_DIRECTORY + "\\" + team.getName() + TEAM_FILE_EXTENSION;

            File yourFile = new File(destFilePath);
            if (!yourFile.exists()) {
                yourFile.createNewFile();
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(yourFile)) {
                tr.transform(new DOMSource(document), new StreamResult(fileOutputStream));
            }
        } catch (TransformerException | IOException te) {
            Exceptions.printStackTrace(te);
        }
        System.err.println(" DONE SAVING ");
        return true;
    }

    private static Element getMainTeamXMLElement(Document doc, Team team) {
        Element teamElement = doc.createElement(TEAM_TAG);
        teamElement.setAttribute(TEAM_NAME_TAG, team.getName());
        teamElement.setAttribute(TEAM_CHAMPIONSHIP_TAG, team.getChampionship().name());
        teamElement.setAttribute(TEAM_PREFERED_COLOR, team.getPreferedColor().toString());
        //create active players elements
        Element activePlayers = doc.createElement(PLAYER_ACTIVE_TAG);
        for (Player p : team.getActivePlayers()) {
            activePlayers.appendChild(getPlayerXMLElement(doc, p));
        }
        teamElement.appendChild(activePlayers);
        //create rested players elements
        Element restedPlayers = doc.createElement(PLAYER_RESTING_TAG);
        for (Player p : team.getRestingPlayers()) {
            restedPlayers.appendChild(getPlayerXMLElement(doc, p));
        }
        teamElement.appendChild(restedPlayers);
        //create opposing teams
        Element oppTeams = doc.createElement(OPPOSING_TEAMS);
        for (Team oppTeam : team.getOpponentTeams()) {
            oppTeams.appendChild(getOppTeamXMLElement(doc, oppTeam));
        }
        teamElement.appendChild(oppTeams);
        //create games
        Element games = doc.createElement(GAMES_TAG);
        for (Game g : team.getGames()) {
            games.appendChild(SaveVisitor.visitGame(doc, g));
        }
        teamElement.appendChild(games);
        return teamElement;
    }

    private static Element getOppTeamXMLElement(Document doc, Team team) {
        Element teamElement = doc.createElement(OPPOSING_TEAM);
        teamElement.setAttribute(TEAM_NAME_TAG, team.getName());
        teamElement.setAttribute(TEAM_CHAMPIONSHIP_TAG, team.getChampionship().name());
        teamElement.setAttribute(TEAM_PREFERED_COLOR, team.getPreferedColor().toString());
        //create active players elements
        Element activePlayers = doc.createElement(PLAYER_ACTIVE_TAG);
        for (Player p : team.getActivePlayers()) {
            activePlayers.appendChild(getPlayerXMLElement(doc, p));
        }
        teamElement.appendChild(activePlayers);
        return teamElement;
    }

    private static Element getPlayerXMLElement(Document doc, Player player) {
        Element playerElement = doc.createElement(PLAYER_TAG);
        playerElement.setAttribute(PLAYER_FIRST_NAME_TAG, player.getFirstName());
        playerElement.setAttribute(PLAYER_LAST_NAME_TAG, player.getLastName());
        playerElement.setAttribute(PLAYER_NUMBER_TAG, "" + player.getNumber());
        playerElement.setAttribute(PLAYER_CURRENT_POSITION_TAG, player.getPositionActuelle().name());
        playerElement.setAttribute(PLAYER_PREFERRED_POSITION_TAG, player.getPositionPreferee().name());
        return playerElement;
    }
}
