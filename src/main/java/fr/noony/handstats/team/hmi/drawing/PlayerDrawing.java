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

import fr.noony.handstats.core.Player;
import fr.noony.handstats.court.InteractiveShootingArea;
import fr.noony.handstats.team.hmi.Events;
import fr.noony.handstats.utils.Calculations;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Arnaud Hamon-Keromen
 */
public class PlayerDrawing implements PropertyChangeListener {

    public enum Orientation {

        VERTICAL, HORIZONTAL
    }

    public static final String PLAYER_ZONE_CLICKED = "PLAYER_ZONE_CLICKED";

    public static final double NB_MAX_FIELD_PLAYERS = 6;

    public static final double STANDARD_PLAYER_HEIGHT = (TeamDrawing.DEFAULT_TEAM_GROUP_SIZE.getHeight()
            - (1 + NB_MAX_FIELD_PLAYERS) * TeamDrawing.DEFAULT_ELEMENT_SEPARATION)
            / NB_MAX_FIELD_PLAYERS;

    public static final double STANDARD_PLAYER_WIDTH = (TeamDrawing.DEFAULT_TEAM_GROUP_SIZE.getWidth()
            - 4 * TeamDrawing.DEFAULT_ELEMENT_SEPARATION
            - STANDARD_PLAYER_HEIGHT)
            / 2.0;
    //
    private static final double INNER_BORDER_WIDTH = 4;
    private static final double NAME_LABEL_HEIGHT = STANDARD_PLAYER_HEIGHT / 3.0;
    private static final double IMAGE_SIZE_HORIZONTAL = STANDARD_PLAYER_HEIGHT - NAME_LABEL_HEIGHT;
    private static final double IMAGE_SIZE_VERTICAL = STANDARD_PLAYER_HEIGHT - 2.0 * INNER_BORDER_WIDTH;
    private static final double CARD_HEIGHT = (STANDARD_PLAYER_HEIGHT - 4.0 * INNER_BORDER_WIDTH) / 3.0;
    //OR ? ;)
    private static final double CARD_WIDTH = CARD_HEIGHT / 1.62;
    //
    private final Player myPlayer;
    private Orientation myOrientation = Orientation.HORIZONTAL;
    //
    private final Group mainNode;
    private Rectangle background;
    private Rectangle foreground;
    private ImageView playerImageView;
    private Label numberLabel;
    private Label nameLabel;
    private Rectangle yellowCard1;
    private Rectangle yellowCard2;
    private ImageView twoMinImageView1;
    private ImageView twoMinImageView2;
    private ImageView twoMinImageView3;
    private Label twoMinutesTimeLabel;
    //
    private final Color idleStrokeColor = Color.BLACK;
    private final Color overStrokeColor = Color.WHITESMOKE;
    private static final Color SELECTED_BACKGROUND_COLOR = Color.FUCHSIA;
    private final Image playerIcon = new Image(PlayerDrawing.class.getResourceAsStream("alien.png"));
    private final Image playerIconInversed = new Image(PlayerDrawing.class.getResourceAsStream("alienInversed.png"));
    private final Image playerIconGreen = new Image(PlayerDrawing.class.getResourceAsStream("alienGreen.png"));
    private final Image playerIconRed = new Image(PlayerDrawing.class.getResourceAsStream("alienRed.png"));
    private final Image timeIcon = new Image(PlayerDrawing.class.getResourceAsStream("time.png"));
    private final Image timeIconInversed = new Image(PlayerDrawing.class.getResourceAsStream("timeInversed.png"));
    private final Image timeIconRed = new Image(PlayerDrawing.class.getResourceAsStream("timeRed.png"));
    //time icon :: https://www.iconfinder.com/icons/175183/time_icon#size=128
    private Color idleBackgroundColor = Color.PINK;
    private Color overBackgroundColor = Color.PINK;
    //
    private final PropertyChangeSupport propertyChangeSupport;
    private final InstanceContent lookupContents = new InstanceContent();
    private final AbstractLookup alookup = new AbstractLookup(lookupContents);
    private static final Logger LOG = Logger.getLogger(InteractiveShootingArea.class.getName());
    private static final Level LOG_LEVEL = Level.FINEST;
    private SelectedState selectedState;
    private String eventNameOnAction;
    private int nbYellowCards = 0;
    private int nb2Mins = 0;
    private boolean redCardReceived = false;

