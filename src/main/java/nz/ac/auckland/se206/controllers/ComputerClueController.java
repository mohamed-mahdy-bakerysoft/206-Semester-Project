package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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

  /**
   * Resets the password guess count and status, allowing the puzzle to be attempted again from the
   * beginning.
   */
  public static void reset() {
    passwordGuesses = 0; // Reset the number of password guesses
    passwordHasBeenGuessed = false; // Reset the guessed status
  }

  // FXML components representing various clickable elements on the computer clue scene.
  @FXML private ImageView passwordHint;
  @FXML private Rectangle rectOpenEmail;
  @FXML private Rectangle rectOpenEmailTherapy;
  @FXML private Rectangle rectEscape;
  @FXML private Rectangle rectBackToInbox;
  @FXML private Rectangle rectInternetExplorer;
  @FXML private Rectangle rectSecurityCamera;
  @FXML private TextField passwordTxtField;
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
    // Get the rectangle that was clicked
    Rectangle clicked = (Rectangle) event.getSource();

    // Determine which rectangle was clicked and navigate to the corresponding scene
    switch (clicked.getId()) {
      case "rectInternetExplorer":
      case "rectBackToInbox":
        // Navigate to the scene associated with Internet Explorer or Back to Inbox
        App.setRoot("computerclue2");
        break;
      case "rectOpenEmail":
        // Navigate to the scene associated with opening an email
        App.setRoot("computerclue3");
        break;
      case "rectOpenEmailTherapy":
        // Navigate to the scene associated with opening an email
        App.setRoot("computerclue5");
        break;
      case "rectEscape":
        // Navigate to the scene associated with escaping
        App.setRoot("computerclue1");
        break;
      case "rectSecurityCamera":
        // Navigate to the scene associated with the security camera
        App.setRoot("computerclue4");
        break;
    }

    // Adding mouse click sound effect
    try {
      // Load the mouse click sound effect
      sound = new Media(App.class.getResource("/sounds/mouseclick.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      // Print the stack trace if there is an error loading the sound effect
      e.printStackTrace();
    }

    // Create a media player for the sound effect and play it
    player = new MediaPlayer(sound);
    player.play();
  }

  /**
   * Handles password input and checks if the entered password is correct. If the password is wrong
   * and more than two attempts have been made, a password hint is displayed.
   *
   * @throws IOException if there is an I/O error during scene transition
   */
  // Remove MouseEvent parameter from handlePassword
  public void handlePassword() throws IOException {
    if (passwordTxtField == null) {
      // Handle the null case for passwordTxtField
      System.err.println("Password text field is not initialized.");
      return;
    }
    // Increment the number of incorrect password guesses
    incrementPasswordGuesses();

    // Check if the entered password matches the correct password
    if (passwordTxtField.getText().equals("Emily2023")) {
      // If the password is correct, navigate to the next scene
      App.setRoot("computerclue1");
      // Set the flag indicating the password has been successfully guessed
      passwordHasBeenGuessed = true;
    } else if (passwordGuesses >= 2 && !passwordTxtField.getText().isEmpty()) {
      // If the password is incorrect and more than two attempts have been made, clear the text
      // field
      passwordTxtField.clear();
      // Display the password hint to assist the user
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
    if (keyEvent.getCode() == KeyCode.ENTER) {
      handlePassword();
    }
  }
}
