/*
 * Copyright (C) 2015 HAMON-KEROMEN A.
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

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Arnaud
 */
public class GameActionComparatorTest {

    public GameActionComparatorTest() {
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

    /**
     * Test of compare method, of class GameActionComparator.
     */
    @Test
    public void testCompare() {
        GameAction o1 = new ShotStop(null, null, null, null, null, "00:00");
        GameAction o2 = new ShotStop(null, null, null, null, null, "00:01");
        GameAction o3 = new ShotStop(null, null, null, null, null, "01:00");
        GameAction o4 = new ShotStop(null, null, null, null, null, "10:00");
        GameAction o4Bis = new ShotStop(null, null, null, null, null, "10:00");
        GameAction o5 = new ShotStop(null, null, null, null, null, "10:01");
        GameActionComparator instance = new GameActionComparator();
        int expResult;
        int result;
        expResult = -1;
        result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        result = instance.compare(o2, o3);
        assertEquals(expResult, result);
        result = instance.compare(o1, o3);
        assertEquals(expResult, result);
        result = instance.compare(o2, o4);
        assertEquals(expResult, result);
        result = instance.compare(o1, o4);
        assertEquals(expResult, result);
        expResult = 0;
        result = instance.compare(o4Bis, o4);
        assertEquals(expResult, result);
        expResult = 1;
        result = instance.compare(o5, o4);
        assertEquals(expResult, result);
        result = instance.compare(o5, o3);
        assertEquals(expResult, result);
        result = instance.compare(o5, o2);
        assertEquals(expResult, result);
        result = instance.compare(o5, o1);
        assertEquals(expResult, result);
    }

}
