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

import java.awt.Dimension;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud
 */
public class SimpleListView extends SimpleComponent {

    private static final Dimension DEFAULT_DIMENSION = new Dimension(200, 400);
    private static final Color DEFAULT_BACKGROUND_FILL = new Color(0.7, 0.7, 0.7, 1);
    private static final Color DEFAULT_STROKE_FILL = Color.BLACK;

    private final Rectangle listViewBackground;
    private final ScrollBar scrollBar;
    //
    private double width;
    private double height;

    public SimpleListView(double x, double y, double listWidth, double listHeight) {
        super();
        super.setPosition(x, y);
        //
        width = listWidth;
        height = listHeight;
        //
        listViewBackground = new Rectangle();
        scrollBar = new ScrollBar();
        initListView();
    }

    public SimpleListView() {
        this(0, 0, DEFAULT_DIMENSION.getWidth(), DEFAULT_DIMENSION.getHeight());
    }

    private void initListView() {
        listViewBackground.setFill(DEFAULT_BACKGROUND_FILL);
        listViewBackground.setStroke(DEFAULT_STROKE_FILL);
        //
        addNode(listViewBackground
        );
        //
        updateSize();
    }

    @Override
    public void setSize(double newWidth, double newHeight) {
        width = newWidth;
        height = newHeight;
        updateSize();
    }

    private void updateSize() {
        listViewBackground.setWidth(width);
        listViewBackground.setHeight(height);
    }

}
