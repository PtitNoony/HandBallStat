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
package fr.noony.handstats;

import static fr.noony.handstats.GameClock.CLOCK_END_SECOND_HALF;
import fr.noony.handstats.core.DefenseBlockedShot;
import fr.noony.handstats.core.Fault;
import fr.noony.handstats.core.FaultAction;
import fr.noony.handstats.core.GameAction;
import fr.noony.handstats.core.GoodShot;
import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.ShotStop;
import fr.noony.handstats.core.Team;
import fr.noony.handstats.court.InteractiveShootingArea;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class Game implements PropertyChangeListener {

    public static final int MIN_PLAYERS_PER_MATCH = 6;
    public static final int MAX_PLAYERS_PER_MATCH = 12;
    public static final int HALF_TIME_DURATION = 30 * 60 * 1000;
    //
    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);
    //
    private Team homeTeam;
    private Team awayTeam;
    private LocalDate gameDate;
    //
    private final List<Player> homePlayers;
    private final List<Player> awayPlayers;
    //
    private final GameClock gameClock;
    //
    private final List<GameAction> gameActions;
    //TODO:remove and focus on game actions
    private final List<GoodShot> homeGoodShots;
    private final List<GoodShot> awayGoodShots;
    private final List<Player> twoMinutesPlayers;
    private boolean isOver;

    public Game() {
        propertyChangeSupport = new PropertyChangeSupport(Game.this);
        lookupContents.add(propertyChangeSupport);
        gameActions = new LinkedList<>();
        homeGoodShots = new LinkedList<>();
        awayGoodShots = new LinkedList<>();
        twoMinutesPlayers = new LinkedList<>();
        //
        homePlayers = new LinkedList<>();
        awayPlayers = new LinkedList<>();
        gameClock = new GameClock();
        gameClock.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(Game.this);
//        boolean ready = checkSetUp();
//        if (!ready) {
//            throw new IllegalStateException("Le match n'est pas bien parametre !!");
//        }
        isOver = false;
    }

    public void setUpGame(Team teamA, Team teamB, LocalDate date) {
        homeTeam = teamA;
        homeTeam.addGame(this);
        awayTeam = teamB;
        gameDate = date;
    }

