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
package fr.noony.handstats.team.newhmi;

import java.awt.Dimension;

/**
 *
 * @author Arnaud
 */
public final class FXScreenUtils {

    public static final int UPPER_BORDER_HEIGHT = 50;

    /**
     * default number of pixels between screens components
     */
    public static final int DEFAULT_ELEMENT_SPACING = 25;

    public static final int NB_ITEMS_PER_LIST = 15;

    public static final double DOCK_VERTICAL_RATIO = 0.18;
    public static final double PAGES_VERTICAL_RATIO = 1 - DOCK_VERTICAL_RATIO;
    public static final String STAGE_DIMENSION_CHANGED = "stageDimensionChanged";

    public static final Dimension DEFAULT_RESOLUTION = new Dimension(800, 600);

    public static final String REQUEST_PARAMETERS_SCREEN = "requestParametersScreen";
    public static final String REQUEST_STATS_SCREEN = "requestStatsScreen";
    public static final String REQUEST_GAME_SCREEN = "requestGameScreen";
    public static final String REQUEST_TEAM_SCREEN = "requestTeamScreen";

    private FXScreenUtils() {
        //utility private constructor
    }

    public static int getFontSize(double componentHeight) {
        return (int) (componentHeight * 0.7);
    }

    public static int getButtonFontSize(double componentHeight) {
        return (int) (componentHeight * 0.5);
    }

}
