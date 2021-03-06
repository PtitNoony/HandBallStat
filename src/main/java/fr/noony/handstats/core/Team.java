/*
 * Copyright (C) 2014 Arnaud
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

import fr.noony.handstats.Championship;
import fr.noony.handstats.Poste;
import static fr.noony.handstats.core.Game.MAX_PLAYERS_PER_MATCH;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javafx.scene.paint.Color;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class Team {

    public static final String NEW_PLAYER = "newPlayer";

    private static final Long START_ID = 101L;

    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);

    private final List<Player> allPlayers;
    private final List<Player> activePlayers;
    private final List<Game> games;

    private final String teamName;
    private final Championship championship;
    private final List<Team> opponentTeams;
    //
    private Color preferedColor = Color.BLUEVIOLET;

    private Player currentGoalKeeper = null;

    private long newPlayerID = START_ID;

    public Team(String name, Championship c) {
        propertyChangeSupport = new PropertyChangeSupport(Team.this);
        lookupContents.add(propertyChangeSupport);
        allPlayers = new LinkedList<>();
        activePlayers = new LinkedList<>();
        games = new LinkedList<>();
        teamName = name;
        championship = c;
        opponentTeams = new LinkedList<>();
    }

    public Player createPlayer(String premon, String nom, int number, Poste positionPreferee, boolean active) {
        return createPlayer(premon, nom, number, positionPreferee, positionPreferee, active);
    }

    public Player createPlayer(String premon, String nom, int number, Poste positionPreferee, Poste positionActuelle, boolean active) {
        for (Player p : allPlayers) {
            if (p.getNumber() == number) {
                throw new IllegalArgumentException("Le numéro " + number + " est deja pris par " + p.getFirstName());
            }
        }
        Player newPlayer = new Player(premon, nom, number, positionPreferee, newPlayerID);
        newPlayerID++;
        if (active) {
            if (activePlayers.size() > MAX_PLAYERS_PER_MATCH) {
                throw new IllegalArgumentException("La liste des joueurs actifs est deja pleine");
            }
            activePlayers.add(newPlayer);
        }
        allPlayers.add(newPlayer);
        firePropertyChange(NEW_PLAYER, active, newPlayer);
        return newPlayer;
    }

    //TODO: use factory (no time)
    public Player addPlayer(String premon, String nom, int number, Poste positionPreferee, Poste positionActuelle, long id, boolean active) {
        for (Player p : allPlayers) {
//            if (p.getNumber() == number) {
//                throw new IllegalArgumentException("Le numéro " + number + " est deja pris par " + p.getFirstName());
//            }
            if (p.getUniqueID() == id) {
                throw new IllegalArgumentException("L'ID " + id + " est deja pris par " + p.getFirstName());
            }
        }
        Player newPlayer = new Player(premon, nom, number, positionPreferee, id);
        newPlayerID = Math.max(newPlayerID, id + 1);
        if (active) {
            if (activePlayers.size() > MAX_PLAYERS_PER_MATCH) {
                throw new IllegalArgumentException("La liste des joueurs actifs est deja pleine");
            }
            activePlayers.add(newPlayer);
        }
        allPlayers.add(newPlayer);
        firePropertyChange(NEW_PLAYER, active, newPlayer);
        return newPlayer;
    }

    public void addOpponentTeam(Team t) {
        opponentTeams.add(t);
    }

    public Player getActivePlayerByNumber(int number) {
        for (Player j : activePlayers) {
            if (j.getNumber() == number) {
                return j;
            }
        }
        return null;
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    public List<Player> getRestingPlayers() {
        List<Player> result = new ArrayList<>();
        allPlayers.stream().filter(p -> (!activePlayers.contains(p))).forEach(p -> result.add(p));
        return result;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public Lookup getLookup() {
        return alookup;
    }

    public String getName() {
        return teamName;
    }

    public Championship getChampionship() {
        return championship;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(teamName);
        sb.append(" - ");
        sb.append(championship);
        return sb.toString();
    }

    public void setPlayerActive(Player player, boolean active) {
        if (active) {
            activePlayers.add(player);
        } else {
            activePlayers.remove(player);
        }
    }

    public boolean containsActivePlayer(Player selectedPlayer) {
        return activePlayers.contains(selectedPlayer);
    }

    public List<Team> getOpponentTeams() {
        return opponentTeams;
    }

    public Color getPreferedColor() {
        return preferedColor;
    }

    public void setPreferedColor(Color color) {
        preferedColor = color;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public List<Game> getGames() {
        return Collections.unmodifiableList(games);
    }

    public Team getOpponentTeam(String awayTeamName) {
        for (Team oppTeam : opponentTeams) {
            if (oppTeam.getName().equals(awayTeamName)) {
                return oppTeam;
            }
        }
        System.err.println(" *** no corresponding team for " + awayTeamName);
        return null;
    }

    @Deprecated
    public Player getPlayer(String playerLastName, String playerFirstName, int playerNumber) {
        for (Player p : getActivePlayers()) {
            if (playerLastName.equals(p.getLastName()) && playerFirstName.equals(p.getFirstName()) && playerNumber == p.getNumber()) {
                return p;
            }
        }
        for (Player p : getRestingPlayers()) {
            if (playerLastName.equals(p.getLastName()) && playerFirstName.equals(p.getFirstName()) && playerNumber == p.getNumber()) {
                return p;
            }
        }
        System.err.println(" PLAYER IS NOT FOUND :: " + playerLastName + "_" + playerFirstName + "_" + playerNumber + " in team " + teamName);
        return null;
    }

    public Player getPlayer(long uniqueID) {
        for (Player p : getActivePlayers()) {
            if (p.getUniqueID() == uniqueID) {
                return p;
            }
        }
        for (Player p : getRestingPlayers()) {
            if (p.getUniqueID() == uniqueID) {
                return p;
            }
        }
        throw new IllegalArgumentException(" PLAYER IS NOT FOUND :: " + uniqueID + " in team " + teamName);
//        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team) {
            Team team = (Team) obj;
            return teamName.equals(team.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.teamName);
        return hash;
    }

    public Game getGameInProgress() {
        for (Game game : games) {
            if (!game.isFinished()) {
                return game;
            }
        }
        return null;
    }

    public void removePlayer(int playerNum) {
        Player playerToRemove = null;
        for (Player p : allPlayers) {
            if (p.getNumber() == playerNum) {
                playerToRemove = p;
                break;
            }
        }
        if (activePlayers.contains(playerToRemove)) {
            activePlayers.remove(playerToRemove);
        }
        if (allPlayers.contains(playerToRemove)) {
            allPlayers.remove(playerToRemove);
        }
    }

    public Player getCurrentGoalKeeper() {
        return currentGoalKeeper;
    }

    public void setCurrentGoalKeeper(Player goalkeeper) {
        if (currentGoalKeeper != null) {
            currentGoalKeeper.setCurrentGoalKeeper(false);
        }
        currentGoalKeeper = goalkeeper;
        currentGoalKeeper.setCurrentGoalKeeper(true);
    }

    public boolean initCurrentGoalKeeper() {
        if (currentGoalKeeper == null) {
            for (Player p : activePlayers) {
                if (Poste.GARDIEN.equals(p.getPositionActuelle())) {
                    currentGoalKeeper = p;
                    currentGoalKeeper.setCurrentGoalKeeper(true);
                    return true;
                }
            }
            throw new IllegalStateException("No GoalKeeper in active players");
        } else {
            return true;
        }

    }

}
