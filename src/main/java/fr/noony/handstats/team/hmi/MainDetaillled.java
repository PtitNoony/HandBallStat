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

import static fr.noony.handstats.team.hmi.Screens.LOADING_PAGE;
import fr.noony.handstats.utils.EnvLoader;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
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
public class MainDetaillled extends Application {

    public static final Dimension DEFAULT_RESOLUTION = new Dimension(800, 600);
    public static final Logger LOG = Logger.getLogger(MainDetaillled.class.getName());

    private static final String WELCOME_MESSAGE = "WELCOME TO HANDSTAT v";

    private Scene myScene;
    private Stage myStage;

    private MainScreensController screensController;
    //
    private Screen loadingPage;

    private double sceneWidth = DEFAULT_RESOLUTION.width;
    private double sceneHeight = DEFAULT_RESOLUTION.height;
    private final List<Screen> applicationScreens = new ArrayList<>();

    private ExitPopUp exitPopUp;

    private Timer loadingTimer;

    /**
     *
     * @param stage primary stage
     */
    @Override
    public void start(Stage stage) {
        myStage = stage;
        loadingTimer = new Timer(500, (ActionEvent e) -> {
            loadingTimer.stop();
            EnvLoader.loadEnvironment();
            Platform.runLater(() -> createPages());
        });
        Platform.runLater(() -> {
            screensController = new MainScreensController();
            myScene = new Scene(screensController, DEFAULT_RESOLUTION.width, DEFAULT_RESOLUTION.height);
            myStage.setScene(myScene);
            myStage.show();
            loadingPage = new Screen(LOADING_PAGE);
            screensController.addScreen(loadingPage);
            screensController.setScreen(LOADING_PAGE, 0.0);
            //
            exitPopUp = new ExitPopUp(stage, screensController);
            //
            myStage.setFullScreen(true);
            EnvLoader.setCurrentResolution((int) myStage.getMaxWidth(), (int) myStage.getMaxHeight());
            myStage.setOnCloseRequest((WindowEvent event) -> {
                Logger.getLogger(MainDetaillled.class.getName()).log(Level.SEVERE, "Closing application window on event :: {0}", new Object[]{event});
                //TODO:: make a pop up to confirm and ask for save
//                screensController.saveTeam();
                event.consume();
                exitPopUp.show(stage);
            });
            Platform.runLater(() -> loadingTimer.start());
        });
    }

    private void displayInitPage() {
        myStage.setFullScreenExitHint(WELCOME_MESSAGE + EnvLoader.getVersion());
//        screensController.setScreen(Screens.INIT_PAGE, EnvLoader.getTeams(), EnvLoader.getPreferedTeam());
        screensController.processEvent(new PropertyChangeEvent(this, Screens.INIT_PAGE, EnvLoader.getTeams(), EnvLoader.getPreferedTeam()));
        myScene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            Logger.getLogger(MainDetaillled.class.getName()).log(Level.INFO, "new width : {0} old {1} new {2}", new Object[]{observableValue, oldSceneWidth, newSceneWidth});
            sceneWidth = newSceneWidth.doubleValue();
            updatePagesSize();
        });
        myScene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            Logger.getLogger(MainDetaillled.class.getName()).log(Level.INFO, "new height : {0} old {1} new {2}", new Object[]{observableValue, oldSceneHeight, newSceneHeight});
            sceneHeight = newSceneHeight.doubleValue();
            updatePagesSize();
        });

    }

    private void createPages() {
        double index = 0;
        double nbPages = Screens.getScreens().size();
        for (String screenName : Screens.getScreens()) {
            final Screen screen = new Screen(screenName);
            screensController.addScreen(screen);
            applicationScreens.add(screen);
            index = index + 1.0;
            loadingPage.loadParameters(index / nbPages);
        }
        screensController.setWindow(myStage);
        displayInitPage();
    }

    private void updatePagesSize() {
        applicationScreens.stream().forEach(screen -> screen.updateSize(sceneWidth, sceneHeight));
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
        LOG.setLevel(Level.SEVERE);
        launch(args);
    }

}
