package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

/**
 * The ComputerClueController class handles interactions with the computer clue interface, such as
 * clicking on various elements and entering a password. It extends the ClueController and manages
 * events related to the computer clue puzzle.
 */
public class ComputerClueController extends ClueController {

  /** Tracks the number of incorrect password guesses made by the user. */
  private static int passwordGuesses = 0;

  /** Flag to indicate whether the password has been successfully guessed. */
  public static boolean passwordHasBeenGuessed = false;

  /**
   * Returns whether the password has been successfully guessed.
   *
   * @return true if the password has been guessed, false otherwise
   */
  public static boolean getPasswordHasBeenGuessed() {
    return passwordHasBeenGuessed;
  }

  /** Increments the number of incorrect password guesses made by the user. */
  private static void incrementPasswordGuesses() {
    passwordGuesses++;
  }

  // FXML components representing various clickable elements on the computer clue scene.
  @FXML private Rectangle rectOpenEmail;
  @FXML private Rectangle rectEscape;
  @FXML private Rectangle rectBackToInbox;
  @FXML private Rectangle rectInternetExplorer;
  @FXML private TextField passwordTxtField;
  @FXML private ImageView passwordHint;
  @FXML private Rectangle rectSecurityCamera;
  private MediaPlayer player;
  private Media sound;

  /**
   * Initializes the computer clue view, including loading any necessary assets.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @Override
  public void initialize() throws ApiProxyException {
    super.initialize();
  }

  /**
   * Handles clicks on various elements of the computer clue interface, navigating to the next clue
   * depending on the clicked element.
   *
   * @param event the mouse event triggered by clicking on a rectangle
   * @throws IOException if there is an I/O error during scene transition
   */
  public void handleComputerClick(MouseEvent event) throws IOException {
    Rectangle clicked = (Rectangle) event.getSource();
    switch (clicked.getId()) {
      case "rectInternetExplorer":
      case "rectBackToInbox":
        App.setRoot("computerclue2");
        break;
      case "rectOpenEmail":
        App.setRoot("computerclue3");
        break;
      case "rectEscape":
        App.setRoot("computerclue1");
        break;
      case "rectSecurityCamera":
        App.setRoot("computerclue4");
        break;
    }
    // adding mouse click sound effect
    try {
      sound = new Media(App.class.getResource("/sounds/mouseclick.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    player = new MediaPlayer(sound);
    player.play();
  }

  /**
   * Handles password input and checks if the entered password is correct. If the password is wrong
   * and more than two attempts have been made, a password hint is displayed.
   *
   * @param event the mouse event triggered by clicking to submit the password
   * @throws IOException if there is an I/O error during scene transition
   */
  public void handlePassword(MouseEvent event) throws IOException {
    incrementPasswordGuesses();
    if (passwordTxtField.getText().equals("willthegoat")) {
      App.setRoot("computerclue1");
      passwordHasBeenGuessed = true;
    } else if (passwordGuesses >= 2 && !passwordTxtField.getText().isEmpty()) {
      passwordTxtField.clear();
      passwordHint.setVisible(true);
    }
  }

  /**
   * Handles pressing the Enter key to submit the password. This method triggers password checking
   * without needing a mouse click.
   *
   * @param keyEvent the key event triggered by pressing the Enter key
   * @throws IOException if there is an I/O error during scene transition
   */
  public void handleEnter(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode().toString().equals("ENTER")) {
      handlePassword(null);
    }
  }

  /**
   * Resets the password guess count and status, allowing the puzzle to be attempted again from the
   * beginning.
   */
  public static void reset() {
    passwordGuesses = 0; // Reset the number of password guesses
    passwordHasBeenGuessed = false; // Reset the guessed status
  }
}
