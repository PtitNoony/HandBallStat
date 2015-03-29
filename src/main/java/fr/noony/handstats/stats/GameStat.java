/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.noony.handstats.stats;

import fr.noony.handstats.Game;
import fr.noony.handstats.core.DefenseBlockedShot;
import fr.noony.handstats.core.FaultAction;
import fr.noony.handstats.core.GameAction;
import fr.noony.handstats.core.GoodShot;
import fr.noony.handstats.core.ShotStop;
import fr.noony.handstats.core.Team;
import java.time.LocalDate;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class GameStat {

    private final Game myGame;
    private final Team homeTeam;
    private final String homeTeamName;
    private Team victor;
    //
    private int nbHomeBlockedShots = 0;
    private int nbAwayBlockedShots = 0;
    private int nbHomeMadeShots = 0;
    private int nbAwayMadeShots = 0;
    private int nbHomeDefStopShots = 0;
    private int nbAwayDefStopShots = 0;

    public GameStat(Game game) {
        myGame = game;
        homeTeam = myGame.getHomeTeam();
        homeTeamName = homeTeam.getName();
        initStats();
    }

    private void initStats() {
        calculateVictor();
        processGameActions();
    }

    private void calculateVictor() {
        if (myGame.getHomeScore() > myGame.getAwayScore()) {
            victor = myGame.getHomeTeam();
        } else if (myGame.getHomeScore() < myGame.getAwayScore()) {
            victor = myGame.getAwayTeam();
        } else {
            //draw
            victor = null;
        }
    }

    private void processGameActions() {
        for (GameAction action : myGame.getActions()) {
            if (action instanceof ShotStop) {
                processShotStop((ShotStop) action);
            } else if (action instanceof GoodShot) {
                processGoodShot((GoodShot) action);
            } else if (action instanceof DefenseBlockedShot) {
                processDefenseBlockedShot((DefenseBlockedShot) action);
            } else if (action instanceof FaultAction) {
                processFaultAction((FaultAction) action);
            } else {
                throw new UnsupportedOperationException("cannont process unkwnon type of game action :" + action);
            }
        }
    }

    public Team getVictor() {
        return victor;
    }

    public boolean isHomeVictor() {
        return victor != null && victor.getName().equals(homeTeamName);
    }

    public boolean isDraw() {
        return victor == null;
    }

    public Team getHomeTeam() {
        return myGame.getHomeTeam();
    }

    public Team getAwayTeam() {
        return myGame.getAwayTeam();
    }

    public int getHomeScore() {
        //usefull ???
        return myGame.getHomeScore();
    }

    public int getAwayScore() {
        //usefull ???
        return myGame.getAwayScore();
    }

    public LocalDate getGameDate() {
        return myGame.getDate();
    }

    private void processShotStop(ShotStop shotStop) {
        if (shotStop.getShooterTeam().getName().equals(homeTeamName)) {
            nbAwayBlockedShots++;
        } else {
            nbHomeBlockedShots++;
        }
    }

    private void processGoodShot(GoodShot goodShot) {
        if (goodShot.getShooterTeam().getName().equals(homeTeamName)) {
            nbHomeMadeShots++;
        } else {
            nbAwayMadeShots++;
        }
    }

    private void processDefenseBlockedShot(DefenseBlockedShot defenseBlockedShot) {
        if (defenseBlockedShot.getShooterTeam().getName().equals(homeTeamName)) {
            nbAwayDefStopShots++;
        } else {
            nbHomeDefStopShots++;
        }
    }

    private void processFaultAction(FaultAction faultAction) {
    }

    public double getHomeAccuracy() {
        return 100 * nbHomeMadeShots / (nbHomeMadeShots + nbAwayDefStopShots + nbAwayBlockedShots);
    }

    public double getAwayAccuracy() {
        return 100 * nbAwayMadeShots / (nbAwayMadeShots + nbHomeBlockedShots + nbHomeDefStopShots);
    }

    public double getHomeShotBlockedPercentage() {
        return 100 * nbHomeBlockedShots / (nbAwayMadeShots + nbHomeBlockedShots);
    }

    public double getAwayShotBlockedPercentage() {
        return 100 * nbAwayBlockedShots / (nbHomeMadeShots + nbAwayBlockedShots);
    }

    public boolean reportsGame(Game selectedGame) {
        return myGame.equals(selectedGame);
    }

}
