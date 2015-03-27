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

import fr.noony.handstats.core.Player;
import javafx.scene.paint.Color;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class GoalKeeperDrawing extends PlayerDrawing {

    public static final String GOAL_SAVE_ACTION_EVENT = "goalSaveActionEvent";

    public GoalKeeperDrawing(Player player, String eventName) {
        super(player, eventName);
        setSize(STANDARD_PLAYER_HEIGHT, STANDARD_PLAYER_WIDTH);
        setColors(Color.STEELBLUE, Color.STEELBLUE);
        setOrientation(Orientation.VERTICAL);
    }

    public GoalKeeperDrawing(Player player) {
        this(player, PLAYER_ZONE_CLICKED);
    }

}
