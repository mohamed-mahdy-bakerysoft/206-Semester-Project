package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class EndingController {
  @FXML private TextArea feedback;
  @FXML private TextArea feedback2;

  private static String thief = SubmitAnswerController.getThief();
  private static String feed = SubmitAnswerController.getFeed();

  @FXML
  private void appendfeedback() {
    thief = SubmitAnswerController.getThief();
    feed = SubmitAnswerController.getFeed();
    if (feed == null) {
      if (thief.equals("hos")) {
        feedback.appendText("feedback unavaiable");
        return;
      } else {
        feedback2.appendText("feedback unavaiable");
        return;
      }
    }
    if (thief.equals("hos")) {
      feedback.setText(feed);
    } else if (thief.equals("curator")) {
      feedback2.setText(feed);
    } else if (thief.equals("janitor")) {
      feedback2.setText(feed);
    } else {

    }
  }
}
