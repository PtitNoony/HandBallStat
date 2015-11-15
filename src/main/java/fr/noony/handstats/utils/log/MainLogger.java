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
package fr.noony.handstats.utils.log;

import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

/**
 *
 * @author Arnaud
 */
public class MainLogger {

    public static final Level GAME_EVENT_LEVEL = Level.INFO;

    public static void log(Level level, String message, Object... o) {
        //UGLY TEST
//        if (!level.equals(Level.OFF)) {
//            // do nothing
//        } else if (Level.INFO.equals(level)) {
//            Logger.info(message, o);
//        } else if (Level.WARNING.equals(level)) {
//            Logger.warn(message, o);
//        } else if (Level.SEVERE.equals(level)) {
//            Logger.error(message, o);
//        } else {
//            Logger.info(message, o);
//        }
        Logger.info(message, o);
    }

    public static void init() {

    }

}
