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
package fr.noony.handstats.team.newhmi.statviewer;

import fr.noony.handstats.core.GameHalf;
import fr.noony.handstats.stats.GameStat;
import fr.noony.handstats.team.hmi.drawing.HalfTimeLine;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author Arnaud
 */
public class GameTimeLineGroup {

    private final Group mainNode;
    private final HalfTimeLine firstHalfTimeline;
    private final HalfTimeLine secondHalfTimeline;

    public GameTimeLineGroup() {
        mainNode = new Group();
        firstHalfTimeline = new HalfTimeLine(GameHalf.FIRST_HALF);
        secondHalfTimeline = new HalfTimeLine(GameHalf.SECOND_HALF);
        //
        initGameTimeLineGroup();
    }

    private void initGameTimeLineGroup() {

    }

    public Node getMainNode() {
        return mainNode;
    }

    public void setGameStat(GameStat stat) {
        firstHalfTimeline.initStat(stat);
        secondHalfTimeline.initStat(stat);
    }

    protected void setPosition(double x, double y) {
        mainNode.setTranslateX(x);
        mainNode.setTranslateY(y);
    }

    protected void setSize(double newWidth, double newHeight) {

    }

    protected void setVisible(boolean visibility) {
        mainNode.setVisible(visibility);
    }

}
