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

import fr.noony.handstats.Championship;
import fr.noony.handstats.Game;
import fr.noony.handstats.Poste;
import fr.noony.handstats.core.DefenseBlockedShot;
import fr.noony.handstats.core.Fault;
import fr.noony.handstats.core.FaultAction;
import fr.noony.handstats.core.GoodShot;
import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.ShotStop;
import fr.noony.handstats.core.Team;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_DEFENSE_BLOCKEDSHOT;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_FAULT;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_FAULT_DESCRIPTION;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_GOALKEEPER_FIRSTNAME;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_GOALKEEPER_NAME;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_GOALKEEPER_NUMBER;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_GOALZONE;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_GOOD_SHOT;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_PLAYER_FIRSTNAME;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_PLAYER_LASTNAME;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_PLAYER_NUMBER;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_SHOTZONE;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_SHOT_STOP;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TEAM;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TIME;
import static fr.noony.handstats.utils.XMLSaver.GAMEACTION_TYPE;
import static fr.noony.handstats.utils.XMLSaver.GAME_AWAY_TEAM_SCORE_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAME_AWAY_TEAM_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAME_DATE_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAME_HOME_TEAM_SCORE_TAG;
import static fr.noony.handstats.utils.XMLSaver.GAME_HOME_TEAM_TAG;
import static fr.noony.handstats.utils.XMLSaver.PLAYER_CURRENT_POSITION_TAG;
import static fr.noony.handstats.utils.XMLSaver.PLAYER_FIRST_NAME_TAG;
import static fr.noony.handstats.utils.XMLSaver.PLAYER_LAST_NAME_TAG;
import static fr.noony.handstats.utils.XMLSaver.PLAYER_NUMBER_TAG;
import static fr.noony.handstats.utils.XMLSaver.PLAYER_PREFERRED_POSITION_TAG;
import static fr.noony.handstats.utils.XMLSaver.TEAM_CHAMPIONSHIP_TAG;
import static fr.noony.handstats.utils.XMLSaver.TEAM_NAME_TAG;
import static fr.noony.handstats.utils.XMLSaver.TEAM_PREFERED_COLOR;
import static fr.noony.handstats.utils.XMLSaver.TEAM_TAG;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public final class XMLLoader {

    private XMLLoader() {

    }

    public static Team loadTeam(Path path) {
        if (path == null) {
            return null;
        }
        Document document;

        DocumentBuilderFactory builderFactory;
        builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            InputSource source = new org.xml.sax.InputSource(path.toFile().getAbsolutePath());
            document = builder.parse(source);
            Element e = document.getDocumentElement();
            // parsing team parameters
            NodeList teamsList = e.getElementsByTagName(TEAM_TAG);
            Element teamElement = (Element) teamsList.item(0);
            String teamName = teamElement.getAttribute(TEAM_NAME_TAG);
            Championship teamChampionship = Championship.valueOf(teamElement.getAttribute(TEAM_CHAMPIONSHIP_TAG));
            Color teamColor = Color.valueOf(teamElement.getAttribute(TEAM_PREFERED_COLOR));
            Team team = new Team(teamName, teamChampionship);
            team.setPreferedColor(teamColor);
            // parsing resting players
            NodeList restingPlayersListElements = teamElement.getElementsByTagName(XMLSaver.PLAYER_RESTING_TAG);
            Element restingPlayersListElement = (Element) restingPlayersListElements.item(0);
            NodeList restingPlayersList = restingPlayersListElement.getElementsByTagName(XMLSaver.PLAYER_TAG);
            for (int i = 0; i < restingPlayersList.getLength(); i++) {
                addPlayer(team, (Element) restingPlayersList.item(i), false);
            }
            //
            // parsing active players
            NodeList activePlayersListElements = teamElement.getElementsByTagName(XMLSaver.PLAYER_ACTIVE_TAG);
            Element activePlayersListElement = (Element) activePlayersListElements.item(0);
            NodeList activePlayersList = activePlayersListElement.getElementsByTagName(XMLSaver.PLAYER_TAG);
            for (int i = 0; i < activePlayersList.getLength(); i++) {
                addPlayer(team, (Element) activePlayersList.item(i), true);
            }
            //
            //Parsing Opposing teams
            NodeList oppTeamsListElements = teamElement.getElementsByTagName(XMLSaver.OPPOSING_TEAMS);
            for (int i = 0; i < oppTeamsListElements.getLength(); i++) {
            }
            Element oppTeamsListElement = (Element) oppTeamsListElements.item(0);
            if (oppTeamsListElement != null) {
                NodeList oppTeamsList = oppTeamsListElement.getElementsByTagName(XMLSaver.OPPOSING_TEAM);
                for (int i = 0; i < oppTeamsList.getLength(); i++) {
                    addOppTeam(team, (Element) oppTeamsList.item(i));
                }
            }
            // parsing games
            NodeList gamesListElements = teamElement.getElementsByTagName(XMLSaver.GAMES_TAG);
            Element gamesListElement = (Element) gamesListElements.item(0);
            if (gamesListElement != null) {
                NodeList gameElement = gamesListElement.getElementsByTagName(XMLSaver.GAME_TAG);
                for (int i = 0; i < gameElement.getLength(); i++) {
                    addGame(team, (Element) gameElement.item(i));
                }
            }
            //
            return team;
        } catch (IOException | SAXException | ParserConfigurationException ex) {
            Logger.getLogger(XMLLoader.class.getName()).log(Level.SEVERE, "", ex);
            return null;
        }
    }

    private static void addPlayer(Team team, Element playerElement, boolean active) {
        String playerFirstName = playerElement.getAttribute(PLAYER_FIRST_NAME_TAG);
        String playerLastName = playerElement.getAttribute(PLAYER_LAST_NAME_TAG);
        int playerNumber = Integer.parseInt(playerElement.getAttribute(PLAYER_NUMBER_TAG));
        Poste playerCurrentPosition = Poste.valueOf(playerElement.getAttribute(PLAYER_CURRENT_POSITION_TAG));
        Poste playerPreferedPosition = Poste.valueOf(playerElement.getAttribute(PLAYER_PREFERRED_POSITION_TAG));
        Player p = new Player(playerFirstName, playerLastName, playerNumber, playerPreferedPosition, playerCurrentPosition);
        team.addPlayer(p, active);
    }

    private static void addOppTeam(Team mainTeam, Element teamElement) {
        String teamName = teamElement.getAttribute(TEAM_NAME_TAG);
        Championship teamChampionship = Championship.valueOf(teamElement.getAttribute(TEAM_CHAMPIONSHIP_TAG));
        Color teamColor = Color.valueOf(teamElement.getAttribute(TEAM_PREFERED_COLOR));
        Team team = new Team(teamName, teamChampionship);
        team.setPreferedColor(teamColor);
        //
        // parsing active players
        NodeList activePlayersListElements = teamElement.getElementsByTagName(XMLSaver.PLAYER_ACTIVE_TAG);
        Element activePlayersListElement = (Element) activePlayersListElements.item(0);
        NodeList activePlayersList = activePlayersListElement.getElementsByTagName(XMLSaver.PLAYER_TAG);
        for (int i = 0; i < activePlayersList.getLength(); i++) {
            addPlayer(team, (Element) activePlayersList.item(i), true);
        }
        mainTeam.addOpponentTeam(team);
    }

    private static void addGame(Team team, Element gameElement) {
        LocalDate gameDate = LocalDate.parse(gameElement.getAttribute(GAME_DATE_TAG));
        int homeScore = Integer.parseInt(gameElement.getAttribute(GAME_HOME_TEAM_SCORE_TAG));
        int awayScore = Integer.parseInt(gameElement.getAttribute(GAME_AWAY_TEAM_SCORE_TAG));
        String homeTeamName = gameElement.getAttribute(GAME_HOME_TEAM_TAG);
        String awayTeamName = gameElement.getAttribute(GAME_AWAY_TEAM_TAG);
        // create Game;
        Game game = new Game();
        Team awayTeam = team.getOpponentTeam(awayTeamName);
        game.setUpGame(team, awayTeam, gameDate);
        NodeList gameActions = gameElement.getElementsByTagName(GAMEACTION_TAG);
        for (int i = 0; i < gameActions.getLength(); i++) {
            processGameActionElement((Element) gameActions.item(i), game);
        }
        assert game.getHomeScore() == homeScore;
        assert game.getAwayScore() == awayScore;
    }

    public static Team getMenTeam() {
        return getTeam("EquipeFranceMasculine.xml");
    }

    public static Team getWomenTeam() {
        return getTeam("EquipeFranceFeminine.xml");
    }

    private static Team getTeam(String fileName) {
        try {
            URL aUrl = XMLLoader.class.getResource(fileName);
            String s = aUrl.getFile();
            File f = Utilities.toFile(aUrl.toURI());
            Path path = Paths.get(f.getPath());
            Team result = XMLLoader.loadTeam(path);
            return result;
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    private static void processGameActionElement(Element gameActionElement, Game game) {
        //TODO: a faire par une autre classe
        String actionType = gameActionElement.getAttribute(GAMEACTION_TYPE);
        String teamName = gameActionElement.getAttribute(GAMEACTION_TEAM);
        String playerLastName = gameActionElement.getAttribute(GAMEACTION_PLAYER_LASTNAME);
        String playerFirstName = gameActionElement.getAttribute(GAMEACTION_PLAYER_FIRSTNAME);
        int playerNumber = Integer.parseInt(gameActionElement.getAttribute(GAMEACTION_PLAYER_NUMBER));
        String actionTime = gameActionElement.getAttribute(GAMEACTION_TIME);
        Team shootingTeam;
        if (teamName.equals(game.getHomeTeam().getName())) {
            shootingTeam = game.getHomeTeam();
        } else {
            shootingTeam = game.getAwayTeam();
        }
        Player player = shootingTeam.getPlayer(playerLastName, playerFirstName, playerNumber);
        Player goalKeeper;
        // TODO : optmiser
        String shotZone;
        String goalZone;
        String fault;
        switch (actionType) {
            case GAMEACTION_DEFENSE_BLOCKEDSHOT:
                shotZone = gameActionElement.getAttribute(GAMEACTION_SHOTZONE);
                DefenseBlockedShot defenseBlockedShot = new DefenseBlockedShot(player, shootingTeam, shotZone, actionTime);
                game.addAction(defenseBlockedShot);
                break;
            case GAMEACTION_FAULT:
                fault = gameActionElement.getAttribute(GAMEACTION_FAULT_DESCRIPTION);
                FaultAction faultAction = new FaultAction(player, shootingTeam, Fault.valueOf(fault), actionTime);
                game.addAction(faultAction);
                break;
            case GAMEACTION_SHOT_STOP:
                String goalLastName = gameActionElement.getAttribute(GAMEACTION_GOALKEEPER_NAME);
                String goalFirstName = gameActionElement.getAttribute(GAMEACTION_GOALKEEPER_FIRSTNAME);
                int goalNumber = Integer.parseInt(gameActionElement.getAttribute(GAMEACTION_GOALKEEPER_NUMBER));
                if (teamName.equals(game.getHomeTeam().getName())) {
                    goalKeeper = game.getAwayTeam().getPlayer(goalLastName, goalFirstName, goalNumber);
                } else {
                    goalKeeper = game.getHomeTeam().getPlayer(goalLastName, goalFirstName, goalNumber);
                }
                shotZone = gameActionElement.getAttribute(GAMEACTION_SHOTZONE);
                goalZone = gameActionElement.getAttribute(GAMEACTION_GOALZONE);
                ShotStop shotStop = new ShotStop(goalKeeper, player, shootingTeam, shotZone, goalZone, actionTime);
                game.addAction(shotStop);
                break;
            case GAMEACTION_GOOD_SHOT:
                shotZone = gameActionElement.getAttribute(GAMEACTION_SHOTZONE);
                goalZone = gameActionElement.getAttribute(GAMEACTION_GOALZONE);
                GoodShot goodShot = new GoodShot(player, shootingTeam, shotZone, goalZone, actionTime);
                game.addAction(goodShot);
                break;
        }
    }

}
