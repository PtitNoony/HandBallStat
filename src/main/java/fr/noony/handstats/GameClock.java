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
package fr.noony.handstats;

import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

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

    private static final Logger LOG = Logger.getLogger(GameClock.class.getName());
    //
    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);
    //
    private ClockState clockState;
    private final Timeline timeline;
    private int nbMinutes;
    private int nbSeconds;
    private StringBuilder sTime;

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

    public Lookup getLookup() {
        return alookup;
    }

    public String getTime() {
        updateStringBuilder();
        return sTime.toString();
    }

    private void nextSecond(ActionEvent e) {
        LOG.log(Level.FINE, "next second :: {0}", e);
        if (nbSeconds == 59) {
            nbSeconds = 0;
            nbMinutes++;
        } else {
            nbSeconds++;
        }
        updateStringBuilder();
        firePropertyChange(CLOCK_VALUE_CHANGED, null, sTime.toString());
        checkTime();
    }

    private void updateStringBuilder() {
        sTime = new StringBuilder();
        if (nbMinutes < 10) {
            sTime.append("0");
        }
        sTime.append(nbMinutes);
        sTime.append(":");
        if (nbSeconds < 10) {
            sTime.append("0");
        }
        sTime.append(nbSeconds);
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