    private boolean isActive = false;

    public enum SelectedState {

        IDLE, SELECTED, DISABLED, RED_OUT
    }

    public PlayerDrawing(Player player, String eventName) {
        propertyChangeSupport = new PropertyChangeSupport(PlayerDrawing.this);
        lookupContents.add(propertyChangeSupport);
        eventNameOnAction = eventName;
        player.getLookup().lookup(PropertyChangeSupport.class).addPropertyChangeListener(PlayerDrawing.this);
        //
        myPlayer = player;
        selectedState = SelectedState.IDLE;
        //
        mainNode = new Group();
        initDrawing();
    }

    public PlayerDrawing(Player player) {
        this(player, PLAYER_ZONE_CLICKED);
    }

    private void initDrawing() {
        background = new Rectangle();
        //TODO::make it in a method
        background.setStroke(idleStrokeColor);
        foreground = new Rectangle();
        foreground.setOpacity(0.0);
        initInteractivity();
        //
        playerImageView = new ImageView(playerIcon);
        playerImageView.setFitWidth(IMAGE_SIZE_HORIZONTAL);
        playerImageView.setFitHeight(IMAGE_SIZE_HORIZONTAL);
        //
        numberLabel = new Label("" + myPlayer.getNumber());
        numberLabel.setFont(new Font(32));
        numberLabel.setAlignment(Pos.CENTER);
        //
        String name = myPlayer.getLastName();
        if (!"".equals(myPlayer.getFirstName())) {
            name += " " + myPlayer.getFirstName().charAt(0) + ".";
        }
        nameLabel = new Label(name);
        nameLabel.setFont(new Font(15));
        nameLabel.setAlignment(Pos.CENTER);
        //
        createCards();
        create2Minutes();
        //
        mainNode.getChildren().add(background);
        mainNode.getChildren().add(playerImageView);
        mainNode.getChildren().add(numberLabel);
        mainNode.getChildren().add(nameLabel);
        mainNode.getChildren().add(yellowCard1);
        mainNode.getChildren().add(yellowCard2);
        mainNode.getChildren().add(twoMinImageView1);
        mainNode.getChildren().add(twoMinImageView2);
        mainNode.getChildren().add(twoMinImageView3);
        mainNode.getChildren().add(twoMinutesTimeLabel);
        mainNode.getChildren().add(foreground);
    }

    private void createCards() {
        double xCard1 = IMAGE_SIZE_HORIZONTAL + STANDARD_PLAYER_WIDTH / 2.0 + INNER_BORDER_WIDTH;
        yellowCard1 = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        yellowCard1.setStroke(Color.BLACK);
        yellowCard1.setStrokeWidth(2.0);
        yellowCard1.setFill(Color.YELLOW);
        yellowCard1.setX(xCard1);
        yellowCard1.setY(INNER_BORDER_WIDTH);
        yellowCard1.setVisible(false);
        //
        yellowCard2 = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        yellowCard2.setStroke(Color.BLACK);
        yellowCard2.setStrokeWidth(2.0);
        yellowCard2.setFill(Color.YELLOW);
        yellowCard2.setX(xCard1 + INNER_BORDER_WIDTH + CARD_WIDTH);
        yellowCard2.setY(INNER_BORDER_WIDTH);
        yellowCard2.setVisible(false);
    }

