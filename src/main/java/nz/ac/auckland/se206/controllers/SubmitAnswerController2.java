package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class SubmitAnswerController2 extends SubmitAnswerController {
  @FXML private ImageView JanitorPhoto;
  @FXML private ImageView HOSPhoto;
  @FXML private ImageView CuratorPhoto;

  public void initialize() {
    super.initialize();
    displayChosenSuspect();
  }

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
