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
package fr.noony.handstats.team.newhmi;

import fr.noony.handstats.team.newhmi.statviewer.StatViewController;
import fr.noony.handstats.utils.EnvLoader;
import fr.noony.handstats.utils.log.MainLogger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.pmw.tinylog.Level;

/**
 *
 * @author Arnaud
 */
public class HMIV2Launcher extends Application {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(HMIV2Launcher.this);

    private Scene myScene;
    private Stage myStage;
    private Group root;
    private Group mainContents;
    private Group screensGroup;

    private double sceneWidth;
    private double sceneHeight;

    private WelcomeScreen welcomeScreen = null;
    private StatViewController statScreen = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        myStage = primaryStage;
        Platform.runLater(() -> {
            createScene();
            listenSceneSizeChanges();

        });

        EnvLoader.loadEnvironment();
    }

    private void createScene() {
        root = new Group();
        sceneWidth = FXScreenUtils.DEFAULT_RESOLUTION.width;
        sceneHeight = FXScreenUtils.DEFAULT_RESOLUTION.height;
        myScene = new Scene(root, sceneWidth, sceneHeight);
        myScene.setFill(new Color(0.2, 0.2, 0.2, 1.0));
        //

        mainContents = new Group();
        root.getChildren().add(mainContents);
        mainContents.setTranslateY(FXScreenUtils.UPPER_BORDER_HEIGHT);
        //
        screensGroup = new Group();
        mainContents.getChildren().add(screensGroup);
        //
        //
        //TODO: factorize code
        //create header panel
        HeaderPanel headerPanel = HeaderPanel.getInstance();
        root.getChildren().add(headerPanel.getNode());
        propertyChangeSupport.addPropertyChangeListener(headerPanel);
        headerPanel.addPropertyChangeListener(event -> handleFullSceenChange(event));

        //create dock
        Dock dock = new Dock();
        mainContents.getChildren().add(dock.getNode());
        propertyChangeSupport.addPropertyChangeListener(dock);
        dock.addPropertyChangeListener(event -> handleSceenChange(event));
        //
        myStage.setScene(myScene);
        myStage.show();
        //
        Platform.runLater(() -> updateSize());
    }

    private void listenSceneSizeChanges() {
        myScene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            MainLogger.log(Level.INFO, "new width : {0} old {1} new {2}", new Object[]{observableValue, oldSceneWidth, newSceneWidth});
            sceneWidth = newSceneWidth.doubleValue();
            updateSize();
        });
        myScene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            MainLogger.log(Level.INFO, "new height : {0} old {1} new {2}", new Object[]{observableValue, oldSceneHeight, newSceneHeight});
            sceneHeight = newSceneHeight.doubleValue();
            updateSize();
        });
    }

    private void updateSize() {
        propertyChangeSupport.firePropertyChange(FXScreenUtils.STAGE_DIMENSION_CHANGED, sceneWidth, sceneHeight - FXScreenUtils.UPPER_BORDER_HEIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void handleFullSceenChange(PropertyChangeEvent event) {
        if (HeaderPanel.SWITCH_FULL_SCREEN_MODE.equals(event.getPropertyName())) {
            myStage.setFullScreen(!myStage.isFullScreen());
        }
    }

    private void handleSceenChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case FXScreenUtils.REQUEST_TEAM_SCREEN:
                HeaderPanel.getInstance().cleanControls();
                handleWelcomeScreenRequest();
                break;
            case FXScreenUtils.REQUEST_GAME_SCREEN:
                HeaderPanel.getInstance().cleanControls();
                handleGameScreenRequest();
                break;
            case FXScreenUtils.REQUEST_STATS_SCREEN:
                HeaderPanel.getInstance().cleanControls();
                handleStatsScreenRequest();
                break;
            case FXScreenUtils.REQUEST_PARAMETERS_SCREEN:
                HeaderPanel.getInstance().cleanControls();
                handleParametersScreenRequest();
                break;
            default:
                throw new UnsupportedOperationException("" + event);
        }
    }

    private void handleWelcomeScreenRequest() {
        if (welcomeScreen == null) {
            //create welcome screen
            welcomeScreen = new WelcomeScreen();
            propertyChangeSupport.addPropertyChangeListener(welcomeScreen);
        }
        clearScreens();
        screensGroup.getChildren().add(welcomeScreen.getNode());
        welcomeScreen.propertyChange(new PropertyChangeEvent(this, FXScreenUtils.STAGE_DIMENSION_CHANGED, sceneWidth, sceneHeight - FXScreenUtils.UPPER_BORDER_HEIGHT));
    }

    private void clearScreens() {
        List<Node> children = screensGroup.getChildren();
        screensGroup.getChildren().removeAll(children);
    }

    private void handleGameScreenRequest() {

        clearScreens();
    }

    private void handleStatsScreenRequest() {
        if (statScreen == null) {
            statScreen = new StatViewController();
            propertyChangeSupport.addPropertyChangeListener(statScreen);
        }
        clearScreens();
        statScreen.repopulateHeaderPanel();
        screensGroup.getChildren().add(statScreen.getNode());
        statScreen.propertyChange(new PropertyChangeEvent(this, FXScreenUtils.STAGE_DIMENSION_CHANGED, sceneWidth, sceneHeight - FXScreenUtils.UPPER_BORDER_HEIGHT));
    }

    private void handleParametersScreenRequest() {

        clearScreens();
    }

}
