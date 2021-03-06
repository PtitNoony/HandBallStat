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
package fr.noony.handstats.utils;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class TimeCalculator {

    public static final String timeToString(int nbMinutes, int nbSeconds) {
        StringBuilder sTime = new StringBuilder();
        if (nbMinutes < 10) {
            sTime.append("0");
        }
        sTime.append(nbMinutes);
        sTime.append(":");
        if (nbSeconds < 10) {
            sTime.append("0");
        }
        sTime.append(nbSeconds);
        return sTime.toString();
    }

    public static final int timeStringToSeconds(String time) {
        return Integer.parseInt(time.split(":")[1]);
    }

    public static final int timeStringToMinutes(String time) {
        return Integer.parseInt(time.split(":")[0]);
    }

}