    private void create2Minutes() {
        double xCard1 = IMAGE_SIZE_HORIZONTAL + STANDARD_PLAYER_WIDTH / 2.0 + INNER_BORDER_WIDTH;
        twoMinImageView1 = new ImageView(timeIcon);
        twoMinImageView1.setFitWidth(CARD_HEIGHT);
        twoMinImageView1.setFitHeight(CARD_HEIGHT);
        twoMinImageView1.setX(xCard1);
        twoMinImageView1.setY(2.0 * INNER_BORDER_WIDTH + CARD_HEIGHT);
        twoMinImageView1.setVisible(false);
        //
        twoMinImageView2 = new ImageView(timeIcon);
        twoMinImageView2.setFitWidth(CARD_HEIGHT);
        twoMinImageView2.setFitHeight(CARD_HEIGHT);
        twoMinImageView2.setX(xCard1 + INNER_BORDER_WIDTH + CARD_WIDTH);
        twoMinImageView2.setY(2.0 * INNER_BORDER_WIDTH + CARD_HEIGHT);
        twoMinImageView2.setVisible(false);
        //
        twoMinImageView3 = new ImageView(timeIcon);
        twoMinImageView3.setFitWidth(CARD_HEIGHT);
        twoMinImageView3.setFitHeight(CARD_HEIGHT);
        twoMinImageView3.setX(xCard1 + 2.0 * (INNER_BORDER_WIDTH + CARD_WIDTH));
        twoMinImageView3.setY(2.0 * INNER_BORDER_WIDTH + CARD_HEIGHT);
        twoMinImageView3.setVisible(false);
        //
        twoMinutesTimeLabel = new Label("0:00");
        twoMinutesTimeLabel.setMinWidth(STANDARD_PLAYER_WIDTH / 2.0 - IMAGE_SIZE_HORIZONTAL);
        twoMinutesTimeLabel.setMaxWidth(STANDARD_PLAYER_WIDTH / 2.0 - IMAGE_SIZE_HORIZONTAL);
        twoMinutesTimeLabel.setMinHeight(CARD_HEIGHT);
        twoMinutesTimeLabel.setMaxHeight(CARD_HEIGHT);
        twoMinutesTimeLabel.setTranslateX(xCard1);
        twoMinutesTimeLabel.setTranslateY(3.0 * INNER_BORDER_WIDTH + 2.0 * CARD_HEIGHT);
        twoMinutesTimeLabel.setVisible(false);
    }

    public void setSelectedState(SelectedState state) {
        switch (selectedState) {
            case IDLE:
            case SELECTED:
                selectedState = state;
                updateDrawing();
                break;
            case RED_OUT:
                //denied
                break;
            case DISABLED:
                //TODO: refaire methode updatedrawing
                background.setFill(Color.DARKGREY);
                break;
            default:
                //nothing
                break;
        }
    }

    private void setActive(boolean active) {
        isActive = active;

    }

    public Lookup getLookup() {
        return alookup;
    }

    private void initInteractivity() {
        foreground.setOnMouseClicked(this::processMouseClick);
        foreground.setOnMouseEntered(this::processMouseEntered);
        foreground.setOnMouseExited(this::processMouseExited);
    }

    private void processMouseClick(MouseEvent event) {
        LOG.log(LOG_LEVEL, "Event {0} on player {1}", new Object[]{event, this});
        switch (selectedState) {
            case IDLE:
            case SELECTED:
                propertyChangeSupport.firePropertyChange(eventNameOnAction, this, myPlayer);
                break;
            case RED_OUT:
                //nothing
                break;
            default:
                //nothing
                break;
        }
    }

    private void processMouseEntered(MouseEvent event) {
        LOG.log(LOG_LEVEL, "Event {0} on player {1}", new Object[]{event, this});
        switch (selectedState) {
            case IDLE:
            case SELECTED:
                background.setFill(overBackgroundColor);
                background.setStroke(overStrokeColor);
                nameLabel.setTextFill(overStrokeColor);
                numberLabel.setTextFill(overStrokeColor);
                playerImageView.setImage(playerIconInversed);
                yellowCard1.setFill(Color.LIGHTGREY);
                yellowCard1.setStroke(Color.WHITESMOKE);
                yellowCard2.setFill(Color.LIGHTGREY);
                yellowCard2.setStroke(Color.WHITESMOKE);
                twoMinImageView1.setImage(timeIconInversed);
                twoMinImageView2.setImage(timeIconInversed);
                twoMinImageView3.setImage(timeIconInversed);
                break;
            case RED_OUT:
                //nothing
                break;
            default:
                //nothing
                break;
        }
    }

    private void processMouseExited(MouseEvent event) {
        LOG.log(LOG_LEVEL, "Event {0} on player {1}", new Object[]{event, this});
        switch (selectedState) {
            case IDLE:
            case SELECTED:
                background.setStroke(idleStrokeColor);
                nameLabel.setTextFill(idleStrokeColor);
                numberLabel.setTextFill(idleStrokeColor);
                playerImageView.setImage(playerIcon);
                playerImageView.setImage(playerIconInversed);
                yellowCard1.setFill(Color.YELLOW);
                yellowCard1.setStroke(Color.BLACK);
                yellowCard2.setFill(Color.YELLOW);
                yellowCard2.setStroke(Color.BLACK);
                twoMinImageView1.setImage(timeIcon);
                twoMinImageView2.setImage(timeIcon);
                twoMinImageView3.setImage(timeIcon);
                updateColors();
                break;
            case RED_OUT:
                //nothing
                break;
            default:
                //nothing
                break;
        }
    }

