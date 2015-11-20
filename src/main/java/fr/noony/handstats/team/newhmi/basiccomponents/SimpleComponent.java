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

import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author Arnaud
 */
public abstract class SimpleComponent {

    private final Group mainNode;

    public SimpleComponent() {
        mainNode = new Group();
    }

    public final Node getNode() {
        return mainNode;
    }

    protected final void addNode(Node node) {
        mainNode.getChildren().add(node);
    }

    public final void setPosition(double x, double y) {
        //within a platform.runlater ?
        mainNode.setTranslateX(x);
        mainNode.setTranslateY(y);
    }

    public abstract void setSize(double newWidth, double newHeight);

}
