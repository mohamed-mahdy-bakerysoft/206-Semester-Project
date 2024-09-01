package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.states.Guessing;

/**
 * Added Controller class for the intelroom view. Handles user interactions within the room where
 * the user can chat with customers and guess their profession.
 */
public class InteragationRoomController {

  private static boolean isFirstTimeInit = true;
  private static GameStateContext context = new GameStateContext();
  private static boolean clueHasBeenInteractedWith = false;

  /**
   * Gets the game context.
   *
   * @return the game context.
   */
  public static GameStateContext getGameContext() {
    return context;
  }

  /**
   * Sets the game over state.
   *
   * @throws IOException if there is an I/O error
   */
  public static void setGameOverState() throws IOException {
    context.setState(context.getGameOverState());
    App.setRoot("badending");
  }

  /**
   * Gets the clueHasBeenInteractedWith boolean to check if the clue has been interacted with.
   *
   * @return the clueHasBeenInteractedWith boolean
   */
  public static boolean getClueHasBeenInteractedWith() {
    return clueHasBeenInteractedWith;
  }

  @FXML private Button btnGuess;
  @FXML private BorderPane mainPane;
  @FXML private Label mins;
  @FXML private Label secs;
  @FXML private ImageView mainLeftArrow;
  @FXML private ImageView mainRightArrow;
  @FXML private ImageView arrowLeft;
  @FXML private ImageView arrowRight;

  private MediaPlayer player;
  private Media sound;

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   *
   * @throws URISyntaxException
   */
  @FXML
  public void initialize() throws ApiProxyException {
    TimeManager timeManager = TimeManager.getInstance();
    timeManager.setTimerLabel(mins, secs);
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("Key " + event.getCode() + " released");
  }

  /**
   * Handles mouse clicks on rectangles representing people in the room.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException, URISyntaxException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  /**
   * Handles mouse clicks on the clues in the room.
   *
   * @param event the mouse event triggered by clicking a clue
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException if there is an error in the URI syntax
   */
  @FXML
  private void handleClueClick(MouseEvent event) throws IOException, URISyntaxException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    clueHasBeenInteractedWith = true;
    if (context.getCurrentState()
        instanceof Guessing) { // if in guessing phase, the clues should not be interacted with
      handleRectangleClick(event);
      return;
    }
    switch (clickedRectangle.getId()) {
      case "rectSecurityCamera":
        App.setRoot("clue1");
        break;
      case "rectBin":
        App.setRoot("clue2");
        break;
      case "rectPaperClue":
        App.setRoot("clue3");
        break;
      case "rectSecurityCamera2":
        App.setRoot("clue4");
        break;
    }
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException, URISyntaxException {
    if (clueHasBeenInteractedWith
        && ChatController
            .getSuspectHasBeenTalkedTo()) { // TO DO: && chatController.getSuspectHasBeenTalkedTo()
      System.out.println("Now in guessing state");
      context.handleGuessClick();
    } else {
      sound =
          new Media(
              App.class
                  .getResource("/sounds/investigate_more_before_guessing.mp3")
                  .toURI()
                  .toString());
      player = new MediaPlayer(sound);
      player.play();
    }
    App.setRoot("room");
  }

  /**
   * Handles the left arrow click event to change perspective of room to the left.
   *
   * @param event mouse click
   * @throws IOException if the FXML file is not found
   */
  public void onClickLeft(MouseEvent event) throws IOException {
    if (context.getCurrentState()
        instanceof Guessing) { // if in guessing phase, the other areas should not be accessible for
      // investigation
      return;
    }
    ImageView clickedArrow = (ImageView) event.getSource();
    if (clickedArrow.getId().equals("mainArrowLeft")) {
      App.setRoot("room");
    }
  }
}
