package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;

/**
 * The SubmitAnswerController2 class extends SubmitAnswerController and is responsible for
 * displaying the chosen suspect's photo based on the player's selection.
 */
public class SubmitAnswerController2 extends SubmitAnswerController {

  // FXML annotations to link with the corresponding ImageView elements in the FXML file
  @FXML private ImageView JanitorPhoto;
  @FXML private ImageView HOSPhoto;
  @FXML private ImageView CuratorPhoto;

  /**
   * Initializes the controller by calling the parent initialize method and then displaying the
   * chosen suspect's photo.
   *
   * @throws URISyntaxException if a URI syntax error occurs
   */
  public void initialize() throws URISyntaxException {
    // Call the initialize method of the parent class
    super.initialize();
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
      JanitorPhoto.setVisible(true);
    } else if (getThief().equals("hos")) {
      HOSPhoto.setVisible(true);
    } else if (getThief().equals("curator")) {
      CuratorPhoto.setVisible(true);
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
    App.setRoot("whosThief");
  }
}
