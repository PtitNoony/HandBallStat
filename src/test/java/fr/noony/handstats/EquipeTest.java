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
        //
        section.createPlayer("alphonse", "A", 1, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("bob", "B", 2, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("charly", "C", 3, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("donald", "D", 4, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("edouard", "E", 5, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("francis", "F", 6, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("georges", "G", 7, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("hubert", "H", 8, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("isodore", "I", 9, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("jules", "J", 10, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("kilo", "K", 11, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("ludo", "L", 12, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("momo", "M", 13, Poste.PIVOT, Poste.PIVOT, false);
        section.createPlayer("nono", "N", 14, Poste.PIVOT, Poste.PIVOT, false);
        //
        return section;
    }

    private Team createEnnemy() {
        Team section = new Team("Section Ennemie", Championship.M_18_NAT);
        //
        section.createPlayer("albert", "A", 1, Poste.AILIER_DROIT, Poste.PIVOT, true);
        section.createPlayer("benoit", "B", 2, Poste.AILIER_DROIT, Poste.PIVOT, true);
        section.createPlayer("captain", "C", 3, Poste.AILIER_GAUCHE, Poste.PIVOT, true);
        section.createPlayer("didier", "D", 4, Poste.ARRIERE_DROIT, Poste.PIVOT, true);
        section.createPlayer("eliot", "E", 5, Poste.ARRIERE_GAUCHE, Poste.PIVOT, true);
        section.createPlayer("fabrice", "F", 6, Poste.ARRIERE_GAUCHE, Poste.PIVOT, true);
        section.createPlayer("guignol", "G", 7, Poste.DEMI_CENTRE, Poste.PIVOT, true);
        section.createPlayer("huges", "H", 8, Poste.GARDIEN, Poste.PIVOT, true);
        section.createPlayer("ivre", "I", 9, Poste.DEMI_CENTRE, Poste.PIVOT, true);
        section.createPlayer("julien", "J", 10, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("karate", "K", 11, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("luc", "L", 12, Poste.PIVOT, Poste.PIVOT, true);
        section.createPlayer("math", "M", 13, Poste.PIVOT, Poste.PIVOT, false);
        section.createPlayer("null", "N", 14, Poste.PIVOT, Poste.PIVOT, false);
        //
        return section;
    }
}
