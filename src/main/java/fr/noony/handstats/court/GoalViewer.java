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
package fr.noony.handstats.court;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class GoalViewer extends Application {

    /**
     *
     * @param stage primary stage
     */
    @Override
    public void start(Stage stage) {
        Platform.runLater(() -> {
            Group root = new Group();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            GoalCageDrawing terrain = new GoalCageDrawing();
            terrain.setPosition(50.0, 50.0);
            root.getChildren().add(terrain.getNode());
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();
        });
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