    private void updateDrawing() {
        //TODO: double switch case with selection state
        switch (myOrientation) {
            case HORIZONTAL:
                drawHorizontal();
                break;
            case VERTICAL:
                drawVertical();
                break;
            default:
                //nothing
                break;
        }
        updateColors();
    }

    private void drawHorizontal() {
        playerImageView.setFitWidth(IMAGE_SIZE_HORIZONTAL);
        playerImageView.setFitHeight(IMAGE_SIZE_HORIZONTAL);
        //
        numberLabel.setMinSize(STANDARD_PLAYER_WIDTH / 2.0, IMAGE_SIZE_HORIZONTAL);
        numberLabel.setMaxSize(STANDARD_PLAYER_WIDTH / 2.0, IMAGE_SIZE_HORIZONTAL);
        numberLabel.setTranslateX(IMAGE_SIZE_HORIZONTAL);
        //
        nameLabel.setMinSize(STANDARD_PLAYER_WIDTH / 2.0 + IMAGE_SIZE_HORIZONTAL, NAME_LABEL_HEIGHT);
        nameLabel.setMaxSize(STANDARD_PLAYER_WIDTH / 2.0 + IMAGE_SIZE_HORIZONTAL, NAME_LABEL_HEIGHT);
        nameLabel.setTranslateY(IMAGE_SIZE_HORIZONTAL);
    }

    private void drawVertical() {
        playerImageView.setFitWidth(IMAGE_SIZE_VERTICAL);
        playerImageView.setFitHeight(IMAGE_SIZE_VERTICAL);
        playerImageView.setTranslateX(INNER_BORDER_WIDTH);
        playerImageView.setTranslateY(INNER_BORDER_WIDTH);
        //
        numberLabel.setMinSize(IMAGE_SIZE_VERTICAL, IMAGE_SIZE_VERTICAL);
        numberLabel.setMaxSize(IMAGE_SIZE_VERTICAL, IMAGE_SIZE_VERTICAL);
        numberLabel.setTranslateX(INNER_BORDER_WIDTH);
        numberLabel.setTranslateY(INNER_BORDER_WIDTH * 2.0 + IMAGE_SIZE_VERTICAL);
        //
        nameLabel.setMinSize(STANDARD_PLAYER_HEIGHT - 2.0 * INNER_BORDER_WIDTH, 3.0 * INNER_BORDER_WIDTH - 2.0 * IMAGE_SIZE_VERTICAL);
        nameLabel.setMaxSize(STANDARD_PLAYER_HEIGHT - 2.0 * INNER_BORDER_WIDTH, 3.0 * INNER_BORDER_WIDTH - 2.0 * IMAGE_SIZE_VERTICAL);
        nameLabel.setTranslateX(INNER_BORDER_WIDTH);
        nameLabel.setTranslateY(INNER_BORDER_WIDTH * 3.0 + 2.0 * IMAGE_SIZE_VERTICAL);
        nameLabel.setFont(new Font(12));
        nameLabel.setWrapText(true);
        //
        double xCard1 = IMAGE_SIZE_HORIZONTAL + STANDARD_PLAYER_WIDTH / 2.0 + INNER_BORDER_WIDTH;
        yellowCard1.setY(xCard1);
        yellowCard1.setX(INNER_BORDER_WIDTH);
        //
        yellowCard2.setY(xCard1 + INNER_BORDER_WIDTH + CARD_WIDTH);
        yellowCard2.setX(INNER_BORDER_WIDTH);
        //
        double xCard2 = IMAGE_SIZE_HORIZONTAL + STANDARD_PLAYER_WIDTH / 2.0 + INNER_BORDER_WIDTH;
        twoMinImageView1.setY(xCard2);
        twoMinImageView1.setX(2.0 * INNER_BORDER_WIDTH + CARD_HEIGHT);
        //
        twoMinImageView2.setY(xCard2 + INNER_BORDER_WIDTH + CARD_WIDTH);
        twoMinImageView2.setX(2.0 * INNER_BORDER_WIDTH + CARD_HEIGHT);
        //
        twoMinImageView3.setY(xCard2 + 2.0 * (INNER_BORDER_WIDTH + CARD_WIDTH));
        twoMinImageView3.setX(2.0 * INNER_BORDER_WIDTH + CARD_HEIGHT);
        //
        twoMinutesTimeLabel.setTranslateY(xCard2 + 3.0 * (INNER_BORDER_WIDTH + CARD_WIDTH));
        twoMinutesTimeLabel.setTranslateX(INNER_BORDER_WIDTH);
    }

