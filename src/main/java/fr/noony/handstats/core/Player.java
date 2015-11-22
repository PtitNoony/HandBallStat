/*
 * Copyright (C) 2014 Arnaud
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

import fr.noony.handstats.Poste;
import fr.noony.handstats.team.hmi.Events;
import java.beans.PropertyChangeSupport;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class Player {

    public static final String END_TWO_MINUTES = "endOfTwoMinutes";
    public static final String TWO_MINUTES_TIME_REMAINING = "twoMinutesTimeRemaining";
    public static final String IS_ACTIVE_GOAL_KEEPER = "isActiveGoalKeeper";
    //
    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);

    private final String firstName;
    private final String lastName;
    private int number;
    //TODO: is it the rigth way ??
    private int twoMinSecondsRemaining = 0;

    private Poste preferedPosition;
    private Poste currentPosition;

    private boolean isInPlace = false;

    private final long uniqueID;

    protected Player(String premon, String nom, int numero, Poste positionPreferee, Poste positionActuelle, long id) {
        propertyChangeSupport = new PropertyChangeSupport(Player.this);
        lookupContents.add(propertyChangeSupport);
        //
        this.firstName = premon;
        this.lastName = nom;
        this.number = numero;
        this.preferedPosition = positionPreferee;
        this.currentPosition = positionActuelle;
        uniqueID = id;
    }

    protected Player(String premon, String nom, int numero, Poste positionPreferee, long id) {
        this(premon, nom, numero, positionPreferee, positionPreferee, id);
    }

    public Lookup getLookup() {
        return alookup;
    }

    public void setNumero(int numero) {
        this.number = numero;
    }

    public String getLastName() {
        return lastName;
    }

    public long getUniqueID() {
        return uniqueID;
    }

    public int getNumber() {
        return number;
    }

    public Poste getPositionPreferee() {
        return preferedPosition;
    }

    public void setPositionPreferee(Poste positionPreferee) {
        this.preferedPosition = positionPreferee;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setPositionActuelle(Poste positionActuelle) {
        this.currentPosition = positionActuelle;
    }

    public Poste getPositionActuelle() {
        return currentPosition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lastName);
        sb.append(" ");
        sb.append(firstName);
        sb.append(" n°");
        sb.append(number);
        sb.append(" ");
        sb.append(preferedPosition);
        return sb.toString();
    }

    public void receiveYellowCard() {
        //TODO save
        firePropertyChange(Events.YELLOW_CARD, null, null);
    }

    public void receive2Minutes() {
        //TODO save
        firePropertyChange(Events.TWO_MINUTES, null, null);
        //TODO: constant
        twoMinSecondsRemaining = 120;
    }

    public void reduceTwoMinuteRemaning() {
        //TODO set protected or use ppty change events ....
        twoMinSecondsRemaining--;
        firePropertyChange(TWO_MINUTES_TIME_REMAINING, null, twoMinSecondsRemaining);
        if (twoMinSecondsRemaining == 0) {
            firePropertyChange(END_TWO_MINUTES, null, this);
        } else if (twoMinSecondsRemaining < 0) {
            throw new IllegalStateException("time remaining for 2 min cannot be <0");
        }
    }

    public void receiverRedCard() {
        //TODO save
        firePropertyChange(Events.RED_CARD, null, null);
    }

    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void setCurrentGoalKeeper(boolean isInGoal) {
        //MOCHE
        isInPlace = isInGoal;
        firePropertyChange(IS_ACTIVE_GOAL_KEEPER, null, isInPlace);
    }

    public boolean isCurrentGoalKeeper() {
        return isInPlace;
    }

}
