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

import java.beans.PropertyChangeEvent;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud
 */
public class WelcomeScreen extends FXScreen {

    private final ScrollPane scrollPane;

    public WelcomeScreen() {
        super();
        scrollPane = new ScrollPane();
        Platform.runLater(() -> initScreen());
    }

    private void initScreen() {
        Rectangle dummyBackGround = new Rectangle(1000, 2000, Color.RED);
        scrollPane.setContent(dummyBackGround);
        //
//        InputStream stream;
//        stream = WelcomeScreen.class.getResourceAsStream("ListCss.css");
//        System.err.println(" CSS :: " + stream);
        //Login.class.getResource("Login.css").toExternalForm()
//        mainNode.getStylesheets().clear();
//        mainNode.setId("mylistview");
//        System.err.println(" STYLE :: " + scrollPane.getStyle());
//        scrollPane.getControlCssMetaData().stream().forEach(meta -> {
//            System.err.println(" CONTROL META ::" + meta);
//        });
//        scrollPane.getCssMetaData().stream().forEach(meta -> {
//            System.err.println(" META ::" + meta);
//        });
//        scrollPane.getStylesheets().stream().forEach(css -> {
//            System.err.println("CSS :" + css);
//        });
        scrollPane.getStylesheets().add(WelcomeScreen.class.getResource("ListCss.css").toExternalForm());
//        mainNode.setStyle("mylistview");
    }

    @Override
    public Node getNode() {
        return scrollPane;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (FXScreenUtils.STAGE_DIMENSION_CHANGED.equals(evt.getPropertyName())) {
            final double width = (double) evt.getOldValue();
            final double height = (double) evt.getNewValue();
            updateSize(width, height);
        }
    }

    @Override
    public void updateSize(final double width, final double height) {
        scrollPane.setPrefSize(width, height * FXScreenUtils.PAGES_VERTICAL_RATIO);
    }

}
