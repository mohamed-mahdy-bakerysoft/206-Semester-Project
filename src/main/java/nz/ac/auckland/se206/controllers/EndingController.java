package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimeManager;

/**
 * The EndingController class is responsible for managing the final feedback displayed at the end of
 * the game based on the player's actions. It handles resetting the game state when the player
 * chooses to restart and displays the feedback depending on the selected suspect.
 */
public class EndingController {

  @FXML private TextArea feedback;
  @FXML private TextArea feedback2;

  private static String thief;
  private static String feed;

  /**
   * Initializes the feedback display for the ending scene. If no feedback is available, it shows a
   * message indicating that the feedback is unavailable based on the selected suspect.
   */
  public void initialize() {

    if (feed == null) {
      if (thief == null) {
        System.out.println("thief is null");
        return;
      }
      if (thief.equals("hos")) {
        feedback.appendText("feedback unavailable");
        return;
      } else {
        feedback2.appendText("feedback unavailable");
        return;
      }
    }
    if (thief.equals("hos")) {
      feedback.setText(feed);
    } else if (thief.equals("curator")) {
      feedback2.setText(feed);
    } else if (thief.equals("janitor")) {
      feedback2.setText(feed);
    }
  }

  /**
   * Sets the feedback string to be displayed in the ending screen.
   *
   * @param feed the feedback string to set
   */
  public static void setFeed(String feed) {
    EndingController.feed = feed;
  }

  /**
   * Sets the suspect identified as the thief.
   *
   * @param thief the suspect identified as the thief
   */
  public static void setThief(String thief) {
    EndingController.thief = thief;
  }

  /**
   * Handles the restart button click event. Resets the game state, timer, and necessary controllers
   * to restart the game from the beginning.
   *
   * @param event the action event triggered by the restart button
   * @throws IOException if there is an error navigating to the start room
   */
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
    App.setRoot("start");
  }
}
