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
package fr.noony.handstats;

import fr.noony.handstats.core.Game;
import fr.noony.handstats.core.Player;
import fr.noony.handstats.core.Team;
import java.time.LocalDate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class EquipeTest {

    public EquipeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testing() {
        Team section = createSection();
        Team ennemy = createEnnemy();
        Game match = new Game();
        match.setUpGame(section, ennemy, LocalDate.now());
    }

    private Team createSection() {
        Team section = new Team("Section Hand", Championship.M_18_NAT);
        Player j1 = new Player("alphonse", "A", 1, Poste.PIVOT, Poste.PIVOT);
        Player j2 = new Player("bob", "B", 2, Poste.PIVOT, Poste.PIVOT);
        Player j3 = new Player("charly", "C", 3, Poste.PIVOT, Poste.PIVOT);
        Player j4 = new Player("donald", "D", 4, Poste.PIVOT, Poste.PIVOT);
        Player j5 = new Player("edouard", "E", 5, Poste.PIVOT, Poste.PIVOT);
        Player j6 = new Player("francis", "F", 6, Poste.PIVOT, Poste.PIVOT);
        Player j7 = new Player("georges", "G", 7, Poste.PIVOT, Poste.PIVOT);
        Player j8 = new Player("hubert", "H", 8, Poste.PIVOT, Poste.PIVOT);
        Player j9 = new Player("isodore", "I", 9, Poste.PIVOT, Poste.PIVOT);
        Player j10 = new Player("jules", "J", 10, Poste.PIVOT, Poste.PIVOT);
        Player j11 = new Player("kilo", "K", 11, Poste.PIVOT, Poste.PIVOT);
        Player j12 = new Player("ludo", "L", 12, Poste.PIVOT, Poste.PIVOT);
        Player j13 = new Player("momo", "M", 13, Poste.PIVOT, Poste.PIVOT);
        Player j14 = new Player("nono", "N", 14, Poste.PIVOT, Poste.PIVOT);
        //
        section.addPlayer(j1, true);
        section.addPlayer(j2, true);
        section.addPlayer(j3, true);
        section.addPlayer(j4, true);
        section.addPlayer(j5, true);
        section.addPlayer(j6, true);
        section.addPlayer(j7, true);
        section.addPlayer(j8, true);
        section.addPlayer(j9, true);
        section.addPlayer(j10, true);
        section.addPlayer(j11, true);
        section.addPlayer(j12, true);
        section.addPlayer(j13, false);
        section.addPlayer(j14, false);
        //
        return section;
    }

    private Team createEnnemy() {
        Team section = new Team("Section Ennemie", Championship.M_18_NAT);
        Player j1 = new Player("albert", "A", 1, Poste.AILIER_DROIT, Poste.PIVOT);
        Player j2 = new Player("benoit", "B", 2, Poste.AILIER_DROIT, Poste.PIVOT);
        Player j3 = new Player("captain", "C", 3, Poste.AILIER_GAUCHE, Poste.PIVOT);
        Player j4 = new Player("didier", "D", 4, Poste.ARRIERE_DROIT, Poste.PIVOT);
        Player j5 = new Player("eliot", "E", 5, Poste.ARRIERE_GAUCHE, Poste.PIVOT);
        Player j6 = new Player("fabrice", "F", 6, Poste.ARRIERE_GAUCHE, Poste.PIVOT);
        Player j7 = new Player("guignol", "G", 7, Poste.DEMI_CENTRE, Poste.PIVOT);
        Player j8 = new Player("huges", "H", 8, Poste.GARDIEN, Poste.PIVOT);
        Player j9 = new Player("ivre", "I", 9, Poste.DEMI_CENTRE, Poste.PIVOT);
        Player j10 = new Player("julien", "J", 10, Poste.PIVOT, Poste.PIVOT);
        Player j11 = new Player("karate", "K", 11, Poste.PIVOT, Poste.PIVOT);
        Player j12 = new Player("luc", "L", 12, Poste.PIVOT, Poste.PIVOT);
        Player j13 = new Player("math", "M", 13, Poste.PIVOT, Poste.PIVOT);
        Player j14 = new Player("null", "N", 14, Poste.PIVOT, Poste.PIVOT);
        //
        section.addPlayer(j1, true);
        section.addPlayer(j2, true);
        section.addPlayer(j3, true);
        section.addPlayer(j4, true);
        section.addPlayer(j5, true);
        section.addPlayer(j6, true);
        section.addPlayer(j7, true);
        section.addPlayer(j8, true);
        section.addPlayer(j9, true);
        section.addPlayer(j10, true);
        section.addPlayer(j11, true);
        section.addPlayer(j12, true);
        section.addPlayer(j13, false);
        section.addPlayer(j14, false);
        //
        return section;
    }
}
