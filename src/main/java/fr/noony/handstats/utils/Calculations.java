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
package fr.noony.handstats.utils;

/**
 *
 * @author Arnaud HAMON
 */
public final class Calculations {

    private Calculations() {
        //private constructor
    }

    public static String convertToTwoMinuteString(int nbSeconds) {
        String result;
        if (nbSeconds > 120) {
            result = "X:xx";
        } else if (nbSeconds == 120) {
            result = "2:00";
        } else if (nbSeconds >= 60) {
            int sec = nbSeconds - 60;
            if (sec < 10) {
                result = "1:0" + sec;
            } else {
                result = "1:" + sec;
            }
        } else if (nbSeconds >= 0) {
            if (nbSeconds < 10) {
                result = "0:0" + nbSeconds;
            } else {
                result = "0:" + nbSeconds;
            }
        } else {
            result = "X:xx";
        }
        return result;
    }

}
