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
import fr.noony.handstats.team.Team;
import fr.noony.handstats.utils.XMLSaver;
import java.beans.PropertyChangeEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Code partly from
 * https://blogs.oracle.com/acaicedo/entry/managing_multiple_screens_in_javafx1
 *
 * @author Arnaud Hamon-Keromen
 */
public class ScreensController extends StackPane {

    private Team currentTeam;

    private final Map<String, Screen> screens = new LinkedHashMap<>();

    public boolean setScreen(final String name, Object... parameters) {
        Screen nextScreen = screens.get(name);
        if (nextScreen != null) {
            //screen loaded
            nextScreen.loadParameters(parameters);
            final DoubleProperty opacity = opacityProperty();

            //Is there is more than one screen
            if (!getChildren().isEmpty()) {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(300), (ActionEvent t) -> {
                            Logger.getLogger(ScreensController.class.getName()).log(Level.OFF, "new key frame on event {0}", t);
                            //remove displayed screen
                            getChildren().remove(0);
                            //add new screen
                            getChildren().add(0, nextScreen.getNode());
                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(300), new KeyValue(opacity, 1.0)));
                            fadeIn.play();
                        }, new KeyValue(opacity, 0.0)));
                fade.play();
            } else {
                //no one else been displayed, then just show
                setOpacity(0.0);
                getChildren().add(nextScreen.getNode());
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(300), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            Logger.getLogger(ScreensController.class.getName()).log(Level.WARNING, "screen hasn't been loaded!\n");
            return false;
        }
    }

    public void addScreen(Screen screen) {
        screens.put(screen.getName(), screen);
        screen.setScreenParent(this);
    }

    protected void processEvent(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Events.ACTION_CREATE_TEAM:
                setScreen(Main.TEAM_CREATION_PAGE);
                break;
            case Events.CANCEL_TEAM_CREATION_EVENT:
                setScreen(Main.INIT_PAGE);
                break;
            case Events.CREATE_TEAM_EVENT:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Main.TEAM_EDITION_PAGE, currentTeam);
                break;
            case Events.ACTION_CHANGE_TEAM:
                setScreen(Main.TEAM_SELECTION_PAGE, evt.getNewValue());
                break;
            case Events.NEW_TEAM_SELECTED_EVENT:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Main.TEAM_MAIN_PAGE, currentTeam);
                break;
            case Events.CANCEL_TEAM_SELECTED_EVENT:
                setScreen(Main.INIT_PAGE);
                break;
            case Events.BACK_TEAM_SELECTION:
                setScreen(Main.TEAM_SELECTION_PAGE);
                break;
            case Events.EDIT_CURRENT_TEAM:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Main.TEAM_EDITION_PAGE, currentTeam);
                break;
            case Events.BACK_TO_TEAM_MAIN:
                currentTeam = (Team) evt.getNewValue();
                setScreen(Main.TEAM_MAIN_PAGE, currentTeam);
                break;
            case Events.CONFIGURE_MATCH:
                setScreen(Main.MATCH_CONFIGURATOR_PAGE, currentTeam);
                break;
            case Events.START_GAME:
                setScreen(Main.MATCH_SCOREBOARD_PAGE, (Game) evt.getNewValue());
                break;
        }
    }

    public void saveTeam() {
        if (currentTeam != null) {
            XMLSaver.saveTeam(currentTeam);
        }

    }

}
