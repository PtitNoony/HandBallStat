/*
 * Copyright (C) 2015 HAMON-KEROMEN A.
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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class Screens {

    public static final String LOADING_PAGE = "LoadingPage";
    public static final String INIT_PAGE = "InitPage";
    public static final String TEAM_CREATION_PAGE = "TeamCreatorPanel";
    public static final String TEAM_EDITION_PAGE = "TeamEditorPanel";
    public static final String PLAYER_EDITION_PAGE = "PlayerEditorPanel";
    public static final String TEAM_MAIN_PAGE = "TeamMainPanel";
    public static final String TEAM_SELECTION_PAGE = "TeamSelectionPanel";
    public static final String MATCH_SCOREBOARD_PAGE = "MatchScoreBoard";
    public static final String MATCH_CONFIGURATOR_PAGE = "MatchConfigurator";
    public static final String STATS_PAGE = "StatMainPage";

    public static List<String> PAGES = null;

    private Screens() {
        //utility private constructor
    }

    public static final List<String> getScreens() {
        if (PAGES != null) {
            return PAGES;
        } else {
            PAGES = new ArrayList<>();
            PAGES.add(INIT_PAGE);
            PAGES.add(TEAM_CREATION_PAGE);
            PAGES.add(TEAM_EDITION_PAGE);
            PAGES.add(PLAYER_EDITION_PAGE);
            PAGES.add(TEAM_MAIN_PAGE);
            PAGES.add(TEAM_SELECTION_PAGE);
            PAGES.add(MATCH_SCOREBOARD_PAGE);
            PAGES.add(MATCH_CONFIGURATOR_PAGE);
            PAGES.add(STATS_PAGE);
            return PAGES;
        }
    }

}
