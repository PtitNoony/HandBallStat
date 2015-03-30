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
package fr.noony.handstats.team.hmi.stats;

import javafx.scene.paint.Color;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class ColorFactory {

    private ColorFactory() {
        //private constructor
    }

    public static final Color getColorForValue(Color baseColor, double value) {
//        Color.hsb(color.getHue(), DEFAULT_SATURATION + DEFAULT_COMPLEMENTARY_SATURATION * values[0], DEFAULT_BRIGHTNESS + DEFAULT_COMPLEMENTARY_BRIGHTNESS * values[0])
        if (value < 0) {
            return Color.GREY;
        }
        double redLevel = baseColor.getRed() + (1 - baseColor.getRed()) * (1.0 - value);
        double greenLevel = baseColor.getGreen() + (1 - baseColor.getGreen()) * (1.0 - value);
        double blueLevel = baseColor.getBlue() + (1 - baseColor.getBlue()) * (1.0 - value);
        return new Color(Math.min(1.0, redLevel), Math.min(1.0, greenLevel), Math.min(1.0, blueLevel), 1.0);
    }

}
