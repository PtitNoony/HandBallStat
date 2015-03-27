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
package fr.noony.handstats.team.hmi;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.Timer;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class Main extends Application {

    public static final Dimension DEFAULT_RESOLUTION = new Dimension(800, 600);

    public static final String INIT_PAGE = "InitPage";
    public static final String TEAM_CREATION_PAGE = "TeamCreatorPanel";
    public static final String TEAM_EDITION_PAGE = "TeamEditorPanel";
    public static final String PLAYER_EDITION_PAGE = "PlayerEditorPanel";
    public static final String TEAM_MAIN_PAGE = "TeamMainPanel";
    public static final String TEAM_SELECTION_PAGE = "TeamSelectionPanel";
    public static final String MATCH_SCOREBOARD_PAGE = "MatchScoreBoard";
    public static final String MATCH_CONFIGURATOR_PAGE = "MatchConfigurator";

    private Scene myScene;
    private Stage myStage;

    private ScreensController screensController;
    private Screen welcomePage;
    private Screen teamCreationPage;
    private Screen teamMainPage;
    private Screen teamEditionPage;
    private Screen playerEditionPage;
    private Screen teamSelectionPage;
    private Screen matchScoreboardPage;
    private Screen matchConfiguratorPage;

    private double sceneWidth = DEFAULT_RESOLUTION.width;
    private double sceneHeight = DEFAULT_RESOLUTION.height;

    private Timer exitTimer;

    /**
     *
     * @param stage primary stage
     */
    @Override
    public void start(Stage stage) {
        myStage = stage;
        exitTimer = new Timer(100, (ActionEvent e) -> {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Timer stopping the application window on event :: {0}", new Object[]{e});
            Platform.exit();
            //mandatory since using swing timer ???
            //System.exit(0);
        });
        Platform.runLater(() -> {
            screensController = new ScreensController();
            myScene = new Scene(screensController, DEFAULT_RESOLUTION.width, DEFAULT_RESOLUTION.height);
            myStage.setScene(myScene);
            initFrame(false);
            myStage.show();
            myStage.setFullScreen(true);
            myStage.setFullScreenExitHint("");
            myStage.setOnCloseRequest((WindowEvent event) -> {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Closing application window on event :: {0}", new Object[]{event});
                //TODO:: make a pop up to confirm and ask for save
                screensController.saveTeam();
                exitTimer.start();
            });
        });
    }

    private void initFrame(boolean fullScreen) {
        myStage.setFullScreenExitHint("WELCOME TO HANDSTAT V0.1 Alpha");
        myStage.setFullScreen(fullScreen);
        createPages();
        screensController.setScreen(welcomePage.getName());
        myScene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "new width : {0} old {1} new {2}", new Object[]{observableValue, oldSceneWidth, newSceneWidth});
            sceneWidth = newSceneWidth.doubleValue();
            updatePagesSize();
        });
        myScene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "new height : {0} old {1} new {2}", new Object[]{observableValue, oldSceneHeight, newSceneHeight});
            sceneHeight = newSceneHeight.doubleValue();
            updatePagesSize();
        });
    }

    private void createPages() {
        welcomePage = new Screen(INIT_PAGE);
        teamCreationPage = new Screen(TEAM_CREATION_PAGE);
        teamEditionPage = new Screen(TEAM_EDITION_PAGE);
        playerEditionPage = new Screen(PLAYER_EDITION_PAGE);
        teamMainPage = new Screen(TEAM_MAIN_PAGE);
        teamSelectionPage = new Screen(TEAM_SELECTION_PAGE);
        matchScoreboardPage = new Screen(MATCH_SCOREBOARD_PAGE);
        matchConfiguratorPage = new Screen(MATCH_CONFIGURATOR_PAGE);
        //
        screensController.addScreen(welcomePage);
        screensController.addScreen(teamCreationPage);
        screensController.addScreen(teamMainPage);
        screensController.addScreen(teamEditionPage);
        screensController.addScreen(playerEditionPage);
        screensController.addScreen(teamSelectionPage);
        screensController.addScreen(matchScoreboardPage);
        screensController.addScreen(matchConfiguratorPage);
    }

    private void updatePagesSize() {
        welcomePage.updateSize(sceneWidth, sceneHeight);
        teamCreationPage.updateSize(sceneWidth, sceneHeight);
        teamEditionPage.updateSize(sceneWidth, sceneHeight);
        playerEditionPage.updateSize(sceneWidth, sceneHeight);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
