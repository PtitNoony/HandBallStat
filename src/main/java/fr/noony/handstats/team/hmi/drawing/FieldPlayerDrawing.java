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
import static fr.noony.handstats.team.hmi.drawing.PlayerDrawing.STANDARD_PLAYER_HEIGHT;
import static fr.noony.handstats.team.hmi.drawing.PlayerDrawing.STANDARD_PLAYER_WIDTH;
import javafx.scene.paint.Color;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class FieldPlayerDrawing extends PlayerDrawing {

    public FieldPlayerDrawing(Player player) {
        super(player);
        setSize(STANDARD_PLAYER_WIDTH, STANDARD_PLAYER_HEIGHT);
        setColors(Color.GAINSBORO, Color.DARKGREY);
        setOrientation(Orientation.HORIZONTAL);
    }

}
