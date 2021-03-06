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
package fr.noony.handstats.stats;

import fr.noony.handstats.core.DefenseBlockedShot;
import fr.noony.handstats.core.FaultAction;
import fr.noony.handstats.core.Game;
import fr.noony.handstats.core.GameAction;
import fr.noony.handstats.core.GameActionComparator;
import fr.noony.handstats.core.GoodShot;
import fr.noony.handstats.core.ShotStop;
import fr.noony.handstats.core.Team;
import fr.noony.handstats.court.HalfCourtDrawing;
import fr.noony.handstats.team.hmi.stats.TerrainAreas;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
    private final int[] homeMadeShotsTerrain;
    private final int[] homeMissedShotsTerrain;
    private final int[] awayMadeShotsTerrain;
    private final int[] awayMissedShotsTerrain;

    public GameStat(Game game) {
        myGame = game;
        homeTeam = myGame.getHomeTeam();
        homeTeamName = homeTeam.getName();
        //
        homeMadeShotsTerrain = new int[HalfCourtDrawing.NB_TERRAIN_AREAS];
        homeMissedShotsTerrain = new int[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            homeMadeShotsTerrain[i] = 0;
        }
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            homeMissedShotsTerrain[i] = 0;
        }
        //
        awayMadeShotsTerrain = new int[HalfCourtDrawing.NB_TERRAIN_AREAS];
        awayMissedShotsTerrain = new int[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            awayMadeShotsTerrain[i] = 0;
        }
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            awayMissedShotsTerrain[i] = 0;
        }
        //
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

    public final Game getGame() {
        return myGame;
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
            for (TerrainAreas area : TerrainAreas.values()) {
                if (shotStop.getShootingZone().contains(area.getName())) {
                    homeMissedShotsTerrain[area.getId()] += 1;
                }
            }
        } else {
            nbHomeBlockedShots++;
            for (TerrainAreas area : TerrainAreas.values()) {
                if (shotStop.getShootingZone().contains(area.getName())) {
                    awayMissedShotsTerrain[area.getId()] += 1;
                }
            }
        }
    }

    private void processGoodShot(GoodShot goodShot) {
        if (goodShot.getShooterTeam().getName().equals(homeTeamName)) {
            nbHomeMadeShots++;
            for (TerrainAreas area : TerrainAreas.values()) {
                if (goodShot.getShootingZone().contains(area.getName())) {
                    homeMadeShotsTerrain[area.getId()] = homeMadeShotsTerrain[area.getId()] + 1;
                }
            }
        } else {
            nbAwayMadeShots++;
            for (TerrainAreas area : TerrainAreas.values()) {
                if (goodShot.getShootingZone().contains(area.getName())) {
                    awayMadeShotsTerrain[area.getId()] += 1;
                }
            }
        }
    }

    private void processDefenseBlockedShot(DefenseBlockedShot defenseBlockedShot) {
        if (defenseBlockedShot.getShooterTeam().getName().equals(homeTeamName)) {
            nbAwayDefStopShots++;
            for (TerrainAreas area : TerrainAreas.values()) {
                if (defenseBlockedShot.getShootingZone().contains(area.getName())) {
                    homeMissedShotsTerrain[area.getId()] += 1;
                }
            }
        } else {
            nbHomeDefStopShots++;
            for (TerrainAreas area : TerrainAreas.values()) {
                if (defenseBlockedShot.getShootingZone().contains(area.getName())) {
                    awayMissedShotsTerrain[area.getId()] += 1;
                }
            }
        }
    }

    private void processFaultAction(FaultAction faultAction) {
    }

    public double getHomeAccuracy() {
        if ((nbHomeMadeShots + nbAwayDefStopShots + nbAwayBlockedShots) == 0) {
            return 0;
        }
        return 100 * nbHomeMadeShots / (nbHomeMadeShots + nbAwayDefStopShots + nbAwayBlockedShots);
    }

    public double getAwayAccuracy() {
        if ((nbAwayMadeShots + nbHomeBlockedShots + nbHomeDefStopShots) == 0) {
            return 0;
        }
        return 100 * nbAwayMadeShots / (nbAwayMadeShots + nbHomeBlockedShots + nbHomeDefStopShots);
    }

    public double getHomeShotBlockedPercentage() {
        if ((nbAwayMadeShots + nbHomeBlockedShots) == 0) {
            return 0;
        }
        return 100 * nbHomeBlockedShots / (nbAwayMadeShots + nbHomeBlockedShots);
    }

    public double getAwayShotBlockedPercentage() {
        if ((nbHomeMadeShots + nbAwayBlockedShots) == 0) {
            return 0;
        }
        return 100 * nbAwayBlockedShots / (nbHomeMadeShots + nbAwayBlockedShots);
    }

    public boolean reportsGame(Game selectedGame) {
        return myGame.equals(selectedGame);
    }

    //NB
    public double[] getHomeNbShotMadeByTerrainAreaRatio() {
        //TODO calculate it only once
        double[] result = new double[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            double tmp = ((double) homeMadeShotsTerrain[i]) / (nbHomeMadeShots);
            //+ nbAwayDefStopShots + nbAwayBlockedShots
            result[i] = tmp;
        }
        return result;
    }

    public double[] getHomeNbShotMissedByTerrainAreaRatio() {
        //TODO calculate it only once
        double[] result = new double[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            result[i] = ((double) homeMissedShotsTerrain[i]) / (nbHomeMadeShots);
            //+ nbAwayDefStopShots + nbAwayBlockedShots
        }
        return result;
    }

    public double[] getAwayNbShotMadeByTerrainAreaRatio() {
        //TODO calculate it only once
        double[] result = new double[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            result[i] = ((double) awayMadeShotsTerrain[i]) / (nbAwayMadeShots);
            //+ nbHomeBlockedShots + nbHomeDefStopShots
        }
        return result;
    }

    public double[] getAwayNbShotMissedByTerrainAreaRatio() {
        //TODO calculate it only once
        double[] result = new double[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            result[i] = ((double) awayMissedShotsTerrain[i]) / (nbAwayMadeShots);
            //+ nbHomeBlockedShots + nbHomeDefStopShots
        }
        return result;
    }

    //%
    public double[] getHomePercShotMadeByTerrainArea() {
        //TODO calculate it only once
        double[] result = new double[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            if (homeMadeShotsTerrain[i] + homeMissedShotsTerrain[i] == 0) {
                result[i] = -1;
            } else {
                result[i] = ((double) homeMadeShotsTerrain[i]) / ((double) homeMadeShotsTerrain[i] + homeMissedShotsTerrain[i]);
            }

        }
        return result;
    }

    public double[] getHomePercShotMissedByTerrainArea() {
        //TODO calculate it only once
        double[] result = new double[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            if (homeMadeShotsTerrain[i] + homeMissedShotsTerrain[i] == 0) {
                result[i] = -1;
            } else {
                result[i] = ((double) homeMissedShotsTerrain[i]) / ((double) homeMadeShotsTerrain[i] + homeMissedShotsTerrain[i]);
            }
        }
        return result;
    }

    public double[] getAwayPercShotMadeByTerrainArea() {
        //TODO calculate it only once
        double[] result = new double[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            if (awayMadeShotsTerrain[i] + awayMissedShotsTerrain[i] == 0) {
                result[i] = -1;
            } else {
                result[i] = ((double) awayMadeShotsTerrain[i]) / ((double) awayMadeShotsTerrain[i] + awayMissedShotsTerrain[i]);
            }
        }
        return result;
    }

    public double[] getAwayPercShotMissedByTerrainArea() {
        //TODO calculate it only once
        double[] result = new double[HalfCourtDrawing.NB_TERRAIN_AREAS];
        for (int i = 0; i < HalfCourtDrawing.NB_TERRAIN_AREAS; i++) {
            if (awayMadeShotsTerrain[i] + awayMissedShotsTerrain[i] == 0) {
                result[i] = -1;
            } else {
                result[i] = ((double) awayMissedShotsTerrain[i]) / ((double) awayMadeShotsTerrain[i] + awayMissedShotsTerrain[i]);
            }
        }
        return result;
    }

    public List<GameAction> getGoalChronology() {
        //TODO calculate it only once
        List<GameAction> result = new LinkedList<>();
        myGame.getActions().stream().filter(action -> action instanceof GoodShot).forEach(action
                -> result.add(action));
        Collections.sort(result, new GameActionComparator());
        return result;
    }

    public int getMaxDelta() {
        //TODO calculate it only once
        //TODO optimize
        List<GoodShot> goodShots = new LinkedList<>();
        int currentDelta = 0;
        int maxDelta = 0;
        for (GameAction action : myGame.getActions()) {
            if (action instanceof GoodShot) {
                goodShots.add((GoodShot) action);
            }
        }
        Collections.sort(goodShots, new GameActionComparator());
        GoodShot currentAction;
        for (int i = 0; i < goodShots.size(); i++) {
            currentAction = goodShots.get(i);
            if (currentAction.getShooterTeam().equals(homeTeam)) {
                currentDelta++;
            } else {
                currentDelta--;
            }
            maxDelta = Math.max(maxDelta, Math.abs(currentDelta));
        }
        return maxDelta;
    }

}
