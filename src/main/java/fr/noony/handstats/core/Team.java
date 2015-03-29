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
import fr.noony.handstats.Game;
import static fr.noony.handstats.Game.MAX_PLAYERS_PER_MATCH;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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

    public void addPlayer(Player j, boolean active) {
        for (Player joueur : allPlayers) {
            if (joueur.getNumero() == j.getNumero()) {
                throw new IllegalArgumentException("Le numéro " + j.getNumero() + " est deja pris par " + joueur.getFirstName());
            }
        }
        if (active) {
            if (activePlayers.size() > MAX_PLAYERS_PER_MATCH) {
                throw new IllegalArgumentException("La liste des joueurs actifs est deja pleine");
            }
            activePlayers.add(j);
        }
        allPlayers.add(j);
        firePropertyChange(NEW_PLAYER, active, j);
    }

    public void addOpponentTeam(Team t) {
        opponentTeams.add(t);
    }

    public Player getActivePlayerByNumber(int number) {
        for (Player j : activePlayers) {
            if (j.getNumero() == number) {
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

    public Player getPlayer(String playerLastName, String playerFirstName, int playerNumber) {
        for (Player p : getActivePlayers()) {
            if (playerLastName.equals(p.getLastName()) && playerFirstName.equals(p.getFirstName()) && playerNumber == p.getNumero()) {
                return p;
            }
        }
        for (Player p : getRestingPlayers()) {
            if (playerLastName.equals(p.getLastName()) && playerFirstName.equals(p.getFirstName()) && playerNumber == p.getNumero()) {
                return p;
            }
        }
        System.err.println(" PLAYER IS NOT FOUND :: " + playerLastName + "_" + playerFirstName + "_" + playerNumber + " in team " + teamName);
        return null;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if(obj instanceof Team){
//            Team team = (Team)obj;
//            return teamName.equals(team.getName());
//        }
//    }
//    
}
