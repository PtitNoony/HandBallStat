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
package fr.noony.handstats.utils;

import fr.noony.handstats.core.Team;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.openide.util.Exceptions;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class EnvLoader {

    public static final String TEAM_RELATIVE_DIRECTORY = "\\Teams";
    //
    private static final String INIT_FILE_FULLNAME = "init.txt";
    private static final String SEPARATOR = " = ";
    private static final String VERSION_PPTY = "version";
    private static final String PREFERED_TEAM_PPTY = "preferedTeam";
    //
    private static final String DEBUG_FOLDER = "C:\\Handstat";
    //
    private String currentPath;
    //
    private String version;
    private Team preferedTeam;
    private final List<Team> teams;
    private final Map<String, String> properties;

    public EnvLoader() {
        version = "unknown";
        preferedTeam = null;
        teams = new LinkedList<>();
        properties = new HashMap<>();
    }

    public final void loadEnvironment() {
        currentPath = System.getProperty("user.dir");
//        System.err.println(" CURRENT PATH :: " + currentPath);
        loadInitFile();
        parseTeamFolder();
        analyzeProperties();
    }

    private void loadInitFile() {
        //test if folder exists
        Path debugFolder = Paths.get(currentPath);
//        System.err.println("Folder exists ::" + Files.exists(debugFolder));
        if (!Files.exists(debugFolder)) {
            throw new UnsupportedOperationException("INIT FOLDER IS NOT FOUND :" + currentPath);
        }
        //test if folder contains the configuration file
        String configFilePath = currentPath + "\\" + INIT_FILE_FULLNAME;
        Path configFile = Paths.get(configFilePath);
//        System.err.println("Config file exists ::" + Files.exists(configFile));
        if (!Files.exists(configFile)) {
            //TODO: remove after dev
//            throw new UnsupportedOperationException("CREATE CONFIGURATION FILE ");
            currentPath = DEBUG_FOLDER;
            configFilePath = currentPath + "\\" + INIT_FILE_FULLNAME;
            configFile = Paths.get(configFilePath);
        }
        // read config file content
        try (BufferedReader configFileReader = Files.newBufferedReader(configFile)) {
            // read config file content
            String line;
            String[] splitLine;
            while ((line = configFileReader.readLine()) != null) {
                System.err.println(" -> " + line);
                splitLine = line.split(SEPARATOR);
                assert splitLine.length == 2;
                properties.put(splitLine[0], splitLine[1]);
            }
            configFileReader.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    private void parseTeamFolder() {
        TeamFileProcessor fileProcessor = new TeamFileProcessor();
        try {
            Files.walkFileTree(Paths.get(currentPath + TEAM_RELATIVE_DIRECTORY), fileProcessor);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        fileProcessor.getTeams().values().stream().forEach(team -> teams.add(team));
    }

    private void analyzeProperties() {
        //set version
        if (properties.get(VERSION_PPTY) != null) {
            version = properties.get(VERSION_PPTY);
        }
        //set prefered team
        String prefTeamName = properties.get(PREFERED_TEAM_PPTY);
        if (prefTeamName != null) {
            //TODO : do it nicely
            for (Team t : teams) {
                if (t.getName().equals(prefTeamName)) {
                    preferedTeam = t;
                    break;
                }
            }
        }
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getPreferedTeam() {
        return preferedTeam;
    }

    public String getVersion() {
        return version;
    }

}
