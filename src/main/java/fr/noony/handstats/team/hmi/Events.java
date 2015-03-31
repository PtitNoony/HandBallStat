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
package fr.noony.handstats.team.hmi;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public final class Events {

    private Events() {

    }

    //
    public static final String OK_EVENT = "okEvent";
    public static final String CANCEL_EVENT = "cancelEvent";
    //
    public static final String CANCEL_TEAM_CREATION_EVENT = "cancelTeamCreationAction";
    public static final String CREATE_TEAM_EVENT = "createTeamCreatorAction";

    public static final String ACTION_CREATE_TEAM = "createTeamAction";

    public static final String ACTION_CHANGE_TEAM = "changeTeamAction";

    //team main panel events
    public static final String BACK_TEAM_SELECTION = "backTeamSelectionEvent";
    public static final String EDIT_CURRENT_TEAM = "editCurrentTeam";
//    public static final String PLAY_MATCH_WITH_CURRENT_TEAM = "playMachtWithTeam";
    public static final String CONFIGURE_MATCH = "configureMatch";
    public static final String DISPLAY_STATS_FROM_MAIN = "displayStatsFromMain";

    //team selection panel events
    public static final String NEW_TEAM_SELECTED_EVENT = "newTeamSelected";
    public static final String CANCEL_TEAM_SELECTED_EVENT = "cancelTeamSelection";

    //team editor panel events
    public static final String BACK_TO_TEAM_MAIN = "backToTeamMainAction";

    //match  configurator events
    public static final String START_GAME = "startGame";

    //match controller events
    public static final String BACK_MACTH_MENU = "backToMatchMenu";
    public static final String YELLOW_CARD = "yellowCard";
    public static final String RED_CARD = "redCard";
    public static final String TWO_MINUTES = "twoMinutes";
    public static final String SEVEN_METERS_GOAL = "7m";
    public static final String GOAL_COUNTERED = "goalCountered";
    public static final String TEAM_STATS = "teamStats";

    //stat pop up
    public static final String BACK_TO_GAME = "backToGame";

}
