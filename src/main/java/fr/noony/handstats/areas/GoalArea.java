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
package fr.noony.handstats.areas;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class GoalArea extends AbstractArea {

    public static final GoalArea LEFT_POST = new GoalArea("LEFT_POST", Side.LEFT);
    public static final GoalArea RIGHT_POST = new GoalArea("RIGHT_POST", Side.RIGHT);
    public static final GoalArea UPPER_POST = new GoalArea("UPPER_POST", Side.CENTER);
    public static final GoalArea UPPER_CENTER = new GoalArea("UPPER_CENTER", Side.CENTER);
    public static final GoalArea UPPER_LEFT = new GoalArea("UPPER_LEFT", Side.LEFT);
    public static final GoalArea UPPER_RIGHT = new GoalArea("UPPER_RIGHT", Side.RIGHT);
    public static final GoalArea LEFT = new GoalArea("UPPER_LEFT", Side.LEFT);
    public static final GoalArea RIGHT = new GoalArea("UPPER_RIGHT", Side.RIGHT);

    private static final String GOAL_INNER_ZONE = "GOAL_INNER";

    public GoalArea(String areaName, Side areaSide) {
        super(areaName, areaSide);
    }

    public GoalArea(int i, int j) {
        super(GOAL_INNER_ZONE + "_" + i + "_" + j, Side.CENTER);
    }

}