//    private boolean checkSetUp() {
//        if (homeTeam.getActivePlayers().size() < MIN_PLAYERS_PER_MATCH || homeTeam.getActivePlayers().size() > MAX_PLAYERS_PER_MATCH) {
//            return false;
//        }
//        if (awayTeam.getActivePlayers().size() < MIN_PLAYERS_PER_MATCH || awayTeam.getActivePlayers().size() > MAX_PLAYERS_PER_MATCH) {
//            return false;
//        }
//        homePlayers = homeTeam.getActivePlayers();
//        awayPlayers = awayTeam.getActivePlayers();
//        return true;
//    }
    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public GameClock getGameClock() {
        return gameClock;
    }

    public void score(Player shootingPlayer, boolean shootingPlayerIsHome, InteractiveShootingArea shootingZone, InteractiveShootingArea goalArea) {
        GoodShot shot;
        if (shootingPlayerIsHome) {
            shot = new GoodShot(shootingPlayer, homeTeam, shootingZone.getName(), goalArea.getName(), gameClock.getTime());
            homeGoodShots.add(shot);
            firePropertyChange("HOME_SCORED", null, homeGoodShots.size());
        } else {
            shot = new GoodShot(shootingPlayer, awayTeam, shootingZone.getName(), goalArea.getName(), gameClock.getTime());
            awayGoodShots.add(shot);
            firePropertyChange("AWAY_SCORED", null, awayGoodShots.size());
        }
        gameActions.add(shot);
    }

    public void addGoalStop(Player goal, Player shootingPlayer, boolean shootingPlayerIsHome, InteractiveShootingArea shootingZone, InteractiveShootingArea goalArea) {
        ShotStop shot;
        if (shootingPlayerIsHome) {
            shot = new ShotStop(goal, shootingPlayer, homeTeam, shootingZone.getName(), goalArea.getName(), gameClock.getTime());
            firePropertyChange("AWAY_BLOCKED_GOAL_ATEMPT", null, goal);
        } else {
            shot = new ShotStop(goal, shootingPlayer, awayTeam, shootingZone.getName(), goalArea.getName(), gameClock.getTime());
            firePropertyChange("HOME_BLOCKED_GOAL_ATEMPT", null, goal);
        }
        gameActions.add(shot);
    }

    public void addGoalCountered(Player shootingPlayer, boolean shootingPlayerIsHome, InteractiveShootingArea shootingZone) {
        DefenseBlockedShot shot;
        if (shootingPlayerIsHome) {
            shot = new DefenseBlockedShot(shootingPlayer, homeTeam, shootingZone.getName(), gameClock.getTime());
            firePropertyChange("AWAY_BLOCKED_BY_DEFENSE_ATEMPT", null, shot);
        } else {
            shot = new DefenseBlockedShot(shootingPlayer, awayTeam, shootingZone.getName(), gameClock.getTime());
            firePropertyChange("HOME_BLOCKED_BY_DEFENSE_ATEMPT", null, shot);
        }
        gameActions.add(shot);
    }

    public void addFault(Player player, boolean shootingPlayerIsHome, Fault fault) {
        FaultAction faultAction;
        if (shootingPlayerIsHome) {
            faultAction = new FaultAction(player, homeTeam, fault, gameClock.getTime());
        } else {
            faultAction = new FaultAction(player, awayTeam, fault, gameClock.getTime());
        }
        switch (fault) {
            case YELLOW_CARD:
                //TODO: check if player has one yellow
                player.receiveYellowCard();
                break;
            case TWO_MINUTES:
                //TODO: check if player has two 2m's
                player.receive2Minutes();
                twoMinutesPlayers.add(player);
                player.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
                break;
            case RED_CARD:
                player.receiverRedCard();
                player.receive2Minutes();
                twoMinutesPlayers.add(player);
                player.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(this);
                break;
            default:
                throw new UnsupportedOperationException("unknown fault :" + fault + " by player " + player);
        }
        gameActions.add(faultAction);
    }

    public Lookup getLookup() {
        return alookup;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeGoodShots.size();
    }

    public int getAwayScore() {
        return awayGoodShots.size();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case GameClock.CLOCK_VALUE_CHANGED:
                reduceTwoMinutesRemaining();
                break;
            case Player.END_TWO_MINUTES:
                Player player = (Player) evt.getNewValue();
                player.getLookup().lookup(PropertyChangeSupport.class).removePropertyChangeListener(this);
                Platform.runLater(() -> twoMinutesPlayers.remove(player));
                break;
            case CLOCK_END_SECOND_HALF:
                //TODO: fix when prolongations
                isOver = true;
                break;
            default:
                //nothing
                break;
        }
    }

    private void reduceTwoMinutesRemaining() {
        twoMinutesPlayers.stream().forEach(p -> p.reduceTwoMinuteRemaning());
    }

    public LocalDate getDate() {
        return gameDate;
    }

    public List<GameAction> getActions() {
        return Collections.unmodifiableList(gameActions);
    }

    public void addAction(GameAction action) {
        //TODO : ajouter les differentes actions aux bons endroits
        if (action instanceof GoodShot) {
            GoodShot goodShot = (GoodShot) action;
            if (goodShot.getShooterTeam().getName().equals(homeTeam.getName())) {
                homeGoodShots.add(goodShot);
            } else {
                awayGoodShots.add(goodShot);
            }
        }
        gameActions.add(action);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(gameDate.toString());
        sb.append("   ");
        sb.append(homeGoodShots.size());
        sb.append("-");
        sb.append(awayGoodShots.size());
        sb.append("  vs. ");
        sb.append(awayTeam.getName());
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        //humm ;)
        if (obj instanceof Game) {
            Game game = (Game) obj;
            return homeTeam.getName().equals(game.getHomeTeam().getName())
                    && awayTeam.getName().equals(game.getAwayTeam().getName())
                    && gameDate.equals(game.getDate())
                    && homeGoodShots.size() == game.getHomeScore()
                    && awayGoodShots.size() == game.getAwayScore();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.propertyChangeSupport);
        hash = 11 * hash + Objects.hashCode(this.lookupContents);
        hash = 11 * hash + Objects.hashCode(this.alookup);
        hash = 11 * hash + Objects.hashCode(this.homeTeam);
        hash = 11 * hash + Objects.hashCode(this.awayTeam);
        hash = 11 * hash + Objects.hashCode(this.gameDate);
        hash = 11 * hash + Objects.hashCode(this.homePlayers);
        hash = 11 * hash + Objects.hashCode(this.awayPlayers);
        hash = 11 * hash + Objects.hashCode(this.gameClock);
        hash = 11 * hash + Objects.hashCode(this.gameActions);
        hash = 11 * hash + Objects.hashCode(this.homeGoodShots);
        hash = 11 * hash + Objects.hashCode(this.awayGoodShots);
        hash = 11 * hash + Objects.hashCode(this.twoMinutesPlayers);
        return hash;
    }

    public boolean isFinished() {
        return isOver;
    }

    public void setStatus(boolean over) {
        //TODO checks it is OK
        isOver = over;
    }

    public void setCurrentTime(String time) {
        gameClock.setTime(time);
    }

    public void close() {
        isOver = true;
    }

}
