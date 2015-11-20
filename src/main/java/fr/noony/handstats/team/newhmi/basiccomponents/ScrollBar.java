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
package fr.noony.handstats.team.newhmi.basiccomponents;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud
 */
public class ScrollBar extends SimpleComponent {

    private final Rectangle scrollBarBackground;

    private double barPencentage = 1.0;
    private double barStartPercentage = 0.0;

    public ScrollBar(double x, double y, double scrollBarWidth, double scrollbarHeight) {
        super();
        super.setPosition(x, y);
        //
        scrollBarBackground = new Rectangle();
        //
        initScrollBar();
    }

    public ScrollBar() {
        this(0, 0, 0, 0);
    }

    private void initScrollBar() {

        //
        addNode(scrollBarBackground);
    }

    @Override
    public void setSize(double newWidth, double newHeight) {

    }

}
