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
package fr.noony.handstats.team.hmi;

import fr.noony.handstats.Game;
import fr.noony.handstats.core.Team;
import fr.noony.handstats.utils.XMLSaver;
import java.beans.PropertyChangeEvent;

/**
 * Code partly from
 * https://blogs.oracle.com/acaicedo/entry/managing_multiple_screens_in_javafx1
 *
 * @author Arnaud Hamon-Keromen
 */
public class MainScreensController extends AbstractScreensController {

    private Team currentTeam;

    @Override
    public void addScreen(Screen screen) {
        super.addScreen(screen);
        screen.setScreenParent(this);
    }

    @Override
    protected void processEvent(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Screens.INIT_PAGE:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Screens.INIT_PAGE, evt.getOldValue(), evt.getNewValue());
                break;
            case Events.ACTION_CREATE_TEAM:
                setScreen(Screens.TEAM_CREATION_PAGE);
                break;
            case Events.CANCEL_TEAM_CREATION_EVENT:
                setScreen(Screens.INIT_PAGE);
                break;
            case Events.CREATE_TEAM_EVENT:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Screens.TEAM_EDITION_PAGE, currentTeam);
                break;
            case Events.ACTION_CHANGE_TEAM:
                setScreen(Screens.TEAM_SELECTION_PAGE, evt.getNewValue());
                break;
            case Events.NEW_TEAM_SELECTED_EVENT:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Screens.TEAM_MAIN_PAGE, currentTeam);
                break;
            case Events.CANCEL_TEAM_SELECTED_EVENT:
                setScreen(Screens.INIT_PAGE);
                break;
            case Events.BACK_TEAM_SELECTION:
                setScreen(Screens.TEAM_SELECTION_PAGE);
                break;
            case Events.EDIT_CURRENT_TEAM:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Screens.TEAM_EDITION_PAGE, currentTeam);
                break;
            case Events.BACK_TO_TEAM_MAIN:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Screens.TEAM_MAIN_PAGE, currentTeam);
                break;
            case Events.CONFIGURE_MATCH:
                setScreen(Screens.MATCH_CONFIGURATOR_PAGE, currentTeam);
                break;
            case Events.START_GAME:
                setScreen(Screens.MATCH_SCOREBOARD_PAGE, (Game) evt.getNewValue());
                break;
            case Events.REUME_GAME:
                setScreen(Screens.MATCH_SCOREBOARD_PAGE, (Game) evt.getNewValue());
                break;
            case Events.CANCEL_MATCH_CONFIGURATION:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Screens.TEAM_MAIN_PAGE, currentTeam);
                break;
            case Events.DISPLAY_STATS_FROM_MAIN:
                setScreen(Screens.STATS_PAGE, currentTeam);
                break;
            case Events.LOAD_GAME_TO_RESUME:
                setScreen(Screens.MATCH_SCOREBOARD_PAGE, (Game) evt.getNewValue());
                break;
            case Events.EDIT_CURRENT_GAME:
                currentTeam = (Team) evt.getOldValue();
                setScreen(Screens.MATCH_CONFIGURATOR_PAGE, currentTeam, evt.getNewValue());
                break;
            default:
                throw new UnsupportedOperationException("unhandled property change event :" + evt.getPropertyName());
        }
    }

    public boolean saveTeam() {
        System.err.println(" TRYNING TO SAVE TEAM :: " + currentTeam);
        if (currentTeam != null) {
            return XMLSaver.saveTeam(currentTeam);
        }
        return false;
    }

}
