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
package fr.noony.handstats.team.hmi;

import fr.noony.handstats.team.hmi.drawing.TeamDrawing;
import fr.noony.handstats.utils.XMLLoader;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openide.util.Exceptions;

/**
 *
 * @author Arnaud
 */
public class ScoreBoardLauncher extends Application {

    private FXMLLoader loader;
    private FXController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //MatchScoreBoard
        loader = new FXMLLoader(getClass().getResource("MatchScoreBoard.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        Parent mainNode = loader.getRoot();
        controller = (FXController) loader.getController();
        Scene myScene = new Scene(mainNode, TeamDrawing.DEFAULT_TEAM_GROUP_SIZE.width, TeamDrawing.DEFAULT_TEAM_GROUP_SIZE.height);
        primaryStage.setScene(myScene);
        primaryStage.setWidth(TeamDrawing.DEFAULT_TEAM_GROUP_SIZE.width);
        primaryStage.setHeight(TeamDrawing.DEFAULT_TEAM_GROUP_SIZE.height);
        primaryStage.show();
        //
        controller.loadParameters(XMLLoader.getMenTeam(), XMLLoader.getWomenTeam());
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
