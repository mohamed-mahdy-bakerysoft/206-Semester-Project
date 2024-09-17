package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimeManager;

public class EndingController {
  @FXML private TextArea feedback;
  @FXML private TextArea feedback2;

  // private static String thief = SubmitAnswerController.getThief();
  // private static String feed = SubmitAnswerController.getFeed();
  private static String thief;
  private static String feed;

  public void initialize() {

    if (feed == null) {
      if (thief == null) {
        System.out.println("thief is null");
        return;
      }
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

  // make setters for feed and tehif
  public static void setFeed(String feed) {
    EndingController.feed = feed;
  }

  public static void setThief(String thief) {
    EndingController.thief = thief;
  }

  @FXML
  private void handleRestartClick(ActionEvent event) throws IOException {
    // Reset the game state
    GameStateContext.getInstance().reset();

    // Reset the timer
    TimeManager.getInstance().resetTimerRestart();
    TimeManager.getInstance().startTimer();

    // Reset suspects talked to map
    InteragationRoomController.resetSuspectsTalkedToMap();

    // Reset the computer clue controller
    ComputerClueController.reset();

    // Reset submit answer state
    SubmitAnswerController.reset();

    // Navigate back to the start room
    App.setRoot("room");
  }

  @FXML
  private void appendfeedback() {
    // thief = SubmitAnswerController.getThief();
    // feed = SubmitAnswerController.getFeed();
    // if (feed == null) {
    //   if (thief.equals("hos")) {
    //     feedback.appendText("feedback unavaiable");
    //     return;
    //   } else {
    //     feedback2.appendText("feedback unavaiable");
    //     return;
    //   }
    // }
    // if (thief.equals("hos")) {
    //   feedback.setText(feed);
    // } else if (thief.equals("curator")) {
    //   feedback2.setText(feed);
    // } else if (thief.equals("janitor")) {
    //   feedback2.setText(feed);
    // } else {

    // }
  }
}
