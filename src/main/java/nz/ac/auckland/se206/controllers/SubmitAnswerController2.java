package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

/**
 * The SubmitAnswerController2 class extends SubmitAnswerController and is responsible for
 * displaying the chosen suspect's photo based on the player's selection.
 */
public class SubmitAnswerController2 extends SubmitAnswerController {

  @FXML private ImageView JanitorPhoto;
  @FXML private ImageView HOSPhoto;
  @FXML private ImageView CuratorPhoto;

  /**
   * Initializes the controller by calling the parent initialize method and then displaying the
   * chosen suspect's photo.
   */
  public void initialize() {
    super.initialize();
    displayChosenSuspect();
  }

  /**
   * Displays the photo of the chosen suspect based on the thief value. Sets the visibility of the
   * corresponding ImageView (Janitor, HOS, or Curator).
   */
  @FXML
  private void displayChosenSuspect() {
    if (getThief().equals("janitor")) {
      JanitorPhoto.setVisible(true);
    } else if (getThief().equals("hos")) {
      HOSPhoto.setVisible(true);
    } else if (getThief().equals("curator")) {
      CuratorPhoto.setVisible(true);
    }
  }
}
