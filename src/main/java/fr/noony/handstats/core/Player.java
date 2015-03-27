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
    //
    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);

    private final String premon;
    private final String nom;
    private int numero;
    //TODO: is it the rigth way ??
    private int twoMinSecondsRemaining = 0;

    private Poste positionPreferee;
    private Poste positionActuelle;

    public Player(String premon, String nom, int numero, Poste positionPreferee, Poste positionActuelle) {
        propertyChangeSupport = new PropertyChangeSupport(Player.this);
        lookupContents.add(propertyChangeSupport);
        //
        this.premon = premon;
        this.nom = nom;
        this.numero = numero;
        this.positionPreferee = positionPreferee;
        this.positionActuelle = positionActuelle;
    }

    public Player(String premon, String nom, int numero, Poste positionPreferee) {
        this(premon, nom, numero, positionPreferee, positionPreferee);
    }

    public Lookup getLookup() {
        return alookup;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getLastName() {
        return nom;
    }

    public int getNumero() {
        return numero;
    }

    public Poste getPositionPreferee() {
        return positionPreferee;
    }

    public void setPositionPreferee(Poste positionPreferee) {
        this.positionPreferee = positionPreferee;
    }

    public String getFirstName() {
        return premon;
    }

    public void setPositionActuelle(Poste positionActuelle) {
        this.positionActuelle = positionActuelle;
    }

    public Poste getPositionActuelle() {
        return positionActuelle;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nom);
        sb.append(" ");
        sb.append(premon);
        sb.append(" n°");
        sb.append(numero);
        sb.append(" ");
        sb.append(positionPreferee);
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

}
