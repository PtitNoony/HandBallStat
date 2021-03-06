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
package fr.noony.handstats.core;

import fr.noony.handstats.utils.TimeCalculator;
import fr.noony.handstats.utils.log.MainLogger;
import java.beans.PropertyChangeSupport;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.pmw.tinylog.Level;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class GameClock {

    public enum ClockState {

        PLAYING, PAUSED
    }
    //
    public static final String CLOCK_STATE_CHANGED = "CLOCK_STATE_CHANGED";
    public static final String CLOCK_VALUE_CHANGED = "CLOCK_VALUE_CHANGED";
    public static final String CLOCK_START_FIRST_HALF = "clockStartFirstHalf";
    public static final String CLOCK_END_FIST_HALF = "clockEndFirstHalf";
    public static final String CLOCK_START_SECOND_HALF = "clockStartSecondHalf";
    public static final String CLOCK_END_SECOND_HALF = "clockEndSecondHalf";
    //
    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);
    //
    private ClockState clockState;
    private final Timeline timeline;
    private int nbMinutes;
    private int nbSeconds;
    private String sTime;

    public GameClock() {
        propertyChangeSupport = new PropertyChangeSupport(GameClock.this);
        lookupContents.add(propertyChangeSupport);
        //
        clockState = ClockState.PAUSED;
        nbMinutes = 0;
        nbSeconds = 0;
        //
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), this::nextSecond));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    protected void setTime(String time) {
        nbSeconds = TimeCalculator.timeStringToSeconds(time);
        nbMinutes = TimeCalculator.timeStringToMinutes(time);
        MainLogger.log(MainLogger.GAME_EVENT_LEVEL, "Setting time", nbMinutes, nbSeconds);
    }

    public Lookup getLookup() {
        return alookup;
    }

    public String getTime() {
        updateStringBuilder();
        return sTime;
    }

    private void nextSecond(ActionEvent e) {
        MainLogger.log(Level.INFO, "next second ::", e);
        if (nbSeconds == 59) {
            nbSeconds = 0;
            nbMinutes++;
        } else {
            nbSeconds++;
        }
        updateStringBuilder();
        firePropertyChange(CLOCK_VALUE_CHANGED, null, sTime);
        checkTime();
    }

    private void updateStringBuilder() {
        sTime = TimeCalculator.timeToString(nbMinutes, nbSeconds);
    }

    private void checkTime() {
        if (nbMinutes == 30 && nbSeconds == 0) {
            firePropertyChange(CLOCK_END_FIST_HALF, null, null);
            setPaused();
        } else if (nbMinutes == 60 && nbSeconds == 0) {
            firePropertyChange(CLOCK_END_SECOND_HALF, null, null);
            setPaused();
        }
    }

    public void startTime() {
        MainLogger.log(MainLogger.GAME_EVENT_LEVEL, "Starting time", nbMinutes, nbSeconds);
        if (nbMinutes == 0 && nbSeconds == 0) {
            // notifier le debut du match
            //enregistrer l'heure ???
            firePropertyChange(CLOCK_START_FIRST_HALF, null, clockState);
        } else if (nbMinutes == 30 && nbSeconds == 0) {
            firePropertyChange(CLOCK_START_SECOND_HALF, null, clockState);
        }
        setPlaying();
    }

    public void pauseTime() {
        MainLogger.log(MainLogger.GAME_EVENT_LEVEL, "Pausing time", nbMinutes, nbSeconds);
        setPaused();
    }

    private void setPlaying() {
        clockState = ClockState.PLAYING;
        firePropertyChange(CLOCK_STATE_CHANGED, null, clockState);
        timeline.play();
    }

    private void setPaused() {
        timeline.pause();
        clockState = ClockState.PAUSED;
        firePropertyChange(CLOCK_STATE_CHANGED, null, clockState);
    }

    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

}