    private void updateColors() {
        switch (selectedState) {
            case IDLE:
                background.setFill(idleBackgroundColor);
                if (isActive) {
                    playerImageView.setImage(playerIconGreen);
                } else {
                    playerImageView.setImage(playerIcon);
                }
                break;
            case SELECTED:
                background.setFill(SELECTED_BACKGROUND_COLOR);
                playerImageView.setImage(playerIconInversed);
                break;
            default:
                //nothing
                break;
        }
    }

    public Node getNode() {
        return mainNode;
    }

    public Player getPlayer() {
        return myPlayer;
    }

    public final void setSize(double newWidth, double newHeight) {
        background.setWidth(newWidth);
        background.setHeight(newHeight);
        foreground.setWidth(newWidth);
        foreground.setHeight(newHeight);
        updateDrawing();
    }

    public final void setPosition(double newX, double newY) {
        mainNode.setTranslateX(newX);
        mainNode.setTranslateY(newY);
        updateDrawing();
    }

    public final void setColors(Color idleColor, Color overColor) {
        idleBackgroundColor = idleColor;
        overBackgroundColor = overColor;
        updateColors();
    }

    public final void setOrientation(Orientation orientation) {
        myOrientation = orientation;
        updateDrawing();
    }

    public final void setVisible(boolean visibility) {
        mainNode.setVisible(visibility);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Events.YELLOW_CARD:
                addYellowCard();
                break;
            case Events.TWO_MINUTES:
                triggerTwoMinutes();
                break;
            case Events.RED_CARD:
                setOutForGame();
                break;
            case Player.TWO_MINUTES_TIME_REMAINING:
                processTwoMinuteTimeRemaining((int) evt.getNewValue());
                break;
            case Player.END_TWO_MINUTES:
                twoMinutesTimeLabel.setVisible(false);
                if (!redCardReceived) {
                    //TODO: inside set state
                    //trop moche
                    selectedState = SelectedState.IDLE;
                    updateColors();
                }
                break;
            case Player.IS_ACTIVE_GOAL_KEEPER:
                isActive = (boolean) evt.getNewValue();
                updateColors();
                break;
            default:
                throw new UnsupportedOperationException("unsupported property change ::" + evt.getPropertyName());
        }
    }

    private void processTwoMinuteTimeRemaining(int nbSecondsLeft) {
        twoMinutesTimeLabel.setText(Calculations.convertToTwoMinuteString(nbSecondsLeft));
    }

    private void addYellowCard() {
        nbYellowCards++;
        if (nbYellowCards == 1) {
            yellowCard1.setVisible(true);
        } else if (nbYellowCards == 2) {
            yellowCard2.setVisible(true);
        } else {
            throw new IllegalStateException("no more yellow card allowed, max=2");
        }
    }

    private void triggerTwoMinutes() {
        nb2Mins++;
        if (nb2Mins == 1) {
            twoMinImageView1.setVisible(true);
        } else if (nb2Mins == 2) {
            twoMinImageView2.setVisible(true);
        } else if (nb2Mins == 3) {
            twoMinImageView3.setVisible(true);
        } else {
            throw new IllegalStateException("no more 2 minutes  allowed, max=3");
        }
        twoMinutesTimeLabel.setText("2:00");
        twoMinutesTimeLabel.setVisible(true);
        setSelectedState(SelectedState.DISABLED);
    }

    private void setOutForGame() {
        redCardReceived = true;
        background.setFill(Color.BLACK);
        background.setStroke(Color.RED);
        nameLabel.setTextFill(Color.RED);
        numberLabel.setTextFill(Color.RED);
        twoMinutesTimeLabel.setTextFill(Color.RED);
        playerImageView.setImage(playerIconRed);
        twoMinImageView1.setImage(timeIconRed);
        twoMinImageView2.setImage(timeIconRed);
        twoMinImageView3.setImage(timeIconRed);
        setSelectedState(SelectedState.RED_OUT);
    }

}
