package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.states.Guessing;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class RoomController implements RoomNavigationHandler {

  private static boolean isFirstTimeInit = true;
  private static GameStateContext context = GameStateContext.getInstance();
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

  /**
   * Sets the clueHasBeenInteractedWith boolean to check if the clue has been interacted with.
   *
   * @return the clueHasBeenInteractedWith boolean
   */
  public static void setClueHasBeenInteractedWith(boolean b) {
    clueHasBeenInteractedWith = false;
  }

  @FXML private BorderPane mainPane;
  @FXML private Button btnGuess;
  @FXML private Button corridorButton;
  @FXML private Button suspect1Button;
  @FXML private Button suspect2Button;
  @FXML private Button suspect3Button;
  @FXML private Label mins;
  @FXML private Label secs;
  @FXML private Label dot;
  @FXML private ImageView mainLeftArrow;
  @FXML private ImageView mainRightArrow;
  @FXML private ImageView arrowLeft;
  @FXML private ImageView arrowRight;
  @FXML private ImageView securityDoorClosed;
  @FXML private ImageView securityDoorOpened;
  @FXML private Rectangle rectSecurityCamera;
  @FXML private Rectangle rectPerson1;
  @FXML private Rectangle rectPerson2;
  @FXML private Rectangle rectPerson3;
  @FXML private Rectangle rectOfficer;
  @FXML private VBox navBar;

  private MediaPlayer player;
  private Media sound;
  private boolean navBarVisible = false;
  private double originalWidth;

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   *
   * @throws URISyntaxException
   */
  @FXML
  public void initialize() throws URISyntaxException {
    TimeManager timeManager = TimeManager.getInstance();
    NavBarUtils.setupNavBarAndSuspectButtons(
        navBar, suspect1Button, suspect2Button, suspect3Button, this);
    if (isFirstTimeInit) {
      System.out.println("First time");
      isFirstTimeInit = false;
    }
    timeManager.setTimerLabel(mins, secs, dot);
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

  // NavBar Methods
  @FXML
  private void toggleNavBar() {
    TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), navBar);
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), navBar);

    if (navBarVisible) {
      // Slide out and fade out, then reduce the window size
      translateTransition.setToX(200); // Move back off-screen to the right
      fadeTransition.setToValue(0); // Fade out to invisible
      navBarVisible = false;
      navBar.setDisable(true); // Disable the navBar
    } else {
      navBar.setDisable(false); // Enable the navBar
      // Slide in and fade in, then increase the window size
      translateTransition.setByX(-200); // Move into view
      fadeTransition.setToValue(1); // Fade in to fully visible
      navBarVisible = true;
    }

    // Play both transitions
    translateTransition.play();
    fadeTransition.play();

    // adding click sound effect
    try {
      sound = new Media(App.class.getResource("/sounds/button.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    player = new MediaPlayer(sound);
    player.play();
  }

  @Override
  public void goToRoom(String roomName) throws IOException {
    // Before navigating, reset the window size if navBar is visible
    Stage stage = (Stage) navBar.getScene().getWindow();
    stage.setWidth(originalWidth);
    // Handle room switching logic
    App.setRoot(roomName);
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
    // Get the rectangle that was clicked
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    // Set the clueHasBeenInteractedWith flag to true
    clueHasBeenInteractedWith = true;

    // If the current state is Guessing, clues should not be interacted with
    if (context.getCurrentState() instanceof Guessing) {
      handleRectangleClick(event);
      return;
    }

    // Determine which clue was clicked based on the rectangle's ID
    switch (clickedRectangle.getId()) {
      case "rectSecurityRoom":
        // Navigate to the clue1 view
        App.setRoot("clue1");
        break;
      case "rectBin":
        // Navigate to the clue2 view
        App.setRoot("clue2");
        break;
      case "rectPaperClue":
        // Navigate to the clue3 view
        App.setRoot("clue3");
        break;
    }

    // Play a click sound effect
    try {
      sound = new Media(App.class.getResource("/sounds/button.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    player = new MediaPlayer(sound);
    player.play();
  }

  /**
   * Handles mouse hover events on the clues in the room.
   *
   * @param event the mouse event triggered by hovering over a clue
   */
  @FXML
  private void onHoverClue(MouseEvent event) {
    Rectangle hoveredClue = (Rectangle) event.getSource();
    switch (hoveredClue.getId()) {
      case "rectSecurityRoom":
        securityDoorClosed.setVisible(false);
        securityDoorOpened.setVisible(true);
        break;
      case "rectBin":
        break;
      case "rectPaperClue":
        break;
    }
  }

  /**
   * Handles mouse exit events on the clues in the room.
   *
   * @param event the mouse event triggered by exiting hover of a clue
   */
  @FXML
  private void onExitClue(MouseEvent event) {
    Rectangle hoveredClue = (Rectangle) event.getSource();
    switch (hoveredClue.getId()) {
      case "rectSecurityRoom":
        securityDoorClosed.setVisible(true);
        securityDoorOpened.setVisible(false);
        break;
      case "rectBin":
        break;
      case "rectPaperClue":
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
  private void onHandleGuessClick(ActionEvent event) throws IOException, URISyntaxException {
    if (clueHasBeenInteractedWith
        && InteragationRoomController.getSuspectsHaveBeenTalkedTo()) { // TO DO: &&
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
  }

  /**
   * Handles the right arrow click event to change perspective of the room to the right.
   *
   * @param event mouse click event.
   * @throws IOException exception if file is not found.
   */
  public void onClickRight(MouseEvent event) throws IOException {
    if (context.getCurrentState()
        instanceof Guessing) { // if in guessing phase, the other areas should not be accessible for
      // investigation
      return;
    }
    ImageView clickedArrow = (ImageView) event.getSource();
    if (clickedArrow.getId().equals("mainArrowRight")) {
      App.setRoot("room2");
    } else if (clickedArrow.getId().equals("arrowRight")) {
      App.setRoot("room");
    }

    // adding click sound effect
    try {
      sound = new Media(App.class.getResource("/sounds/button.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    player = new MediaPlayer(sound);
    player.play();
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
      System.out.println("clicked");
      App.setRoot("room3");
    } else if (clickedArrow.getId().equals("arrowLeft")) {
      App.setRoot("room");
    }

    // adding click sound effect
    try {
      sound = new Media(App.class.getResource("/sounds/button.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    player = new MediaPlayer(sound);
    player.play();
  }
}
