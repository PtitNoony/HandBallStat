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
package fr.noony.handstats.team.hmi.stats;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public enum TerrainAreas {

    ZONE_AREA("_ZONE", 0),
    CLOSE_AREA_1("_CLOSE_1", 1),
    CLOSE_AREA_2("_CLOSE_2", 2),
    CLOSE_AREA_3("_CLOSE_3", 3),
    CLOSE_AREA_4("_CLOSE_4", 4),
    CLOSE_AREA_5("_CLOSE_5", 5),
    FAR_AREA_1("_COURT_1", 6),
    FAR_AREA_2("_COURT_2", 7),
    FAR_AREA_3("_COURT_3", 8),
    PENALTY_AREA("_PENALTY", 9);

    private final String name;
    private final int id;

    TerrainAreas(String n, int i) {
        name = n;
        id = i;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
