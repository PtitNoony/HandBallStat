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
package fr.noony.handstats.team.hmi.drawing;

import fr.noony.handstats.core.Team;
import fr.noony.handstats.utils.XMLLoader;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class TeamDrawingTest {

    public TeamDrawingTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        JFXPanel fXPanel = new JFXPanel();
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
     * Test of getNode method, of class TeamDrawing.
     */
    @Test
    public void testGetNode() {
        TeamDrawing instance = new TeamDrawing(TeamDrawing.TeamSide.LEFT);
        Node result = instance.getNode();
        Assert.assertNotNull(result);
    }

    /**
     * Test of setPosition method, of class TeamDrawing.
     */
    @Test
    public void testSetPosition() {
        double x = 0.0;
        double y = 0.0;
        TeamDrawing instance = new TeamDrawing(TeamDrawing.TeamSide.LEFT);
        instance.setPosition(x, y);
    }

    /**
     * Test of setSize method, of class TeamDrawing.
     */
    @Test
    public void testSetSize() {
        double newWidth = 0.0;
        double newHeight = 0.0;
        TeamDrawing instance = new TeamDrawing(TeamDrawing.TeamSide.LEFT);
        instance.setSize(newWidth, newHeight);
    }

    /**
     * Test of setTeam method, of class TeamDrawing.
     */
    @Test
    public void testSetTeam() {
        Team currentTeam = XMLLoader.getMenTeam();
        TeamDrawing instance = new TeamDrawing(TeamDrawing.TeamSide.LEFT);
        instance.setTeam(currentTeam);
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }
        TeamDrawing instance2 = new TeamDrawing(TeamDrawing.TeamSide.RIGHT);
        instance2.setTeam(currentTeam);
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }
    }

}
