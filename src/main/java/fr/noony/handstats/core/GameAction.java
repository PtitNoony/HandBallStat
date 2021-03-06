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
package fr.noony.handstats.core;

import fr.noony.handstats.utils.TimeCalculator;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public abstract class GameAction implements Saveable {

    private final String actionTime;

    public GameAction(String time) {
        actionTime = time;
    }

    public final String getActionTime() {
        return actionTime;
    }

    public int getMinutes() {
        return TimeCalculator.timeStringToMinutes(actionTime);
    }

    public int getSeconds() {
        return TimeCalculator.timeStringToSeconds(actionTime);
    }
}
