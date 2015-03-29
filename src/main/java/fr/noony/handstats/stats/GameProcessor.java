/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.noony.handstats.stats;

import fr.noony.handstats.Game;
import fr.noony.handstats.core.Team;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class GameProcessor {

    private final Team myTeam;
    private final List<Game> games;
    private final List<GameStat> gameStats;
    //
    private int nbVictories;

    public GameProcessor(Team team) {
        myTeam = team;
        games = new LinkedList<>();
        gameStats = new LinkedList<>();
        //
        nbVictories = 0;
        //
        initiate();

    }

    private void initiate() {
        for (Game game : myTeam.getGames()) {
            games.add(game);
            gameStats.add(new GameStat(game));
        }
        updateOverallStats();
    }

    public void addGame(Game game) {
        games.add(game);
        gameStats.add(new GameStat(game));
        updateOverallStats();
    }

    public int getNbGamesPlayed() {
        return games.size();
    }

    private void updateOverallStats() {
        nbVictories = 0;
        for (GameStat gameStat : gameStats) {
            if (gameStat.getVictor() != null && myTeam.getName().equals(gameStat.getVictor().getName())) {
                nbVictories++;
            }
        }
    }

    public int getNbVictories() {
        return nbVictories;
    }

    public double getPercentageVictories() {
        // format xx.xx
        int percentage = (int) (10000 * nbVictories / games.size());
        return percentage / 100;
    }

    public double getPercentageShotMade() {
        //TODO calculate one time
        double tmp = 0.0;
        for (GameStat gameStat : gameStats) {
            tmp += gameStat.getHomeAccuracy();
        }
        int percentage = (int) (100 * tmp / gameStats.size());
        return percentage / 100;
    }

    public double getPercentageStops() {
        //TODO calculate one time
        double tmp = 0.0;
        for (GameStat gameStat : gameStats) {
            tmp += gameStat.getHomeShotBlockedPercentage();
        }
        int percentage = (int) (100 * tmp / gameStats.size());
        return percentage / 100;
    }

    public GameStat getGameStats(Game selectedGame) {
        System.err.println("start seraching for game " + selectedGame.toString());
        for (GameStat stat : gameStats) {
            if (stat.reportsGame(selectedGame)) {
                System.err.println(" c'est " + stat);
                return stat;
            }
        }
        System.err.println("fail !!");
        return null;
    }
}
