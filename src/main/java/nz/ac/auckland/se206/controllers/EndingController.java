package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.Guessing;

/**
 * The EndingController class is responsible for managing the final feedback displayed at the end of
 * the game based on the player's actions. It handles resetting the game state when the player
 * chooses to restart and displays the feedback depending on the selected suspect.
 */
public class EndingController {

  private static String thief;
  private static String feed;

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

  @FXML private TextArea feedback;
  @FXML private TextArea feedback2;
  private MediaPlayer player;
  private Media sound;

  /**
   * Initializes the feedback display for the ending scene. If no feedback is available, it shows a
   * message indicating that the feedback is unavailable based on the selected suspect.
   */
  /**
   * Initializes the feedback display for the ending scene. If no feedback is available, it shows a
   * message indicating that the feedback is unavailable based on the selected suspect. It also
   * plays the appropriate audio feedback based on the game result.
   *
   * @throws URISyntaxException if there is an error with the URI syntax for media files
   */
  public void initialize() throws URISyntaxException {
    // Check if feedback is null
    if (thief == null && feed != null) {
      System.out.println("thief is null");
      feedback2.setText(feed);
      return;
    }
    if (feed == null) {
      // Check if thief is null
      if (thief == null) {
        System.out.println("thief is null");
        return;
      }
      // Display feedback unavailable message based on the thief
      if (thief.equals("hos")) {
        feedback.appendText("feedback unavailable");
        return;
      } else {
        feedback2.appendText("feedback unavailable");
        return;
      }
    }
    // Display feedback based on the thief
    if (thief.equals("hos")) {
      feedback.setText(feed);
    } else if (thief.equals("curator")) {
      feedback2.setText(feed);
    } else if (thief.equals("janitor")) {
      feedback2.setText(feed);
    }

    // Log the current game state
    System.out.println("game state:" + RoomController.getGameContext().getCurrentState());

    // Play the TTS audio when the game is over
    if (RoomController.getGameContext().getCurrentState() instanceof GameOver) {
      // Play winning audio if the player wins
      if (Guessing.getGameResult()) {
        sound = new Media(App.class.getResource("/sounds/correct_you_win.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
      } else { // Play losing audio if the player loses
        sound =
            new Media(
                App.class.getResource("/sounds/better_luck_next_time.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
      }
    }
  }

  /**
   * Handles the restart button click event. Resets the game state, timer, and necessary controllers
   * to restart the game from the beginning.
   *
   * @param event the action event triggered by the restart button
   * @throws IOException if there is an error navigating to the start room
   */
  @FXML
  private void onHandleRestartClick(ActionEvent event) throws IOException {
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
    feed = null;
    thief = null;

    // Navigate back to the start room
    App.setRoot("start");

    // adding click sound effect
    try {
      sound = new Media(App.class.getResource("/sounds/button.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    player = new MediaPlayer(sound);
    player.play();
  }
}
