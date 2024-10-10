package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.speech.MP3Player;

/**
 * The SubmitAnswerController2 class extends SubmitAnswerController and is responsible for
 * displaying the chosen suspect's photo based on the player's selection.
 */
public class SubmitAnswerSecondController extends SubmitAnswerController {

  // FXML annotations to link with the corresponding ImageView elements in the FXML file
  @FXML private ImageView janitorPhoto;
  @FXML private ImageView hosPhoto;
  @FXML private ImageView curatorPhoto;
  private MP3Player player;

  /**
   * Initializes the controller by calling the parent initialize method and then displaying the
   * chosen suspect's photo.
   *
   * @throws URISyntaxException if a URI syntax error occurs
   */
  public void initialize() throws URISyntaxException {
    // Call the initialize method of the parent class
    super.initialize();
    player = new MP3Player("src/main/resources/sounds/button.mp3");
    // Display the photo of the chosen suspect
    displayChosenSuspect();
  }

  /**
   * Displays the photo of the chosen suspect based on the thief value. Sets the visibility of the
   * corresponding ImageView (Janitor, HOS, or Curator).
   */
  @FXML
  private void displayChosenSuspect() {
    // Check the value of the thief and set the corresponding ImageView to visible
    if (getThief().equals("janitor")) {
      janitorPhoto.setVisible(true);
    } else if (getThief().equals("hos")) {
      hosPhoto.setVisible(true);
    } else if (getThief().equals("curator")) {
      curatorPhoto.setVisible(true);
    }
  }

  /**
   * Handles the action of going back to the previous screen.
   *
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void onGoBack() throws IOException {
    // Set the root to the "whosThief" screen
    player.play();
    thief = null;
    answer = null;
    App.setRoot("whosThief");
  }
}
