package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class SubmitAnswerController {

  @FXML private Button submitButton;
  @FXML private TextArea answerTxtArea;

  public void initialize() {}

  public void sendAnswer() {

    System.out.println("Answer submitted: " + answerTxtArea.getText());
  }
}
