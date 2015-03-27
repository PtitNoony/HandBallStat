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
package fr.noony.handstats.utils;

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
public class CalculationsTest {

    public CalculationsTest() {
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
     * Test of convertToTwoMinuteString method, of class Calculations.
     */
    @Test
    public void testConvertToTwoMinuteString() {
        int nbSeconds = 0;
        String expResult = "0:00";
        String result = Calculations.convertToTwoMinuteString(nbSeconds);
        assertEquals(expResult, result);
        //
        nbSeconds = -1;
        expResult = "X:xx";
        result = Calculations.convertToTwoMinuteString(nbSeconds);
        assertEquals(expResult, result);
        //
        nbSeconds = 1;
        expResult = "0:01";
        result = Calculations.convertToTwoMinuteString(nbSeconds);
        assertEquals(expResult, result);
        //
        nbSeconds = 10;
        expResult = "0:10";
        result = Calculations.convertToTwoMinuteString(nbSeconds);
        assertEquals(expResult, result);
        //
        nbSeconds = 60;
        expResult = "1:00";
        result = Calculations.convertToTwoMinuteString(nbSeconds);
        assertEquals(expResult, result);
        //
        nbSeconds = 61;
        expResult = "1:01";
        result = Calculations.convertToTwoMinuteString(nbSeconds);
        assertEquals(expResult, result);
        //
        nbSeconds = 70;
        expResult = "1:10";
        result = Calculations.convertToTwoMinuteString(nbSeconds);
        assertEquals(expResult, result);
        //
        nbSeconds = 120;
        expResult = "2:00";
        result = Calculations.convertToTwoMinuteString(nbSeconds);
        assertEquals(expResult, result);
        //
        nbSeconds = 121;
        expResult = "X:xx";
        result = Calculations.convertToTwoMinuteString(nbSeconds);
        assertEquals(expResult, result);
    }

}
