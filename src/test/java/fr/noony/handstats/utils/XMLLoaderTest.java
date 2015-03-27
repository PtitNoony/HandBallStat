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

import fr.noony.handstats.team.Team;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Utilities;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class XMLLoaderTest {

    public XMLLoaderTest() {
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
    public void testLoadTeamNull() throws URISyntaxException {
        Team result = XMLLoader.loadTeam(null);
        Assert.assertNull(result);
    }

    /**
     * Test of loadTeam method, of class XMLLoader.
     *
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testLoadTeam() throws URISyntaxException {
        URL aUrl = XMLLoader.class.getResource("EquipeFranceMasculine.xml");
        String s = aUrl.getFile();
        File f = Utilities.toFile(aUrl.toURI());
        Path path = Paths.get(f.getPath());
        Team result = XMLLoader.loadTeam(path);
        Assert.assertNotNull(result);
    }

    @Test
    public void testLoadTeam2() throws URISyntaxException {
        URL aUrl = XMLLoader.class.getResource("EquipeFranceFeminine.xml");
        String s = aUrl.getFile();
        File f = Utilities.toFile(aUrl.toURI());
        Path path = Paths.get(f.getPath());
        Team result = XMLLoader.loadTeam(path);
        Assert.assertNotNull(result);
    }

}
