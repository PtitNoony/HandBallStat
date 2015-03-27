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
package fr.noony.handstats.areas;

/**
 *
 * @author Arnaud
 */
public class CourtArea extends AbstractArea {

    public static final CourtArea LEFT_ZONE = new CourtArea("LEFT_ZONE", Side.LEFT);
    public static final CourtArea LEFT_SHOOT_EXT_LEFT = new CourtArea("LEFT_SHOOT_EXT_LEFT", Side.LEFT);
    public static final CourtArea LEFT_SHOOT_INT_LEFT = new CourtArea("LEFT_SHOOT_INT_LEFT", Side.LEFT);
    public static final CourtArea LEFT_SHOOT_CENTER = new CourtArea("LEFT_SHOOT_CENTER", Side.LEFT);
    public static final CourtArea LEFT_SHOOT_EXT_RIGHT = new CourtArea("LEFT_SHOOT_EXT_RIGHT", Side.LEFT);
    public static final CourtArea LEFT_SHOOT_INT_RIGHT = new CourtArea("LEFT_SHOOT_INT_RIGHT", Side.LEFT);
    public static final CourtArea LEFT_FAR_LEFT = new CourtArea("LEFT_FAR_LEFT", Side.LEFT);
    public static final CourtArea LEFT_FAR_CENTER = new CourtArea("LEFT_FAR_CENTER", Side.LEFT);
    public static final CourtArea LEFT_FAR_RIGHT = new CourtArea("LEFT_FAR_RIGHT", Side.LEFT);
    public static final CourtArea RIGHT_ZONE = new CourtArea("RIGHT_ZONE", Side.RIGHT);
    public static final CourtArea RIGHT_SHOOT_EXT_LEFT = new CourtArea("RIGHT_SHOOT_EXT_LEFT", Side.RIGHT);
    public static final CourtArea RIGHT_SHOOT_INT_LEFT = new CourtArea("RIGHT_SHOOT_INT_LEFT", Side.RIGHT);
    public static final CourtArea RIGHT_SHOOT_CENTER = new CourtArea("RIGHT_SHOOT_CENTER", Side.RIGHT);
    public static final CourtArea RIGHT_SHOOT_EXT_RIGHT = new CourtArea("RIGHT_SHOOT_EXT_RIGHT", Side.RIGHT);
    public static final CourtArea RIGHT_SHOOT_INT_RIGHT = new CourtArea("RIGHT_SHOOT_INT_RIGHT", Side.RIGHT);
    public static final CourtArea RIGHT_FAR_LEFT = new CourtArea("RIGHT_FAR_LEFT", Side.RIGHT);
    public static final CourtArea RIGHT_FAR_CENTER = new CourtArea("RIGHT_FAR_CENTER", Side.RIGHT);
    public static final CourtArea RIGHT_FAR_RIGHT = new CourtArea("RIGHT_FAR_RIGHT", Side.RIGHT);

    public CourtArea(String areaName, Side areaSide) {
        super(areaName, areaSide);
    }

}
