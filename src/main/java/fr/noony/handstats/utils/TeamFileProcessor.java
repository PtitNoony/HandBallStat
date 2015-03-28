/*
 * Copyright 2015 Arnaud.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.noony.handstats.utils;

import fr.noony.handstats.core.Team;
import static fr.noony.handstats.team.hmi.InitPageController.TEAM_FILE_EXTENSION;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arnaud Hamon-Keromen
 *
 * Based on code from: http://www.javapractices.com/topic/TopicAction.do?Id=68
 *
 */
public class TeamFileProcessor extends SimpleFileVisitor<Path> {

    private final Map<String, Team> teams = new HashMap<>();
    private int nbTeamFiles = 0;

    @Override
    public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {
        if (aFile.getFileName().toString().endsWith(TEAM_FILE_EXTENSION)) {
            analyzeTeamFile(aFile);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    private void analyzeTeamFile(Path aFile) {
        // TODO: analyser les conflits et les doublons
        nbTeamFiles++;
        System.err.println("Analysing team :: " + aFile);
        //use static private variables ???
        Team team = XMLLoader.loadTeam(aFile);
        System.err.println("loaded team :: " + team);
        teams.put(team.getName(), team);
    }

    public Map<String, Team> getTeams() {
        return teams;
    }

    public int getNbTeamFiles() {
        return nbTeamFiles;
    }

}
