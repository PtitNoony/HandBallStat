package fr.noony.handstats.team.hmi;

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
import fr.noony.handstats.team.Team;
import fr.noony.handstats.utils.TeamFileProcessor;
import fr.noony.handstats.utils.XMLLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.openide.util.Exceptions;

/**
 * FXML Controller class
 *
 * @author Arnaud Hamon-Keromen
 */
public class InitPageController extends FXController {

    public static final String TEAM_FILE_EXTENSION = ".xml";
    public static final String TEAM_RELATIVE_DIRECTORY = "\\Teams";

    private static final String CONFIG_PREFERED_TEAM = "preferedTeam";

    private String currentPath;
    public static final String INIT_FILE_FULLNAME = "init.txt";
    public static final String DEBUG_FOLDER = "C:\\Handstat";

    private Team selectedteam;
    private final List<Team> teamsInFolder = new LinkedList<>();

    private final Map<String, String> configuration = new LinkedHashMap<>();

    @FXML
    private Pane mainNode;
    @FXML
    private Label selectedTeamLabel;
    @FXML
    private Button createTeamButton;
    @FXML
    private Button changeTeamButton;
    @FXML
    private Button manageTeamButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        currentPath = System.getProperty("user.dir");
//        System.err.println("CURRENT PATH ::");
//        System.err.println(currentPath);
        loadInitFile();
        parseTeamFolder();
        initPage();

    }

    private void loadInitFile() {
//        try {
////            boolean fileExists = Files.exists(Path);
//            //File initFile = new File(currentPath + INIT_FILE);
//            List<String> records = new ArrayList<String>();
//
//            BufferedReader reader = new BufferedReader(new FileReader(currentPath + INIT_FILE));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                records.add(line);
//            }
//            reader.close();
//            for (String s : records) {
//                System.err.println("-> " + s);
//            }
//
//        } catch (Exception e) {
//            System.err.println(e);
//        }
        //get current directory
        //for release version

        //
        System.err.println("!!!DEBUG VERSION!!!");
        //test if folder C/handstat exists
        Path debugFolder = Paths.get(DEBUG_FOLDER);
        System.err.println("Folder exists ::" + Files.exists(debugFolder));
        if (!Files.exists(debugFolder)) {
            throw new UnsupportedOperationException("CREATE DEFAULT FOLDER");
        }
        //test if folder contains the configuration file
        String configFilePath = DEBUG_FOLDER + "\\" + INIT_FILE_FULLNAME;
        Path configFile = Paths.get(configFilePath);
        System.err.println("Config file exists ::" + Files.exists(configFile));
        if (!Files.exists(configFile)) {
            throw new UnsupportedOperationException("CREATE CONF FILE ");
        }
        // read config file content
        try (BufferedReader configFileReader = Files.newBufferedReader(configFile)) {
            // read config file content
            String line;
            while ((line = configFileReader.readLine()) != null) {
                System.err.println(" -> " + line);
            }
            System.err.println("TODO : ajouter les pptes");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    private void parseTeamFolder() {
        //get prefered team
        String selectedTeamName = configuration.get(CONFIG_PREFERED_TEAM);
        if (selectedTeamName != null) {
            System.err.println("A TEAM IS SET IN CONFIG FILE :: " + selectedTeamName);
            String selectedTeamPath = DEBUG_FOLDER + TEAM_RELATIVE_DIRECTORY + "\\" + selectedTeamName + TEAM_FILE_EXTENSION;
            Path path = Paths.get(selectedTeamPath);
            selectedteam = XMLLoader.loadTeam(path);
        } else {
            System.err.println("NO TEAM SET IN CONFIG FILE");
            selectedteam = null;
        }
        //
        System.err.println("TODO : parse team folder to find other teams");
        //attention à ne pas mettre en doublon l'equipe selectionnee
        TeamFileProcessor fileProcessor = new TeamFileProcessor();
        try {
            Files.walkFileTree(Paths.get(DEBUG_FOLDER + TEAM_RELATIVE_DIRECTORY), fileProcessor);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        for (Team team : fileProcessor.getTeams().values()) {
            teamsInFolder.add(team);
        }
    }

    private void initPage() {
        createTeamButton.setDisable(false);
        if (selectedteam != null) {
            selectedTeamLabel.setText(selectedteam.getName());
            manageTeamButton.setDisable(false);
        } else {
            selectedTeamLabel.setText("");
            manageTeamButton.setDisable(true);
        }
        //
        changeTeamButton.setDisable(teamsInFolder.isEmpty());

    }

    @FXML
    public void newTeamAction(ActionEvent event) {
        firePropertyChange(Events.ACTION_CREATE_TEAM, null, event);
    }

    @FXML
    public void changeTeamAction(ActionEvent event) {
        firePropertyChange(Events.ACTION_CHANGE_TEAM, null, teamsInFolder);
    }

    @Override
    public void updateSize(double width, double height) {
        //TODO
    }

    @Override
    public void loadParameters(Object... params) {
        //TODO
    }

}
