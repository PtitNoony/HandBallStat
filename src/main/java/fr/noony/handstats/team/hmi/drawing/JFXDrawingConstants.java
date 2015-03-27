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
package fr.noony.handstats.team.hmi.drawing;

import javafx.scene.paint.Color;

/**
 *
 * @author Arnaud
 */
public class JFXDrawingConstants {

    private JFXDrawingConstants() {
    }
    private static final double TO_DOUBLE = 255.0;
    public static final double DEFAULT_ALPHA_VALUE = 1.0;

    private static double toJFXDouble(int d) {
        return ((double) d) / TO_DOUBLE;
    }

    private static Color toJFXColor(java.awt.Color awtColor) {
        return new Color(toJFXDouble(awtColor.getRed()), toJFXDouble(awtColor.getGreen()), toJFXDouble(awtColor.getBlue()), DEFAULT_ALPHA_VALUE);
    }
    //CDS COLOR PALET
    public static final Color CDS_AMBER = toJFXColor(DrawingConstants.CDS_AMBER);
    public static final Color CDS_BLACK = toJFXColor(DrawingConstants.CDS_BLACK);
    public static final Color CDS_CYAN = toJFXColor(DrawingConstants.CDS_CYAN);
    public static final Color CDS_LIGHT_BLUE = toJFXColor(DrawingConstants.CDS_LIGHT_BLUE);
    public static final Color CDS_HYPERLINK = toJFXColor(DrawingConstants.CDS_HYPERLINK);
    public static final Color CDS_MAGENTA = toJFXColor(DrawingConstants.CDS_MAGENTA);
    public static final Color CDS_RED = toJFXColor(DrawingConstants.CDS_RED);
    public static final Color CDS_YELLOW = toJFXColor(DrawingConstants.CDS_YELLOW);
    public static final Color CDS_LIGHT_GREEN = toJFXColor(DrawingConstants.CDS_LIGHT_GREEN);
    public static final Color CDS_WHITE = toJFXColor(DrawingConstants.CDS_WHITE);
    public static final Color CDS_VERY_LIGHT_GREY = toJFXColor(DrawingConstants.CDS_VERY_LIGHT_GREY);
    public static final Color CDS_LIGHT_GREY = toJFXColor(DrawingConstants.CDS_LIGHT_GREY);
    public static final Color CDS_GREY = toJFXColor(DrawingConstants.CDS_GREY);
    public static final Color CDS_DARK_GREY = toJFXColor(DrawingConstants.CDS_DARK_GREY);
    public static final Color CDS_VERY_DARK_GREY = toJFXColor(DrawingConstants.CDS_VERY_DARK_GREY);
    //iFMS COLOR PALET
    public static final Color IFMS_WHITE = toJFXColor(DrawingConstants.IFMS_WHITE);
    public static final Color IMFS_LIGHT_GREY = toJFXColor(DrawingConstants.IMFS_LIGHT_GREY);
    public static final Color IMFS_LIGHT_GREY2 = toJFXColor(DrawingConstants.IMFS_LIGHT_GREY2);
    public static final Color IMFS_GREY = toJFXColor(DrawingConstants.IMFS_GREY);
    public static final Color IMFS_LIGHT_FADED_GREY = toJFXColor(DrawingConstants.IMFS_LIGHT_FADED_GREY);
    public static final Color IMFS_FADED_GREY = toJFXColor(DrawingConstants.IMFS_FADED_GREY);
    public static final Color IMFS_YELLOW = toJFXColor(DrawingConstants.IMFS_YELLOW);
    public static final Color IMFS_YELLOW2 = toJFXColor(DrawingConstants.IMFS_YELLOW2);
    public static final Color IMFS_BLUE = toJFXColor(DrawingConstants.IMFS_BLUE);
    public static final Color IMFS_GREEN = toJFXColor(DrawingConstants.IMFS_GREEN);
    public static final Color IMFS_FADED_GREEN = toJFXColor(DrawingConstants.IMFS_FADED_GREEN);
    public static final Color IMFS_BROWN = toJFXColor(DrawingConstants.IMFS_BROWN);
    public static final Color IMFS_BLACK = toJFXColor(DrawingConstants.IMFS_BLACK);
    public static final Color IMFS_BACKGROUND_GREY1 = toJFXColor(DrawingConstants.IMFS_BACKGROUND_GREY1);
    public static final Color IMFS_BACKGROUND_GREY2 = toJFXColor(DrawingConstants.IMFS_BACKGROUND_GREY2);
    public static final Color IMFS_BACKGROUND_GREY3 = toJFXColor(DrawingConstants.IMFS_BACKGROUND_GREY3);
}
